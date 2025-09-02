import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { learningItemService, categoryService } from '../api/services'
import { useQuery } from '@tanstack/react-query'
import { Button } from '../components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card'
import { ArrowLeft, Save } from 'lucide-react'
import { useToast } from '../components/ui/use-toast.jsx'

const NewLearningItem = () => {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const { toast } = useToast()
  
  // State for the new learning item
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    status: 'NOT_STARTED',
    priority: 'MEDIUM',
    categoryId: '',
    targetDate: ''
  })

  // Fetch categories for the dropdown
  const { data: categories } = useQuery(
    ['categories'],
    () => categoryService.getAll(),
    {
      refetchOnWindowFocus: false,
    }
  )

  // Mutation for creating a learning item
  const createMutation = useMutation(
    (newItem) => learningItemService.create(newItem),
    {
      onSuccess: (data) => {
        queryClient.invalidateQueries(['learningItems'])
        toast({
          title: "Success!",
          description: "Learning item created successfully.",
        })
        navigate(`/learning-items/${data.id}`)
      },
      onError: (error) => {
        console.error('Error creating learning item:', error)
        toast({
          title: "Error",
          description: "Failed to create learning item. Please try again.",
          variant: "destructive"
        })
      }
    }
  )

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    createMutation.mutate(formData)
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Button 
          onClick={() => navigate('/learning-items')} 
          variant="ghost" 
          size="sm"
        >
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <h1 className="text-3xl font-bold tracking-tight">Create New Learning Item</h1>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>Basic Information</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="grid gap-4">
              <div>
                <label htmlFor="title" className="block text-sm font-medium mb-1">
                  Title*
                </label>
                <input
                  id="title"
                  name="title"
                  type="text"
                  value={formData.title}
                  onChange={handleChange}
                  required
                  className="w-full p-2 border rounded-md"
                  placeholder="Enter a title for your learning item"
                />
              </div>

              <div>
                <label htmlFor="description" className="block text-sm font-medium mb-1">
                  Description
                </label>
                <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  rows={4}
                  className="w-full p-2 border rounded-md"
                  placeholder="Describe what you want to learn"
                />
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label htmlFor="status" className="block text-sm font-medium mb-1">
                    Status*
                  </label>
                  <select
                    id="status"
                    name="status"
                    value={formData.status}
                    onChange={handleChange}
                    required
                    className="w-full p-2 border rounded-md"
                  >
                    <option value="NOT_STARTED">Not Started</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="ON_HOLD">On Hold</option>
                    <option value="COMPLETED">Completed</option>
                  </select>
                </div>

                <div>
                  <label htmlFor="priority" className="block text-sm font-medium mb-1">
                    Priority*
                  </label>
                  <select
                    id="priority"
                    name="priority"
                    value={formData.priority}
                    onChange={handleChange}
                    required
                    className="w-full p-2 border rounded-md"
                  >
                    <option value="LOW">Low</option>
                    <option value="MEDIUM">Medium</option>
                    <option value="HIGH">High</option>
                  </select>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label htmlFor="categoryId" className="block text-sm font-medium mb-1">
                    Category
                  </label>
                  <select
                    id="categoryId"
                    name="categoryId"
                    value={formData.categoryId}
                    onChange={handleChange}
                    className="w-full p-2 border rounded-md"
                  >
                    <option value="">Select a category</option>
                    {categories?.map(category => (
                      <option key={category.id} value={category.id}>
                        {category.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label htmlFor="targetDate" className="block text-sm font-medium mb-1">
                    Target Completion Date
                  </label>
                  <input
                    id="targetDate"
                    name="targetDate"
                    type="date"
                    value={formData.targetDate}
                    onChange={handleChange}
                    className="w-full p-2 border rounded-md"
                  />
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        <div className="flex justify-end gap-4">
          <Button
            type="button"
            variant="outline"
            onClick={() => navigate('/learning-items')}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            disabled={createMutation.isLoading}
          >
            <Save className="mr-2 h-4 w-4" />
            {createMutation.isLoading ? 'Creating...' : 'Create Learning Item'}
          </Button>
        </div>
      </form>
    </div>
  )
}

export default NewLearningItem
