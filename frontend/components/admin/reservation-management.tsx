"use client"

import { useState, useEffect } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { 
  Calendar, 
  Search, 
  Filter, 
  Plus,
  Edit,
  Trash2,
  User,
  Bed,
  DollarSign,
  Clock,
  CheckCircle,
  XCircle
} from 'lucide-react'
import { COMPONENT_TEMPLATES } from '@/lib/style-constants'
import { apiClient, API_ENDPOINTS, Reservation, UpdateReservationRequest } from '@/lib/api'

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080/api"

export function ReservationManagement() {
  const [reservations, setReservations] = useState<Reservation[]>([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedStatus, setSelectedStatus] = useState<string>('all')
  const [selectedDateRange, setSelectedDateRange] = useState<string>('all')
  const [showEditForm, setShowEditForm] = useState(false)
  const [editing, setEditing] = useState(false)
  const [selectedReservation, setSelectedReservation] = useState<Reservation | null>(null)
  const [editFormData, setEditFormData] = useState({
    checkIn: '',
    checkOut: '',
    numberOfGuests: 1
  })

  useEffect(() => {
    fetchReservations()
  }, [])

  const fetchReservations = async () => {
    try {
      setLoading(true)
      const response = await apiClient.get<any>(API_ENDPOINTS.ADMIN_RESERVATIONS.ALL)
      
      // Handle paginated response from Spring Boot
      const reservationsData = response.content || response
      setReservations(Array.isArray(reservationsData) ? reservationsData : [])
    } catch (error) {
      console.error('Failed to fetch reservations:', error)
      // Fallback to mock data if API fails
      const mockReservations: Reservation[] = [
        {
          id: 1,
          checkIn: '2024-01-15',
          checkOut: '2024-01-18',
          numberOfGuests: 2,
          totalPrice: 450.00,
          currency: 'EUR',
          status: 'CONFIRMED',
          clientId: 1,
          clientName: 'John Doe',
          clientEmail: 'john@example.com',
          roomId: 1,
          roomNumber: '101',
          roomType: 'SINGLE',
          pointsUsed: 0,
          pointsDiscount: 0,
          createdAt: '2024-01-10T10:00:00Z',
          updatedAt: '2024-01-10T10:00:00Z'
        },
        {
          id: 2,
          checkIn: '2024-01-20',
          checkOut: '2024-01-22',
          numberOfGuests: 1,
          totalPrice: 300.00,
          currency: 'EUR',
          status: 'CANCELLED',
          clientId: 2,
          clientName: 'Jane Smith',
          clientEmail: 'jane@example.com',
          roomId: 2,
          roomNumber: '102',
          roomType: 'DOUBLE',
          pointsUsed: 50,
          pointsDiscount: 25.00,
          createdAt: '2024-01-12T14:30:00Z',
          updatedAt: '2024-01-12T14:30:00Z'
        }
      ]
      setReservations(mockReservations)
    } finally {
      setLoading(false)
    }
  }

  const getStatusBadgeVariant = (status: string) => {
    switch (status) {
      case 'CONFIRMED':
        return 'default'
      case 'CANCELLED':
        return 'destructive'
      default:
        return 'outline'
    }
  }

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'CONFIRMED':
        return <CheckCircle className="h-4 w-4 text-green-600" />
      case 'CANCELLED':
        return <XCircle className="h-4 w-4 text-red-600" />
      default:
        return <Clock className="h-4 w-4 text-gray-600" />
    }
  }

  const handleEditReservation = (reservationId: number) => {
    const reservation = reservations.find(r => r.id === reservationId)
    if (reservation) {
      setEditFormData({
        checkIn: reservation.checkIn,
        checkOut: reservation.checkOut,
        numberOfGuests: reservation.numberOfGuests
      })
      setSelectedReservation(reservation)
      setShowEditForm(true)
    }
  }

  const handleDeleteReservation = async (reservationId: number) => {
    const reservation = reservations.find(r => r.id === reservationId)
    if (!reservation) return

    const confirmMessage = `Are you sure you want to cancel reservation #${reservation.id}?\n\nThis will cancel the reservation and mark it as CANCELLED.`
    
    if (!confirm(confirmMessage)) {
      return
    }

    try {
      // Make DELETE request with proper error handling
      const response = await fetch(`${API_BASE_URL}/admin/reservations/${reservationId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
        },
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(`HTTP ${response.status}: ${errorText}`)
      }
      
      alert(`Reservation #${reservation.id} has been cancelled successfully!`)
      fetchReservations() // Refresh the reservation list
    } catch (error: any) {
      console.error('Failed to cancel reservation:', error)
      const errorMessage = error?.message || 'Network error occurred'
      alert(`Failed to cancel reservation: ${errorMessage}`)
    }
  }

  const handleUpdateReservation = async (e: React.FormEvent) => {
    e.preventDefault()
    if (editing || !selectedReservation) return

    try {
      setEditing(true)

      // Validate dates are in the future
      const today = new Date()
      const checkInDate = new Date(editFormData.checkIn)
      const checkOutDate = new Date(editFormData.checkOut)
      
      if (checkInDate <= today) {
        alert('Check-in date must be in the future')
        return
      }
      
      if (checkOutDate <= checkInDate) {
        alert('Check-out date must be after check-in date')
        return
      }

      // Update the reservation with only the fields the backend expects
      await apiClient.put(API_ENDPOINTS.ADMIN_RESERVATIONS.UPDATE(selectedReservation.id.toString()), editFormData)

      // Reset form and close modal
      setEditFormData({
        checkIn: '',
        checkOut: '',
        numberOfGuests: 1
      })
      setShowEditForm(false)
      setSelectedReservation(null)

      // Refresh reservation list
      fetchReservations()

      alert('Reservation updated successfully!')
    } catch (error: any) {
      console.error('Failed to update reservation:', error)
      const errorMessage = error?.response?.data?.message || error?.message || 'Unknown error'
      alert(`Failed to update reservation: ${errorMessage}`)
    } finally {
      setEditing(false)
    }
  }

  const handleEditFormChange = (field: string, value: string | number) => {
    setEditFormData(prev => ({
      ...prev,
      [field]: value
    }))
  }

  const filteredReservations = reservations.filter(reservation => {
    const matchesSearch = reservation.clientName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         reservation.clientEmail.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         reservation.roomNumber.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesStatus = selectedStatus === 'all' || reservation.status === selectedStatus
    return matchesSearch && matchesStatus
  })

  if (loading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-foreground">Reservation Management</h1>
            <p className="text-muted-foreground">
              Manage hotel reservations and bookings
            </p>
          </div>
        </div>
        <div className="flex items-center justify-center py-12">
          <div className="text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>
            <p className="text-muted-foreground">Loading reservations...</p>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-foreground">Reservation Management</h1>
          <p className="text-muted-foreground">
            Manage hotel reservations and bookings
          </p>
        </div>
      </div>

      {/* Filters */}
      <Card>
        <CardContent className="pt-6">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                <Input
                  placeholder="Search by client name, email, or room number..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="flex gap-4">
              <select
                value={selectedStatus}
                onChange={(e) => setSelectedStatus(e.target.value)}
                className="px-3 py-2 border rounded-md"
              >
                <option value="all">All Status</option>
                <option value="CONFIRMED">Confirmed</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
              <Button variant="outline">
                <Filter className="h-4 w-4 mr-2" />
                Filter
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Reservations Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredReservations.map((reservation) => (
          <Card key={reservation.id} className={COMPONENT_TEMPLATES.cardHover}>
            <CardHeader className="pb-3">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  <div className="h-10 w-10 rounded-full bg-primary flex items-center justify-center">
                    <Calendar className="h-5 w-5 text-primary-foreground" />
                  </div>
                  <div>
                    <CardTitle className="text-lg">Reservation #{reservation.id}</CardTitle>
                    <p className="text-sm text-muted-foreground">{reservation.clientName}</p>
                  </div>
                </div>
                <Badge variant={getStatusBadgeVariant(reservation.status)}>
                  {reservation.status}
                </Badge>
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <div className="flex items-center space-x-2">
                  <span className="text-sm text-muted-foreground">Room:</span>
                  <span className="text-sm font-medium">{reservation.roomNumber} ({reservation.roomType})</span>
                </div>
                <div className="flex items-center space-x-2">
                  <span className="text-sm text-muted-foreground">Check-in:</span>
                  <span className="text-sm">{new Date(reservation.checkIn).toLocaleDateString()}</span>
                </div>
                <div className="flex items-center space-x-2">
                  <span className="text-sm text-muted-foreground">Check-out:</span>
                  <span className="text-sm">{new Date(reservation.checkOut).toLocaleDateString()}</span>
                </div>
                <div className="flex items-center space-x-2">
                  <span className="text-sm text-muted-foreground">Guests:</span>
                  <span className="text-sm">{reservation.numberOfGuests}</span>
                </div>
                <div className="flex items-center space-x-2">
                  <span className="text-sm text-muted-foreground">Total:</span>
                  <span className="text-sm font-medium">{reservation.totalPrice} {reservation.currency}</span>
                </div>
                {reservation.pointsUsed && reservation.pointsUsed > 0 && (
                  <div className="flex items-center space-x-2">
                    <span className="text-sm text-muted-foreground">Points Used:</span>
                    <span className="text-sm">{reservation.pointsUsed}</span>
                  </div>
                )}
                <div className="flex items-center space-x-2">
                  <span className="text-sm text-muted-foreground">Status:</span>
                  <div className="flex items-center space-x-1">
                    {getStatusIcon(reservation.status)}
                    <span className="text-sm capitalize">{reservation.status.toLowerCase()}</span>
                  </div>
                </div>
              </div>
              <div className="flex gap-2 mt-4">
                <Button 
                  variant="outline" 
                  size="sm" 
                  className="flex-1"
                  onClick={() => handleEditReservation(reservation.id)}
                >
                  <Edit className="h-4 w-4 mr-2" />
                  Edit
                </Button>
                <Button 
                  variant="outline"
                  size="sm" 
                  className="flex-1"
                  onClick={() => handleDeleteReservation(reservation.id)}
                >
                  <XCircle className="h-4 w-4 mr-2" />
                  Cancel
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Empty State */}
      {filteredReservations.length === 0 && (
        <Card>
          <CardContent className="text-center py-8">
            <Calendar className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <h3 className="text-lg font-medium text-foreground mb-2">No reservations found</h3>
            <p className="text-muted-foreground">
              {searchTerm || selectedStatus !== 'all' 
                ? 'Try adjusting your search or filter criteria'
                : 'No reservations have been created yet'
              }
            </p>
          </CardContent>
        </Card>
      )}

      {/* Edit Reservation Form Modal */}
      {showEditForm && selectedReservation && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-full max-w-md mx-4">
            <CardHeader>
              <CardTitle>Edit Reservation #{selectedReservation.id}</CardTitle>
              <p className="text-sm text-muted-foreground">
                Note: To change reservation status, use the Cancel button instead.
              </p>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleUpdateReservation} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm font-medium">Check-in Date</label>
                    <Input
                      type="date"
                      value={editFormData.checkIn}
                      onChange={(e) => handleEditFormChange('checkIn', e.target.value)}
                      required
                    />
                  </div>
                  <div>
                    <label className="text-sm font-medium">Check-out Date</label>
                    <Input
                      type="date"
                      value={editFormData.checkOut}
                      onChange={(e) => handleEditFormChange('checkOut', e.target.value)}
                      required
                    />
                  </div>
                </div>

                <div>
                  <label className="text-sm font-medium">Number of Guests</label>
                  <Input
                    type="number"
                    min="1"
                    max="10"
                    value={editFormData.numberOfGuests}
                    onChange={(e) => handleEditFormChange('numberOfGuests', parseInt(e.target.value))}
                    required
                  />
                </div>


                <div className="flex gap-2">
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => {
                      setShowEditForm(false)
                      setSelectedReservation(null)
                    }}
                    className="flex-1"
                    disabled={editing}
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    className="flex-1"
                    disabled={editing}
                  >
                    {editing ? 'Updating...' : 'Update Reservation'}
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  )
}
