import { ProtectedRoute } from "@/components/protected-route"
import { AdminLayout } from "@/components/admin/admin-layout"
import AdminLeaveManagement from "@/components/admin/leave-management"

export default function AdminLeavePage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <AdminLayout>
        <AdminLeaveManagement />
      </AdminLayout>
    </ProtectedRoute>
  )
}
