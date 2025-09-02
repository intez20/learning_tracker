export interface Category {
  id: string;
  name: string;
  description: string;
  color: string;
}

export interface CategoryDTO extends Category {
  projectCount: number;
}

export interface Project {
  id: string;
  name: string;
  description: string;
  status: 'NOT_STARTED' | 'IN_PROGRESS' | 'ON_HOLD' | 'COMPLETED';
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  categoryId: string;
  startDate: string; // ISO date string
  estimatedCompletionDate: string | null; // ISO date string
  completionDate: string | null; // ISO date string
}

export interface ProjectDTO extends Project {
  categoryName: string;
  totalTimeSpent: number;
  resourceCount: number;
  goalCount: number;
  completedGoalCount: number;
  completionPercentage: number;
  lastActivity: string; // ISO date string
}

export interface LearningResource {
  id: string;
  title: string;
  url: string;
  description: string;
  resourceType: string;
  author: string;
  projectId: string;
  isPrimary: boolean;
  notes: string;
}

export interface LearningResourceDTO extends LearningResource {
  projectName: string;
}

export interface ProgressEntry {
  id: string;
  date: string; // ISO date string
  minutesSpent: number;
  description: string;
  projectId: string;
  challenges: string;
  learnings: string;
  nextSteps: string;
  mood: string;
}

export interface ProgressEntryDTO extends ProgressEntry {
  projectName: string;
}

export interface LearningGoal {
  id: string;
  title: string;
  description: string;
  projectId: string;
  dueDate: string | null; // ISO date string
  completed: boolean;
  completedDate: string | null; // ISO date string
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
}

export interface LearningGoalDTO extends LearningGoal {
  projectName: string;
  isOverdue: boolean;
  daysUntilDue: number;
}

export interface CodeSnippet {
  id: string;
  title: string;
  description: string;
  code: string;
  language: string;
  tags: string;
  projectId: string;
}

export interface CodeSnippetDTO extends CodeSnippet {
  projectName: string;
}

export interface WeeklyMilestone {
  id: string;
  title: string;
  description: string;
  projectId: string;
  weekStartDate: string; // ISO date string
  weekEndDate: string; // ISO date string
  completed: boolean;
  completedDate: string | null; // ISO date string
  plannedTasks: string;
  completedTasks: string;
  reflection: string;
}

export interface WeeklyMilestoneDTO extends WeeklyMilestone {
  projectName: string;
  completionPercentage: number;
  isCurrent: boolean;
}

export interface DashboardStats {
  totalProjects: number;
  activeProjects: number;
  completedProjects: number;
  totalCategories: number;
  totalLearningTime: number;
  learningTimeThisWeek: number;
  learningTimeThisMonth: number;
  streakDays: string[]; // ISO date strings
  currentStreak: number;
  longestStreak: number;
  incompleteHighPriorityGoals: number;
  timeSpentByCategory: Record<string, number>;
  timeSpentByProject: Record<string, number>;
  topProjects: ProjectDTO[];
  categories: CategoryDTO[];
  upcomingGoals: LearningGoalDTO[];
  recentEntries: ProgressEntryDTO[];
}
