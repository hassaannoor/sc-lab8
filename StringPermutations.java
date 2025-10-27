import java.util.*;

/**
 * StringPermutations - A utility class for generating all permutations of a string
 * 
 * Specifications:
 * - Generates all permutations using recursive and iterative approaches
 * - Supports duplicate handling (include or exclude)
 * - Provides time complexity analysis
 * 
 * Time Complexity Analysis:
 * - Recursive approach: O(n! * n) where n is string length
 *   - n! permutations, each taking O(n) time to generate
 * - Iterative approach: O(n! * n) - same complexity, different implementation
 * - Space complexity: O(n! * n) for storing all permutations
 * 
 * @author Muhammad Hassaan Noor for Lab Task 2
 * @version 1.0
 */
public class StringPermutations {
    
    private boolean includeDuplicates;
    
    /**
     * Constructor for StringPermutations
     * 
     * @param includeDuplicates whether to include duplicate permutations
     */
    public StringPermutations(boolean includeDuplicates) {
        this.includeDuplicates = includeDuplicates;
    }
    
    /**
     * Generates all permutations of a string using recursion
     * 
     * Preconditions:
     * - input string must not be null
     * 
     * Postconditions:
     * - Returns a list of all permutations
     * - If includeDuplicates is false, returns only unique permutations
     * - Empty string returns list with one empty string
     * 
     * @param str the input string
     * @return List of all permutations
     * @throws IllegalArgumentException if input is null
     */
    public List<String> generatePermutationsRecursive(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        
        List<String> result = new ArrayList<>();
        
        if (str.isEmpty()) {
            result.add("");
            return result;
        }
        
        generatePermutationsRecursiveHelper(str.toCharArray(), 0, result);
        
        // Remove duplicates if needed
        if (!includeDuplicates) {
            return new ArrayList<>(new LinkedHashSet<>(result));
        }
        
        return result;
    }
    
    /**
     * Recursive helper method for generating permutations
     * Uses backtracking algorithm with character swapping
     * 
     * @param chars character array being permuted
     * @param index current position in the array
     * @param result list to store permutations
     */
    private void generatePermutationsRecursiveHelper(char[] chars, int index, List<String> result) {
        // Base case: reached end of array
        if (index == chars.length - 1) {
            result.add(new String(chars));
            return;
        }
        
        // Recursive case: try swapping each remaining character with current position
        for (int i = index; i < chars.length; i++) {
            // Swap characters at index and i
            swap(chars, index, i);
            
            // Recursively generate permutations for remaining characters
            generatePermutationsRecursiveHelper(chars, index + 1, result);
            
            // Backtrack: restore original order
            swap(chars, index, i);
        }
    }
    
    /**
     * Generates all permutations using an iterative approach
     * Uses Heap's algorithm for better performance
     * 
     * @param str the input string
     * @return List of all permutations
     * @throws IllegalArgumentException if input is null
     */
    public List<String> generatePermutationsIterative(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        
        List<String> result = new ArrayList<>();
        
        if (str.isEmpty()) {
            result.add("");
            return result;
        }
        
        char[] chars = str.toCharArray();
        int n = chars.length;
        int[] indices = new int[n];
        
        result.add(new String(chars));
        
        int i = 0;
        while (i < n) {
            if (indices[i] < i) {
                // Swap based on whether i is even or odd
                if (i % 2 == 0) {
                    swap(chars, 0, i);
                } else {
                    swap(chars, indices[i], i);
                }
                
                result.add(new String(chars));
                indices[i]++;
                i = 0;
            } else {
                indices[i] = 0;
                i++;
            }
        }
        
        // Remove duplicates if needed
        if (!includeDuplicates) {
            return new ArrayList<>(new LinkedHashSet<>(result));
        }
        
        return result;
    }
    
    /**
     * Alternative recursive approach using string manipulation
     * More intuitive but slightly less efficient due to string concatenation
     * 
     * @param str the input string
     * @return List of all permutations
     */
    public List<String> generatePermutationsRecursiveAlt(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        
        List<String> result = new ArrayList<>();
        
        if (str.isEmpty()) {
            result.add("");
            return result;
        }
        
        generatePermutationsRecursiveAltHelper("", str, result);
        
        if (!includeDuplicates) {
            return new ArrayList<>(new LinkedHashSet<>(result));
        }
        
        return result;
    }
    
    /**
     * Helper method for alternative recursive approach
     * 
     * @param prefix the current permutation being built
     * @param remaining the remaining characters to permute
     * @param result list to store permutations
     */
    private void generatePermutationsRecursiveAltHelper(String prefix, String remaining, List<String> result) {
        // Base case: no more characters to add
        if (remaining.isEmpty()) {
            result.add(prefix);
            return;
        }
        
        // Recursive case: try each remaining character as the next character
        for (int i = 0; i < remaining.length(); i++) {
            String newPrefix = prefix + remaining.charAt(i);
            String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
            generatePermutationsRecursiveAltHelper(newPrefix, newRemaining, result);
        }
    }
    
    /**
     * Utility method to swap two characters in an array
     * 
     * @param chars the character array
     * @param i first index
     * @param j second index
     */
    private void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }
    
    /**
     * Displays permutations in a formatted manner
     * 
     * @param permutations list of permutations to display
     */
    public void displayPermutations(List<String> permutations) {
        System.out.println("\n=== Permutations ===");
        System.out.println("Total count: " + permutations.size());
        System.out.println("\nPermutations:");
        
        for (int i = 0; i < permutations.size(); i++) {
            System.out.printf("%4d: %s\n", i + 1, permutations.get(i));
        }
    }
    
    /**
     * Compares performance of recursive vs iterative approaches
     * 
     * @param str the input string
     */
    public void comparePerformance(String str) {
        System.out.println("\n=== Performance Comparison ===");
        System.out.println("Input string: \"" + str + "\" (length: " + str.length() + ")");
        
        // Test recursive approach
        long startTime = System.nanoTime();
        List<String> recursiveResult = generatePermutationsRecursive(str);
        long recursiveTime = System.nanoTime() - startTime;
        
        // Test iterative approach
        startTime = System.nanoTime();
        List<String> iterativeResult = generatePermutationsIterative(str);
        long iterativeTime = System.nanoTime() - startTime;
        
        System.out.println("\nRecursive approach:");
        System.out.println("  Time: " + recursiveTime / 1_000_000.0 + " ms");
        System.out.println("  Permutations: " + recursiveResult.size());
        
        System.out.println("\nIterative approach:");
        System.out.println("  Time: " + iterativeTime / 1_000_000.0 + " ms");
        System.out.println("  Permutations: " + iterativeResult.size());
        
        System.out.println("\nSpeed comparison: ");
        if (recursiveTime < iterativeTime) {
            System.out.println("  Recursive is " + 
                String.format("%.2f", (double) iterativeTime / recursiveTime) + "x faster");
        } else {
            System.out.println("  Iterative is " + 
                String.format("%.2f", (double) recursiveTime / iterativeTime) + "x faster");
        }
    }
    
    /**
     * Main method for command-line usage
     * 
     * Usage: java StringPermutations <string> [-d] [-c] [-i]
     *   -d: exclude duplicate permutations
     *   -c: compare performance of recursive vs iterative
     *   -i: use iterative algorithm
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java StringPermutations <string> [-d] [-c] [-i]");
            System.out.println("  -d: Exclude duplicate permutations");
            System.out.println("  -c: Compare performance (recursive vs iterative)");
            System.out.println("  -i: Use iterative algorithm");
            return;
        }
        
        String input = args[0];
        boolean includeDuplicates = true;
        boolean compare = false;
        boolean useIterative = false;
        
        // Parse options
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-d")) {
                includeDuplicates = false;
            } else if (args[i].equals("-c")) {
                compare = true;
            } else if (args[i].equals("-i")) {
                useIterative = true;
            }
        }
        
        try {
            StringPermutations generator = new StringPermutations(includeDuplicates);
            
            if (compare) {
                generator.comparePerformance(input);
            } else {
                List<String> permutations;
                
                if (useIterative) {
                    System.out.println("Using iterative algorithm...");
                    permutations = generator.generatePermutationsIterative(input);
                } else {
                    System.out.println("Using recursive algorithm...");
                    permutations = generator.generatePermutationsRecursive(input);
                }
                
                generator.displayPermutations(permutations);
            }
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}