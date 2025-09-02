import { api } from './api';
import { ProjectDTO, Project } from '../types';

const BASE_URL = '/projects';

export const ProjectService = {
  getAllProjects: async (): Promise<ProjectDTO[]> => {
    const response = await api.get(BASE_URL);
    return response.data;
  },

  getProjectById: async (id: string): Promise<ProjectDTO> => {
    const response = await api.get(`${BASE_URL}/${id}`);
    return response.data;
  },

  createProject: async (project: Omit<Project, 'id'>): Promise<ProjectDTO> => {
    const response = await api.post(BASE_URL, project);
    return response.data;
  },

  updateProject: async (id: string, project: Omit<Project, 'id'>): Promise<ProjectDTO> => {
    const response = await api.put(`${BASE_URL}/${id}`, project);
    return response.data;
  },

  deleteProject: async (id: string): Promise<void> => {
    await api.delete(`${BASE_URL}/${id}`);
  },

  getProjectsByCategory: async (categoryId: string): Promise<ProjectDTO[]> => {
    const response = await api.get(`${BASE_URL}/category/${categoryId}`);
    return response.data;
  },

  getProjectsByStatus: async (status: string): Promise<ProjectDTO[]> => {
    const response = await api.get(`${BASE_URL}/status/${status}`);
    return response.data;
  },

  getProjectsByPriority: async (priority: string): Promise<ProjectDTO[]> => {
    const response = await api.get(`${BASE_URL}/priority/${priority}`);
    return response.data;
  },

  getActiveProjects: async (): Promise<ProjectDTO[]> => {
    const response = await api.get(`${BASE_URL}/active`);
    return response.data;
  },
};
