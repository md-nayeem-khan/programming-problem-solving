# Problem Generator - Quick Reference

## Basic Usage

```powershell
.\scripts\new-problem.ps1 -Id <id> -Title <title> -Platform <platform> [options]
```

## Parameters

| Parameter | Required | Description | Example |
|-----------|----------|-------------|---------|
| `-Id` | ✅ Yes | Problem ID | `1`, `42`, `4A`, `1234B` |
| `-Title` | ✅ Yes | Problem title | `"Two Sum"`, `"Watermelon"` |
| `-Platform` | ✅ Yes | Platform (`leetcode`, `codeforces`, `atcoder`, `hackerrank`, `other`) | `leetcode` |
| `-Difficulty` | ❌ No | Difficulty (`easy`, `medium`, `hard`, `unrated`). Default: `medium` | `easy` |
| `-Tags` | ❌ No | Comma-separated tags | `"Array,HashTable"` |
| `-Url` | ❌ No | Problem URL | `"https://leetcode.com/problems/two-sum/"` |
| `-DryRun` | ❌ No | Preview without creating files | (switch) |

## Examples

### LeetCode Problem
```powershell
.\scripts\new-problem.ps1 `
    -Id 1 `
    -Title "Two Sum" `
    -Platform leetcode `
    -Difficulty easy `
    -Tags "Array,HashTable" `
    -Url "https://leetcode.com/problems/two-sum/"
```

**Creates:**
- `src/main/java/com/cp/problems/leetcode/P001_TwoSum.java`
- `src/test/java/com/cp/problems/leetcode/P001_TwoSumTest.java`
- `src/test/resources/testcases/leetcode/001_two_sum.txt`

### Codeforces Problem
```powershell
.\scripts\new-problem.ps1 `
    -Id "4A" `
    -Title "Watermelon" `
    -Platform codeforces `
    -Difficulty easy `
    -Tags "Math,Brute Force"
```

**Creates:**
- `src/main/java/com/cp/problems/codeforces/CF_4_A.java`
- `src/test/java/com/cp/problems/codeforces/CF_4_A_Test.java`
- `src/test/resources/testcases/codeforces/4_A.txt`

### Minimal Example
```powershell
.\scripts\new-problem.ps1 -Id 42 -Title "Test Problem" -Platform leetcode
```

### Dry Run (Preview Only)
```powershell
.\scripts\new-problem.ps1 -Id 99 -Title "Preview" -Platform leetcode -DryRun
```

## File Naming Conventions

### LeetCode
- Solution: `P{padded_id}_{TitleCamelCase}.java` (e.g., `P001_TwoSum.java`)
- Test: `P{padded_id}_{TitleCamelCase}Test.java`
- Test cases: `{padded_id}_{title_snake_case}.txt` (e.g., `001_two_sum.txt`)

### Codeforces
- Solution: `CF_{id}.java` (e.g., `CF_4_A.java`)
- Test: `CF_{id}_Test.java`
- Test cases: `{id}.txt` (e.g., `4_A.txt`)

### Other Platforms
- Solution: `{TitleCamelCase}.java`
- Test: `{TitleCamelCase}Test.java`
- Test cases: `{title_snake_case}.txt`

## What Gets Generated

### 1. Solution File
- Package declaration
- Imports (`BaseSolution`, `Problem` annotation)
- `@Problem` annotation with all metadata
- Platform-specific boilerplate:
  - **LeetCode**: Empty class extending `BaseSolution`
  - **Codeforces**: `solve()` method + `main()` + `runStdin()`
- TODO comments for guidance

### 2. Test File
- Package and imports
- `@DisplayName` with problem info
- Platform-specific test structure:
  - **LeetCode**: Parameterized test with `TestCaseLoader`
  - **Codeforces**: Single test with `SolutionRunner`
- TODO comments showing how to parse inputs/outputs

### 3. Test Case Template
- Header with problem info
- Platform-specific format examples:
  - **LeetCode**: `INPUT/OUTPUT` blocks with key=value pairs
  - **Codeforces**: `INPUT/OUTPUT` blocks with raw lines

## Next Steps After Generation

1. **Implement solution** in the generated `.java` file
2. **Add test cases** to the `.txt` file (copy from problem description)
3. **Update test file** to parse inputs and call your solution method
4. **Run tests**: `mvn test -Dtest=P{id}_{Title}Test`

## Tips

- Use `-DryRun` first to preview what will be generated
- The script won't overwrite files without confirmation
- All directories are created automatically
- Tags are comma-separated without spaces: `"Array,HashTable"` not `"Array, HashTable"`
- For multi-word titles, use quotes: `"Add Two Numbers"`

## Help

```powershell
Get-Help .\scripts\new-problem.ps1
Get-Help .\scripts\new-problem.ps1 -Detailed
Get-Help .\scripts\new-problem.ps1 -Examples
```
