"use client"

import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'

export default function DebugDetailedPage() {
  const [email, setEmail] = useState('admin@example.com')
  const [password, setPassword] = useState('Pass123@')
  const [result, setResult] = useState<string>('')
  const [isLoading, setIsLoading] = useState(false)
  const [localStorageData, setLocalStorageData] = useState<{token: string | null, user: string | null}>({token: null, user: null})

  // Safely access localStorage on client side
  useEffect(() => {
    if (typeof window !== 'undefined') {
      setLocalStorageData({
        token: localStorage.getItem('token'),
        user: localStorage.getItem('user')
      })
    }
  }, [])

  const testLogin = async () => {
    setIsLoading(true)
    setResult('Testing login...')

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password })
      })

      const responseText = await response.text()
      let parsedResponse
      
      try {
        parsedResponse = JSON.parse(responseText)
      } catch {
        parsedResponse = responseText
      }
      
      setResult(`Status: ${response.status}\nResponse: ${JSON.stringify(parsedResponse, null, 2)}`)
    } catch (error: any) {
      setResult(`Error: ${error.message}`)
    } finally {
      setIsLoading(false)
    }
  }

  const testClientLogin = async () => {
    setIsLoading(true)
    setResult('Testing CLIENT login...')

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: 'client@example.com', password: 'Pass123@' })
      })

      const responseText = await response.text()
      let parsedResponse
      
      try {
        parsedResponse = JSON.parse(responseText)
      } catch {
        parsedResponse = responseText
      }
      
      setResult(`CLIENT Status: ${response.status}\nResponse: ${JSON.stringify(parsedResponse, null, 2)}`)
    } catch (error: any) {
      setResult(`CLIENT Error: ${error.message}`)
    } finally {
      setIsLoading(false)
    }
  }

  const testEmployeeLogin = async () => {
    setIsLoading(true)
    setResult('Testing EMPLOYEE login...')

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: 'employee@example.com', password: 'Pass123@' })
      })

      const responseText = await response.text()
      let parsedResponse
      
      try {
        parsedResponse = JSON.parse(responseText)
      } catch {
        parsedResponse = responseText
      }
      
      setResult(`EMPLOYEE Status: ${response.status}\nResponse: ${JSON.stringify(parsedResponse, null, 2)}`)
    } catch (error: any) {
      setResult(`EMPLOYEE Error: ${error.message}`)
    } finally {
      setIsLoading(false)
    }
  }

  const testFrontendLogin = async () => {
    setIsLoading(true)
    setResult('Testing frontend login...')

    try {
      // Simulate the frontend login process
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password })
      })

      if (!response.ok) {
        const errorText = await response.text()
        setResult(`Frontend Login Failed:\nStatus: ${response.status}\nError: ${errorText}`)
        return
      }

      const loginResponse = await response.json()
      
      // Store token and user data like the frontend does
      if (typeof window !== 'undefined') {
        localStorage.setItem('token', loginResponse.token)
        localStorage.setItem('user', JSON.stringify({
          id: loginResponse.userId,
          email: loginResponse.email,
          firstName: loginResponse.firstName,
          lastName: loginResponse.lastName,
          role: loginResponse.role,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        }))
        // Update state
        setLocalStorageData({
          token: loginResponse.token,
          user: JSON.stringify({
            id: loginResponse.userId,
            email: loginResponse.email,
            firstName: loginResponse.firstName,
            lastName: loginResponse.lastName,
            role: loginResponse.role,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString(),
          })
        })
      }

      setResult(`Frontend Login Success:\n${JSON.stringify(loginResponse, null, 2)}`)
    } catch (error: any) {
      setResult(`Frontend Login Error: ${error.message}`)
    } finally {
      setIsLoading(false)
    }
  }

  const clearStorage = () => {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      setLocalStorageData({token: null, user: null})
    }
    setResult('Storage cleared')
  }

  return (
    <div className="min-h-screen bg-background p-8">
      <div className="max-w-4xl mx-auto space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>Detailed Authentication Debug</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <Label className="block text-sm font-medium mb-2">Email</Label>
                <Input
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="admin@example.com"
                />
              </div>
              <div>
                <Label className="block text-sm font-medium mb-2">Password</Label>
                <Input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Password"
                />
              </div>
            </div>

            <div className="flex gap-4 flex-wrap">
              <Button onClick={testLogin} disabled={isLoading}>
                Test Admin Login
              </Button>
              <Button onClick={testClientLogin} disabled={isLoading} variant="outline">
                Test Client Login
              </Button>
              <Button onClick={testEmployeeLogin} disabled={isLoading} variant="outline">
                Test Employee Login
              </Button>
              <Button onClick={testFrontendLogin} disabled={isLoading} variant="secondary">
                Test Frontend Login
              </Button>
              <Button onClick={clearStorage} disabled={isLoading} variant="destructive">
                Clear Storage
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
              <p><strong>Current localStorage:</strong></p>
              <p>Token: {localStorageData.token ? 'Present' : 'Not found'}</p>
              <p>User: {localStorageData.user ? 'Present' : 'Not found'}</p>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
