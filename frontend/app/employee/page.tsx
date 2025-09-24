"use client"

import { ProtectedRoute } from '@/components/protected-route'
import EmployeeDashboard from '@/components/employee-dashboard'

export default function EmployeeDashboardPage() {
  return (
    <ProtectedRoute requiredRole="EMPLOYEE">
      <EmployeeDashboard />
    </ProtectedRoute>
  )
}
