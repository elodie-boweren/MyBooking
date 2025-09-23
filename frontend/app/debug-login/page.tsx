"use client"

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'

export default function DebugLoginPage() {
  const [email, setEmail] = useState('admin@example.com')
  const [password, setPassword] = useState('Pass123@')
  const [result, setResult] = useState<string>('')
  const [isLoading, setIsLoading] = useState(false)

  const testLogin = async () => {
    setIsLoading(true)
    setResult('Testing login...')

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      })

      const responseText = await response.text()
      
      setResult(`Status: ${response.status}\nResponse: ${responseText}`)
    } catch (error: any) {
      setResult(`Error: ${error.message}`)
    } finally {
      setIsLoading(false)
    }
  }

  const testEmployeeLogin = async () => {
    setIsLoading(true)
    setResult('Testing employee login...')

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: 'employee@example.com', password: 'Pass123@' }),
      })

      const responseText = await response.text()
      
      setResult(`Status: ${response.status}\nResponse: ${responseText}`)
    } catch (error: any) {
      setResult(`Error: ${error.message}`)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-background p-8">
      <div className="max-w-4xl mx-auto space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>Debug Login Endpoint</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium mb-2">Email</label>
                <Input
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="admin@example.com"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Password</label>
                <Input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Pass123@"
                />
              </div>
            </div>

            <div className="flex gap-4">
              <Button onClick={testLogin} disabled={isLoading}>
                Test Admin Login
              </Button>
              <Button onClick={testEmployeeLogin} disabled={isLoading} variant="outline">
                Test Employee Login
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
              <p><strong>Expected:</strong> Status 200 with JWT token</p>
              <p><strong>If 401:</strong> Check credentials or backend authentication</p>
              <p><strong>If 500:</strong> Check backend logs for errors</p>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
