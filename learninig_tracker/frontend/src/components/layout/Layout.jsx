import React from 'react'
import { Link, useLocation } from 'react-router-dom'
import { useTheme } from '../theme-provider'
import { Sun, Moon, Layers, BookOpen, Target, BarChart3, WifiOff } from 'lucide-react'

const Layout = ({ children }) => {
  const { theme, setTheme } = useTheme()
  const location = useLocation()

  const isActive = (path) => {
    return location.pathname === path
      ? 'bg-primary text-primary-foreground'
      : 'hover:bg-secondary'
  }

  return (
    <div className="min-h-screen flex">
      {/* Sidebar */}
      <div className="bg-card w-64 p-4 shadow-lg flex flex-col">
        <div className="mb-8">
          <h1 className="text-2xl font-bold">Learning Tracker</h1>
        </div>

        <nav className="flex-1 space-y-2">
          <Link
            to="/"
            className={`flex items-center gap-3 p-2 rounded-md ${isActive('/')}`}
          >
            <BookOpen size={20} />
            <span>Dashboard</span>
          </Link>

          <Link
            to="/categories"
            className={`flex items-center gap-3 p-2 rounded-md ${isActive('/categories')}`}
          >
            <Layers size={20} />
            <span>Categories</span>
          </Link>

          <Link
            to="/learning-items"
            className={`flex items-center gap-3 p-2 rounded-md ${isActive('/learning-items')}`}
          >
            <BookOpen size={20} />
            <span>Learning Items</span>
          </Link>

          <Link
            to="/goals"
            className={`flex items-center gap-3 p-2 rounded-md ${isActive('/goals')}`}
          >
            <Target size={20} />
            <span>Goals</span>
          </Link>

          <Link
            to="/analytics"
            className={`flex items-center gap-3 p-2 rounded-md ${isActive('/analytics')}`}
          >
            <BarChart3 size={20} />
            <span>Analytics</span>
          </Link>

          <Link
            to="/test-connection"
            className={`flex items-center gap-3 p-2 rounded-md ${isActive('/test-connection')}`}
          >
            <WifiOff size={20} />
            <span>Test Connection</span>
          </Link>
        </nav>

        <div className="mt-auto pt-4 border-t">
          <button
            onClick={() => setTheme(theme === 'dark' ? 'light' : 'dark')}
            className="flex items-center gap-2 p-2 rounded-md w-full hover:bg-secondary"
          >
            {theme === 'dark' ? <Sun size={20} /> : <Moon size={20} />}
            <span>{theme === 'dark' ? 'Light Mode' : 'Dark Mode'}</span>
          </button>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 p-6 overflow-auto">
        {children}
      </div>
    </div>
  )
}

export default Layout
