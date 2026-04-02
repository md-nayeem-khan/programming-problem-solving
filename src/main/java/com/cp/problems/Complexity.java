package com.cp.problems;

import java.lang.annotation.*;

/**
 * Annotation to document time and space complexity of algorithms and solutions.
 * 
 * Use this to clearly communicate algorithmic complexity during interviews.
 * Interviewers ALWAYS ask about complexity, so having this documented shows preparation.
 * 
 * Example usage:
 * <pre>
 * {@code
 * @Complexity(time = "O(n log n)", space = "O(1)")
 * public void mergeSort(int[] arr) {
 *     // implementation
 * }
 * }
 * </pre>
 * 
 * Common complexities:
 * - Time: O(1), O(log n), O(n), O(n log n), O(n²), O(2^n), O(n!)
 * - Space: O(1), O(log n), O(n), O(n²)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Documented
public @interface Complexity {
    /**
     * Time complexity in Big-O notation
     * Examples: "O(n)", "O(n log n)", "O(V + E)"
     */
    String time();
    
    /**
     * Space complexity in Big-O notation
     * Examples: "O(1)", "O(n)", "O(V)"
     */
    String space() default "O(1)";
    
    /**
     * Optional explanation of the complexity
     * Example: "Hash table lookups are O(1) average case"
     */
    String explanation() default "";
}
