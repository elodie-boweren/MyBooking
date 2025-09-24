"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { installationApi } from "@/lib/api"

export default function TestInstallationsPage() {
  const [installations, setInstallations] = useState<any[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const testInstallations = async () => {
    setLoading(true)
    setError(null)
    try {
      console.log("Testing installations API...")
      const result = await installationApi.getAllInstallations()
      console.log("Installations result:", result)
      setInstallations(Array.isArray(result) ? result : [])
    } catch (err: any) {
      console.error("Error:", err)
      setError(err.message || "Failed to load installations")
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="container mx-auto p-6">
      <Card>
        <CardHeader>
          <CardTitle>Test Installations API</CardTitle>
        </CardHeader>
        <CardContent>
          <Button onClick={testInstallations} disabled={loading}>
            {loading ? "Loading..." : "Test Installations API"}
          </Button>
          
          {error && (
            <div className="mt-4 p-4 bg-red-50 border border-red-200 rounded">
              <h3 className="text-red-800 font-semibold">Error:</h3>
              <p className="text-red-600">{error}</p>
            </div>
          )}
          
          {installations.length > 0 && (
            <div className="mt-4">
              <h3 className="font-semibold">Installations ({installations.length}):</h3>
              <pre className="bg-gray-100 p-4 rounded text-sm overflow-auto">
                {JSON.stringify(installations, null, 2)}
              </pre>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
