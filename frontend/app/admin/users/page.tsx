"use client"

import { AdminLayout } from '@/components/admin/admin-layout'
import { UserManagement } from '@/components/admin/user-management'
import { ProtectedRoute } from '@/components/protected-route'

export default function AdminUsersPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <AdminLayout>
        <UserManagement />
      </AdminLayout>
    </ProtectedRoute>
  )
}
