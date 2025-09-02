import React, { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card'
import { Button } from '../components/ui/button'
import { 
  BarChart3, 
  TrendingUp, 
  Calendar, 
  Target, 
  BookOpen, 
  Clock,
  Award,
  Activity,
  PieChart,
  LineChart,
  CheckCircle2
} from 'lucide-react'
import { analyticsService } from '../api/services'

const Analytics = () => {
  const [timeRange, setTimeRange] = useState('30d')

  const { data: overview, isLoading: overviewLoading } = useQuery(
    ['analytics-overview'],
    () => analyticsService.getOverview(),
    {
      refetchOnWindowFocus: false,
    }
  )

  const { data: progressData, isLoading: progressLoading } = useQuery(
    ['analytics-progress', timeRange],
    () => analyticsService.getProgressData(timeRange),
    {
      refetchOnWindowFocus: false,
    }
  )
  
  const { data: recentActivity, isLoading: activityLoading } = useQuery(
    ['analytics-activity'],
    () => analyticsService.getRecentActivity(),
    {
      refetchOnWindowFocus: false,
    }
  )
  
  const { data: insights, isLoading: insightsLoading } = useQuery(
    ['analytics-insights'],
    () => analyticsService.getInsights(),
    {
      refetchOnWindowFocus: false,
    }
  )

  const StatCard = ({ title, value, subtitle, icon: Icon, trend, color = 'blue' }) => (
    <Card>
      <CardContent className="p-6">
        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-muted-foreground">{title}</p>
            <p className="text-2xl font-bold">{value}</p>
            {subtitle && (
              <p className="text-sm text-muted-foreground">{subtitle}</p>
            )}
          </div>
          <div className={`p-3 rounded-full bg-${color}-100`}>
            <Icon className={`h-6 w-6 text-${color}-600`} />
          </div>
        </div>
        {trend && (
          <div className={`flex items-center mt-4 text-sm ${
            trend.direction === 'up' ? 'text-green-600' : 'text-red-600'
          }`}>
            <TrendingUp className="h-4 w-4 mr-1" />
            {trend.value} from last period
          </div>
        )}
      </CardContent>
    </Card>
  )

  const ProgressChart = ({ title, data, type = 'bar' }) => (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          {type === 'bar' ? <BarChart3 className="h-5 w-5" /> : <LineChart className="h-5 w-5" />}
          {title}
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          {data.map((item, index) => (
            <div key={index} className="space-y-2">
              <div className="flex justify-between text-sm">
                <span>{item.week || item.month || item.category}</span>
                <span className="font-medium">
                  {item.hours ? `${item.hours}h` : `${item.completed}/${item.total}`}
                </span>
              </div>
              <div className="w-full bg-secondary h-2 rounded-full overflow-hidden">
                <div
                  className="bg-primary h-full transition-all"
                  style={{ 
                    width: `${
                      item.percentage || 
                      (item.hours ? Math.min((item.hours / 20) * 100, 100) : 
                       Math.min((item.completed / item.total) * 100, 100))
                    }%` 
                  }}
                />
              </div>
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  )

  if (overviewLoading || progressLoading || activityLoading || insightsLoading) {
    return (
      <div className="flex items-center justify-center h-96">
        <div className="text-center">
          <BarChart3 className="h-12 w-12 animate-pulse text-muted-foreground mx-auto mb-4" />
          <p>Loading analytics...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Learning Analytics</h1>
          <p className="text-muted-foreground mt-2">
            Track your learning progress and insights.
          </p>
        </div>
        <div className="flex items-center gap-2">
          <select
            value={timeRange}
            onChange={(e) => setTimeRange(e.target.value)}
            className="px-3 py-2 border rounded-md"
          >
            <option value="7d">Last 7 days</option>
            <option value="30d">Last 30 days</option>
            <option value="90d">Last 90 days</option>
            <option value="1y">Last year</option>
          </select>
        </div>
      </div>

      {/* Overview Stats */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Learning Goals"
          value={overview?.completedGoals || 0}
          subtitle={`${overview?.totalGoals || 0} total`}
          icon={Target}
          color="blue"
        />
        <StatCard
          title="Study Hours"
          value={`${overview?.totalHours || 0}h`}
          subtitle={`~${overview?.avgSessionTime || 0}min avg session`}
          icon={Clock}
          color="green"
        />
        <StatCard
          title="Projects"
          value={overview?.completedProjects || 0}
          subtitle={`${overview?.totalProjects || 0} total`}
          icon={BookOpen}
          color="purple"
        />
        <StatCard
          title="Streak"
          value={`${overview?.streakDays || 0} days`}
          subtitle="Current streak"
          icon={Award}
          color="orange"
        />
      </div>

      {/* Charts Section */}
      <div className="grid gap-6 lg:grid-cols-2">
        {/* Weekly Progress */}
        <ProgressChart
          title="Weekly Study Hours"
          data={progressData?.weeklyProgress || []}
          type="bar"
        />

        {/* Goal Completion Rate */}
        <ProgressChart
          title="Monthly Goal Completion"
          data={progressData?.monthlyGoals || []}
          type="line"
        />
      </div>

      {/* Category Breakdown */}
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <PieChart className="h-5 w-5" />
              Learning Categories
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {progressData?.categoryBreakdown?.map((category, index) => (
                <div key={index} className="space-y-2">
                  <div className="flex justify-between text-sm">
                    <span>{category.category}</span>
                    <span className="font-medium">{category.hours}h ({category.percentage}%)</span>
                  </div>
                  <div className="w-full bg-secondary h-2 rounded-full overflow-hidden">
                    <div
                      className="bg-primary h-full transition-all"
                      style={{ width: `${category.percentage}%` }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Recent Activity */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Activity className="h-5 w-5" />
              Recent Activity
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {recentActivity?.map((activity) => {
                // Determine the icon based on activity type
                let Icon = BookOpen;
                let iconColor = 'text-blue-600';
                
                if (activity.type === 'GOAL_COMPLETED') {
                  Icon = CheckCircle2;
                  iconColor = 'text-green-600';
                } else if (activity.type === 'GOAL_CREATED') {
                  Icon = Target;
                  iconColor = 'text-purple-600';
                } else if (activity.type === 'HOURS_LOGGED') {
                  Icon = Clock;
                  iconColor = 'text-orange-600';
                }
                
                // Format the timestamp
                const activityDate = new Date(activity.timestamp);
                const now = new Date();
                const diffHours = Math.floor((now - activityDate) / (1000 * 60 * 60));
                const diffDays = Math.floor(diffHours / 24);
                
                let timeAgo;
                if (diffHours < 1) {
                  timeAgo = 'Just now';
                } else if (diffHours < 24) {
                  timeAgo = `${diffHours} ${diffHours === 1 ? 'hour' : 'hours'} ago`;
                } else {
                  timeAgo = `${diffDays} ${diffDays === 1 ? 'day' : 'days'} ago`;
                }
                
                return (
                  <div key={activity.id} className="flex items-center gap-3 p-3 bg-secondary/50 rounded-lg">
                    <Icon className={`h-4 w-4 ${iconColor}`} />
                    <div className="flex-1">
                      <p className="text-sm font-medium">{activity.title}</p>
                      <p className="text-xs text-muted-foreground">{timeAgo}</p>
                    </div>
                  </div>
                );
              })}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Insights */}
      <Card>
        <CardHeader>
          <CardTitle>Learning Insights</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            <div className="p-4 bg-blue-50 rounded-lg">
              <h4 className="font-medium text-blue-900 mb-2">Most Productive Day</h4>
              <p className="text-sm text-blue-700">
                You tend to learn most effectively on <strong>{insights?.mostProductiveDay?.day || 'Monday'}</strong> with an average of {insights?.mostProductiveDay?.averageHours || 2} hours.
              </p>
            </div>
            <div className="p-4 bg-green-50 rounded-lg">
              <h4 className="font-medium text-green-900 mb-2">Goal Completion Rate</h4>
              <p className="text-sm text-green-700">
                You complete <strong>{insights?.goalCompletionRate || 0}%</strong> of your goals on time. {insights?.goalCompletionRate > 70 ? 'Great consistency!' : 'Keep pushing!'}
              </p>
            </div>
            <div className="p-4 bg-purple-50 rounded-lg">
              <h4 className="font-medium text-purple-900 mb-2">Learning Streak</h4>
              <p className="text-sm text-purple-700">
                Your longest streak was <strong>{insights?.longestStreak || 0} days</strong>. Keep building those habits!
              </p>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

export default Analytics
