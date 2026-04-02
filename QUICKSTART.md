# Quick Start - Problem Generator

## TL;DR
```powershell
# Create LeetCode problem
.\scripts\new-problem.ps1 -Id 1 -Title "Two Sum" -Platform leetcode -Difficulty easy

# Create Codeforces problem  
.\scripts\new-problem.ps1 -Id "4A" -Title "Watermelon" -Platform codeforces

# Preview first (recommended)
.\scripts\new-problem.ps1 -Id 42 -Title "Test" -Platform leetcode -DryRun
```

## Required Parameters
- **-Id**: Problem number (e.g., `1`, `42`, `4A`)
- **-Title**: Problem name (e.g., `"Two Sum"`)
- **-Platform**: `leetcode`, `codeforces`, `atcoder`, `hackerrank`, or `other`

## Optional Parameters
- **-Difficulty**: `easy`, `medium`, `hard`, `unrated` (default: `medium`)
- **-Tags**: `"Array,HashTable,DFS"` (comma-separated, no spaces)
- **-Url**: Problem link
- **-DryRun**: Preview without creating files

## What You Get
✅ Solution file with `@Problem` annotation  
✅ Test file with proper test structure  
✅ Test case template with correct format  
✅ All directories created automatically  
✅ Platform-specific naming conventions

## Next Steps
1. Run the script to generate files
2. Implement your solution in the `.java` file
3. Add test cases to the `.txt` file
4. Run: `mvn test -Dtest=P{id}_{Title}Test`

## Examples

**Full LeetCode Problem:**
```powershell
.\scripts\new-problem.ps1 `
    -Id 1 `
    -Title "Two Sum" `
    -Platform leetcode `
    -Difficulty easy `
    -Tags "Array,HashTable" `
    -Url "https://leetcode.com/problems/two-sum/"
```

**Simple Codeforces:**
```powershell
.\scripts\new-problem.ps1 -Id "1234B" -Title "Sample" -Platform codeforces
```

For more details: See `scripts/README.md`
