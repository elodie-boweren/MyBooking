import { ProtectedRoute } from "@/components/protected-route"
import { AdminLayout } from "@/components/admin/admin-layout"
import AdminShiftManagement from "@/components/admin/shift-management"

export default function AdminShiftsPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <AdminLayout>
        <AdminShiftManagement />
      </AdminLayout>
    </ProtectedRoute>
  )
}
