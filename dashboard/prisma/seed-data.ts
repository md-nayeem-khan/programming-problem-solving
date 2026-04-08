import { PrismaClient } from '@prisma/client'

const prisma = new PrismaClient()

async function main() {
  console.log('🌱 Starting comprehensive seed...')

  // Clear existing data
  await prisma.dailyProgress.deleteMany()
  await prisma.mockInterview.deleteMany()
  await prisma.revision.deleteMany()
  await prisma.session.deleteMany()
  await prisma.submission.deleteMany()
  await prisma.problemPattern.deleteMany()
  await prisma.problemTag.deleteMany()
  await prisma.problem.deleteMany()
  console.log('✅ Cleared existing data')

  // Sample problems with patterns
  const problems = [
    // Easy - Strong patterns
    {
      platform: 'LeetCode',
      problemId: '1',
      title: 'Two Sum',
      difficulty: 'Easy',
      url: 'https://leetcode.com/problems/two-sum/',
      source: 'NeetCode',
      company: 'Amazon',
      patterns: ['Two Pointers'],
      tags: ['Array', 'Hash Table']
    },
    {
      platform: 'LeetCode',
      problemId: '121',
      title: 'Best Time to Buy and Sell Stock',
      difficulty: 'Easy',
      url: 'https://leetcode.com/problems/best-time-to-buy-and-sell-stock/',
      source: 'NeetCode',
      company: 'Google',
      patterns: ['Sliding Window', 'Kadane\'s Algorithm'],
      tags: ['Array', 'DP']
    },
    {
      platform: 'LeetCode',
      problemId: '217',
      title: 'Contains Duplicate',
      difficulty: 'Easy',
      url: 'https://leetcode.com/problems/contains-duplicate/',
      source: 'NeetCode',
      company: 'Microsoft',
      patterns: ['Two Pointers'],
      tags: ['Array', 'Hash Table']
    },
    // Medium - Mixed patterns
    {
      platform: 'LeetCode',
      problemId: '3',
      title: 'Longest Substring Without Repeating Characters',
      difficulty: 'Medium',
      url: 'https://leetcode.com/problems/longest-substring-without-repeating-characters/',
      source: 'NeetCode',
      company: 'Amazon',
      patterns: ['Sliding Window'],
      tags: ['String', 'Hash Table']
    },
    {
      platform: 'LeetCode',
      problemId: '200',
      title: 'Number of Islands',
      difficulty: 'Medium',
      url: 'https://leetcode.com/problems/number-of-islands/',
      source: 'Company',
      company: 'Meta',
      patterns: ['Graph DFS', 'Graph BFS'],
      tags: ['Graph', 'DFS', 'BFS']
    },
    {
      platform: 'LeetCode',
      problemId: '207',
      title: 'Course Schedule',
      difficulty: 'Medium',
      url: 'https://leetcode.com/problems/course-schedule/',
      source: 'Company',
      company: 'Google',
      patterns: ['Topological Sort', 'Graph DFS'],
      tags: ['Graph', 'Topological Sort']
    },
    {
      platform: 'LeetCode',
      problemId: '146',
      title: 'LRU Cache',
      difficulty: 'Medium',
      url: 'https://leetcode.com/problems/lru-cache/',
      source: 'Company',
      company: 'Amazon',
      patterns: ['Linked List'],
      tags: ['Design', 'Hash Table', 'Linked List']
    },
    // Hard - Weak patterns
    {
      platform: 'LeetCode',
      problemId: '72',
      title: 'Edit Distance',
      difficulty: 'Hard',
      url: 'https://leetcode.com/problems/edit-distance/',
      source: 'Company',
      company: 'Google',
      patterns: ['Edit Distance', '2D DP'],
      tags: ['DP', 'String']
    },
    {
      platform: 'LeetCode',
      problemId: '42',
      title: 'Trapping Rain Water',
      difficulty: 'Hard',
      url: 'https://leetcode.com/problems/trapping-rain-water/',
      source: 'NeetCode',
      company: 'Apple',
      patterns: ['Two Pointers', 'Monotonic Stack'],
      tags: ['Array', 'Two Pointers', 'Stack']
    },
    {
      platform: 'LeetCode',
      problemId: '297',
      title: 'Serialize and Deserialize Binary Tree',
      difficulty: 'Hard',
      url: 'https://leetcode.com/problems/serialize-and-deserialize-binary-tree/',
      source: 'Company',
      company: 'Meta',
      patterns: ['Tree DFS', 'Tree BFS'],
      tags: ['Tree', 'Design']
    }
  ]

  console.log('📝 Creating problems...')
  const createdProblems = []
  
  for (const prob of problems) {
    const problem = await prisma.problem.create({
      data: {
        platform: prob.platform,
        problemId: prob.problemId,
        title: prob.title,
        difficulty: prob.difficulty,
        url: prob.url,
        source: prob.source,
        company: prob.company,
      }
    })
    
    // Add patterns
    for (const patternName of prob.patterns) {
      const pattern = await prisma.pattern.findFirst({
        where: { name: patternName }
      })
      if (pattern) {
        await prisma.problemPattern.create({
          data: {
            problemId: problem.id,
            patternId: pattern.id
          }
        })
      }
    }
    
    // Add tags
    for (const tagName of prob.tags) {
      await prisma.problemTag.create({
        data: {
          problemId: problem.id,
          tag: tagName
        }
      })
    }
    
    createdProblems.push(problem)
  }
  
  console.log(`✅ Created ${createdProblems.length} problems`)

  // Create submissions with varying performance
  console.log('📊 Creating submissions...')
  const submissions = [
    // Strong performance - Easy problems
    { problemId: createdProblems[0].id, timeSeconds: 600, status: 'solved', attemptType: 'First', wasHintUsed: false, patternRecognitionSeconds: 60 },
    { problemId: createdProblems[1].id, timeSeconds: 720, status: 'solved', attemptType: 'First', wasHintUsed: false, patternRecognitionSeconds: 90 },
    { problemId: createdProblems[2].id, timeSeconds: 540, status: 'solved', attemptType: 'First', wasHintUsed: false, patternRecognitionSeconds: 45 },
    
    // Medium performance - Medium problems
    { problemId: createdProblems[3].id, timeSeconds: 1200, status: 'solved', attemptType: 'First', wasHintUsed: false, patternRecognitionSeconds: 120 },
    { problemId: createdProblems[4].id, timeSeconds: 2400, status: 'solved', attemptType: 'First', wasHintUsed: true, patternRecognitionSeconds: 300 },
    { problemId: createdProblems[5].id, timeSeconds: 2700, status: 'solved', attemptType: 'First', wasHintUsed: true, patternRecognitionSeconds: 420 },
    { problemId: createdProblems[6].id, timeSeconds: 1800, status: 'solved', attemptType: 'First', wasHintUsed: false, patternRecognitionSeconds: 180 },
    
    // Weak performance - Hard problems
    { problemId: createdProblems[7].id, timeSeconds: 3600, status: 'solved', attemptType: 'First', wasHintUsed: true, patternRecognitionSeconds: 600 },
    { problemId: createdProblems[8].id, timeSeconds: 4200, status: 'failed', attemptType: 'First', wasHintUsed: true, patternRecognitionSeconds: 900 },
    { problemId: createdProblems[9].id, timeSeconds: 3000, status: 'partial', attemptType: 'First', wasHintUsed: true, patternRecognitionSeconds: 480 },
  ]

  const today = new Date()
  const yesterday = new Date(today)
  yesterday.setDate(yesterday.getDate() - 1)
  const twoDaysAgo = new Date(today)
  twoDaysAgo.setDate(twoDaysAgo.getDate() - 2)

  for (let i = 0; i < submissions.length; i++) {
    const sub = submissions[i]
    const date = i < 3 ? today : i < 7 ? yesterday : twoDaysAgo
    
    await prisma.submission.create({
      data: {
        problemId: sub.problemId,
        attemptNumber: 1,
        timeSpentSeconds: sub.timeSeconds,
        status: sub.status,
        attemptType: sub.attemptType,
        wasHintUsed: sub.wasHintUsed,
        patternRecognitionSeconds: sub.patternRecognitionSeconds,
        submittedAt: date,
        createdAt: date,
        mistakeNote: sub.status === 'failed' ? 'Struggled with base case' : undefined,
        approachNote: sub.status === 'solved' ? 'Used pattern template' : undefined,
      }
    })
  }
  
  console.log(`✅ Created ${submissions.length} submissions`)

  // Create revisions (spaced repetition)
  console.log('🔄 Creating revisions...')
  const tomorrow = new Date(today)
  tomorrow.setDate(tomorrow.getDate() + 1)
  const threeDaysLater = new Date(today)
  threeDaysLater.setDate(threeDaysLater.getDate() + 3)

  const firstSubmissions = await prisma.submission.findMany({
    where: { attemptNumber: 1 },
    take: 5
  })

  for (let i = 0; i < Math.min(3, firstSubmissions.length); i++) {
    await prisma.revision.create({
      data: {
        submissionId: firstSubmissions[i].id,
        intervalLevel: 1,
        nextReviewDate: i === 0 ? today : i === 1 ? tomorrow : threeDaysLater,
        completed: false
      }
    })
  }
  
  console.log('✅ Created revisions')

  // Create mock interviews
  console.log('🎯 Creating mock interviews...')
  const mockInterviews = [
    { problemId: createdProblems[0].id, solved: true, timeSeconds: 1800, explanationScore: 4, codeQualityScore: 5, daysAgo: 7 },
    { problemId: createdProblems[3].id, solved: true, timeSeconds: 2400, explanationScore: 5, codeQualityScore: 4, daysAgo: 5 },
    { problemId: createdProblems[4].id, solved: false, timeSeconds: 2700, explanationScore: 3, codeQualityScore: 3, daysAgo: 3 },
    { problemId: createdProblems[6].id, solved: true, timeSeconds: 2100, explanationScore: 4, codeQualityScore: 4, daysAgo: 1 },
  ]

  for (const mock of mockInterviews) {
    const mockDate = new Date(today)
    mockDate.setDate(mockDate.getDate() - mock.daysAgo)
    
    await prisma.mockInterview.create({
      data: {
        problemId: mock.problemId,
        date: mockDate,
        timeTakenSeconds: mock.timeSeconds,
        solved: mock.solved,
        explanationScore: mock.explanationScore,
        codeQualityScore: mock.codeQualityScore,
        overallScore: Math.round((mock.explanationScore + mock.codeQualityScore) / 2)
      }
    })
  }
  
  console.log(`✅ Created ${mockInterviews.length} mock interviews`)

  // Create daily progress
  console.log('📈 Creating daily progress...')
  const dailyProgressData = [
    { date: twoDaysAgo, problemsSolved: 3, totalTimeSpent: 6300, patternsWorked: 3 },
    { date: yesterday, problemsSolved: 4, totalTimeSpent: 9300, patternsWorked: 4 },
    { date: today, problemsSolved: 3, totalTimeSpent: 5400, patternsWorked: 2 },
  ]

  for (const dp of dailyProgressData) {
    await prisma.dailyProgress.create({
      data: {
        date: dp.date,
        problemsSolved: dp.problemsSolved,
        totalTimeSpent: dp.totalTimeSpent,
        patternsWorked: dp.patternsWorked
      }
    })
  }
  
  console.log('✅ Created daily progress records')

  console.log('\n🎉 Seed completed successfully!')
  console.log(`
📊 Summary:
   - ${createdProblems.length} problems created
   - ${submissions.length} submissions logged
   - 3 revisions scheduled
   - ${mockInterviews.length} mock interviews completed
   - 3 days of progress tracked
  `)
}

main()
  .catch((e) => {
    console.error('❌ Seed failed:', e)
    process.exit(1)
  })
  .finally(async () => {
    await prisma.$disconnect()
  })
