import { useState, useEffect } from 'react'
import axios from 'axios'
import { Button } from '../components/ui/button'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '../components/ui/card'
import { Alert, AlertTitle, AlertDescription } from '../components/ui/alert'

const TestBackendConnection = () => {
  const [testResult, setTestResult] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const testBackendConnection = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await axios.get('/api/test')
      setTestResult(response.data)
      console.log('Backend connection test result:', response.data)
    } catch (err) {
      console.error('Backend connection test failed:', err)
      setError(err.message || 'Failed to connect to backend')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    testBackendConnection()
  }, [])

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Backend Connection Test</h1>
      
      <Card className="mb-6">
        <CardHeader>
          <CardTitle>Connection Test</CardTitle>
          <CardDescription>
            This page tests the connection between the frontend and backend.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Button 
            onClick={testBackendConnection}
            disabled={loading}
          >
            {loading ? 'Testing...' : 'Test Connection'}
          </Button>
          
          {error && (
            <Alert variant="destructive" className="mt-4">
              <AlertTitle>Error</AlertTitle>
              <AlertDescription>
                {error}
                <div className="mt-2">
                  <p className="text-sm">
                    This could be due to:
                  </p>
                  <ul className="list-disc list-inside text-sm">
                    <li>Backend server not running</li>
                    <li>CORS issues</li>
                    <li>Incorrect proxy configuration</li>
                    <li>Network connectivity issues</li>
                  </ul>
                </div>
              </AlertDescription>
            </Alert>
          )}
          
          {testResult && !error && (
            <Alert variant="default" className="mt-4 bg-green-50">
              <AlertTitle>Success!</AlertTitle>
              <AlertDescription>
                <p>The frontend successfully connected to the backend.</p>
                <pre className="bg-gray-100 p-2 rounded mt-2 text-xs">
                  {JSON.stringify(testResult, null, 2)}
                </pre>
              </AlertDescription>
            </Alert>
          )}
        </CardContent>
      </Card>
      
      <Card>
        <CardHeader>
          <CardTitle>Debug Information</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-2">
            <div>
              <strong>Frontend URL:</strong> {window.location.origin}
            </div>
            <div>
              <strong>API Base URL:</strong> {axios.defaults.baseURL || 'Not set (using relative URLs)'}
            </div>
            <div>
              <strong>Vite Proxy Configuration:</strong> Requests to /api/* are proxied to http://localhost:8081
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}

export default TestBackendConnection
