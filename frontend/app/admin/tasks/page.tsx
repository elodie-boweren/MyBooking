import { ProtectedRoute } from "@/components/protected-route"
import AdminTaskManagement from "@/components/admin/task-management"

export default function AdminTasksPage() {
  return (
    <ProtectedRoute requiredRole="ADMIN">
      <div className="min-h-screen bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <AdminTaskManagement />
        </div>
      </div>
    </ProtectedRoute>
  )
}
