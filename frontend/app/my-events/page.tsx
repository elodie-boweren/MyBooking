"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { 
  Calendar, 
  Clock, 
  Users, 
  MapPin, 
  DollarSign, 
  Search, 
  Filter,
  RefreshCw,
  X,
  AlertCircle
} from "lucide-react"
import { eventBookingApi } from "@/lib/api"
import type { EventBookingResponse, EventBookingSearchCriteria } from "@/lib/api"
import { toast } from "sonner"

const getEventTypeBadge = (eventType: string) => {
  const badges: Record<string, string> = {
    WEDDING: "bg-pink-100 text-pink-800 border-pink-200",
    CONFERENCE: "bg-blue-100 text-blue-800 border-blue-200",
    BIRTHDAY: "bg-yellow-100 text-yellow-800 border-yellow-200",
    CORPORATE: "bg-purple-100 text-purple-800 border-purple-200",
    SPA: "bg-green-100 text-green-800 border-green-200",
    FITNESS: "bg-orange-100 text-orange-800 border-orange-200"
  }
  return badges[eventType] || "bg-gray-100 text-gray-800 border-gray-200"
}

const getStatusBadge = (status: string) => {
  const badges: Record<string, string> = {
    PENDING: "bg-yellow-100 text-yellow-800 border-yellow-200",
    CONFIRMED: "bg-green-100 text-green-800 border-green-200",
    CANCELLED: "bg-red-100 text-red-800 border-red-200"
  }
  return badges[status] || "bg-gray-100 text-gray-800 border-gray-200"
}

const formatDateTime = (dateTime: string) => {
  return new Date(dateTime).toLocaleString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

export default function MyEventsPage() {
  const [bookings, setBookings] = useState<EventBookingResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState("")
  const [statusFilter, setStatusFilter] = useState<string>("all")
  const [eventTypeFilter, setEventTypeFilter] = useState<string>("all")
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)

  // Load bookings
  const loadBookings = async () => {
    try {
      setLoading(true)
      const criteria: EventBookingSearchCriteria = {
        page: currentPage,
        size: 10
      }
      
      if (statusFilter !== "all") criteria.status = statusFilter
      if (eventTypeFilter !== "all") criteria.eventType = eventTypeFilter
      
      const response = await eventBookingApi.searchBookings(criteria)
      setBookings(response.content)
      setTotalPages(response.totalPages)
    } catch (error: any) {
      console.error("Failed to load bookings:", error)
      toast.error("Failed to load your event bookings")
    } finally {
      setLoading(false)
    }
  }

  // Load bookings on component mount and when filters change
  useEffect(() => {
    loadBookings()
  }, [currentPage, statusFilter, eventTypeFilter])

  // Handle booking cancellation
  const handleCancelBooking = async (bookingId: number, eventName: string) => {
    if (!confirm(`Are you sure you want to cancel your booking for "${eventName}"?`)) {
      return
    }

    try {
      await eventBookingApi.cancelBooking(bookingId, "Cancelled by user")
      toast.success("Booking cancelled successfully")
      loadBookings() // Refresh the list
    } catch (error: any) {
      console.error("Failed to cancel booking:", error)
      toast.error("Failed to cancel booking. Please try again.")
    }
  }

  // Handle search
  const handleSearch = () => {
    setCurrentPage(0)
    loadBookings()
  }

  // Clear filters
  const handleClearFilters = () => {
    setSearchTerm("")
    setStatusFilter("all")
    setEventTypeFilter("all")
    setCurrentPage(0)
  }

  // Filter bookings by search term
  const filteredBookings = bookings.filter(booking =>
    booking.eventName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    booking.eventType.toLowerCase().includes(searchTerm.toLowerCase())
  )

  return (
    <div className="min-h-screen bg-background">
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-foreground mb-2">My Event Bookings</h1>
          <p className="text-muted-foreground">
            Manage your event bookings and view booking history
          </p>
        </div>

        {/* Filters */}
        <div className="bg-muted/50 rounded-lg p-6 mb-8">
          <div className="flex flex-col lg:flex-row gap-4">
            {/* Search */}
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                <Input
                  placeholder="Search events..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>

            {/* Status Filter */}
            <div className="lg:w-48">
              <Select value={statusFilter} onValueChange={setStatusFilter}>
                <SelectTrigger>
                  <SelectValue placeholder="Filter by status" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All Status</SelectItem>
                  <SelectItem value="PENDING">Pending</SelectItem>
                  <SelectItem value="CONFIRMED">Confirmed</SelectItem>
                  <SelectItem value="CANCELLED">Cancelled</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* Event Type Filter */}
            <div className="lg:w-48">
              <Select value={eventTypeFilter} onValueChange={setEventTypeFilter}>
                <SelectTrigger>
                  <SelectValue placeholder="Filter by type" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All Types</SelectItem>
                  <SelectItem value="WEDDING">Wedding</SelectItem>
                  <SelectItem value="CONFERENCE">Conference</SelectItem>
                  <SelectItem value="SPA">Spa</SelectItem>
                  <SelectItem value="FITNESS">Fitness</SelectItem>
                  <SelectItem value="CORPORATE">Corporate</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {/* Action Buttons */}
            <div className="flex gap-2">
              <Button onClick={handleSearch} variant="outline" size="sm">
                <Search className="h-4 w-4 mr-2" />
                Search
              </Button>
              <Button onClick={handleClearFilters} variant="outline" size="sm">
                <X className="h-4 w-4 mr-2" />
                Clear
              </Button>
              <Button onClick={loadBookings} variant="outline" size="sm" disabled={loading}>
                <RefreshCw className={`h-4 w-4 mr-2 ${loading ? 'animate-spin' : ''}`} />
                Refresh
              </Button>
            </div>
          </div>
        </div>

        {/* Bookings List */}
        {loading ? (
          <div className="flex items-center justify-center py-12">
            <RefreshCw className="h-8 w-8 animate-spin text-muted-foreground" />
            <span className="ml-2 text-muted-foreground">Loading your bookings...</span>
          </div>
        ) : filteredBookings.length === 0 ? (
          <div className="text-center py-12">
            <AlertCircle className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <h3 className="text-lg font-semibold text-foreground mb-2">No bookings found</h3>
            <p className="text-muted-foreground mb-4">
              {searchTerm || statusFilter !== "all" || eventTypeFilter !== "all"
                ? "Try adjusting your search criteria"
                : "You haven't booked any events yet"
              }
            </p>
            {!searchTerm && statusFilter === "all" && eventTypeFilter === "all" && (
              <Button asChild>
                <a href="/events">Browse Events</a>
              </Button>
            )}
          </div>
        ) : (
          <div className="space-y-6">
            {filteredBookings.map((booking) => (
              <Card key={booking.id} className="overflow-hidden">
                <CardHeader className="pb-4">
                  <div className="flex items-start justify-between">
                    <div className="space-y-2">
                      <CardTitle className="text-xl">{booking.eventName}</CardTitle>
                      <div className="flex items-center gap-2">
                        <Badge className={getEventTypeBadge(booking.eventType)}>
                          {booking.eventType}
                        </Badge>
                        <Badge className={getStatusBadge(booking.status)}>
                          {booking.status}
                        </Badge>
                      </div>
                    </div>
                    <div className="text-right">
                      <div className="flex items-center gap-1 text-lg font-semibold text-foreground">
                        <DollarSign className="h-4 w-4" />
                        {booking.eventPrice} {booking.eventCurrency}
                      </div>
                      <p className="text-sm text-muted-foreground">per person</p>
                    </div>
                  </div>
                </CardHeader>

                <CardContent className="space-y-4">
                  {/* Event Details */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <Calendar className="h-4 w-4" />
                      <span>Start: {formatDateTime(booking.eventStartAt)}</span>
                    </div>
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <Clock className="h-4 w-4" />
                      <span>End: {formatDateTime(booking.eventEndAt)}</span>
                    </div>
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <Users className="h-4 w-4" />
                      <span>Booking ID: #{booking.id}</span>
                    </div>
                    <div className="flex items-center gap-2 text-muted-foreground">
                      <Calendar className="h-4 w-4" />
                      <span>Booked: {formatDateTime(booking.createdAt)}</span>
                    </div>
                  </div>

                  {/* Booking Actions */}
                  <div className="flex items-center justify-between pt-4 border-t border-border">
                    <div className="text-sm text-muted-foreground">
                      Booking created on {formatDateTime(booking.createdAt)}
                    </div>
                    <div className="flex gap-2">
                      {booking.status === "PENDING" && (
                        <Button
                          variant="destructive"
                          size="sm"
                          onClick={() => handleCancelBooking(booking.id, booking.eventName)}
                        >
                          Cancel Booking
                        </Button>
                      )}
                      {booking.status === "CONFIRMED" && (
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleCancelBooking(booking.id, booking.eventName)}
                        >
                          Cancel Booking
                        </Button>
                      )}
                      {booking.status === "CANCELLED" && (
                        <span className="text-sm text-muted-foreground">
                          This booking has been cancelled
                        </span>
                      )}
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}

            {/* Pagination */}
            {totalPages > 1 && (
              <div className="flex items-center justify-center gap-2 pt-6">
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => setCurrentPage(prev => Math.max(0, prev - 1))}
                  disabled={currentPage === 0}
                >
                  Previous
                </Button>
                <span className="text-sm text-muted-foreground">
                  Page {currentPage + 1} of {totalPages}
                </span>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => setCurrentPage(prev => Math.min(totalPages - 1, prev + 1))}
                  disabled={currentPage >= totalPages - 1}
                >
                  Next
                </Button>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  )
}