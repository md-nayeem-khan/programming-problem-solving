#!/usr/bin/env pwsh
<#
.SYNOPSIS
    Generate a new competitive programming problem with solution, test, and test case files.

.DESCRIPTION
    Creates a new problem following the project's conventions:
    - Solution file in src/main/java/com/cp/problems/{platform}/
    - Test file in src/test/java/com/cp/problems/{platform}/
    - Test case file in src/test/resources/testcases/{platform}/

.PARAMETER Id
    Problem ID (required). Examples: "1", "42", "4A", "1234B"

.PARAMETER Title
    Problem title (required). Examples: "Two Sum", "Watermelon"

.PARAMETER Platform
    Platform name (required). Valid values: leetcode, codeforces, atcoder, hackerrank, other

.PARAMETER Difficulty
    Problem difficulty (optional). Valid values: easy, medium, hard, unrated. Default: medium

.PARAMETER Tags
    Comma-separated list of tags (optional). Examples: "Array,HashTable", "Math,Greedy"

.PARAMETER Url
    Problem URL (optional). Example: "https://leetcode.com/problems/two-sum/"

.PARAMETER DryRun
    Preview what will be created without writing files

.EXAMPLE
    .\new-problem.ps1 -Id 1 -Title "Two Sum" -Platform leetcode -Difficulty easy -Tags "Array,HashTable" -Url "https://leetcode.com/problems/two-sum/"

.EXAMPLE
    .\new-problem.ps1 -Id "4A" -Title "Watermelon" -Platform codeforces -Difficulty easy -Tags "Math,Brute Force"

.EXAMPLE
    .\new-problem.ps1 -Id 42 -Title "Test Problem" -Platform leetcode -DryRun
#>

param(
    [Parameter(Mandatory=$true)]
    [string]$Id,
    
    [Parameter(Mandatory=$true)]
    [string]$Title,
    
    [Parameter(Mandatory=$true)]
    [ValidateSet("leetcode", "codeforces", "atcoder", "hackerrank", "other", IgnoreCase=$true)]
    [string]$Platform,
    
    [Parameter(Mandatory=$false)]
    [ValidateSet("easy", "medium", "hard", "unrated", IgnoreCase=$true)]
    [string]$Difficulty = "medium",
    
    [Parameter(Mandatory=$false)]
    [string]$Tags = "",
    
    [Parameter(Mandatory=$false)]
    [string]$Url = "",
    
    [Parameter(Mandatory=$false)]
    [switch]$DryRun
)

# Set error action preference
$ErrorActionPreference = "Stop"

# Get project root (parent of scripts directory)
$ProjectRoot = Split-Path -Parent $PSScriptRoot

# ============================================================================
# Helper Functions
# ============================================================================

function ConvertTo-CamelCase {
    param([string]$Text)
    
    # Split on spaces, hyphens, underscores
    $words = $Text -split '[\s\-_]+'
    
    # Capitalize first letter of each word, join without spaces
    $result = ($words | ForEach-Object {
        if ($_.Length -gt 0) {
            $_.Substring(0,1).ToUpper() + $_.Substring(1).ToLower()
        }
    }) -join ''
    
    return $result
}

function ConvertTo-SnakeCase {
    param([string]$Text)
    
    # Convert to lowercase and replace spaces/hyphens with underscores
    $result = $Text.ToLower() -replace '[\s\-]+', '_'
    
    # Remove any characters that aren't alphanumeric or underscore
    $result = $result -replace '[^a-z0-9_]', ''
    
    return $result
}

function Format-ProblemId {
    param([string]$Id, [string]$Platform)
    
    if ($Platform -eq "leetcode") {
        # Pad with leading zeros to 3 digits
        return $Id.PadLeft(3, '0')
    }
    
    return $Id
}

function Get-JavaTags {
    param([string]$Tags)
    
    if ([string]::IsNullOrWhiteSpace($Tags)) {
        return '{}'
    }
    
    $tagArray = $Tags -split ',' | ForEach-Object { 
        $_.Trim() 
    } | Where-Object { 
        $_.Length -gt 0 
    }
    
    $quotedTags = $tagArray | ForEach-Object { "`"$_`"" }
    return "{$($quotedTags -join ', ')}"
}

function Get-PlatformEnum {
    param([string]$Platform)
    
    switch ($Platform.ToLower()) {
        "leetcode"    { return "LEETCODE" }
        "codeforces"  { return "CODEFORCES" }
        "atcoder"     { return "ATCODER" }
        "hackerrank"  { return "HACKERRANK" }
        default       { return "OTHER" }
    }
}

function Get-DifficultyEnum {
    param([string]$Difficulty)
    
    switch ($Difficulty.ToLower()) {
        "easy"     { return "EASY" }
        "medium"   { return "MEDIUM" }
        "hard"     { return "HARD" }
        default    { return "UNRATED" }
    }
}

# ============================================================================
# File Name Generation
# ============================================================================

function Get-FileNames {
    param($Config)
    
    $platformLower = $Config.Platform.ToLower()
    $formattedId = Format-ProblemId -Id $Config.Id -Platform $platformLower
    $titleCamel = ConvertTo-CamelCase -Text $Config.Title
    $titleSnake = ConvertTo-SnakeCase -Text $Config.Title
    
    $result = @{}
    
    if ($platformLower -eq "leetcode") {
        $result.SolutionFileName = "P${formattedId}_${titleCamel}.java"
        $result.TestFileName = "P${formattedId}_${titleCamel}Test.java"
        $result.TestCaseFileName = "${formattedId}_${titleSnake}.txt"
        $result.ClassName = "P${formattedId}_${titleCamel}"
    }
    elseif ($platformLower -eq "codeforces") {
        $cleanId = $Config.Id -replace '[^a-zA-Z0-9_]', '_'
        $result.SolutionFileName = "CF_${cleanId}.java"
        $result.TestFileName = "CF_${cleanId}_Test.java"
        $result.TestCaseFileName = "${cleanId}.txt"
        $result.ClassName = "CF_${cleanId}"
    }
    else {
        # Generic format for other platforms
        $result.SolutionFileName = "${titleCamel}.java"
        $result.TestFileName = "${titleCamel}Test.java"
        $result.TestCaseFileName = "${titleSnake}.txt"
        $result.ClassName = $titleCamel
    }
    
    return $result
}

function Get-FilePaths {
    param($Config, $FileNames)
    
    $platformLower = $Config.Platform.ToLower()
    
    return @{
        SolutionDir = Join-Path $ProjectRoot "src\main\java\com\cp\problems\$platformLower"
        TestDir = Join-Path $ProjectRoot "src\test\java\com\cp\problems\$platformLower"
        TestCaseDir = Join-Path $ProjectRoot "src\test\resources\testcases\$platformLower"
        SolutionFile = Join-Path $ProjectRoot "src\main\java\com\cp\problems\$platformLower\$($FileNames.SolutionFileName)"
        TestFile = Join-Path $ProjectRoot "src\test\java\com\cp\problems\$platformLower\$($FileNames.TestFileName)"
        TestCaseFile = Join-Path $ProjectRoot "src\test\resources\testcases\$platformLower\$($FileNames.TestCaseFileName)"
    }
}

# ============================================================================
# Template Generation
# ============================================================================

function New-SolutionTemplate {
    param($Config, $FileNames)
    
    $platformLower = $Config.Platform.ToLower()
    $platformEnum = Get-PlatformEnum -Platform $platformLower
    $difficultyEnum = Get-DifficultyEnum -Difficulty $Config.Difficulty
    $javaTags = Get-JavaTags -Tags $Config.Tags
    $className = $FileNames.ClassName
    
    $urlLine = if ($Config.Url) { "        url = `"$($Config.Url)`"" } else { "" }
    
    if ($platformLower -eq "leetcode") {
        return @"
package com.cp.problems.$platformLower;

import com.cp.problems.BaseSolution;
import com.cp.problems.Problem;

/**
 * $($Config.Platform) #$($Config.Id) — $($Config.Title)
 *
 * TODO: Add problem description
 *
 * TODO: Add solution approach
 */
@Problem(
        id = "$($Config.Id)",
        title = "$($Config.Title)",
        platform = Problem.Platform.$platformEnum,
        difficulty = Problem.Difficulty.$difficultyEnum,
        tags = $javaTags$(if ($urlLine) { "," })
$(if ($urlLine) { $urlLine })
)
public class $className extends BaseSolution {

    // TODO: Implement your solution method(s) here
    public void solve() {
        // Your code here
    }
}
"@
    }
    else {
        # Codeforces or other stdin/stdout platforms
        return @"
package com.cp.problems.$platformLower;

import com.cp.problems.BaseSolution;
import com.cp.problems.Problem;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * $($Config.Title)
 *
 * TODO: Add problem description
 *
 * TODO: Add solution approach
 */
@Problem(
        id = "$($Config.Id)",
        title = "$($Config.Title)",
        platform = Problem.Platform.$platformEnum,
        difficulty = Problem.Difficulty.$difficultyEnum,
        tags = $javaTags$(if ($urlLine) { "," })
$(if ($urlLine) { $urlLine })
)
public class $className extends BaseSolution {

    @Override
    public void solve(Scanner in, PrintWriter out) {
        // TODO: Read input and write output
        // Example:
        // int n = in.nextInt();
        // out.println(result);
    }

    public static void main(String[] args) {
        new $className().runStdin();
    }

    public void runStdin() {
        try (PrintWriter out = new PrintWriter(System.out)) {
            solve(new Scanner(System.in), out);
        }
    }
}
"@
    }
}

function New-TestTemplate {
    param($Config, $FileNames)
    
    $platformLower = $Config.Platform.ToLower()
    $className = $FileNames.ClassName
    $testCaseFileName = $FileNames.TestCaseFileName
    
    if ($platformLower -eq "leetcode") {
        $formattedId = Format-ProblemId -Id $Config.Id -Platform $platformLower
        return @"
package com.cp.problems.$platformLower;

import com.cp.testcases.InputParser;
import com.cp.testcases.TestCaseLoader;
import com.cp.testcases.TestCaseLoader.TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("$($Config.Platform) #$($Config.Id) — $($Config.Title)")
class $($className)Test {

    private final $className solution = new $className();

    // Load test cases once; stream indices as parameter source
    private static final List<TestCase> CASES =
            TestCaseLoader.load("$platformLower/$testCaseFileName");

    static Stream<Integer> caseIndices() {
        return IntStream.range(0, CASES.size()).boxed();
    }

    @ParameterizedTest(name = "case #{index}")
    @MethodSource("caseIndices")
    void test(int i) {
        TestCase tc = CASES.get(i);
        
        // TODO: Parse input parameters
        // Example:
        // int[] nums = InputParser.parseIntArray(tc.getParam("nums"));
        // int target = InputParser.parseInt(tc.getParam("target"));
        
        // TODO: Parse expected output
        // Example:
        // int[] expected = InputParser.parseIntArray(tc.output);
        
        // TODO: Call solution method and assert
        // Example:
        // int[] actual = solution.solve(nums, target);
        // assertArrayEquals(expected, actual);
    }
}
"@
    }
    else {
        # Codeforces or other stdin/stdout platforms
        return @"
package com.cp.problems.$platformLower;

import com.cp.testcases.SolutionRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("$($Config.Platform) #$($Config.Id) — $($Config.Title)")
class $($className)Test {

    @Test
    void testAllCases() {
        SolutionRunner.runAndAssert(new $className(), "$platformLower/$testCaseFileName");
    }
}
"@
    }
}

function New-TestCaseTemplate {
    param($Config)
    
    $platformLower = $Config.Platform.ToLower()
    
    if ($platformLower -eq "leetcode") {
        return @"
# $($Config.Platform) #$($Config.Id) - $($Config.Title)
# Format: INPUT block with key=value pairs, OUTPUT block with expected result

INPUT
param1 = value1
param2 = value2
OUTPUT
expected_output

INPUT
param1 = value1
param2 = value2
OUTPUT
expected_output
"@
    }
    else {
        # Codeforces or other stdin/stdout platforms
        return @"
# $($Config.Platform) #$($Config.Id) - $($Config.Title)
# Format: Each test case is INPUT followed by OUTPUT

INPUT
input_line_1
input_line_2
OUTPUT
expected_output_line_1
expected_output_line_2

INPUT
input_line_1
OUTPUT
expected_output_line_1
"@
    }
}

# ============================================================================
# Main Logic
# ============================================================================

function Main {
    Write-Host "🚀 Problem Generator" -ForegroundColor Cyan
    Write-Host ("=" * 60) -ForegroundColor Gray
    
    # Configuration
    $config = @{
        Id = $Id
        Title = $Title
        Platform = $Platform.ToLower()
        Difficulty = $Difficulty.ToLower()
        Tags = $Tags
        Url = $Url
    }
    
    # Generate file names
    $fileNames = Get-FileNames -Config $config
    $filePaths = Get-FilePaths -Config $config -FileNames $fileNames
    
    # Display summary
    Write-Host "`n📋 Configuration:" -ForegroundColor Yellow
    Write-Host "   ID:         $($config.Id)"
    Write-Host "   Title:      $($config.Title)"
    Write-Host "   Platform:   $($config.Platform)"
    Write-Host "   Difficulty: $($config.Difficulty)"
    Write-Host "   Tags:       $(if ($config.Tags) { $config.Tags } else { '(none)' })"
    Write-Host "   URL:        $(if ($config.Url) { $config.Url } else { '(none)' })"
    
    Write-Host "`n📁 Files to create:" -ForegroundColor Yellow
    Write-Host "   Solution:   $($filePaths.SolutionFile)"
    Write-Host "   Test:       $($filePaths.TestFile)"
    Write-Host "   Test cases: $($filePaths.TestCaseFile)"
    
    if ($DryRun) {
        Write-Host "`n🔍 DRY RUN MODE - No files will be created" -ForegroundColor Magenta
        Write-Host "`n--- Solution File ($($fileNames.SolutionFileName)) ---" -ForegroundColor Green
        Write-Host (New-SolutionTemplate -Config $config -FileNames $fileNames)
        Write-Host "`n--- Test File ($($fileNames.TestFileName)) ---" -ForegroundColor Green
        Write-Host (New-TestTemplate -Config $config -FileNames $fileNames)
        Write-Host "`n--- Test Case File ($($fileNames.TestCaseFileName)) ---" -ForegroundColor Green
        Write-Host (New-TestCaseTemplate -Config $config)
        return
    }
    
    # Check if files already exist
    $existingFiles = @()
    if (Test-Path $filePaths.SolutionFile) { $existingFiles += "Solution" }
    if (Test-Path $filePaths.TestFile) { $existingFiles += "Test" }
    if (Test-Path $filePaths.TestCaseFile) { $existingFiles += "TestCase" }
    
    if ($existingFiles.Count -gt 0) {
        Write-Host "`n⚠️  WARNING: The following files already exist:" -ForegroundColor Red
        $existingFiles | ForEach-Object { Write-Host "   - $_" -ForegroundColor Red }
        $response = Read-Host "`nOverwrite? (y/N)"
        if ($response -ne 'y' -and $response -ne 'Y') {
            Write-Host "❌ Aborted" -ForegroundColor Red
            return
        }
    }
    
    # Create directories
    Write-Host "`n📂 Creating directories..." -ForegroundColor Yellow
    @($filePaths.SolutionDir, $filePaths.TestDir, $filePaths.TestCaseDir) | ForEach-Object {
        if (-not (Test-Path $_)) {
            New-Item -ItemType Directory -Path $_ -Force | Out-Null
            Write-Host "   ✓ Created: $_" -ForegroundColor Green
        }
    }
    
    # Generate and write files
    Write-Host "`n✍️  Generating files..." -ForegroundColor Yellow
    
    $solutionContent = New-SolutionTemplate -Config $config -FileNames $fileNames
    $solutionContent | Out-File -FilePath $filePaths.SolutionFile -Encoding UTF8
    Write-Host "   ✓ Created: $($fileNames.SolutionFileName)" -ForegroundColor Green
    
    $testContent = New-TestTemplate -Config $config -FileNames $fileNames
    $testContent | Out-File -FilePath $filePaths.TestFile -Encoding UTF8
    Write-Host "   ✓ Created: $($fileNames.TestFileName)" -ForegroundColor Green
    
    $testCaseContent = New-TestCaseTemplate -Config $config
    $testCaseContent | Out-File -FilePath $filePaths.TestCaseFile -Encoding UTF8
    Write-Host "   ✓ Created: $($fileNames.TestCaseFileName)" -ForegroundColor Green
    
    Write-Host "`n✅ Success! Problem files created." -ForegroundColor Green
    Write-Host "`n📝 Next steps:" -ForegroundColor Cyan
    Write-Host "   1. Open $($fileNames.SolutionFileName) and implement your solution"
    Write-Host "   2. Add test cases to $($fileNames.TestCaseFileName)"
    Write-Host "   3. Update the test file if needed: $($fileNames.TestFileName)"
    Write-Host "   4. Run tests: mvn test -Dtest=$($fileNames.ClassName)Test"
}

# Run main
Main
