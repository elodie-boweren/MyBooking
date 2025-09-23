"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Badge } from "@/components/ui/badge"
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { CalendarIcon, Search, CheckCircle, XCircle, Clock, Users, MapPin } from "lucide-react"
import { format } from "date-fns"
import { useToast } from "@/hooks/use-toast"

interface Booking {
  id: string
  roomName: string
  roomFloor: number
  userName: string
  userEmail: string
  date: string
  startTime: string
  endTime: string
  title: string
  attendees: number
  status: "confirmed" | "pending" | "cancelled"
  createdAt: string
}

export function BookingManagement() {
  const [bookings, setBookings] = useState<Booking[]>([])
  const [filteredBookings, setFilteredBookings] = useState<Booking[]>([])
  const [searchTerm, setSearchTerm] = useState("")
  const [statusFilter, setStatusFilter] = useState("all")
  const [dateFilter, setDateFilter] = useState<Date>()
  const { toast } = useToast()

  // Mock data - replace with API call
  useEffect(() => {
    const mockBookings: Booking[] = [
      {
        id: "1",
        roomName: "Conference Room A",
        roomFloor: 1,
        userName: "John Doe",
        userEmail: "john.doe@company.com",
        date: "2024-01-22",
        startTime: "10:00",
        endTime: "11:30",
        title: "Team Standup Meeting",
        attendees: 8,
        status: "confirmed",
        createdAt: "2024-01-20 14:30",
      },
      {
        id: "2",
        roomName: "Meeting Room B",
        roomFloor: 2,
        userName: "Jane Smith",
        userEmail: "jane.smith@company.com",
        date: "2024-01-22",
        startTime: "14:00",
        endTime: "15:00",
        title: "Client Presentation",
        attendees: 4,
        status: "pending",
        createdAt: "2024-01-21 09:15",
      },
      {
        id: "3",
        roomName: "Executive Boardroom",
        roomFloor: 3,
        userName: "Mike Johnson",
        userEmail: "mike.johnson@company.com",
        date: "2024-01-23",
        startTime: "16:00",
        endTime: "17:30",
        title: "Board Meeting",
        attendees: 12,
        status: "confirmed",
        createdAt: "2024-01-19 11:45",
      },
      {
        id: "4",
        roomName: "Creative Studio",
        roomFloor: 2,
        userName: "Sarah Wilson",
        userEmail: "sarah.wilson@company.com",
        date: "2024-01-21",
        startTime: "09:30",
        endTime: "10:30",
        title: "Design Review",
        attendees: 6,
        status: "cancelled",
        createdAt: "2024-01-20 16:20",
      },
    ]
    setBookings(mockBookings)
    setFilteredBookings(mockBookings)
  }, [])

  // Filter bookings
  useEffect(() => {
    const filtered = bookings.filter((booking) => {
      const matchesSearch =
        booking.roomName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        booking.userName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        booking.title.toLowerCase().includes(searchTerm.toLowerCase())

      const matchesStatus = statusFilter === "all" || booking.status === statusFilter
      const matchesDate = !dateFilter || booking.date === format(dateFilter, "yyyy-MM-dd")

      return matchesSearch && matchesStatus && matchesDate
    })
    setFilteredBookings(filtered)
  }, [bookings, searchTerm, statusFilter, dateFilter])

  const handleApproveBooking = async (bookingId: string) => {
    try {
      const response = await fetch(`/api/admin/bookings/${bookingId}/approve`, {
        method: "PATCH",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      })

      if (response.ok) {
        setBookings((prev) =>
          prev.map((booking) => (booking.id === bookingId ? { ...booking, status: "confirmed" as const } : booking)),
        )
        toast({
          title: "Booking approved",
          description: "The booking has been confirmed",
        })
      }
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to approve booking",
        variant: "destructive",
      })
    }
  }

  const handleRejectBooking = async (bookingId: string) => {
    try {
      const response = await fetch(`/api/admin/bookings/${bookingId}/reject`, {
        method: "PATCH",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      })

      if (response.ok) {
        setBookings((prev) =>
          prev.map((booking) => (booking.id === bookingId ? { ...booking, status: "cancelled" as const } : booking)),
        )
        toast({
          title: "Booking rejected",
          description: "The booking has been cancelled",
        })
      }
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to reject booking",
        variant: "destructive",
      })
    }
  }

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "confirmed":
        return (
          <Badge className="bg-green-100 text-green-800">
            <CheckCircle className="h-3 w-3 mr-1" />
            Confirmed
          </Badge>
        )
      case "pending":
        return (
          <Badge variant="secondary">
            <Clock className="h-3 w-3 mr-1" />
            Pending
          </Badge>
        )
      case "cancelled":
        return (
          <Badge variant="destructive">
            <XCircle className="h-3 w-3 mr-1" />
            Cancelled
          </Badge>
        )
      default:
        return <Badge variant="outline">{status}</Badge>
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-foreground">Booking Management</h2>
        <p className="text-muted-foreground">Review and manage room reservations</p>
      </div>

      {/* Search and Filters */}
      <div className="flex flex-col md:flex-row gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
          <Input
            placeholder="Search bookings..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="pl-10"
          />
        </div>
        <div className="flex gap-2">
          <Select value={statusFilter} onValueChange={setStatusFilter}>
            <SelectTrigger className="w-[130px]">
              <SelectValue placeholder="Status" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">All Status</SelectItem>
              <SelectItem value="confirmed">Confirmed</SelectItem>
              <SelectItem value="pending">Pending</SelectItem>
              <SelectItem value="cancelled">Cancelled</SelectItem>
            </SelectContent>
          </Select>

          <Popover>
            <PopoverTrigger asChild>
              <Button variant="outline" className="w-[140px] justify-start text-left font-normal bg-transparent">
                <CalendarIcon className="mr-2 h-4 w-4" />
                {dateFilter ? format(dateFilter, "MMM dd") : "Filter by date"}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0">
              <Calendar mode="single" selected={dateFilter} onSelect={setDateFilter} initialFocus />
            </PopoverContent>
          </Popover>

          {(statusFilter !== "all" || dateFilter) && (
            <Button
              variant="outline"
              onClick={() => {
                setStatusFilter("all")
                setDateFilter(undefined)
              }}
            >
              Clear
            </Button>
          )}
        </div>
      </div>

      {/* Bookings List */}
      <div className="space-y-4">
        {filteredBookings.map((booking) => (
          <Card key={booking.id}>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div className="space-y-2">
                  <div className="flex items-center space-x-4">
                    <h3 className="font-semibold text-lg">{booking.title}</h3>
                    {getStatusBadge(booking.status)}
                  </div>

                  <div className="flex items-center space-x-6 text-sm text-muted-foreground">
                    <div className="flex items-center">
                      <MapPin className="h-4 w-4 mr-1" />
                      {booking.roomName} (Floor {booking.roomFloor})
                    </div>
                    <div className="flex items-center">
                      <Users className="h-4 w-4 mr-1" />
                      {booking.attendees} attendees
                    </div>
                    <div>
                      {format(new Date(booking.date), "MMM dd, yyyy")} • {booking.startTime} - {booking.endTime}
                    </div>
                  </div>

                  <div className="text-sm">
                    <span className="font-medium">{booking.userName}</span>
                    <span className="text-muted-foreground"> ({booking.userEmail})</span>
                  </div>
                </div>

                {booking.status === "pending" && (
                  <div className="flex space-x-2">
                    <Button size="sm" onClick={() => handleApproveBooking(booking.id)}>
                      <CheckCircle className="h-4 w-4 mr-1" />
                      Approve
                    </Button>
                    <Button size="sm" variant="outline" onClick={() => handleRejectBooking(booking.id)}>
                      <XCircle className="h-4 w-4 mr-1" />
                      Reject
                    </Button>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredBookings.length === 0 && (
        <div className="text-center py-12">
          <p className="text-muted-foreground">No bookings match your current filters.</p>
        </div>
      )}
    </div>
  )
}
