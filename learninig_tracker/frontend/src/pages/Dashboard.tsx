import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { DashboardService } from '../services/dashboardService';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { BarChart2, BookOpen, CheckCircle, Clock, Target } from 'lucide-react';

const Dashboard = () => {
  const { data: stats, isLoading, error } = useQuery(['dashboardStats'], DashboardService.getDashboardStats);

  if (isLoading) {
    return <div className="flex items-center justify-center h-96">Loading dashboard data...</div>;
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <h2 className="text-xl font-bold text-destructive">Error loading dashboard</h2>
          <p className="mt-2">Please try again later or check your connection.</p>
        </div>
      </div>
    );
  }

  // Transform data for charts
  const categoryData = stats ? 
    Object.entries(stats.timeSpentByCategory).map(([name, minutes]) => ({
      name,
      minutes,
      hours: Math.round(minutes / 60 * 10) / 10,
    })) : [];

  const projectData = stats ? 
    Object.entries(stats.timeSpentByProject).map(([name, minutes]) => ({
      name,
      minutes,
      hours: Math.round(minutes / 60 * 10) / 10,
    })) : [];

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Learning Dashboard</h1>
        <p className="text-muted-foreground mt-2">
          Track your learning progress and stay motivated.
        </p>
      </div>
      
      {/* Stats overview */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Total Projects</CardTitle>
            <BookOpen className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats?.totalProjects || 0}</div>
            <p className="text-xs text-muted-foreground mt-1">
              {stats?.activeProjects || 0} active
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Total Learning Time</CardTitle>
            <Clock className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {stats?.totalLearningTime 
                ? `${Math.floor(stats.totalLearningTime / 60)} hours` 
                : '0 hours'}
            </div>
            <p className="text-xs text-muted-foreground mt-1">
              {stats?.learningTimeThisWeek 
                ? `${Math.floor(stats.learningTimeThisWeek / 60)} hours this week` 
                : '0 hours this week'}
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Current Streak</CardTitle>
            <Target className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats?.currentStreak || 0} days</div>
            <p className="text-xs text-muted-foreground mt-1">
              Longest: {stats?.longestStreak || 0} days
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">High Priority Goals</CardTitle>
            <CheckCircle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats?.incompleteHighPriorityGoals || 0}</div>
            <p className="text-xs text-muted-foreground mt-1">
              Pending completion
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Charts */}
      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center">
              <BarChart2 className="mr-2 h-5 w-5" />
              Time Spent by Category
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="h-80">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={categoryData} layout="vertical">
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis type="number" unit="h" />
                  <YAxis dataKey="name" type="category" width={120} />
                  <Tooltip 
                    formatter={(value) => [`${value} hours`, 'Time Spent']}
                    labelFormatter={(value) => `Category: ${value}`}
                  />
                  <Bar dataKey="hours" fill="#8884d8" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center">
              <BarChart2 className="mr-2 h-5 w-5" />
              Time Spent by Project
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="h-80">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={projectData} layout="vertical">
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis type="number" unit="h" />
                  <YAxis dataKey="name" type="category" width={120} />
                  <Tooltip 
                    formatter={(value) => [`${value} hours`, 'Time Spent']}
                    labelFormatter={(value) => `Project: ${value}`}
                  />
                  <Bar dataKey="hours" fill="#82ca9d" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Upcoming goals */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center">
            <Target className="mr-2 h-5 w-5" />
            Upcoming Goals
          </CardTitle>
        </CardHeader>
        <CardContent>
          {stats?.upcomingGoals && stats.upcomingGoals.length > 0 ? (
            <div className="space-y-4">
              {stats.upcomingGoals.slice(0, 5).map((goal) => (
                <div key={goal.id} className="flex justify-between border-b pb-2">
                  <div>
                    <p className="font-medium">{goal.title}</p>
                    <p className="text-sm text-muted-foreground">{goal.projectName}</p>
                  </div>
                  <div className="flex items-center">
                    <span className={`px-2 py-1 rounded-full text-xs ${
                      goal.priority === 'HIGH' 
                        ? 'bg-red-100 text-red-800' 
                        : goal.priority === 'MEDIUM'
                        ? 'bg-yellow-100 text-yellow-800'
                        : 'bg-green-100 text-green-800'
                    }`}>
                      {goal.priority}
                    </span>
                    <span className="ml-2 text-sm">
                      {goal.dueDate ? new Date(goal.dueDate).toLocaleDateString() : 'No due date'}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-muted-foreground">No upcoming goals found.</p>
          )}
        </CardContent>
      </Card>

      {/* Recent entries */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center">
            <Clock className="mr-2 h-5 w-5" />
            Recent Progress Entries
          </CardTitle>
        </CardHeader>
        <CardContent>
          {stats?.recentEntries && stats.recentEntries.length > 0 ? (
            <div className="space-y-4">
              {stats.recentEntries.slice(0, 5).map((entry) => (
                <div key={entry.id} className="flex justify-between border-b pb-2">
                  <div>
                    <p className="font-medium">{entry.projectName}</p>
                    <p className="text-sm text-muted-foreground">
                      {entry.description.substring(0, 100)}
                      {entry.description.length > 100 ? '...' : ''}
                    </p>
                  </div>
                  <div className="flex flex-col items-end">
                    <span className="text-sm font-medium">
                      {Math.floor(entry.minutesSpent / 60)} h {entry.minutesSpent % 60} m
                    </span>
                    <span className="text-xs text-muted-foreground">
                      {new Date(entry.date).toLocaleDateString()}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p className="text-muted-foreground">No recent entries found.</p>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default Dashboard;
