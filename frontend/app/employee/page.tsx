"use client"

import { ProtectedRoute } from '@/components/protected-route'

export default function EmployeeDashboardPage() {
  return (
    <ProtectedRoute requiredRole="EMPLOYEE">
      <div className="min-h-screen bg-background p-8">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-3xl font-bold text-foreground mb-4">Employee Dashboard</h1>
          <p className="text-muted-foreground">
            Welcome to the employee dashboard. This will be implemented soon.
          </p>
        </div>
      </div>
    </ProtectedRoute>
  )
}
