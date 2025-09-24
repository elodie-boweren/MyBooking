import { ProtectedRoute } from "@/components/protected-route"
import { AdminLayout } from "@/components/admin/admin-layout"
import AdminTrainingManagement from "@/components/admin/training-management"

export default function AdminTrainingPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <AdminLayout>
        <AdminTrainingManagement />
      </AdminLayout>
    </ProtectedRoute>
  )
}
