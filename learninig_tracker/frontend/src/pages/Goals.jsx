import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { Link } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card'
import { Button } from '../components/ui/button'
import { Target, Plus, Calendar, CheckCircle2, Clock, AlertTriangle, Filter } from 'lucide-react'

// Mock service - replace with actual service when available
const goalsService = {
  getAll: async () => {
    // This would normally call your backend API
    return []
  },
  create: async (goal) => {
    // This would normally call your backend API
    return goal
  },
  update: async (id, goal) => {
    // This would normally call your backend API
    return goal
  },
  delete: async (id) => {
    // This would normally call your backend API
    return true
  },
  markComplete: async (id) => {
    // This would normally call your backend API
    return true
  }
}

const Goals = () => {
  const [filter, setFilter] = useState('ALL')
  const [isModalOpen, setIsModalOpen] = useState(false)
  const queryClient = useQueryClient()

  const { data: goals, isLoading, error } = useQuery(
    ['goals'],
    () => goalsService.getAll(),
    {
      refetchOnWindowFocus: false,
    }
  )

  const createMutation = useMutation(
    (goalData) => goalsService.create(goalData),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['goals'])
        setIsModalOpen(false)
      },
    }
  )

  const completeMutation = useMutation(
    (id) => goalsService.markComplete(id),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['goals'])
      },
    }
  )

  const filteredGoals = goals ? goals.filter(goal => {
    if (filter === 'ALL') return true
    if (filter === 'ACTIVE') return !goal.completed
    if (filter === 'COMPLETED') return goal.completed
    if (filter === 'OVERDUE') return !goal.completed && goal.dueDate && new Date(goal.dueDate) < new Date()
    return true
  }) : []

  const getGoalStatus = (goal) => {
    if (goal.completed) return 'completed'
    if (goal.dueDate && new Date(goal.dueDate) < new Date()) return 'overdue'
    return 'active'
  }

  const getStatusIcon = (status) => {
    switch (status) {
      case 'completed':
        return <CheckCircle2 className="h-4 w-4 text-green-600" />
      case 'overdue':
        return <AlertTriangle className="h-4 w-4 text-red-600" />
      default:
        return <Target className="h-4 w-4 text-blue-600" />
    }
  }

  const getStatusBadge = (status) => {
    const styles = {
      'completed': 'bg-green-100 text-green-800',
      'overdue': 'bg-red-100 text-red-800',
      'active': 'bg-blue-100 text-blue-800'
    }
    
    const labels = {
      'completed': 'Completed',
      'overdue': 'Overdue',
      'active': 'Active'
    }

    // Default to active if status is undefined
    const displayStatus = status || 'active';
    
    return (
      <span className={`px-2 py-1 text-xs font-medium rounded-full ${styles[displayStatus] || styles.active}`}>
        {labels[displayStatus] || 'Active'}
      </span>
    )
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <Target className="h-12 w-12 animate-pulse text-muted-foreground mx-auto mb-4" />
          <p>Loading goals...</p>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <h2 className="text-xl font-bold text-destructive">Error loading goals</h2>
          <p className="mt-2">Please try again later or check your connection.</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Learning Goals</h1>
          <p className="text-muted-foreground mt-2">
            Set and track your learning objectives.
          </p>
        </div>
        <Button onClick={() => setIsModalOpen(true)}>
          <Plus className="mr-2 h-4 w-4" /> Add Goal
        </Button>
      </div>

      {/* Filters */}
      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2">
          <Filter className="h-4 w-4 text-muted-foreground" />
          <select
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
            className="px-3 py-2 border rounded-md"
          >
            <option value="ALL">All Goals</option>
            <option value="ACTIVE">Active</option>
            <option value="COMPLETED">Completed</option>
            <option value="OVERDUE">Overdue</option>
          </select>
        </div>
      </div>

      {/* Goals Grid */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {filteredGoals.map((goal) => {
          const status = getGoalStatus(goal)
          const daysLeft = goal.dueDate 
            ? Math.ceil((new Date(goal.dueDate) - new Date()) / (1000 * 60 * 60 * 24))
            : null

          return (
            <Card key={goal.id} className="hover:shadow-md transition-shadow">
              <CardHeader>
                <div className="flex items-start justify-between">
                  <CardTitle className="text-lg line-clamp-2 flex items-center gap-2">
                    {getStatusIcon(status)}
                    {goal.title}
                  </CardTitle>
                  {getStatusBadge(status)}
                </div>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <p className="text-sm text-muted-foreground line-clamp-2">
                    {goal.description || 'No description provided'}
                  </p>

                  {/* Progress */}
                  {goal.targetValue && (
                    <div className="space-y-2">
                      <div className="flex items-center justify-between text-sm">
                        <span>Progress</span>
                        <span>{goal.currentValue || 0} / {goal.targetValue} {goal.unit || ''}</span>
                      </div>
                      <div className="w-full bg-secondary h-2 rounded-full overflow-hidden">
                        <div
                          className="bg-primary h-full transition-all"
                          style={{ 
                            width: `${Math.min(((goal.currentValue || 0) / goal.targetValue) * 100, 100)}%` 
                          }}
                        />
                      </div>
                    </div>
                  )}

                  {/* Due Date */}
                  {goal.dueDate && (
                    <div className="flex items-center justify-between text-sm">
                      <div className="flex items-center text-muted-foreground">
                        <Calendar className="h-4 w-4 mr-1" />
                        Due {new Date(goal.dueDate).toLocaleDateString()}
                      </div>
                      {daysLeft !== null && (
                        <span className={`text-xs font-medium ${
                          daysLeft < 0 ? 'text-red-600' : 
                          daysLeft <= 7 ? 'text-yellow-600' : 
                          'text-green-600'
                        }`}>
                          {daysLeft < 0 ? `${Math.abs(daysLeft)} days overdue` :
                           daysLeft === 0 ? 'Due today' :
                           `${daysLeft} days left`}
                        </span>
                      )}
                    </div>
                  )}

                  {/* Priority */}
                  {goal.priority && (
                    <div className="flex items-center justify-between text-sm">
                      <span className="text-muted-foreground">Priority</span>
                      <span className={`font-medium ${
                        goal.priority === 'high' ? 'text-red-600' :
                        goal.priority === 'medium' ? 'text-yellow-600' :
                        'text-green-600'
                      }`}>
                        {goal.priority.charAt(0).toUpperCase() + goal.priority.slice(1)}
                      </span>
                    </div>
                  )}

                  {/* Actions */}
                  <div className="flex gap-2 pt-2">
                    {!goal.completed && (
                      <Button 
                        onClick={() => completeMutation.mutate(goal.id)}
                        disabled={completeMutation.isLoading}
                        size="sm"
                        className="flex-1"
                      >
                        <CheckCircle2 className="mr-1 h-3 w-3" />
                        Complete
                      </Button>
                    )}
                    <Button variant="outline" size="sm" className="flex-1">
                      Edit
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          )
        })}

        {/* Empty state */}
        {filteredGoals.length === 0 && (
          <div className="col-span-full flex flex-col items-center justify-center py-12">
            <Target className="h-24 w-24 text-muted-foreground mb-4" />
            <h3 className="text-lg font-medium mb-2">No goals found</h3>
            <p className="text-muted-foreground text-center mb-4">
              {filter !== 'ALL' 
                ? 'No goals match your current filter.' 
                : 'Set your first learning goal to start tracking your progress.'}
            </p>
            {filter === 'ALL' && (
              <Button onClick={() => setIsModalOpen(true)}>
                <Plus className="mr-2 h-4 w-4" /> Create Goal
              </Button>
            )}
          </div>
        )}
      </div>

      {/* Goal Creation Modal Placeholder */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-background rounded-lg p-6 w-full max-w-md">
            <h2 className="text-xl font-bold mb-4">Add New Goal</h2>
            <p className="text-muted-foreground mb-4">
              Goal creation form would go here. This is a placeholder for now.
            </p>
            <div className="flex justify-end space-x-2">
              <Button variant="outline" onClick={() => setIsModalOpen(false)}>
                Cancel
              </Button>
              <Button onClick={() => setIsModalOpen(false)}>
                Create Goal
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default Goals
