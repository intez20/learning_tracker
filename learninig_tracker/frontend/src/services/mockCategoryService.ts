import { CategoryDTO } from '../types';

// Mock data for categories
const mockCategories: CategoryDTO[] = [
  { id: '1', name: 'Programming', description: 'Software development and coding', color: '#4A63EE', projectCount: 5 },
  { id: '2', name: 'Design', description: 'UI/UX and graphic design', color: '#FF5733', projectCount: 3 },
  { id: '3', name: 'Data Science', description: 'Machine learning and data analysis', color: '#33FF57', projectCount: 2 },
  { id: '4', name: 'DevOps', description: 'Infrastructure and deployment', color: '#9B59B6', projectCount: 1 },
  { id: '5', name: 'Soft Skills', description: 'Communication and leadership', color: '#F1C40F', projectCount: 2 },
];

// Replace the original CategoryService with a mock implementation
export const CategoryService = {
  getAllCategories: async (): Promise<CategoryDTO[]> => {
    // Simulate network delay
    await new Promise(resolve => setTimeout(resolve, 500));
    return mockCategories;
  },

  getCategoryById: async (id: string): Promise<CategoryDTO> => {
    // Simulate network delay
    await new Promise(resolve => setTimeout(resolve, 300));
    const category = mockCategories.find(cat => cat.id === id);
    if (!category) {
      throw new Error(`Category with id ${id} not found`);
    }
    return category;
  },

  createCategory: async (category: Omit<CategoryDTO, 'id'>): Promise<CategoryDTO> => {
    // Simulate network delay
    await new Promise(resolve => setTimeout(resolve, 600));
    const newId = (mockCategories.length + 1).toString();
    const newCategory = { ...category, id: newId };
    mockCategories.push(newCategory);
    return newCategory;
  },

  updateCategory: async (id: string, category: Partial<CategoryDTO>): Promise<CategoryDTO> => {
    // Simulate network delay
    await new Promise(resolve => setTimeout(resolve, 400));
    const index = mockCategories.findIndex(cat => cat.id === id);
    if (index === -1) {
      throw new Error(`Category with id ${id} not found`);
    }
    mockCategories[index] = { ...mockCategories[index], ...category };
    return mockCategories[index];
  },

  deleteCategory: async (id: string): Promise<void> => {
    // Simulate network delay
    await new Promise(resolve => setTimeout(resolve, 300));
    const index = mockCategories.findIndex(cat => cat.id === id);
    if (index === -1) {
      throw new Error(`Category with id ${id} not found`);
    }
    mockCategories.splice(index, 1);
  }
};
