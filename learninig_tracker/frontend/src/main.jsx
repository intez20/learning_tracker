import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './globals.css'
import { ThemeProvider } from './components/theme-provider'
import { ToastProvider } from './components/ui/use-toast.jsx'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 60 * 1000, // 1 minute
    },
  },
})

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <ThemeProvider defaultTheme="system" storageKey="learning-tracker-theme">
        <ToastProvider>
          <App />
        </ToastProvider>
      </ThemeProvider>
    </QueryClientProvider>
  </React.StrictMode>,
)
