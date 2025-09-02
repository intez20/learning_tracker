import axios from 'axios'

// API base URL
const API_URL = '/api'

// Analytics service for retrieving analytics data
const analyticsService = {
  // Get overview analytics (total goals, hours, projects, etc.)
  getOverview: async () => {
    try {
      const response = await axios.get(`${API_URL}/analytics/overview`)
      return response.data
    } catch (error) {
      console.error('Error fetching analytics overview:', error)
      
      // Fallback to mock data if the API is not available
      return {
        totalGoals: 12,
        completedGoals: 7,
        totalHours: 145,
        totalProjects: 5,
        completedProjects: 2,
        streakDays: 14,
        avgSessionTime: 65 // minutes
      }
    }
  },
  
  // Get progress data for charts (weekly progress, category breakdown, etc.)
  getProgressData: async (timeRange = '30d') => {
    try {
      const response = await axios.get(`${API_URL}/analytics/progress?timeRange=${timeRange}`)
      return response.data
    } catch (error) {
      console.error('Error fetching analytics progress data:', error)
      
      // Fallback to mock data if the API is not available
      return {
        weeklyProgress: [
          { week: 'Week 1', hours: 8, goals: 2 },
          { week: 'Week 2', hours: 12, goals: 3 },
          { week: 'Week 3', hours: 10, goals: 1 },
          { week: 'Week 4', hours: 15, goals: 4 },
        ],
        categoryBreakdown: [
          { category: 'Programming', percentage: 45, hours: 65 },
          { category: 'Design', percentage: 25, hours: 36 },
          { category: 'Languages', percentage: 20, hours: 29 },
          { category: 'Business', percentage: 10, hours: 15 },
        ],
        monthlyGoals: [
          { month: 'Jan', completed: 3, total: 5 },
          { month: 'Feb', completed: 4, total: 6 },
          { month: 'Mar', completed: 7, total: 8 },
          { month: 'Apr', completed: 2, total: 4 },
        ]
      }
    }
  },
  
  // Get recent activity data
  getRecentActivity: async (limit = 4) => {
    try {
      const response = await axios.get(`${API_URL}/analytics/recent-activity?limit=${limit}`)
      return response.data
    } catch (error) {
      console.error('Error fetching recent activity:', error)
      
      // Fallback to mock data if the API is not available
      return [
        {
          id: 1,
          type: 'GOAL_COMPLETED',
          title: 'Completed "React Hooks" goal',
          timestamp: '2025-08-31T10:30:00Z'
        },
        {
          id: 2,
          type: 'PROJECT_STARTED',
          title: 'Started new project "Portfolio Website"',
          timestamp: '2025-08-30T14:45:00Z'
        },
        {
          id: 3,
          type: 'GOAL_CREATED',
          title: 'Set new goal "Learn TypeScript"',
          timestamp: '2025-08-28T09:15:00Z'
        },
        {
          id: 4,
          type: 'HOURS_LOGGED',
          title: 'Logged 3 hours of study time',
          timestamp: '2025-08-26T16:20:00Z'
        }
      ]
    }
  },
  
  // Get learning insights
  getInsights: async () => {
    try {
      const response = await axios.get(`${API_URL}/analytics/insights`)
      return response.data
    } catch (error) {
      console.error('Error fetching learning insights:', error)
      
      // Fallback to mock data if the API is not available
      return {
        mostProductiveDay: {
          day: 'Tuesday',
          averageHours: 2.5
        },
        goalCompletionRate: 78,
        longestStreak: 21
      }
    }
  }
}

export default analyticsService
