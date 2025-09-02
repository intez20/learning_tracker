import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { 
  BarChart2, 
  BookOpen, 
  Code, 
  FolderKanban, 
  LayoutDashboard, 
  LucideIcon, 
  Menu, 
  Settings, 
  Target,
  Calendar,
  Clock
} from 'lucide-react';

type SidebarItem = {
  title: string;
  icon: LucideIcon;
  path: string;
};

const sidebarItems: SidebarItem[] = [
  {
    title: 'Dashboard',
    icon: LayoutDashboard,
    path: '/',
  },
  {
    title: 'Categories',
    icon: FolderKanban,
    path: '/categories',
  },
  {
    title: 'Projects',
    icon: BookOpen,
    path: '/projects',
  },
  {
    title: 'Progress',
    icon: BarChart2,
    path: '/progress',
  },
  {
    title: 'Goals',
    icon: Target,
    path: '/goals',
  },
  {
    title: 'Resources',
    icon: BookOpen,
    path: '/resources',
  },
  {
    title: 'Code Snippets',
    icon: Code,
    path: '/snippets',
  },
  {
    title: 'Weekly Milestones',
    icon: Calendar,
    path: '/milestones',
  },
  {
    title: 'Time Tracking',
    icon: Clock,
    path: '/time-tracking',
  },
  {
    title: 'Settings',
    icon: Settings,
    path: '/settings',
  },
];

const Layout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [sidebarOpen, setSidebarOpen] = React.useState(false);
  const location = useLocation();

  return (
    <div className="flex min-h-screen bg-background">
      {/* Mobile sidebar toggle */}
      <button
        className="fixed bottom-4 right-4 z-50 rounded-full bg-primary p-3 text-primary-foreground shadow-lg md:hidden"
        onClick={() => setSidebarOpen(!sidebarOpen)}
      >
        <Menu size={24} />
      </button>

      {/* Sidebar */}
      <aside
        className={`fixed inset-y-0 left-0 z-40 w-64 transform bg-card shadow-lg transition-transform duration-200 ease-in-out md:relative md:translate-x-0 ${
          sidebarOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <div className="flex h-20 items-center border-b px-6">
          <h1 className="text-xl font-bold">Learning Tracker</h1>
        </div>
        <nav className="mt-6 px-4">
          <ul className="space-y-2">
            {sidebarItems.map((item) => (
              <li key={item.path}>
                <Link
                  to={item.path}
                  className={`flex items-center rounded-md px-4 py-3 transition-colors ${
                    location.pathname === item.path
                      ? 'bg-primary/10 text-primary'
                      : 'text-foreground hover:bg-muted'
                  }`}
                >
                  <item.icon className="mr-3 h-5 w-5" />
                  <span>{item.title}</span>
                </Link>
              </li>
            ))}
          </ul>
        </nav>
      </aside>

      {/* Main content */}
      <main className="flex-1 overflow-x-hidden p-6 md:p-8">
        {children}
      </main>

      {/* Backdrop for mobile sidebar */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 z-30 bg-black/50 md:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}
    </div>
  );
};

export default Layout;
