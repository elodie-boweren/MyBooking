"use client"

import { AdminLayout } from '@/components/admin/admin-layout'
import { AnalyticsDashboard } from '@/components/admin/analytics-dashboard'
import { ProtectedRoute } from '@/components/protected-route'

export default function AdminAnalyticsPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <AdminLayout>
        <AnalyticsDashboard />
      </AdminLayout>
    </ProtectedRoute>
  )
}
