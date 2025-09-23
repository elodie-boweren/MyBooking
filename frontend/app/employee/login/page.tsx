"use client"

import type React from "react"

import { useState } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { Building2, Users } from "lucide-react"
import Link from "next/link"

export default function EmployeeLogin() {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [error, setError] = useState("")
  const [loading, setLoading] = useState(false)
  const router = useRouter()

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError("")

    // Mock employee authentication
    if (email === "employee@company.com" && password === "employee123") {
      const employeeData = {
        id: "2",
        name: "John Smith",
        email: "employee@company.com",
        role: "employee",
        department: "Operations",
        employeeId: "EMP001",
        position: "Operations Specialist",
        manager: "Sarah Johnson",
        startDate: "2023-01-15",
      }

      localStorage.setItem("employee", JSON.stringify(employeeData))
      router.push("/employee/dashboard")
    } else {
      setError("Invalid employee credentials")
    }

    setLoading(false)
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <div className="flex items-center justify-center gap-2 mb-4">
            <Building2 className="h-8 w-8 text-blue-600" />
            <span className="text-2xl font-bold text-gray-900">RoomReserve</span>
          </div>
          <div className="flex items-center justify-center gap-2 mb-2">
            <Users className="h-5 w-5 text-gray-600" />
            <span className="text-lg font-semibold text-gray-700">Employee Portal</span>
          </div>
          <p className="text-gray-600">Access your schedule, tasks, and announcements</p>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>Employee Sign In</CardTitle>
            <CardDescription>Enter your employee credentials to access your dashboard</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleLogin} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="your.email@company.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <Input
                  id="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>

              {error && (
                <Alert variant="destructive">
                  <AlertDescription>{error}</AlertDescription>
                </Alert>
              )}

              <Button type="submit" className="w-full" disabled={loading}>
                {loading ? "Signing in..." : "Sign In"}
              </Button>
            </form>

            <div className="mt-6 text-center">
              <p className="text-sm text-gray-600">Demo credentials: employee@company.com / employee123</p>
              <div className="mt-4 pt-4 border-t">
                <Link href="/" className="text-sm text-blue-600 hover:text-blue-800">
                  ← Back to Customer Portal
                </Link>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
