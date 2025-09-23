"use client"

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'

export default function TestConnectionPage() {
  const [result, setResult] = useState<string>('')
  const [isLoading, setIsLoading] = useState(false)

  const testBackendConnection = async () => {
    setIsLoading(true)
    setResult('Testing connection...')

    try {
      // Test 1: Basic connectivity
      const response = await fetch('http://localhost:8080/api/rooms', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })

      if (response.ok) {
        const data = await response.json()
        setResult(`✅ Backend connection successful!\n\nResponse: ${JSON.stringify(data, null, 2)}`)
      } else {
        setResult(`❌ Backend responded with status: ${response.status}\n\nResponse: ${await response.text()}`)
      }
    } catch (error: any) {
      setResult(`❌ Connection failed!\n\nError: ${error.message}\n\nThis is likely a CORS issue or the backend is not running.`)
    } finally {
      setIsLoading(false)
    }
  }

  const testAuthEndpoint = async () => {
    setIsLoading(true)
    setResult('Testing auth endpoint...')

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: 'test@example.com',
          password: 'testpassword'
        }),
      })

      const responseText = await response.text()
      setResult(`Auth endpoint test:\nStatus: ${response.status}\nResponse: ${responseText}`)
    } catch (error: any) {
      setResult(`❌ Auth endpoint failed!\n\nError: ${error.message}`)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-background p-8">
      <div className="max-w-4xl mx-auto space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>Backend Connection Test</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex gap-4">
              <Button onClick={testBackendConnection} disabled={isLoading}>
                Test Basic Connection
              </Button>
              <Button onClick={testAuthEndpoint} disabled={isLoading} variant="outline">
                Test Auth Endpoint
              </Button>
            </div>

            {result && (
              <Alert>
                <AlertDescription>
                  <pre className="whitespace-pre-wrap text-sm">{result}</pre>
                </AlertDescription>
              </Alert>
            )}

            <div className="text-sm text-muted-foreground">
              <p><strong>Expected Backend URL:</strong> http://localhost:8080/api</p>
              <p><strong>Common Issues:</strong></p>
              <ul className="list-disc list-inside mt-2 space-y-1">
                <li>CORS not configured on Spring Boot backend</li>
                <li>Backend not running on port 8080</li>
                <li>Network connectivity issues</li>
                <li>Firewall blocking the connection</li>
              </ul>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
