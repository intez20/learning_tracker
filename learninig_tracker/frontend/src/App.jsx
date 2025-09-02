import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { Toaster } from './components/ui/toaster-new'
import Layout from './components/layout/Layout'
import Dashboard from './pages/Dashboard'
import Categories from './pages/Categories'
import LearningItems from './pages/LearningItems'
import ItemDetail from './pages/ItemDetail'
import NewLearningItem from './pages/NewLearningItem'
import Goals from './pages/Goals'
import Analytics from './pages/Analytics'
import TestBackendConnection from './pages/TestBackendConnection'

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/categories" element={<Categories />} />
          <Route path="/learning-items" element={<LearningItems />} />
          <Route path="/learning-items/new" element={<NewLearningItem />} />
          <Route path="/learning-items/:id" element={<ItemDetail />} />
          <Route path="/goals" element={<Goals />} />
          <Route path="/analytics" element={<Analytics />} />
          <Route path="/test-connection" element={<TestBackendConnection />} />
        </Routes>
      </Layout>
      <Toaster />
    </Router>
  )
}

export default App
