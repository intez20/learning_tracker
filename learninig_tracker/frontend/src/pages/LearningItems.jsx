import React, { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { learningItemService } from '../api/services'
import { Link } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card'
import { Button } from '../components/ui/button'
import { PlusCircle, Search, Filter, BookOpen, Clock, Target } from 'lucide-react'

const LearningItems = () => {
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState('ALL')
  const queryClient = useQueryClient()

  const { data: learningItems, isLoading, error } = useQuery(
    ['learningItems'],
    () => learningItemService.getAll(),
    {
      refetchOnWindowFocus: false,
    }
  )

  const filteredItems = learningItems
    ? learningItems.filter(item => {
        const matchesSearch = item.title.toLowerCase().includes(searchTerm.toLowerCase())
        const matchesStatus = statusFilter === 'ALL' || item.status === statusFilter
        return matchesSearch && matchesStatus
      })
    : []

  const getStatusBadge = (status) => {
    const statusStyles = {
      'NOT_STARTED': 'bg-gray-100 text-gray-800',
      'IN_PROGRESS': 'bg-blue-100 text-blue-800',
      'COMPLETED': 'bg-green-100 text-green-800',
      'ON_HOLD': 'bg-yellow-100 text-yellow-800'
    }
    
    // Default to NOT_STARTED if status is undefined
    const displayStatus = status || 'NOT_STARTED';
    
    return (
      <span className={`px-2 py-1 text-xs font-medium rounded-full ${statusStyles[displayStatus] || statusStyles.NOT_STARTED}`}>
        {displayStatus.replace('_', ' ')}
      </span>
    )
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <BookOpen className="h-12 w-12 animate-pulse text-muted-foreground mx-auto mb-4" />
          <p>Loading learning items...</p>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <h2 className="text-xl font-bold text-destructive">Error loading learning items</h2>
          <p className="mt-2">Please try again later or check your connection.</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Learning Items</h1>
          <p className="text-muted-foreground mt-2">
            Manage your learning projects and track your progress.
          </p>
        </div>
        <Link to="/learning-items/new">
          <Button>
            <PlusCircle className="mr-2 h-4 w-4" /> Add Learning Item
          </Button>
        </Link>
      </div>

      {/* Filters and Search */}
      <div className="flex flex-col sm:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
          <input
            type="text"
            placeholder="Search learning items..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-3 py-2 border rounded-md"
          />
        </div>
        <div className="flex items-center gap-2">
          <Filter className="h-4 w-4 text-muted-foreground" />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-3 py-2 border rounded-md"
          >
            <option value="ALL">All Status</option>
            <option value="NOT_STARTED">Not Started</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="COMPLETED">Completed</option>
            <option value="ON_HOLD">On Hold</option>
          </select>
        </div>
      </div>

      {/* Learning Items Grid */}
      <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
        {filteredItems.map((item) => (
          <Card key={item.id} className="hover:shadow-md transition-shadow">
            <CardHeader>
              <div className="flex items-start justify-between">
                <CardTitle className="text-lg line-clamp-2">{item.title}</CardTitle>
                {getStatusBadge(item.status)}
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <p className="text-sm text-muted-foreground line-clamp-2">
                  {item.description || 'No description available'}
                </p>
                
                {/* Progress Bar */}
                <div className="space-y-2">
                  <div className="flex items-center justify-between text-sm">
                    <span>Progress</span>
                    <span>{item.progressPercentage || 0}%</span>
                  </div>
                  <div className="w-full bg-secondary h-2 rounded-full overflow-hidden">
                    <div
                      className="bg-primary h-full transition-all"
                      style={{ width: `${item.progressPercentage || 0}%` }}
                    />
                  </div>
                </div>

                {/* Stats */}
                <div className="flex items-center justify-between text-sm text-muted-foreground">
                  <div className="flex items-center">
                    <Clock className="h-4 w-4 mr-1" />
                    {item.totalHours || 0}h
                  </div>
                  {item.targetDate && (
                    <div className="flex items-center">
                      <Target className="h-4 w-4 mr-1" />
                      {new Date(item.targetDate).toLocaleDateString()}
                    </div>
                  )}
                </div>

                {/* Action Button */}
                <Link to={`/learning-items/${item.id}`} className="block">
                  <Button variant="outline" className="w-full">
                    View Details
                  </Button>
                </Link>
              </div>
            </CardContent>
          </Card>
        ))}

        {/* Empty state */}
        {filteredItems.length === 0 && (
          <div className="col-span-full flex flex-col items-center justify-center py-12">
            <BookOpen className="h-24 w-24 text-muted-foreground mb-4" />
            <h3 className="text-lg font-medium mb-2">No learning items found</h3>
            <p className="text-muted-foreground text-center mb-4">
              {searchTerm || statusFilter !== 'ALL' 
                ? 'Try adjusting your search or filter criteria.' 
                : 'Get started by creating your first learning item.'}
            </p>
            {(!searchTerm && statusFilter === 'ALL') && (
              <Link to="/learning-items/new">
                <Button>
                  <PlusCircle className="mr-2 h-4 w-4" /> Create Learning Item
                </Button>
              </Link>
            )}
          </div>
        )}
      </div>
    </div>
  )
}

export default LearningItems
