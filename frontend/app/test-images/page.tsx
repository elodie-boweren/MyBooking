"use client"

import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'

export default function TestImagesPage() {
  const [testResults, setTestResults] = useState<{[key: string]: boolean}>({})

  const testImages = {
    SINGLE: 'https://images.pexels.com/photos/271624/pexels-photo-271624.jpeg?w=600&h=400&fit=crop&auto=format&q=95',
    DOUBLE: 'https://images.pexels.com/photos/271743/pexels-photo-271743.jpeg?w=600&h=400&fit=crop&auto=format&q=95',
    DELUXE: 'https://images.pexels.com/photos/279746/pexels-photo-279746.jpeg?w=600&h=400&fit=crop&auto=format&q=95',
    FAMILY: 'https://images.pexels.com/photos/210265/pexels-photo-210265.jpeg?w=600&h=400&fit=crop&auto=format&q=95'
  }

  const testImage = (roomType: string, url: string) => {
    const img = new Image()
    img.onload = () => {
      setTestResults(prev => ({ ...prev, [roomType]: true }))
    }
    img.onerror = () => {
      setTestResults(prev => ({ ...prev, [roomType]: false }))
    }
    img.src = url
  }

  const testAllImages = () => {
    Object.entries(testImages).forEach(([roomType, url]) => {
      testImage(roomType, url)
    })
  }

  return (
    <div className="min-h-screen bg-background p-8">
      <div className="max-w-4xl mx-auto space-y-6">
        <div>
          <h1 className="text-3xl font-bold">Image Test Page</h1>
          <p className="text-muted-foreground">Testing if room images are loading correctly</p>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>Test Controls</CardTitle>
          </CardHeader>
          <CardContent>
            <Button onClick={testAllImages} className="mb-4">
              Test All Images
            </Button>
            <div className="space-y-2">
              {Object.entries(testResults).map(([roomType, success]) => (
                <div key={roomType} className="flex items-center space-x-2">
                  <span className="font-medium">{roomType}:</span>
                  <span className={success ? 'text-green-600' : 'text-red-600'}>
                    {success ? '✅ Loading' : '❌ Failed'}
                  </span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {Object.entries(testImages).map(([roomType, url]) => (
            <Card key={roomType}>
              <CardHeader>
                <CardTitle>{roomType} Room</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="relative h-48 w-full overflow-hidden rounded-lg bg-muted">
                  <img
                    src={url}
                    alt={`${roomType} room`}
                    className="w-full h-full object-cover"
                    loading="lazy"
                    onLoad={() => setTestResults(prev => ({ ...prev, [roomType]: true }))}
                    onError={() => setTestResults(prev => ({ ...prev, [roomType]: false }))}
                  />
                </div>
                <p className="text-sm text-muted-foreground mt-2">
                  URL: {url}
                </p>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </div>
  )
}
