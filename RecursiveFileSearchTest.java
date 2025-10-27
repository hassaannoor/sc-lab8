import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Comprehensive JUnit test suite for RecursiveFileSearch (Task 1)
 * 
 * Test Coverage:
 * - Base cases and edge cases
 * - Recursive steps and termination
 * - Error handling and invalid inputs
 * - Performance and correctness validation
 * - Case sensitivity testing
 * - File counting accuracy
 * 
 * @author Lab Task 3 - Task 1 Tests
 * @version 1.0
 */
@DisplayName("RecursiveFileSearch Tests - Task 1")
public class RecursiveFileSearchTest {
    
    @TempDir
    Path tempDir;
    
    /**
     * Sets up test directory structure with sample files
     * Creates a realistic directory tree for testing
     */
    @BeforeEach
    void setupTestDirectory() throws IOException {
        // Create directory structure
        Files.createDirectory(tempDir.resolve("subdir1"));
        Files.createDirectory(tempDir.resolve("subdir2"));
        Files.createDirectory(tempDir.resolve("subdir1/nested"));
        
        // Create test files
        Files.createFile(tempDir.resolve("test.txt"));
        Files.createFile(tempDir.resolve("subdir1/test.txt"));
        Files.createFile(tempDir.resolve("subdir1/nested/test.txt"));
        Files.createFile(tempDir.resolve("subdir2/example.java"));
        Files.createFile(tempDir.resolve("subdir2/readme.md"));
    }
    
    // ==================== Basic Functionality Tests ====================
    
    @Test
    @DisplayName("Test basic file search - single file")
    void testBasicSingleFileSearch() throws IOException {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        assertNotNull(results, "Results should not be null");
        assertTrue(results.containsKey("test.txt"), "Results should contain test.txt");
        assertEquals(3, results.get("test.txt").size(), 
            "Should find 3 occurrences of test.txt");
        assertEquals(3, searcher.getFileCount("test.txt"),
            "File count should be 3");
    }
    
    @Test
    @DisplayName("Test multiple file search")
    void testMultipleFileSearch() throws IOException {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt", "example.java", "readme.md");
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        assertEquals(3, results.get("test.txt").size(), 
            "Should find 3 test.txt files");
        assertEquals(1, results.get("example.java").size(),
            "Should find 1 example.java file");
        assertEquals(1, results.get("readme.md").size(),
            "Should find 1 readme.md file");
    }
    
    @Test
    @DisplayName("Test file not found")
    void testFileNotFound() throws IOException {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("nonexistent.txt");
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        assertTrue(results.get("nonexistent.txt").isEmpty(),
            "Should return empty list for non-existent file");
        assertEquals(0, searcher.getFileCount("nonexistent.txt"),
            "File count should be 0 for non-existent file");
    }
    
    // ==================== Case Sensitivity Tests ====================
    
    @Test
    @DisplayName("Test case-sensitive search")
    void testCaseSensitiveSearch() throws IOException {
        RecursiveFileSearch caseSensitive = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        Map<String, List<String>> results = caseSensitive.searchFiles(
            tempDir.toString(), fileNames);
        
        // Should find 3 "test.txt" files (not "Test.txt")
        assertEquals(3, results.get("test.txt").size(),
            "Case-sensitive search should find only lowercase test.txt");
    }
    
    @Test
    @DisplayName("Test case-insensitive search")
    void testCaseInsensitiveSearch() throws IOException {
        // Create a subdirectory with a different-case filename for testing
        Files.createDirectory(tempDir.resolve("caseTest"));
        Files.createFile(tempDir.resolve("caseTest/TEST.TXT"));
        
        RecursiveFileSearch caseInsensitive = new RecursiveFileSearch(false);
        List<String> fileNames = Arrays.asList("test.txt");
        
        Map<String, List<String>> results = caseInsensitive.searchFiles(
            tempDir.toString(), fileNames);
        
        // On Linux (case-sensitive FS): finds 3 test.txt + 1 TEST.TXT = 4
        // On Windows (case-insensitive FS): TEST.TXT matches test.txt, finds 4 
        assertEquals(4, results.get("test.txt").size(),
            "Case-insensitive search should find both test.txt and TEST.TXT");
    }
    
    @Test
    @DisplayName("Test case-insensitive with uppercase query")
    void testCaseInsensitiveUppercaseQuery() throws IOException {
        // Use the directory structure from setup (3 test.txt files)
        RecursiveFileSearch searcher = new RecursiveFileSearch(false);
        List<String> fileNames = Arrays.asList("TEST.TXT");
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        // Should match all 3 lowercase test.txt files with uppercase query
        // (Works on both Windows and Linux because we only have lowercase files in setup)
        assertEquals(3, results.get("TEST.TXT").size(),
            "Should match all 3 test.txt files regardless of query case");
    }
    
    // ==================== Error Handling Tests ====================
    
    @Test
    @DisplayName("Test null directory path throws exception")
    void testNullDirectoryPath() {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> searcher.searchFiles(null, fileNames),
            "Should throw exception for null directory path"
        );
        
        assertTrue(exception.getMessage().contains("null"),
            "Error message should mention null");
    }
    
    @Test
    @DisplayName("Test empty directory path throws exception")
    void testEmptyDirectoryPath() {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> searcher.searchFiles("", fileNames),
            "Should throw exception for empty directory path"
        );
        
        assertTrue(exception.getMessage().contains("empty"),
            "Error message should mention empty");
    }
    
    @Test
    @DisplayName("Test null file names list throws exception")
    void testNullFileNamesList() {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> searcher.searchFiles(tempDir.toString(), null),
            "Should throw exception for null file list"
        );
        
        assertTrue(exception.getMessage().contains("null"),
            "Error message should mention null");
    }
    
    @Test
    @DisplayName("Test empty file names list throws exception")
    void testEmptyFileNamesList() {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> emptyList = new ArrayList<>();
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> searcher.searchFiles(tempDir.toString(), emptyList),
            "Should throw exception for empty file list"
        );
        
        assertTrue(exception.getMessage().contains("empty"),
            "Error message should mention empty");
    }
    
    @Test
    @DisplayName("Test non-existent directory throws exception")
    void testNonExistentDirectory() {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        IOException exception = assertThrows(
            IOException.class,
            () -> searcher.searchFiles("/nonexistent/path/xyz", fileNames),
            "Should throw IOException for non-existent directory"
        );
        
        assertTrue(exception.getMessage().contains("does not exist"),
            "Error message should mention directory doesn't exist");
    }
    
    @Test
    @DisplayName("Test file path instead of directory throws exception")
    void testFilePathInsteadOfDirectory() throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> searcher.searchFiles(filePath.toString(), fileNames),
            "Should throw exception when path is a file, not directory"
        );
        
        assertTrue(exception.getMessage().contains("not a directory"),
            "Error message should mention it's not a directory");
    }
    
    // ==================== File Count Tests ====================
    
    @Test
    @DisplayName("Test count accuracy for duplicate files")
    void testFileCountAccuracy() throws IOException {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        searcher.searchFiles(tempDir.toString(), fileNames);
        
        assertEquals(3, searcher.getFileCount("test.txt"),
            "File count should accurately reflect number of occurrences");
    }
    
    @Test
    @DisplayName("Test count for multiple files")
    void testMultipleFileCount() throws IOException {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt", "example.java", "readme.md");
        
        searcher.searchFiles(tempDir.toString(), fileNames);
        
        assertEquals(3, searcher.getFileCount("test.txt"));
        assertEquals(1, searcher.getFileCount("example.java"));
        assertEquals(1, searcher.getFileCount("readme.md"));
    }
    
    @Test
    @DisplayName("Test count for non-searched file returns 0")
    void testCountForNonSearchedFile() throws IOException {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        searcher.searchFiles(tempDir.toString(), fileNames);
        
        assertEquals(0, searcher.getFileCount("other.txt"),
            "Count for non-searched file should be 0");
    }
    
    // ==================== Recursion Tests ====================
    
    @Test
    @DisplayName("Test empty directory")
    void testEmptyDirectory() throws IOException {
        Path emptyDir = tempDir.resolve("empty");
        Files.createDirectory(emptyDir);
        
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        Map<String, List<String>> results = searcher.searchFiles(
            emptyDir.toString(), fileNames);
        
        assertTrue(results.get("test.txt").isEmpty(),
            "Should return empty list for empty directory");
    }
    
    @Test
    @DisplayName("Test deeply nested directories")
    void testDeeplyNestedDirectories() throws IOException {
        // Create deep nesting (5 levels)
        Path deep = tempDir.resolve("a/b/c/d/e");
        Files.createDirectories(deep);
        Files.createFile(deep.resolve("deep.txt"));
        
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("deep.txt");
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        assertEquals(1, results.get("deep.txt").size(),
            "Should find file in deeply nested directory");
        assertTrue(results.get("deep.txt").get(0).contains("deep.txt"),
            "Result should contain the file path");
    }
    
    @Test
    @DisplayName("Test very deeply nested directories (10 levels)")
    void testVeryDeeplyNestedDirectories() throws IOException {
        // Create very deep nesting (10 levels)
        Path deepPath = tempDir;
        for (int i = 0; i < 10; i++) {
            deepPath = deepPath.resolve("level" + i);
        }
        Files.createDirectories(deepPath);
        Files.createFile(deepPath.resolve("deep_file.txt"));
        
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("deep_file.txt");
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        assertEquals(1, results.get("deep_file.txt").size(),
            "Should handle very deep nesting");
    }
    
    @Test
    @DisplayName("Test multiple nested subdirectories")
    void testMultipleNestedSubdirectories() throws IOException {
        // Create multiple branches
        Files.createDirectories(tempDir.resolve("branch1/sub1"));
        Files.createDirectories(tempDir.resolve("branch1/sub2"));
        Files.createDirectories(tempDir.resolve("branch2/sub1"));
        Files.createDirectories(tempDir.resolve("branch2/sub2"));
        
        // Create files in each branch
        Files.createFile(tempDir.resolve("branch1/sub1/target.txt"));
        Files.createFile(tempDir.resolve("branch1/sub2/target.txt"));
        Files.createFile(tempDir.resolve("branch2/sub1/target.txt"));
        Files.createFile(tempDir.resolve("branch2/sub2/target.txt"));
        
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("target.txt");
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        assertEquals(4, results.get("target.txt").size(),
            "Should find all files across multiple branches");
    }
    
    // ==================== Edge Case Tests ====================
    
    @Test
    @DisplayName("Test file with special characters in filename")
    void testSpecialCharactersInFilename() throws IOException {
        Files.createFile(tempDir.resolve("test-file_123.txt"));
        Files.createFile(tempDir.resolve("test.file.txt"));
        Files.createFile(tempDir.resolve("test@file#.txt"));
        
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList(
            "test-file_123.txt", 
            "test.file.txt",
            "test@file#.txt"
        );
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        assertEquals(1, results.get("test-file_123.txt").size());
        assertEquals(1, results.get("test.file.txt").size());
        assertEquals(1, results.get("test@file#.txt").size());
    }
    
    @Test
    @DisplayName("Test files with same name in same directory")
    void testDuplicateFilesInDifferentLocations() throws IOException {
        // Files already created in setup: test.txt in root, subdir1, and subdir1/nested
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        List<String> paths = results.get("test.txt");
        assertEquals(3, paths.size(), "Should find all duplicate files");
        
        // Verify all paths are unique
        Set<String> uniquePaths = new HashSet<>(paths);
        assertEquals(3, uniquePaths.size(), "All paths should be unique");
    }
    
    @Test
    @DisplayName("Test search with whitespace in directory names")
    void testWhitespaceInDirectoryNames() throws IOException {
        Path dirWithSpace = tempDir.resolve("my documents");
        Files.createDirectory(dirWithSpace);
        Files.createFile(dirWithSpace.resolve("file.txt"));
        
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("file.txt");
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        assertEquals(1, results.get("file.txt").size(),
            "Should handle directories with spaces");
    }
    
    @Test
    @DisplayName("Test large number of files")
    void testLargeNumberOfFiles() throws IOException {
        // Create 100 files
        for (int i = 0; i < 100; i++) {
            Files.createFile(tempDir.resolve("file" + i + ".txt"));
        }
        
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            fileNames.add("file" + i + ".txt");
        }
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        for (int i = 0; i < 100; i++) {
            assertEquals(1, results.get("file" + i + ".txt").size(),
                "Should find file" + i + ".txt");
        }
    }
    
    @Test
    @DisplayName("Test search returns correct full paths")
    void testCorrectFullPaths() throws IOException {
        RecursiveFileSearch searcher = new RecursiveFileSearch(true);
        List<String> fileNames = Arrays.asList("test.txt");
        
        Map<String, List<String>> results = searcher.searchFiles(
            tempDir.toString(), fileNames);
        
        List<String> paths = results.get("test.txt");
        for (String path : paths) {
            assertTrue(path.contains(tempDir.toString()),
                "Path should contain base directory");
            assertTrue(path.endsWith("test.txt"),
                "Path should end with filename");
            assertTrue(new File(path).exists(),
                "Path should point to existing file");
        }
    }
}