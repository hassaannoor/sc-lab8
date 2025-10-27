import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Comprehensive JUnit test suite for StringPermutations (Task 2)
 * 
 * Test Coverage:
 * - Base cases (empty, single, double character strings)
 * - Recursive steps and algorithm correctness
 * - Duplicate handling (include/exclude)
 * - Iterative vs Recursive consistency
 * - Error handling and edge cases
 * - Performance validation
 * - Factorial correctness
 * 
 * @author Muhammad Hassaan Noor for Lab Task 3 - Task 2 Tests
 * @version 1.0
 */
@DisplayName("StringPermutations Tests - Task 2")
public class StringPermutationsTest {
    
    // ==================== Base Case Tests ====================
    
    @Test
    @DisplayName("Test empty string returns single empty permutation")
    void testEmptyString() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("");
        
        assertEquals(1, result.size(), "Empty string should have 1 permutation");
        assertEquals("", result.get(0), "Permutation should be empty string");
    }
    
    @Test
    @DisplayName("Test single character string")
    void testSingleCharacter() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("A");
        
        assertEquals(1, result.size(), "Single character should have 1 permutation");
        assertEquals("A", result.get(0), "Permutation should be the character itself");
    }
    
    @Test
    @DisplayName("Test two character string")
    void testTwoCharacters() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("AB");
        
        assertEquals(2, result.size(), "Two characters should have 2! = 2 permutations");
        assertTrue(result.contains("AB"), "Should contain AB");
        assertTrue(result.contains("BA"), "Should contain BA");
    }
    
    @Test
    @DisplayName("Test three character string")
    void testThreeCharacters() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("ABC");
        
        assertEquals(6, result.size(), "Three characters should have 3! = 6 permutations");
        
        // Verify all expected permutations exist
        String[] expected = {"ABC", "ACB", "BAC", "BCA", "CAB", "CBA"};
        for (String perm : expected) {
            assertTrue(result.contains(perm), "Missing permutation: " + perm);
        }
    }
    
    // ==================== Duplicate Handling Tests ====================
    
    @Test
    @DisplayName("Test string with duplicate characters - include duplicates")
    void testDuplicatesIncluded() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("AAB");
        
        // With duplicates: 3! = 6 permutations (some will be identical)
        assertEquals(6, result.size(), "Should generate 3! = 6 permutations including duplicates");
    }
    
    @Test
    @DisplayName("Test string with duplicate characters - exclude duplicates")
    void testDuplicatesExcluded() {
        StringPermutations generator = new StringPermutations(false);
        List<String> result = generator.generatePermutationsRecursive("AAB");
        
        // Unique permutations: AAB, ABA, BAA = 3
        assertEquals(3, result.size(), "Should have 3 unique permutations");
        assertTrue(result.contains("AAB"), "Should contain AAB");
        assertTrue(result.contains("ABA"), "Should contain ABA");
        assertTrue(result.contains("BAA"), "Should contain BAA");
        
        // Verify all are unique
        Set<String> uniqueSet = new HashSet<>(result);
        assertEquals(result.size(), uniqueSet.size(), "All permutations should be unique");
    }
    
    @Test
    @DisplayName("Test all identical characters")
    void testAllIdenticalCharacters() {
        StringPermutations generator = new StringPermutations(false);
        List<String> result = generator.generatePermutationsRecursive("AAA");
        
        assertEquals(1, result.size(), "All identical chars should have 1 unique permutation");
        assertEquals("AAA", result.get(0), "Should be the original string");
    }
    
    @Test
    @DisplayName("Test multiple different duplicate groups")
    void testMultipleDuplicateGroups() {
        StringPermutations generator = new StringPermutations(false);
        List<String> result = generator.generatePermutationsRecursive("AABB");
        
        // Unique permutations of AABB should be limited
        assertTrue(result.size() <= 6, "Should have at most 4!/(2!*2!) = 6 unique permutations");
        
        // Verify all are unique
        Set<String> uniqueSet = new HashSet<>(result);
        assertEquals(uniqueSet.size(), result.size(), "All permutations should be unique");
        
        // Verify some expected permutations
        assertTrue(result.contains("AABB"), "Should contain AABB");
        assertTrue(result.contains("ABAB"), "Should contain ABAB");
        assertTrue(result.contains("ABBA"), "Should contain ABBA");
        assertTrue(result.contains("BAAB"), "Should contain BAAB");
        assertTrue(result.contains("BABA"), "Should contain BABA");
        assertTrue(result.contains("BBAA"), "Should contain BBAA");
    }
    
    // ==================== Error Handling Tests ====================
    
    @Test
    @DisplayName("Test null string throws exception - recursive")
    void testNullStringRecursive() {
        StringPermutations generator = new StringPermutations(true);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> generator.generatePermutationsRecursive(null),
            "Should throw exception for null input"
        );
        
        assertTrue(exception.getMessage().contains("null"),
            "Error message should mention null");
    }
    
    @Test
    @DisplayName("Test null string throws exception - iterative")
    void testNullStringIterative() {
        StringPermutations generator = new StringPermutations(true);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> generator.generatePermutationsIterative(null),
            "Should throw exception for null input"
        );
        
        assertTrue(exception.getMessage().contains("null"),
            "Error message should mention null");
    }
    
    @Test
    @DisplayName("Test null string throws exception - alternative recursive")
    void testNullStringRecursiveAlt() {
        StringPermutations generator = new StringPermutations(true);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> generator.generatePermutationsRecursiveAlt(null),
            "Should throw exception for null input"
        );
        
        assertTrue(exception.getMessage().contains("null"),
            "Error message should mention null");
    }
    
    // ==================== Iterative Algorithm Tests ====================
    
    @Test
    @DisplayName("Test iterative algorithm with basic string")
    void testIterativeBasic() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsIterative("ABC");
        
        assertEquals(6, result.size(), "Should generate 6 permutations");
        
        String[] expected = {"ABC", "ACB", "BAC", "BCA", "CAB", "CBA"};
        for (String perm : expected) {
            assertTrue(result.contains(perm), "Should contain permutation: " + perm);
        }
    }
    
    @Test
    @DisplayName("Test iterative with empty string")
    void testIterativeEmptyString() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsIterative("");
        
        assertEquals(1, result.size(), "Empty string should have 1 permutation");
        assertEquals("", result.get(0), "Should be empty string");
    }
    
    @Test
    @DisplayName("Test iterative with single character")
    void testIterativeSingleCharacter() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsIterative("X");
        
        assertEquals(1, result.size(), "Single character should have 1 permutation");
        assertEquals("X", result.get(0), "Should be the character");
    }
    
    @Test
    @DisplayName("Test iterative with duplicates excluded")
    void testIterativeDuplicatesExcluded() {
        StringPermutations generator = new StringPermutations(false);
        List<String> result = generator.generatePermutationsIterative("AAB");
        
        assertEquals(3, result.size(), "Should have 3 unique permutations");
        
        Set<String> uniqueSet = new HashSet<>(result);
        assertEquals(3, uniqueSet.size(), "All should be unique");
    }
    
    // ==================== Algorithm Consistency Tests ====================
    
    @Test
    @DisplayName("Test recursive vs iterative produce same results - small")
    void testRecursiveVsIterativeSmall() {
        StringPermutations generator = new StringPermutations(true);
        String input = "ABC";
        
        List<String> recursive = generator.generatePermutationsRecursive(input);
        List<String> iterative = generator.generatePermutationsIterative(input);
        
        assertEquals(recursive.size(), iterative.size(),
            "Both should generate same number of permutations");
        
        // Convert to sets for comparison (order might differ)
        Set<String> recursiveSet = new HashSet<>(recursive);
        Set<String> iterativeSet = new HashSet<>(iterative);
        
        assertEquals(recursiveSet, iterativeSet,
            "Both should generate same set of permutations");
    }
    
    @Test
    @DisplayName("Test recursive vs iterative produce same results - medium")
    void testRecursiveVsIterativeMedium() {
        StringPermutations generator = new StringPermutations(true);
        String input = "ABCD";
        
        List<String> recursive = generator.generatePermutationsRecursive(input);
        List<String> iterative = generator.generatePermutationsIterative(input);
        
        assertEquals(24, recursive.size(), "Should have 4! = 24 permutations");
        assertEquals(24, iterative.size(), "Should have 4! = 24 permutations");
        
        Set<String> recursiveSet = new HashSet<>(recursive);
        Set<String> iterativeSet = new HashSet<>(iterative);
        
        assertEquals(recursiveSet, iterativeSet,
            "Both algorithms should produce identical results");
    }
    
    @Test
    @DisplayName("Test alternative recursive vs main recursive consistency")
    void testAlternativeRecursiveConsistency() {
        StringPermutations generator = new StringPermutations(true);
        String input = "ABCD";
        
        List<String> main = generator.generatePermutationsRecursive(input);
        List<String> alt = generator.generatePermutationsRecursiveAlt(input);
        
        Set<String> mainSet = new HashSet<>(main);
        Set<String> altSet = new HashSet<>(alt);
        
        assertEquals(mainSet, altSet,
            "Both recursive implementations should produce same permutations");
    }
    
    // ==================== Factorial Correctness Tests ====================
    
    @Test
    @DisplayName("Test factorial correctness for 4 characters")
    void testFactorialCorrectnessFour() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("ABCD");
        
        assertEquals(24, result.size(), "4! should equal 24");
    }
    
    @Test
    @DisplayName("Test factorial correctness for 5 characters")
    void testFactorialCorrectnessFive() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("ABCDE");
        
        assertEquals(120, result.size(), "5! should equal 120");
    }
    
    @Test
    @DisplayName("Test factorial correctness for 6 characters")
    void testFactorialCorrectnessSix() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("ABCDEF");
        
        assertEquals(720, result.size(), "6! should equal 720");
    }
    
    @Test
    @DisplayName("Test factorial correctness for 7 characters")
    void testFactorialCorrectnessSeven() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("ABCDEFG");
        
        assertEquals(5040, result.size(), "7! should equal 5040");
    }
    
    // ==================== Character Type Tests ====================
    
    @Test
    @DisplayName("Test numeric string permutations")
    void testNumericString() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("123");
        
        assertEquals(6, result.size(), "Should generate 6 permutations");
        assertTrue(result.contains("123"), "Should contain 123");
        assertTrue(result.contains("321"), "Should contain 321");
        assertTrue(result.contains("132"), "Should contain 132");
        assertTrue(result.contains("213"), "Should contain 213");
        assertTrue(result.contains("231"), "Should contain 231");
        assertTrue(result.contains("312"), "Should contain 312");
    }
    
    @Test
    @DisplayName("Test special characters in string")
    void testSpecialCharacters() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("@#$");
        
        assertEquals(6, result.size(), "Special characters should work like normal chars");
        assertTrue(result.contains("@#$"), "Should contain original order");
        assertTrue(result.contains("$#@"), "Should contain reversed order");
    }
    
    @Test
    @DisplayName("Test mixed case string")
    void testMixedCase() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("AaBb");
        
        assertEquals(24, result.size(), "4! = 24 permutations");
        
        // Verify case is preserved
        for (String perm : result) {
            long upperCount = perm.chars().filter(Character::isUpperCase).count();
            long lowerCount = perm.chars().filter(Character::isLowerCase).count();
            assertEquals(2, upperCount, "Should have 2 uppercase letters");
            assertEquals(2, lowerCount, "Should have 2 lowercase letters");
        }
    }
    
    @Test
    @DisplayName("Test string with whitespace")
    void testStringWithWhitespace() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("A B");
        
        assertEquals(6, result.size(), "3! = 6 permutations including space");
        assertTrue(result.contains("A B"), "Should contain original with space");
        assertTrue(result.contains(" AB"), "Should contain space at start");
        assertTrue(result.contains("AB "), "Should contain space at end");
    }
    
    @Test
    @DisplayName("Test string with multiple spaces")
    void testMultipleSpaces() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("A  "); // A and two spaces
        
        assertEquals(6, result.size(), "Should treat each space as separate character");
    }
    
    // ==================== Permutation Property Tests ====================
    
    @Test
    @DisplayName("Test permutation length matches input")
    void testPermutationLength() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("ABCDE");
        
        for (String perm : result) {
            assertEquals(5, perm.length(), "Each permutation should have length 5");
        }
    }
    
    @Test
    @DisplayName("Test all permutations contain same characters")
    void testPermutationsContainSameCharacters() {
        StringPermutations generator = new StringPermutations(true);
        String input = "ABCD";
        List<String> result = generator.generatePermutationsRecursive(input);
        
        char[] originalChars = input.toCharArray();
        Arrays.sort(originalChars);
        
        for (String perm : result) {
            char[] permChars = perm.toCharArray();
            Arrays.sort(permChars);
            assertArrayEquals(originalChars, permChars,
                "Permutation should contain same characters as input");
        }
    }
    
    @Test
    @DisplayName("Test no duplicate permutations in unique mode")
    void testNoDuplicatesInUniqueMode() {
        StringPermutations generator = new StringPermutations(false);
        List<String> result = generator.generatePermutationsRecursive("AABBCC");
        
        Set<String> uniqueSet = new HashSet<>(result);
        assertEquals(uniqueSet.size(), result.size(),
            "Should have no duplicate permutations in unique mode");
    }
    
    @Test
    @DisplayName("Test permutations are complete set")
    void testPermutationsAreComplete() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("ABC");
        
        Set<String> resultSet = new HashSet<>(result);
        
        // Manually verify all 6 permutations exist
        assertTrue(resultSet.contains("ABC"));
        assertTrue(resultSet.contains("ACB"));
        assertTrue(resultSet.contains("BAC"));
        assertTrue(resultSet.contains("BCA"));
        assertTrue(resultSet.contains("CAB"));
        assertTrue(resultSet.contains("CBA"));
        assertEquals(6, resultSet.size(), "Should have exactly 6 unique permutations");
    }
    
    // ==================== Performance Tests ====================
    
    @Test
    @DisplayName("Test performance with moderate string length")
    void testPerformanceModerate() {
        StringPermutations generator = new StringPermutations(true);
        String input = "ABCDEF"; // 6! = 720 permutations
        
        long startTime = System.currentTimeMillis();
        List<String> result = generator.generatePermutationsRecursive(input);
        long duration = System.currentTimeMillis() - startTime;
        
        assertEquals(720, result.size(), "Should generate 720 permutations");
        assertTrue(duration < 1000, "Should complete in under 1 second (actual: " + duration + "ms)");
    }
    
    @Test
    @DisplayName("Test iterative performance vs recursive")
    void testIterativeVsRecursivePerformance() {
        StringPermutations generator = new StringPermutations(true);
        String input = "ABCDEF";
        
        // Test recursive
        long recStart = System.nanoTime();
        List<String> recResult = generator.generatePermutationsRecursive(input);
        long recTime = System.nanoTime() - recStart;
        
        // Test iterative
        long iterStart = System.nanoTime();
        List<String> iterResult = generator.generatePermutationsIterative(input);
        long iterTime = System.nanoTime() - iterStart;
        
        assertEquals(recResult.size(), iterResult.size(),
            "Both should generate same number of permutations");
        
        // Both should complete reasonably fast
        assertTrue(recTime < 1_000_000_000, "Recursive should complete in under 1 second");
        assertTrue(iterTime < 1_000_000_000, "Iterative should complete in under 1 second");
    }
    
    @Test
    @DisplayName("Test large input doesn't cause stack overflow")
    void testNoStackOverflow() {
        StringPermutations generator = new StringPermutations(true);
        
        // 8! = 40,320 permutations - should not overflow
        assertDoesNotThrow(() -> {
            List<String> result = generator.generatePermutationsRecursive("ABCDEFGH");
            assertEquals(40320, result.size(), "8! should equal 40320");
        }, "Should not cause stack overflow for reasonable input");
    }
    
    // ==================== Edge Case Tests ====================
    
    @Test
    @DisplayName("Test single repeated character (7 times)")
    void testSingleRepeatedCharacter() {
        StringPermutations generator = new StringPermutations(false);
        List<String> result = generator.generatePermutationsRecursive("AAAAAAA");
        
        assertEquals(1, result.size(), "All identical should have 1 unique permutation");
        assertEquals("AAAAAAA", result.get(0), "Should be original string");
    }
    
    @Test
    @DisplayName("Test alternating characters")
    void testAlternatingCharacters() {
        StringPermutations generator = new StringPermutations(false);
        List<String> result = generator.generatePermutationsRecursive("ABABAB");
        
        // Should have limited unique permutations
        Set<String> uniqueSet = new HashSet<>(result);
        assertEquals(uniqueSet.size(), result.size(),
            "All permutations should be unique in unique mode");
        
        // Verify permutations are correct
        for (String perm : result) {
            long aCount = perm.chars().filter(c -> c == 'A').count();
            long bCount = perm.chars().filter(c -> c == 'B').count();
            assertEquals(3, aCount, "Should have 3 A's");
            assertEquals(3, bCount, "Should have 3 B's");
        }
    }
    
    @Test
    @DisplayName("Test unicode characters")
    void testUnicodeCharacters() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("αβγ");
        
        assertEquals(6, result.size(), "Unicode should work like regular chars");
        assertTrue(result.contains("αβγ"), "Should contain original");
        assertTrue(result.contains("γβα"), "Should contain reversed");
    }
    
    @Test
    @DisplayName("Test empty result handling")
    void testEmptyResultHandling() {
        StringPermutations generator = new StringPermutations(true);
        List<String> result = generator.generatePermutationsRecursive("");
        
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should contain empty string permutation");
        assertEquals(1, result.size(), "Should have exactly one permutation");
    }
    
    // ==================== Alternative Implementation Tests ====================
    
    @Test
    @DisplayName("Test alternative recursive with various inputs")
    void testAlternativeRecursiveVariousInputs() {
        StringPermutations generator = new StringPermutations(true);
        
        // Test various lengths
        assertEquals(1, generator.generatePermutationsRecursiveAlt("").size());
        assertEquals(1, generator.generatePermutationsRecursiveAlt("A").size());
        assertEquals(2, generator.generatePermutationsRecursiveAlt("AB").size());
        assertEquals(6, generator.generatePermutationsRecursiveAlt("ABC").size());
        assertEquals(24, generator.generatePermutationsRecursiveAlt("ABCD").size());
    }
    
    @Test
    @DisplayName("Test alternative recursive with duplicates")
    void testAlternativeRecursiveWithDuplicates() {
        StringPermutations generator = new StringPermutations(false);
        List<String> result = generator.generatePermutationsRecursiveAlt("AAB");
        
        assertEquals(3, result.size(), "Should have 3 unique permutations");
        Set<String> uniqueSet = new HashSet<>(result);
        assertEquals(3, uniqueSet.size(), "All should be unique");
    }
    
    // ==================== Comprehensive Integration Tests ====================
    
    @Test
    @DisplayName("Test all three implementations produce consistent results")
    void testAllImplementationsConsistent() {
        StringPermutations generator = new StringPermutations(true);
        String input = "ABCD";
        
        List<String> recursive = generator.generatePermutationsRecursive(input);
        List<String> iterative = generator.generatePermutationsIterative(input);
        List<String> alternative = generator.generatePermutationsRecursiveAlt(input);
        
        Set<String> recursiveSet = new HashSet<>(recursive);
        Set<String> iterativeSet = new HashSet<>(iterative);
        Set<String> alternativeSet = new HashSet<>(alternative);
        
        assertEquals(recursiveSet, iterativeSet,
            "Recursive and iterative should match");
        assertEquals(recursiveSet, alternativeSet,
            "Recursive and alternative should match");
        assertEquals(24, recursiveSet.size(),
            "All should have 24 unique permutations");
    }
    
    @Test
    @DisplayName("Test correctness with systematic verification")
    void testCorrectnessSystematic() {
        StringPermutations generator = new StringPermutations(true);
        String input = "ABCD";
        List<String> result = generator.generatePermutationsRecursive(input);
        
        // Convert to set to check uniqueness
        Set<String> uniquePerms = new HashSet<>(result);
        
        // Should have exactly 24 unique permutations
        assertEquals(24, uniquePerms.size(), "Should have 24 unique permutations");
        
        // Every permutation should have length 4
        for (String perm : result) {
            assertEquals(4, perm.length(), "Each permutation should have length 4");
            
            // Each should contain exactly one of each character
            assertTrue(perm.contains("A"), "Should contain A");
            assertTrue(perm.contains("B"), "Should contain B");
            assertTrue(perm.contains("C"), "Should contain C");
            assertTrue(perm.contains("D"), "Should contain D");
        }
    }
}