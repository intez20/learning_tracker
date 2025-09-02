import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import Categories from './pages/Categories';
import Projects from './pages/Projects';
import ProjectDetail from './pages/ProjectDetail';
import Goals from './pages/Goals';
import Resources from './pages/Resources';
import CodeSnippets from './pages/CodeSnippets';
import WeeklyMilestones from './pages/WeeklyMilestones';
import Progress from './pages/Progress';
import Settings from './pages/Settings';
import NotFound from './pages/NotFound';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000, // 5 minutes
    },
  },
});

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <Layout>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/categories" element={<Categories />} />
            <Route path="/projects" element={<Projects />} />
            <Route path="/projects/:id" element={<ProjectDetail />} />
            <Route path="/goals" element={<Goals />} />
            <Route path="/resources" element={<Resources />} />
            <Route path="/snippets" element={<CodeSnippets />} />
            <Route path="/milestones" element={<WeeklyMilestones />} />
            <Route path="/progress" element={<Progress />} />
            <Route path="/time-tracking" element={<Progress />} />
            <Route path="/settings" element={<Settings />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </Layout>
      </Router>
    </QueryClientProvider>
  );
};

export default App;
