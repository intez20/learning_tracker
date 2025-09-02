import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

export const categoryService = {
  getAll: async () => {
    const response = await api.get('/categories')
    return response.data
  },
  
  getById: async (id) => {
    const response = await api.get(`/categories/${id}`)
    return response.data
  },
  
  create: async (category) => {
    const response = await api.post('/categories', category)
    return response.data
  },
  
  update: async (id, category) => {
    const response = await api.put(`/categories/${id}`, category)
    return response.data
  },
  
  delete: async (id) => {
    await api.delete(`/categories/${id}`)
    return id
  }
}

// LEARNING ITEMS SERVICE - Connected to backend
export const learningItemService = {
  getAll: async () => {
    const response = await api.get('/learning-items')
    return response.data
  },
  
  getById: async (id) => {
    const response = await api.get(`/learning-items/${id}`)
    return response.data
  },
  
  getByCategory: async (categoryId) => {
    const response = await api.get(`/learning-items/category/${categoryId}`)
    return response.data
  },
  
  getByStatus: async (status) => {
    const response = await api.get(`/learning-items/status/${status}`)
    return response.data
  },
  
  search: async (searchTerm) => {
    const response = await api.get(`/learning-items/search?searchTerm=${searchTerm}`)
    return response.data
  },
  
  create: async (item) => {
    const response = await api.post('/learning-items', item)
    return response.data
  },
  
  update: async (id, item) => {
    const response = await api.put(`/learning-items/${id}`, item)
    return response.data
  },
  
  delete: async (id) => {
    await api.delete(`/learning-items/${id}`)
    return id
  }
}

// PROGRESS ENTRIES SERVICE - Connected to backend
export const progressEntryService = {
  getAllByLearningItemId: async (learningItemId) => {
    const response = await api.get(`/progress-entries/learning-item/${learningItemId}`)
    return response.data
  },
  
  create: async (entry) => {
    const response = await api.post('/progress-entries', entry)
    return response.data
  },
  
  update: async (id, entry) => {
    const response = await api.put(`/progress-entries/${id}`, entry)
    return response.data
  },
  
  delete: async (id) => {
    await api.delete(`/progress-entries/${id}`)
    return id
  }
}

// GOALS SERVICE - Connected to backend
export const goalService = {
  getAll: async () => {
    const response = await api.get('/goals')
    return response.data
  },
  
  getById: async (id) => {
    const response = await api.get(`/goals/${id}`)
    return response.data
  },
  
  getByLearningItemId: async (learningItemId) => {
    const response = await api.get(`/goals/learning-item/${learningItemId}`)
    return response.data
  },
  
  create: async (goal) => {
    const response = await api.post('/goals', goal)
    return response.data
  },
  
  update: async (id, goal) => {
    const response = await api.put(`/goals/${id}`, goal)
    return response.data
  },
  
  delete: async (id) => {
    await api.delete(`/goals/${id}`)
    return id
  }
}

// TEMPORARY MOCK - Backend endpoint not implemented yet
export const analyticsService = {
  getWeeklySummary: async () => {
    console.log('Note: analytics endpoint not implemented in backend yet')
    return { days: [], totalHours: 0 }
  },
  
  getMonthlySummary: async () => {
    console.log('Note: analytics endpoint not implemented in backend yet')
    return { months: [], totalHours: 0 }
  },
  
  getHoursByCategory: async () => {
    console.log('Note: analytics endpoint not implemented in backend yet')
    return []
  },
  
  getLearningStreaks: async () => {
    console.log('Note: analytics endpoint not implemented in backend yet')
    return { currentStreak: 0, longestStreak: 0 }
  },
  
  getGoalCompletionRates: async () => {
    console.log('Note: analytics endpoint not implemented in backend yet')
    return { completed: 0, total: 0 }
  },
  
  exportData: async (format) => {
    console.log('Note: analytics endpoint not implemented in backend yet')
    return { url: '#' }
  }
}
