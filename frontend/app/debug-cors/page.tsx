"use client"

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Alert, AlertDescription } from '@/components/ui/alert'

export default function DebugCorsPage() {
  const [result, setResult] = useState<string>('')
  const [isLoading, setIsLoading] = useState(false)

  const testCorsPreflight = async () => {
    setIsLoading(true)
    setResult('Testing CORS preflight...')

    try {
      // Test OPTIONS request (preflight)
      const optionsResponse = await fetch('http://localhost:8080/api/auth/login', {
        method: 'OPTIONS',
        headers: {
          'Access-Control-Request-Method': 'POST',
          'Access-Control-Request-Headers': 'Content-Type',
          'Origin': 'http://localhost:3000'
        }
      })

      setResult(`OPTIONS Response:\nStatus: ${optionsResponse.status}\nHeaders: ${JSON.stringify(Object.fromEntries(optionsResponse.headers.entries()), null, 2)}`)
    } catch (error: any) {
      setResult(`OPTIONS Error: ${error.message}`)
    } finally {
      setIsLoading(false)
    }
  }

  const testSimplePost = async () => {
    setIsLoading(true)
    setResult('Testing simple POST...')

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email: 'admin@example.com', password: 'Pass123@' })
      })

      const responseText = await response.text()
      setResult(`POST Response:\nStatus: ${response.status}\nResponse: ${responseText}`)
    } catch (error: any) {
      setResult(`POST Error: ${error.message}`)
    } finally {
      setIsLoading(false)
    }
  }

  const testWithCredentials = async () => {
    setIsLoading(true)
    setResult('Testing POST with credentials...')

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({ email: 'admin@example.com', password: 'Pass123@' })
      })

      const responseText = await response.text()
      setResult(`POST with credentials:\nStatus: ${response.status}\nResponse: ${responseText}`)
    } catch (error: any) {
      setResult(`POST with credentials Error: ${error.message}`)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-background p-8">
      <div className="max-w-4xl mx-auto space-y-6">
        <Card>
          <CardHeader>
            <CardTitle>CORS Debug Tests</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex gap-4">
              <Button onClick={testCorsPreflight} disabled={isLoading}>
                Test OPTIONS (Preflight)
              </Button>
              <Button onClick={testSimplePost} disabled={isLoading} variant="outline">
                Test Simple POST
              </Button>
              <Button onClick={testWithCredentials} disabled={isLoading} variant="outline">
                Test POST with Credentials
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
              <p><strong>Expected:</strong></p>
              <ul className="list-disc list-inside mt-2 space-y-1">
                <li>OPTIONS: Status 200 with CORS headers</li>
                <li>POST: Status 200 with JWT token</li>
                <li>If OPTIONS fails: CORS preflight issue</li>
                <li>If POST fails: Authentication issue</li>
              </ul>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
