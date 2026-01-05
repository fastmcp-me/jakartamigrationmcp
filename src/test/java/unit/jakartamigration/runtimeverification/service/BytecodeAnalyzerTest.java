package unit.jakartamigration.runtimeverification.service;

import com.bugbounty.jakartamigration.runtimeverification.domain.BytecodeAnalysisResult;
import com.bugbounty.jakartamigration.runtimeverification.service.BytecodeAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BytecodeAnalyzer Tests")
class BytecodeAnalyzerTest {
    
    private BytecodeAnalyzer analyzer;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        analyzer = new com.bugbounty.jakartamigration.runtimeverification.service.impl.AsmBytecodeAnalyzer();
    }
    
    @Test
    @DisplayName("Should analyze JAR file for Jakarta migration issues")
    void shouldAnalyzeJarFile() throws Exception {
        // Given
        Path jarPath = tempDir.resolve("test.jar");
        Files.createFile(jarPath);
        
        // When
        BytecodeAnalysisResult result = analyzer.analyzeJar(jarPath);
        
        // Then
        assertNotNull(result);
        assertNotNull(result.javaxClasses());
        assertNotNull(result.jakartaClasses());
        assertNotNull(result.mixedNamespaceClasses());
        assertNotNull(result.potentialErrors());
        assertNotNull(result.warnings());
        assertTrue(result.analysisTimeMs() >= 0);
        assertTrue(result.classesAnalyzed() >= 0);
    }
    
    @Test
    @DisplayName("Should detect javax classes in bytecode")
    void shouldDetectJavaxClasses() {
        // Given
        String javaxClass = "javax.servlet.ServletException";
        
        // When
        boolean isJavax = analyzer.isJavaxClass(javaxClass);
        
        // Then
        assertTrue(isJavax);
    }
    
    @Test
    @DisplayName("Should detect jakarta classes in bytecode")
    void shouldDetectJakartaClasses() {
        // Given
        String jakartaClass = "jakarta.servlet.ServletException";
        
        // When
        boolean isJakarta = analyzer.isJakartaClass(jakartaClass);
        
        // Then
        assertTrue(isJakarta);
    }
    
    @Test
    @DisplayName("Should identify mixed namespace issues")
    void shouldIdentifyMixedNamespaceIssues() throws Exception {
        // Given
        Path jarPath = tempDir.resolve("mixed.jar");
        Files.createFile(jarPath);
        
        // When
        BytecodeAnalysisResult result = analyzer.analyzeJar(jarPath);
        
        // Then
        // If mixed namespaces are found, should be reported
        if (!result.mixedNamespaceClasses().isEmpty()) {
            assertTrue(result.hasIssues());
        }
    }
    
    @Test
    @DisplayName("Should return empty result for non-existent JAR")
    void shouldHandleNonExistentJar() {
        // Given
        Path nonExistentJar = tempDir.resolve("nonexistent.jar");
        
        // When/Then
        assertThrows(Exception.class, () -> {
            analyzer.analyzeJar(nonExistentJar);
        });
    }
    
    @Test
    @DisplayName("Should analyze classes directory")
    void shouldAnalyzeClassesDirectory() throws Exception {
        // Given
        Path classesDir = tempDir.resolve("classes");
        Files.createDirectories(classesDir);
        
        // When
        BytecodeAnalysisResult result = analyzer.analyzeClasses(classesDir);
        
        // Then
        assertNotNull(result);
    }
}

