"use client"

import { AdminLayout } from '@/components/admin/admin-layout'
import { RoomManagement } from '@/components/admin/room-management'
import { ProtectedRoute } from '@/components/protected-route'

export default function AdminRoomsPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <AdminLayout>
        <RoomManagement />
      </AdminLayout>
    </ProtectedRoute>
  )
}
