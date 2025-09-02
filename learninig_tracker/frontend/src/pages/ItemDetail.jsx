import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { learningItemService } from '../api/services'
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card'
import { Button } from '../components/ui/button'
import { ArrowLeft, Edit, Trash2, Clock, Target, Calendar, BookOpen, CheckCircle } from 'lucide-react'

const ItemDetail = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [isEditing, setIsEditing] = useState(false)

  // Redirect to the NewLearningItem component if id is 'new'
  useEffect(() => {
    if (id === 'new') {
      navigate('/learning-items/new', { replace: true })
    }
  }, [id, navigate])

  const { data: item, isLoading, error } = useQuery(
    ['learningItem', id],
    () => learningItemService.getById(id),
    {
      enabled: !!id && id !== 'new',
      refetchOnWindowFocus: false,
    }
  )

  const deleteMutation = useMutation(
    () => learningItemService.delete(id),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['learningItems'])
        navigate('/learning-items')
      },
    }
  )

  const updateStatusMutation = useMutation(
    (newStatus) => learningItemService.updateStatus(id, newStatus),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['learningItem', id])
        queryClient.invalidateQueries(['learningItems'])
      },
    }
  )

  const handleDelete = () => {
    if (window.confirm('Are you sure you want to delete this learning item?')) {
      deleteMutation.mutate()
    }
  }

  const handleStatusUpdate = (newStatus) => {
    updateStatusMutation.mutate(newStatus)
  }

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
      <span className={`px-3 py-1 text-sm font-medium rounded-full ${statusStyles[displayStatus] || statusStyles.NOT_STARTED}`}>
        {displayStatus.replace('_', ' ')}
      </span>
    )
  }

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <BookOpen className="h-12 w-12 animate-pulse text-muted-foreground mx-auto mb-4" />
          <p>Loading learning item...</p>
        </div>
      </div>
    )
  }

  if (error || !item) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <h2 className="text-xl font-bold text-destructive">Learning item not found</h2>
          <p className="mt-2">The item you're looking for doesn't exist or has been deleted.</p>
          <Button 
            onClick={() => navigate('/learning-items')} 
            className="mt-4"
            variant="outline"
          >
            <ArrowLeft className="mr-2 h-4 w-4" />
            Back to Learning Items
          </Button>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button 
            onClick={() => navigate('/learning-items')} 
            variant="ghost" 
            size="sm"
          >
            <ArrowLeft className="h-4 w-4" />
          </Button>
          {item && (
            <div>
              <h1 className="text-3xl font-bold tracking-tight">{item.title}</h1>
              <div className="flex items-center gap-2 mt-2">
                {getStatusBadge(item.status)}
                {item.category && (
                  <span className="px-2 py-1 bg-accent text-accent-foreground text-xs rounded">
                    {item.category}
                  </span>
                )}
              </div>
            </div>
          )}
        </div>
        <div className="flex items-center gap-2">
          <Button 
            onClick={() => setIsEditing(true)} 
            variant="outline" 
            size="sm"
          >
            <Edit className="mr-2 h-4 w-4" />
            Edit
          </Button>
          <Button 
            onClick={handleDelete} 
            variant="destructive" 
            size="sm"
            disabled={deleteMutation.isLoading}
          >
            <Trash2 className="mr-2 h-4 w-4" />
            Delete
          </Button>
        </div>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        {/* Main Content */}
        <div className="lg:col-span-2 space-y-6">
          {/* Description */}
          <Card>
            <CardHeader>
              <CardTitle>Description</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground">
                {item?.description || 'No description provided.'}
              </p>
            </CardContent>
          </Card>

          {/* Progress */}
          <Card>
            <CardHeader>
              <CardTitle>Progress</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <span className="text-sm font-medium">Overall Progress</span>
                  <span className="text-sm font-bold">{item?.progressPercentage || 0}%</span>
                </div>
                <div className="w-full bg-secondary h-3 rounded-full overflow-hidden">
                  <div
                    className="bg-primary h-full transition-all duration-300"
                    style={{ width: `${item?.progressPercentage || 0}%` }}
                  />
                </div>
                <div className="grid grid-cols-2 gap-4 pt-4">
                  <div className="flex items-center gap-2">
                    <Clock className="h-4 w-4 text-muted-foreground" />
                    <span className="text-sm">
                      <strong>{item?.totalHours || 0}</strong> hours spent
                    </span>
                  </div>
                  {item?.estimatedHours && (
                    <div className="flex items-center gap-2">
                      <Target className="h-4 w-4 text-muted-foreground" />
                      <span className="text-sm">
                        <strong>{item.estimatedHours}</strong> hours estimated
                      </span>
                    </div>
                  )}
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Status Actions */}
          <Card>
            <CardHeader>
              <CardTitle>Quick Actions</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex flex-wrap gap-2">
                {item?.status !== 'IN_PROGRESS' && (
                  <Button 
                    onClick={() => handleStatusUpdate('IN_PROGRESS')}
                    disabled={updateStatusMutation.isLoading}
                    size="sm"
                  >
                    Start Learning
                  </Button>
                )}
                {item?.status !== 'COMPLETED' && (
                  <Button 
                    onClick={() => handleStatusUpdate('COMPLETED')}
                    disabled={updateStatusMutation.isLoading}
                    size="sm"
                    variant="outline"
                  >
                    <CheckCircle className="mr-2 h-4 w-4" />
                    Mark Complete
                  </Button>
                )}
                {item?.status !== 'ON_HOLD' && (
                  <Button 
                    onClick={() => handleStatusUpdate('ON_HOLD')}
                    disabled={updateStatusMutation.isLoading}
                    size="sm"
                    variant="outline"
                  >
                    Put On Hold
                  </Button>
                )}
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Details */}
          <Card>
            <CardHeader>
              <CardTitle>Details</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {item?.startDate && (
                <div className="flex items-center gap-2">
                  <Calendar className="h-4 w-4 text-muted-foreground" />
                  <div>
                    <p className="text-sm font-medium">Start Date</p>
                    <p className="text-sm text-muted-foreground">
                      {new Date(item.startDate).toLocaleDateString()}
                    </p>
                  </div>
                </div>
              )}
              
              {item?.targetDate && (
                <div className="flex items-center gap-2">
                  <Target className="h-4 w-4 text-muted-foreground" />
                  <div>
                    <p className="text-sm font-medium">Target Date</p>
                    <p className="text-sm text-muted-foreground">
                      {new Date(item.targetDate).toLocaleDateString()}
                    </p>
                  </div>
                </div>
              )}

              {item?.difficulty && (
                <div className="flex items-center gap-2">
                  <div>
                    <p className="text-sm font-medium">Difficulty</p>
                    <div className="flex items-center mt-1">
                      {[...Array(5)].map((_, i) => (
                        <div
                          key={i}
                          className={`h-2 w-2 rounded-full mr-1 ${
                            i < item.difficulty ? 'bg-primary' : 'bg-muted'
                          }`}
                        />
                      ))}
                    </div>
                  </div>
                </div>
              )}

              {item?.priority && (
                <div>
                  <p className="text-sm font-medium">Priority</p>
                  <p className="text-sm text-muted-foreground capitalize">
                    {item.priority.toLowerCase()}
                  </p>
                </div>
              )}
            </CardContent>
          </Card>

          {/* Resources */}
          {item?.resources && item.resources.length > 0 && (
            <Card>
              <CardHeader>
                <CardTitle>Resources</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-2">
                  {item.resources.map((resource, index) => (
                    <div key={index} className="p-2 bg-muted rounded-md">
                      <a 
                        href={resource.url} 
                        target="_blank" 
                        rel="noopener noreferrer"
                        className="text-sm font-medium text-primary hover:underline"
                      >
                        {resource.title}
                      </a>
                      {resource.type && (
                        <p className="text-xs text-muted-foreground">{resource.type}</p>
                      )}
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          )}
        </div>
      </div>
    </div>
  )
}

export default ItemDetail
