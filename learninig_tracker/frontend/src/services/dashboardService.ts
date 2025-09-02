import { api } from './api';
import { DashboardStats } from '../types';

const BASE_URL = '/dashboard';

export const DashboardService = {
  getDashboardStats: async (): Promise<DashboardStats> => {
    const response = await api.get(`${BASE_URL}/stats`);
    return response.data;
  },
};
