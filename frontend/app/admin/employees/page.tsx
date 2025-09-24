import { AdminLayout } from '@/components/admin/admin-layout'
import { EmployeeManagement } from '@/components/admin/employee-management'

export default function AdminEmployeesPage() {
  return (
    <AdminLayout>
      <EmployeeManagement />
    </AdminLayout>
  )
}
