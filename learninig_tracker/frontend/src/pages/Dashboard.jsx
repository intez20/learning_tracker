import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { learningItemService } from '../api/services'
import { Link } from 'react-router-dom'
import { PlusCircle, Loader2 } from 'lucide-react'
import { Button } from '../components/ui/button'

const Dashboard = () => {
  const { data: learningItems, isLoading, error } = useQuery(
    ['learningItems'],
    () => learningItemService.getAll(),
    {
      refetchOnWindowFocus: false,
    }
  )

  // Calculate stats
  const calculateStats = () => {
    if (!learningItems) return { total: 0, completed: 0, inProgress: 0, totalHours: 0 }

    const total = learningItems.length
    const completed = learningItems.filter(item => item.status === 'COMPLETED').length
    const inProgress = learningItems.filter(item => item.status === 'IN_PROGRESS').length
    const totalHours = learningItems.reduce((sum, item) => sum + item.totalHours, 0)

    return { total, completed, inProgress, totalHours }
  }

  const stats = calculateStats()

  // Get recent activity (last 5 items)
  const recentItems = learningItems
    ? [...learningItems]
        .sort((a, b) => new Date(b.createdDate) - new Date(a.createdDate))
        .slice(0, 5)
    : []

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-full">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    )
  }

  if (error) {
    return (
      <div className="p-4 bg-destructive/10 text-destructive rounded-md">
        Error loading data: {error.message}
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Dashboard</h1>
        <Link to="/learning-items/new">
          <Button className="flex items-center gap-2">
            <PlusCircle size={18} />
            Add New Item
          </Button>
        </Link>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="bg-card p-4 rounded-lg shadow">
          <h3 className="text-sm font-medium text-muted-foreground">Total Items</h3>
          <p className="text-3xl font-bold">{stats.total}</p>
        </div>
        <div className="bg-card p-4 rounded-lg shadow">
          <h3 className="text-sm font-medium text-muted-foreground">Completed</h3>
          <p className="text-3xl font-bold">{stats.completed}</p>
        </div>
        <div className="bg-card p-4 rounded-lg shadow">
          <h3 className="text-sm font-medium text-muted-foreground">In Progress</h3>
          <p className="text-3xl font-bold">{stats.inProgress}</p>
        </div>
        <div className="bg-card p-4 rounded-lg shadow">
          <h3 className="text-sm font-medium text-muted-foreground">Total Hours</h3>
          <p className="text-3xl font-bold">{stats.totalHours.toFixed(1)}</p>
        </div>
      </div>

      {/* Recent Activity */}
      <div className="bg-card p-6 rounded-lg shadow">
        <h2 className="text-xl font-bold mb-4">Recent Activity</h2>
        {recentItems.length > 0 ? (
          <div className="space-y-4">
            {recentItems.map(item => (
              <Link
                key={item.id}
                to={`/learning-items/${item.id}`}
                className="block"
              >
                <div className="flex items-center justify-between p-3 bg-background rounded-md hover:bg-accent transition-colors">
                  <div>
                    <h3 className="font-medium">{item.title}</h3>
                    <p className="text-sm text-muted-foreground">
                      Status: {item.status.replace('_', ' ')}
                    </p>
                  </div>
                  <div className="flex items-center">
                    <div className="bg-secondary w-32 h-2 rounded-full overflow-hidden">
                      <div
                        className="bg-primary h-full"
                        style={{ width: `${item.progressPercentage}%` }}
                      />
                    </div>
                    <span className="ml-2 text-sm">{item.progressPercentage}%</span>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        ) : (
          <p className="text-muted-foreground">No learning items yet. Create your first one!</p>
        )}
      </div>

      {/* Upcoming Deadlines */}
      <div className="bg-card p-6 rounded-lg shadow">
        <h2 className="text-xl font-bold mb-4">Upcoming Deadlines</h2>
        {learningItems && learningItems.some(item => item.targetDate) ? (
          <div className="space-y-4">
            {learningItems
              .filter(item => item.targetDate && new Date(item.targetDate) > new Date())
              .sort((a, b) => new Date(a.targetDate) - new Date(b.targetDate))
              .slice(0, 3)
              .map(item => (
                <Link
                  key={item.id}
                  to={`/learning-items/${item.id}`}
                  className="block"
                >
                  <div className="flex items-center justify-between p-3 bg-background rounded-md hover:bg-accent transition-colors">
                    <div>
                      <h3 className="font-medium">{item.title}</h3>
                      <p className="text-sm text-muted-foreground">
                        Due: {new Date(item.targetDate).toLocaleDateString()}
                      </p>
                    </div>
                    <div className="flex items-center">
                      <div className="bg-secondary w-32 h-2 rounded-full overflow-hidden">
                        <div
                          className="bg-primary h-full"
                          style={{ width: `${item.progressPercentage}%` }}
                        />
                      </div>
                      <span className="ml-2 text-sm">{item.progressPercentage}%</span>
                    </div>
                  </div>
                </Link>
              ))}
          </div>
        ) : (
          <p className="text-muted-foreground">No upcoming deadlines.</p>
        )}
      </div>
    </div>
  )
}

export default Dashboard
