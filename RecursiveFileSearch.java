import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * RecursiveFileSearch - A utility class for recursively searching files in directories
 * 
 * Specifications:
 * - Searches for one or multiple files in a directory tree
 * - Supports case-sensitive and case-insensitive search
 * - Counts occurrences of each file
 * - Handles symbolic links and permission errors gracefully
 * 
 * @author Muhammad Hassaan Noor for Lab Task 1
 * @version 1.0
 */
public class RecursiveFileSearch {
    
    private boolean caseSensitive;
    private Map<String, List<String>> foundFiles;
    private Map<String, Integer> fileCount;
    
    /**
     * Constructor for RecursiveFileSearch
     * 
     * @param caseSensitive whether the search should be case-sensitive
     */
    public RecursiveFileSearch(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        this.foundFiles = new HashMap<>();
        this.fileCount = new HashMap<>();
    }
    
    /**
     * Searches for multiple files in a directory recursively
     * 
     * Preconditions:
     * - directoryPath must not be null
     * - fileNames must not be null or empty
     * 
     * Postconditions:
     * - Returns a map with file names as keys and list of paths as values
     * - Updates internal count of file occurrences
     * 
     * @param directoryPath the root directory to search
     * @param fileNames list of file names to search for
     * @return Map containing found files and their paths
     * @throws IllegalArgumentException if inputs are invalid
     * @throws IOException if directory access fails
     */
    public Map<String, List<String>> searchFiles(String directoryPath, List<String> fileNames) 
            throws IllegalArgumentException, IOException {
        
        // Input validation
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Directory path cannot be null or empty");
        }
        
        if (fileNames == null || fileNames.isEmpty()) {
            throw new IllegalArgumentException("File names list cannot be null or empty");
        }
        
        File directory = new File(directoryPath);
        
        if (!directory.exists()) {
            throw new IOException("Directory does not exist: " + directoryPath);
        }
        
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + directoryPath);
        }
        
        // Initialize data structures
        foundFiles.clear();
        fileCount.clear();
        
        for (String fileName : fileNames) {
            foundFiles.put(fileName, new ArrayList<>());
            fileCount.put(fileName, 0);
        }
        
        // Start recursive search
        searchRecursive(directory, fileNames);
        
        return new HashMap<>(foundFiles);
    }
    
    /**
     * Recursive helper method to search for files
     * 
     * @param currentDir the current directory being searched
     * @param fileNames list of file names to search for
     */
    private void searchRecursive(File currentDir, List<String> fileNames) {
        try {
            File[] files = currentDir.listFiles();
            
            // Base case: no files in directory or permission denied
            if (files == null) {
                return;
            }
            
            for (File file : files) {
                try {
                    // Skip symbolic links to avoid infinite loops
                    if (Files.isSymbolicLink(file.toPath())) {
                        continue;
                    }
                    
                    // Check if current file matches any search criteria
                    for (String targetFileName : fileNames) {
                        if (matchesFileName(file.getName(), targetFileName)) {
                            String fullPath = file.getAbsolutePath();
                            foundFiles.get(targetFileName).add(fullPath);
                            fileCount.put(targetFileName, fileCount.get(targetFileName) + 1);
                        }
                    }
                    
                    // Recursive case: if it's a directory, search inside
                    if (file.isDirectory()) {
                        searchRecursive(file, fileNames);
                    }
                    
                } catch (SecurityException e) {
                    // Skip files/directories without permission
                    System.err.println("Access denied: " + file.getPath());
                }
            }
        } catch (SecurityException e) {
            System.err.println("Cannot access directory: " + currentDir.getPath());
        }
    }
    
    /**
     * Checks if a file name matches the target based on case sensitivity
     * 
     * @param fileName the actual file name
     * @param targetName the target file name
     * @return true if names match, false otherwise
     */
    private boolean matchesFileName(String fileName, String targetName) {
        if (caseSensitive) {
            return fileName.equals(targetName);
        } else {
            return fileName.equalsIgnoreCase(targetName);
        }
    }
    
    /**
     * Gets the count of occurrences for a specific file
     * 
     * @param fileName the file name
     * @return number of occurrences found
     */
    public int getFileCount(String fileName) {
        return fileCount.getOrDefault(fileName, 0);
    }
    
    /**
     * Displays the search results in a formatted manner
     */
    public void displayResults() {
        System.out.println("\n=== Search Results ===");
        
        for (String fileName : foundFiles.keySet()) {
            List<String> paths = foundFiles.get(fileName);
            int count = fileCount.get(fileName);
            
            System.out.println("\nFile: " + fileName);
            System.out.println("Occurrences: " + count);
            
            if (paths.isEmpty()) {
                System.out.println("Status: NOT FOUND");
            } else {
                System.out.println("Status: FOUND");
                System.out.println("Locations:");
                for (String path : paths) {
                    System.out.println("  - " + path);
                }
            }
        }
    }
    
    /**
     * Main method for command-line usage
     * 
     * Usage: java RecursiveFileSearch <directory> <file1> [file2] ... [-i for case-insensitive]
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java RecursiveFileSearch <directory> <file1> [file2] ... [-i]");
            System.out.println("  -i: Enable case-insensitive search (optional)");
            return;
        }
        
        // Parse arguments
        String directory = args[0];
        List<String> fileNames = new ArrayList<>();
        boolean caseSensitive = true;
        
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-i")) {
                caseSensitive = false;
            } else {
                fileNames.add(args[i]);
            }
        }
        
        if (fileNames.isEmpty()) {
            System.out.println("Error: At least one file name must be specified");
            return;
        }
        
        // Perform search
        try {
            RecursiveFileSearch searcher = new RecursiveFileSearch(caseSensitive);
            
            System.out.println("Searching in: " + directory);
            System.out.println("Files to find: " + fileNames);
            System.out.println("Case-sensitive: " + caseSensitive);
            
            searcher.searchFiles(directory, fileNames);
            searcher.displayResults();
            
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}