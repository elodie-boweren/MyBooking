"use client"

import { AdminLayout } from '@/components/admin/admin-layout'
import { SystemSettings } from '@/components/admin/system-settings'
import { ProtectedRoute } from '@/components/protected-route'

export default function AdminSettingsPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <AdminLayout>
        <SystemSettings />
      </AdminLayout>
    </ProtectedRoute>
  )
}
