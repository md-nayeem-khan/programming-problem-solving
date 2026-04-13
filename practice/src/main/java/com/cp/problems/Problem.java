package com.cp.problems;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate any solution class with metadata.
 *
 * Example:
 *   @Problem(
 *     id = "1",
 *     title = "Two Sum",
 *     platform = Problem.Platform.LEETCODE,
 *     difficulty = Problem.Difficulty.EASY,
 *     tags = {"Array", "HashTable"},
 *     url = "https://leetcode.com/problems/two-sum/"
 *   )
 *   public class P001_TwoSum extends BaseSolution { ... }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Problem {

    String id()         default "";
    String title()      default "";
    Platform platform() default Platform.LEETCODE;
    Difficulty difficulty() default Difficulty.MEDIUM;
    String[] tags()     default {};
    String url()        default "";
    String notes()      default "";

    enum Platform { LEETCODE, CODEFORCES, ATCODER, HACKERRANK, OTHER }

    enum Difficulty { EASY, MEDIUM, HARD, UNRATED }
}
