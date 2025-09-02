// Temporarily importing and re-exporting the mock service
// Remove this and uncomment the original implementation when your backend is ready
import { CategoryService } from './mockCategoryService';
export { CategoryService };

/*
import { api } from './api';
import { CategoryDTO, Category } from '../types';

const BASE_URL = '/categories';

export const CategoryService = {
  getAllCategories: async (): Promise<CategoryDTO[]> => {
    const response = await api.get(BASE_URL);
    return response.data;
  },

  getCategoryById: async (id: string): Promise<CategoryDTO> => {
    const response = await api.get(`${BASE_URL}/${id}`);
    return response.data;
  },

  createCategory: async (category: Omit<Category, 'id'>): Promise<CategoryDTO> => {
    const response = await api.post(BASE_URL, category);
    return response.data;
  },

  updateCategory: async (id: string, category: Omit<Category, 'id'>): Promise<CategoryDTO> => {
    const response = await api.put(`${BASE_URL}/${id}`, category);
    return response.data;
  },

  deleteCategory: async (id: string): Promise<void> => {
    await api.delete(`${BASE_URL}/${id}`);
  },

  searchCategories: async (name: string): Promise<CategoryDTO[]> => {
    const response = await api.get(`${BASE_URL}/search`, { params: { name } });
    return response.data;
  }
};
*/
