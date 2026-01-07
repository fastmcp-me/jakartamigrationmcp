package adrianmikula.jakartamigration.sourcecodescanning.service.impl;

import adrianmikula.jakartamigration.sourcecodescanning.domain.FileUsage;
import adrianmikula.jakartamigration.sourcecodescanning.domain.ImportStatement;
import adrianmikula.jakartamigration.sourcecodescanning.domain.SourceCodeAnalysisResult;
import adrianmikula.jakartamigration.sourcecodescanning.service.SourceCodeScanner;
import lombok.extern.slf4j.Slf4j;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.CompilationUnit;
import org.openrewrite.SourceFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of SourceCodeScanner using OpenRewrite JavaParser.
 * Provides fast, AST-based scanning for javax.* usage in Java source files.
 */
@Service
@Slf4j
public class SourceCodeScannerImpl implements SourceCodeScanner {
    
    private final JavaParser javaParser;
    
    public SourceCodeScannerImpl() {
        this.javaParser = JavaParser.fromJavaVersion()
            .build();
    }
    
    @Override
    public SourceCodeAnalysisResult scanProject(Path projectPath) {
        if (projectPath == null || !Files.exists(projectPath) || !Files.isDirectory(projectPath)) {
            log.warn("Invalid project path: {}", projectPath);
            return SourceCodeAnalysisResult.empty();
        }
        
        try {
            // Discover all Java files
            List<Path> javaFiles = discoverJavaFiles(projectPath);
            
            if (javaFiles.isEmpty()) {
                log.info("No Java files found in project: {}", projectPath);
                return SourceCodeAnalysisResult.empty();
            }
            
            log.info("Scanning {} Java files in project: {}", javaFiles.size(), projectPath);
            
            // Scan files in parallel
            AtomicInteger totalScanned = new AtomicInteger(0);
            List<FileUsage> usages = javaFiles.parallelStream()
                .map(file -> {
                    totalScanned.incrementAndGet();
                    FileUsage usage = scanFile(file);
                    if (usage.hasJavaxUsage()) {
                        log.debug("Found javax usage in: {}", file);
                        return usage;
                    }
                    return null;
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
            
            int totalJavaxImports = usages.stream()
                .mapToInt(FileUsage::getJavaxImportCount)
                .sum();
            
            log.info("Scan complete: {} files scanned, {} files with javax usage, {} total javax imports",
                totalScanned.get(), usages.size(), totalJavaxImports);
            
            return new SourceCodeAnalysisResult(
                usages,
                totalScanned.get(),
                usages.size(),
                totalJavaxImports
            );
            
        } catch (Exception e) {
            log.error("Error scanning project: {}", projectPath, e);
            return SourceCodeAnalysisResult.empty();
        }
    }
    
    @Override
    public FileUsage scanFile(Path filePath) {
        if (filePath == null || !Files.exists(filePath)) {
            log.warn("Invalid file path: {}", filePath);
            return new FileUsage(filePath, List.of(), 0);
        }
        
        try {
            String content = Files.readString(filePath);
            int lineCount = countLines(content);
            
            // Parse with OpenRewrite
            List<SourceFile> sourceFiles = javaParser.parse(content).collect(java.util.stream.Collectors.toList());
            
            if (sourceFiles.isEmpty()) {
                log.debug("No source files found in file: {}", filePath);
                return new FileUsage(filePath, List.of(), lineCount);
            }
            
            // Extract javax imports from all compilation units
            List<ImportStatement> imports = new ArrayList<>();
            for (SourceFile sourceFile : sourceFiles) {
                if (sourceFile instanceof CompilationUnit) {
                    CompilationUnit cu = (CompilationUnit) sourceFile;
                    imports.addAll(extractJavaxImports(cu, content));
                }
            }
            
            return new FileUsage(filePath, imports, lineCount);
            
        } catch (Exception e) {
            log.warn("Error scanning file: {}", filePath, e);
            return new FileUsage(filePath, List.of(), 0);
        }
    }
    
    /**
     * Discovers all Java files in the project, excluding build directories.
     */
    private List<Path> discoverJavaFiles(Path projectPath) {
        List<Path> javaFiles = new ArrayList<>();
        
        try (Stream<Path> paths = Files.walk(projectPath)) {
            paths
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .filter(this::shouldScanFile)
                .forEach(javaFiles::add);
        } catch (IOException e) {
            log.error("Error discovering Java files in: {}", projectPath, e);
        }
        
        return javaFiles;
    }
    
    /**
     * Determines if a file should be scanned (excludes build directories).
     */
    private boolean shouldScanFile(Path file) {
        String path = file.toString().replace('\\', '/');
        return !path.contains("/target/") &&
               !path.contains("/build/") &&
               !path.contains("/.git/") &&
               !path.contains("/node_modules/") &&
               !path.contains("/.gradle/") &&
               !path.contains("/.mvn/") &&
               !path.contains("/.idea/") &&
               !path.contains("/.vscode/") &&
               !path.contains("/out/") &&
               !path.contains("/bin/");
    }
    
    /**
     * Extracts javax.* imports from a compilation unit.
     */
    private List<ImportStatement> extractJavaxImports(CompilationUnit cu, String content) {
        List<ImportStatement> imports = new ArrayList<>();
        
        List<J.Import> importDeclarations = cu.getImports();
        String[] lines = content.split("\n");
        
        for (J.Import imp : importDeclarations) {
            String importName = imp.getQualid().toString();
            
            if (importName.startsWith("javax.")) {
                String jakartaEquivalent = importName.replace("javax.", "jakarta.");
                
                // Find line number by searching for the import in content
                int lineNumber = findLineNumber(lines, importName);
                
                // Extract package name (e.g., "javax.servlet" from "javax.servlet.ServletException")
                String javaxPackage = extractPackageName(importName);
                
                imports.add(new ImportStatement(
                    importName,
                    javaxPackage,
                    jakartaEquivalent,
                    lineNumber
                ));
            }
        }
        
        return imports;
    }
    
    /**
     * Finds the line number of an import statement in the content.
     */
    private int findLineNumber(String[] lines, String importName) {
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("import") && lines[i].contains(importName)) {
                return i + 1; // Line numbers are 1-based
            }
        }
        return 1; // Default to line 1 if not found
    }
    
    /**
     * Extracts package name from fully qualified class name.
     * Example: "javax.servlet.ServletException" -> "javax.servlet"
     */
    private String extractPackageName(String fullyQualifiedName) {
        int lastDot = fullyQualifiedName.lastIndexOf('.');
        if (lastDot > 0) {
            return fullyQualifiedName.substring(0, lastDot);
        }
        return fullyQualifiedName;
    }
    
    /**
     * Counts lines in content.
     */
    private int countLines(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        return content.split("\n").length;
    }
}

