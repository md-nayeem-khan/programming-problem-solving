
---

# 🧠 0. Product Goal (Anchor Everything to This)

app should answer:

```text
"Am I ready to pass a FAANG coding interview today?"
```

So every feature should support:

* Pattern mastery
* Speed
* Recall
* Interview simulation

---

# 🧱 1. Core Data Models (Foundation)

## 1.1 Problem Log Model

```ts
ProblemLog {
  id: string
  title: string
  source: "NeetCode" | "Company"
  company?: "Amazon" | "Google" | "Meta"
  difficulty: "Easy" | "Medium" | "Hard"
  pattern: string

  timeTaken: number // minutes
  solved: boolean
  wasHintUsed: boolean

  attemptType: "First" | "Revision"
  date: Date

  mistakeNote?: string
  approachNote?: string

  nextReviewDate?: Date
}
```

### 🔍 Why this matters:

* Tracks **quality of solving**, not just completion
* Enables **analytics + readiness scoring**
* Supports **revision system**

---

## 1.2 Pattern Model

```ts
PatternStats {
  pattern: string
  totalSolved: number
  avgTime: number
  hintUsageRate: number
  confidence: "Weak" | "Medium" | "Strong"
}
```

### 🔍 Why this matters:

* Core signal of **interview readiness**
* Identifies weak areas (e.g., Graphs, DP)

---

## 1.3 Mock Interview Model

```ts
MockInterview {
  id: string
  date: Date
  problemTitle: string
  timeLimit: number // usually 45 min

  solved: boolean
  timeTaken: number
  explanationScore: number // 1–5
  codeQualityScore: number // 1–5
}
```

### 🔍 Why this matters:

* Simulates real FAANG conditions
* Tracks **performance under pressure**

---

# 📊 2. Core Features (MVP)

---

## ✅ 2.1 Problem Logging System

### Features:

* Manual entry form
* Tag problem with:

  * pattern
  * difficulty
  * company
* Record:

  * time taken
  * hint usage
  * notes

### UX idea:

* Fast entry (like Notion quick add)
* Keyboard-first input

### 🎯 Goal:

Track **how well** you solved, not just *what* you solved.

---

## ✅ 2.2 Pattern Tracking Dashboard

### Display:

```text
Pattern        Solved   Avg Time   Confidence
-------------------------------------------
Sliding Window   10       22 min      Strong
Graphs           5        40 min      Weak
DP               3        50 min      Weak
```

### Logic:

```ts
if (avgTime > 35 || hintUsageRate > 0.4)
  → Weak
else if (avgTime < 25 && hintUsageRate < 0.2)
  → Strong
```

### 🎯 Goal:

Convert raw solving into **pattern mastery insight**

---

## ✅ 2.3 Time Performance Analytics

### Metrics:

* Avg time per problem
* Avg time per difficulty
* Avg time per pattern

### Benchmarks:

```text
Easy   ≤ 15 min
Medium ≤ 25 min
Hard   ≤ 40 min
```

### 🎯 Goal:

Ensure **interview speed readiness**

---

## ✅ 2.4 Interview Readiness Score (Core Feature)

### Scoring system:

```ts
score =
  +2 → solved <25 min without hint
  +1 → solved with hint
  0  → failed
```

### Final score:

```ts
readiness = totalScore / (2 * totalProblems)
```

### Output:

```text
0.8+   → Ready
0.6–0.8 → Almost Ready
<0.6   → Not Ready
```

### 🎯 Goal:

Give a **clear yes/no signal**

---

## 🔁 3. Revision System (Spaced Repetition)

---

## ✅ 3.1 Auto Review Scheduler

### Logic:

```text
After solving:
+1 day
+3 days
+7 days
+14 days
```

### Data:

```ts
nextReviewDate: Date
```

### UI:

* “Today’s Revision” list

---

## ✅ 3.2 Revision Tracking

Track:

* Was it solved again without help?
* Time improvement?

### 🎯 Goal:

Convert short-term solving → long-term memory

---

# 🧠 4. Intelligence Layer (High Impact Features)

---

## 🔥 4.1 Weakness Detection Engine

### Logic:

```ts
if (avgTime > threshold AND hintUsageRate high)
  → mark as Weak
```

### Output:

```text
Weak Areas:
- Graph BFS/DFS
- Dynamic Programming
```

---

## 🔥 4.2 Smart Recommendations

Based on weakness:

```text
"You should solve 5 more Graph problems this week"
```

---

## 🔥 4.3 Pattern Recognition Speed Tracker

Add field:

```ts
patternRecognitionTime: number
```

### Target:

```text
< 2 minutes
```

---

# 🎯 5. Company-Specific Readiness

---

## ✅ 5.1 Company Dashboard

Example:

```text
Amazon:
- Solved: 70/100
- Avg Time: 27 min
- Ready: YES

Google:
- Solved: 30/100
- Avg Time: 35 min
- Ready: NO
```

### 🎯 Goal:

Align prep with **actual interview pipelines**

---

# 🎭 6. Mock Interview Mode

---

## ✅ 6.1 Timed Simulation

* 45-minute timer
* Random problem
* No hints allowed

---

## ✅ 6.2 Evaluation Metrics

Track:

* completion
* time
* explanation score

---

## ✅ 6.3 History Tracking

```text
Mock #1 → Fail
Mock #5 → Pass
Mock #10 → Strong Pass
```

### 🎯 Goal:

Prepare for **real pressure**

---

# 📅 7. Consistency & Discipline Features

---

## ✅ 7.1 Daily Streak

```text
🔥 14-day streak
```

---

## ✅ 7.2 Weekly Progress

```text
This week:
- 18 problems solved
- 4 patterns improved
```

---

# 🧾 8. Mistake & Learning Journal

---

## ✅ 8.1 Mistake Notes

Examples:

```text
- Forgot edge case for empty input
- Wrong sliding window shrink logic
```

---

## ✅ 8.2 Weekly Review Page

Show:

* most common mistakes
* repeated patterns of failure

### 🎯 Goal:

Accelerate learning via reflection

---

# 🧩 9. Suggested App Structure (Next.js)

```text
/app
  /dashboard
  /problems
  /patterns
  /company
  /revision
  /mock
  /analytics
```

---

# 🚀 10. MVP vs Advanced (Build Order)

## Phase 1 (MVP)

* Problem Log
* Pattern Tracking
* Basic Analytics

## Phase 2

* Readiness Score
* Revision System
* Weakness Detection

## Phase 3

* Mock Interviews
* Company Dashboard
* Smart Recommendations

---

# 🧠 Final Insight (Most Important)

If you implement only ONE killer feature, make it:

> 🎯 **Pattern-wise confidence + readiness score**

Because:

```text
FAANG interviews = pattern recognition speed + execution
```

Not:

```text
total problems solved
```

---




---

# 🧭 1. Overall Layout Structure

```text
--------------------------------------------------
| Sidebar        | Top Navbar                    |
|                ---------------------------------
|                | Dashboard Content            |
|                |                              |
|                |  (Grid Layout Sections)      |
--------------------------------------------------
```

---

# 📁 2. Sidebar (Left Navigation)

```text
--------------------------------
| LOGO / App Name              |
|------------------------------|
| 🏠 Dashboard                |
| 📚 Problems                 |
| 🧠 Patterns                 |
| 🏢 Company                  |
| 🔁 Revision                 |
| 🎯 Mock Interviews          |
| 📊 Analytics                |
|------------------------------|
| ⚙️ Settings                 |
--------------------------------
```

👉 Keep it **simple + fast navigation**

---

# 🔝 3. Top Navbar

```text
--------------------------------------------------
| 🔍 Search Problem   | 🔔 | 👤 Profile |
--------------------------------------------------
```

Optional:

* Quick add button: `+ Add Problem`

---

# 📊 4. Main Dashboard Layout (Grid System)

Use a **2-column responsive grid**

```text
--------------------------------------------------
| Readiness Score      | Daily Progress           |
--------------------------------------------------
| Pattern Strength     | Weak Areas               |
--------------------------------------------------
| Time Performance     | Company Readiness        |
--------------------------------------------------
| Revision Queue       | Mock Interview Progress  |
--------------------------------------------------
```

---

# 🧠 5. Section-by-Section Wireframe

---

## 🟢 5.1 Readiness Score (TOP PRIORITY)

```text
-----------------------------------------
| 🎯 Interview Readiness                |
|---------------------------------------|
| Score: 0.72                          |
| Status: 🟡 Almost Ready              |
|                                       |
| ███████████████░░░░░                  |
|                                       |
| Tips:                                 |
| - Improve Graph problems              |
| - Reduce avg time to <25 min          |
-----------------------------------------
```

👉 This is your **main KPI card**

---

## 📅 5.2 Daily Progress

```text
-----------------------------------------
| 📅 Today                              |
|---------------------------------------|
| Problems Solved: 3                    |
| Time Spent: 2.1 hrs                  |
| Streak: 🔥 12 days                   |
|                                       |
| Goal: 2 problems/day ✅               |
-----------------------------------------
```

---

## 🧠 5.3 Pattern Strength

```text
-----------------------------------------
| 🧠 Pattern Strength                  |
|---------------------------------------|
| Sliding Window   ██████████  Strong   |
| Trees            ███████░░░  Medium   |
| Graphs           ███░░░░░░░  Weak     |
| DP               ██░░░░░░░░  Weak     |
-----------------------------------------
```

👉 Use:

* Progress bars
* Color coding (green/yellow/red)

---

## ⚠️ 5.4 Weak Areas (Auto Generated)

```text
-----------------------------------------
| ⚠️ Weak Areas                        |
|---------------------------------------|
| 1. Graph BFS/DFS                     |
|    - Avg Time: 42 min                |
|    - Hint Usage: High                |
|                                       |
| 2. Dynamic Programming               |
|    - Low completion rate             |
-----------------------------------------
```

👉 This is your **action panel**

---

## ⏱️ 5.5 Time Performance

```text
-----------------------------------------
| ⏱️ Time Performance                 |
|---------------------------------------|
| Easy Avg:   12 min ✅               |
| Medium Avg: 32 min ⚠️               |
| Hard Avg:   50 min ❌               |
|                                       |
| Target: Medium ≤ 25 min              |
-----------------------------------------
```

Optional:

* small line chart

---

## 🏢 5.6 Company Readiness

```text
-----------------------------------------
| 🏢 Company Readiness                |
|---------------------------------------|
| Amazon     █████████░  75%  🟡        |
| Google     ██████░░░░  50%  🔴        |
| Meta       ████░░░░░░  40%  🔴        |
-----------------------------------------
```

👉 Helps decide **where to apply first**

---

## 🔁 5.7 Revision Queue

```text
-----------------------------------------
| 🔁 Revision Today                   |
|---------------------------------------|
| 1. Longest Substring                |
| 2. Course Schedule                 |
| 3. LRU Cache                        |
|                                       |
| [Start Revision]                     |
-----------------------------------------
```

---

## 🎯 5.8 Mock Interview Progress

```text
-----------------------------------------
| 🎯 Mock Interviews                 |
|---------------------------------------|
| Completed: 6                        |
| Passed: 3                           |
|                                       |
| Recent:                              |
| #6 → Pass ✅                        |
| #5 → Fail ❌                        |
-----------------------------------------
```

---

# 🎨 6. UI Design Guidelines

---

## Colors (keep minimal)

```text
Green  → Strong / Good
Yellow → Medium / Warning
Red    → Weak / Needs work
Gray   → Neutral
```

---

## Typography

* Headings → Bold
* Metrics → Large font (important!)
* Labels → Small + subtle

---

## Spacing

* Use **cards with padding**
* Avoid clutter
* Each section = 1 clear purpose

---

# ⚡ 7. UX Enhancements (High Impact)

---

## 🔥 Quick Add Modal

```text
[ + Add Problem ]

→ Opens modal:
- Title
- Pattern
- Time taken
- Hint used
- Notes
```

---

## 🔥 Hover Insights

Hover on pattern:

```text
Graphs:
- Avg time: 42 min
- Problems solved: 5
- Confidence: Weak
```

---

## 🔥 Click Drill-down

Click "Graphs" →
→ See all graph problems + mistakes

---

# 🧩 8. Suggested Component Breakdown (Next.js)

```text
/components
  /dashboard
    ReadinessCard.tsx
    PatternChart.tsx
    WeaknessPanel.tsx
    TimeStats.tsx
    CompanyStats.tsx
    RevisionList.tsx
    MockStats.tsx
```

---

# 🧠 Final Design Principle

Your dashboard should feel like:

```text
A control panel for your interview readiness
```

Not:

```text
A list of solved problems
```

---

