import { AdminLayout } from '@/components/admin/admin-layout'
import { ReservationManagement } from '@/components/admin/reservation-management'

export default function AdminReservationsPage() {
  return (
    <AdminLayout>
      <ReservationManagement />
    </AdminLayout>
  )
}
