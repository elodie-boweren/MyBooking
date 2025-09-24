"use client"

import { AdminLayout } from '@/components/admin/admin-layout'
import { DashboardOverview } from '@/components/admin/dashboard-overview'
import { ProtectedRoute } from '@/components/protected-route'

export default function AdminDashboardPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <AdminLayout>
        <div className="space-y-6">
          {/* Page Header */}
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-foreground">Dashboard</h1>
              <p className="text-muted-foreground">
                Welcome to the MyBooking admin dashboard
              </p>
            </div>
            <div className="text-sm text-muted-foreground">
              Last updated: {new Date().toLocaleString()}
            </div>
          </div>

          {/* Dashboard Overview */}
          <DashboardOverview />

          {/* HR Management System */}
          <div className="space-y-4">
            <div>
              <h2 className="text-xl font-semibold text-foreground mb-2">HR Management System</h2>
              <p className="text-sm text-muted-foreground mb-4">
                Manage employee tasks, shifts, training, and leave requests
              </p>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              <a 
                href="/admin/tasks" 
                className="p-4 border border-border rounded-lg hover:bg-muted transition-colors"
              >
                <h3 className="font-medium text-foreground">Task Management</h3>
                <p className="text-sm text-muted-foreground">Assign and monitor employee tasks</p>
              </a>
              
              <a 
                href="/admin/shifts" 
                className="p-4 border border-border rounded-lg hover:bg-muted transition-colors"
              >
                <h3 className="font-medium text-foreground">Shift Management</h3>
                <p className="text-sm text-muted-foreground">Schedule and manage employee shifts</p>
              </a>
              
              <a 
                href="/admin/training" 
                className="p-4 border border-border rounded-lg hover:bg-muted transition-colors"
              >
                <h3 className="font-medium text-foreground">Training Management</h3>
                <p className="text-sm text-muted-foreground">Assign and track employee training</p>
              </a>
              
              <a 
                href="/admin/leave" 
                className="p-4 border border-border rounded-lg hover:bg-muted transition-colors"
              >
                <h3 className="font-medium text-foreground">Leave Management</h3>
                <p className="text-sm text-muted-foreground">Approve and manage leave requests</p>
              </a>
            </div>
          </div>
        </div>
      </AdminLayout>
    </ProtectedRoute>
  )
}