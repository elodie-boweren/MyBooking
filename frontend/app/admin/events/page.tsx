import { ProtectedRoute } from "@/components/protected-route"
import { AdminLayout } from "@/components/admin/admin-layout"
import AdminEventManagement from "@/components/admin/event-management"

export default function AdminEventsPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <AdminLayout>
        <AdminEventManagement />
      </AdminLayout>
    </ProtectedRoute>
  )
}
