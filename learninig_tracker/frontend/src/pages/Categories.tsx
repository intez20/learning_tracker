import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { CategoryService } from '../services/categoryService';
import { CategoryDTO } from '../types';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { FolderKanban, Plus, Edit, Trash2 } from 'lucide-react';

interface CategoryFormData {
  name: string;
  description: string;
  color: string;
}

const Categories = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCategory, setEditingCategory] = useState<CategoryDTO | null>(null);
  const [formData, setFormData] = useState<CategoryFormData>({
    name: '',
    description: '',
    color: '#3b82f6', // Default blue color
  });

  const queryClient = useQueryClient();

  const { data: categories, isLoading, error } = useQuery(
    ['categories'],
    CategoryService.getAllCategories
  );

  const createMutation = useMutation(
    (data: CategoryFormData) => CategoryService.createCategory(data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['categories']);
        resetForm();
      },
    }
  );

  const updateMutation = useMutation(
    ({ id, data }: { id: string; data: CategoryFormData }) => 
      CategoryService.updateCategory(id, data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['categories']);
        resetForm();
      },
    }
  );

  const deleteMutation = useMutation(
    (id: string) => CategoryService.deleteCategory(id),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['categories']);
      },
    }
  );

  const resetForm = () => {
    setFormData({
      name: '',
      description: '',
      color: '#3b82f6',
    });
    setEditingCategory(null);
    setIsModalOpen(false);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (editingCategory) {
      updateMutation.mutate({
        id: editingCategory.id,
        data: formData,
      });
    } else {
      createMutation.mutate(formData);
    }
  };

  const handleEdit = (category: CategoryDTO) => {
    setEditingCategory(category);
    setFormData({
      name: category.name,
      description: category.description,
      color: category.color,
    });
    setIsModalOpen(true);
  };

  const handleDelete = (id: string) => {
    if (window.confirm('Are you sure you want to delete this category?')) {
      deleteMutation.mutate(id);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  if (isLoading) {
    return <div className="flex items-center justify-center h-96">Loading categories...</div>;
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <h2 className="text-xl font-bold text-destructive">Error loading categories</h2>
          <p className="mt-2">Please try again later or check your connection.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Categories</h1>
          <p className="text-muted-foreground mt-2">
            Organize your learning projects into categories.
          </p>
        </div>
        <Button onClick={() => setIsModalOpen(true)}>
          <Plus className="mr-2 h-4 w-4" /> Add Category
        </Button>
      </div>

      {/* Categories grid */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {categories?.map((category) => (
          <Card 
            key={category.id}
            className="overflow-hidden"
            style={{ borderTop: `4px solid ${category.color}` }}
          >
            <CardHeader className="pb-2">
              <CardTitle className="flex items-center text-xl">
                <div 
                  className="h-4 w-4 rounded-full mr-2"
                  style={{ backgroundColor: category.color }}
                />
                {category.name}
              </CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-muted-foreground mb-4">
                {category.description || 'No description'}
              </p>
              <div className="flex items-center justify-between">
                <div className="text-sm">
                  <span className="font-medium">{category.projectCount}</span> projects
                </div>
                <div className="flex space-x-2">
                  <Button 
                    variant="outline" 
                    size="sm"
                    onClick={() => handleEdit(category)}
                  >
                    <Edit className="h-4 w-4" />
                  </Button>
                  <Button 
                    variant="outline" 
                    size="sm"
                    onClick={() => handleDelete(category.id)}
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}

        {/* Add category card */}
        <Card 
          className="flex flex-col items-center justify-center h-48 border-dashed cursor-pointer hover:bg-accent/50 transition-colors"
          onClick={() => setIsModalOpen(true)}
        >
          <FolderKanban className="h-12 w-12 text-muted-foreground mb-2" />
          <p className="text-muted-foreground font-medium">Add new category</p>
        </Card>
      </div>

      {/* Modal for adding/editing category */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-background rounded-lg p-6 w-full max-w-md">
            <h2 className="text-xl font-bold mb-4">
              {editingCategory ? 'Edit Category' : 'Add New Category'}
            </h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-1" htmlFor="name">
                  Name
                </label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border rounded-md"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1" htmlFor="description">
                  Description
                </label>
                <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  className="w-full px-3 py-2 border rounded-md"
                  rows={3}
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1" htmlFor="color">
                  Color
                </label>
                <div className="flex items-center space-x-2">
                  <input
                    type="color"
                    id="color"
                    name="color"
                    value={formData.color}
                    onChange={handleInputChange}
                    className="w-10 h-10"
                  />
                  <input
                    type="text"
                    value={formData.color}
                    onChange={handleInputChange}
                    name="color"
                    className="w-full px-3 py-2 border rounded-md"
                  />
                </div>
              </div>
              <div className="flex justify-end space-x-2 pt-4">
                <Button type="button" variant="outline" onClick={resetForm}>
                  Cancel
                </Button>
                <Button type="submit">
                  {editingCategory ? 'Update' : 'Create'}
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Categories;
