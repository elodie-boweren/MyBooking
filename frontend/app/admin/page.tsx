"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from "recharts"
import { RoomManagement } from "@/components/admin/room-management"
import { UserManagement } from "@/components/admin/user-management"
import { BookingManagement } from "@/components/admin/booking-management"
import {
  CalendarDays,
  Users,
  Building,
  TrendingUp,
  Clock,
  CheckCircle,
  XCircle,
  BarChart3,
  Shield,
  Activity,
} from "lucide-react"

interface DashboardStats {
  totalRooms: number
  totalUsers: number
  todayBookings: number
  utilizationRate: number
  availableRooms: number
  occupiedRooms: number
  pendingBookings: number
}

interface BookingData {
  time: string
  bookings: number
}

interface RoomUtilization {
  name: string
  value: number
  color: string
}

export default function AdminDashboard() {
  const [stats, setStats] = useState<DashboardStats>({
    totalRooms: 0,
    totalUsers: 0,
    todayBookings: 0,
    utilizationRate: 0,
    availableRooms: 0,
    occupiedRooms: 0,
    pendingBookings: 0,
  })

  const [bookingData, setBookingData] = useState<BookingData[]>([])
  const [roomUtilization, setRoomUtilization] = useState<RoomUtilization[]>([])
  const [recentBookings, setRecentBookings] = useState<any[]>([])

  // Mock data - replace with API calls to Spring Boot backend
  useEffect(() => {
    const mockStats: DashboardStats = {
      totalRooms: 12,
      totalUsers: 156,
      todayBookings: 28,
      utilizationRate: 73,
      availableRooms: 7,
      occupiedRooms: 5,
      pendingBookings: 3,
    }

    const mockBookingData: BookingData[] = [
      { time: "8:00", bookings: 2 },
      { time: "9:00", bookings: 8 },
      { time: "10:00", bookings: 12 },
      { time: "11:00", bookings: 15 },
      { time: "12:00", bookings: 6 },
      { time: "13:00", bookings: 4 },
      { time: "14:00", bookings: 18 },
      { time: "15:00", bookings: 14 },
      { time: "16:00", bookings: 10 },
      { time: "17:00", bookings: 7 },
    ]

    const mockRoomUtilization: RoomUtilization[] = [
      { name: "Conference Rooms", value: 45, color: "#0891b2" },
      { name: "Meeting Rooms", value: 30, color: "#8b5cf6" },
      { name: "Phone Booths", value: 15, color: "#f87171" },
      { name: "Training Rooms", value: 10, color: "#4b5563" },
    ]

    const mockRecentBookings = [
      {
        id: "1",
        room: "Conference Room A",
        user: "John Doe",
        time: "10:00 - 11:30",
        status: "confirmed",
        attendees: 8,
      },
      {
        id: "2",
        room: "Meeting Room B",
        user: "Jane Smith",
        time: "14:00 - 15:00",
        status: "pending",
        attendees: 4,
      },
      {
        id: "3",
        room: "Executive Boardroom",
        user: "Mike Johnson",
        time: "16:00 - 17:30",
        status: "confirmed",
        attendees: 12,
      },
      {
        id: "4",
        room: "Creative Studio",
        user: "Sarah Wilson",
        time: "09:30 - 10:30",
        status: "cancelled",
        attendees: 6,
      },
    ]

    setStats(mockStats)
    setBookingData(mockBookingData)
    setRoomUtilization(mockRoomUtilization)
    setRecentBookings(mockRecentBookings)
  }, [])

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "confirmed":
        return (
          <Badge className="bg-green-100 text-green-800 border-green-200">
            <CheckCircle className="h-3 w-3 mr-1" />
            Confirmed
          </Badge>
        )
      case "pending":
        return (
          <Badge className="bg-yellow-100 text-yellow-800 border-yellow-200">
            <Clock className="h-3 w-3 mr-1" />
            Pending
          </Badge>
        )
      case "cancelled":
        return (
          <Badge className="bg-red-100 text-red-800 border-red-200">
            <XCircle className="h-3 w-3 mr-1" />
            Cancelled
          </Badge>
        )
      default:
        return <Badge variant="outline">{status}</Badge>
    }
  }

  return (
    <div className="min-h-screen bg-background">
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-foreground mb-2 flex items-center">
            <Shield className="h-8 w-8 mr-3 text-primary" />
            Admin Dashboard
          </h1>
          <p className="text-muted-foreground text-lg">
            <BarChart3 className="inline-block h-4 w-4 mr-2" />
            System monitoring and management interface
          </p>
        </div>

        <Tabs defaultValue="overview" className="space-y-6">
          <TabsList className="grid w-full grid-cols-4">
            <TabsTrigger value="overview">Overview</TabsTrigger>
            <TabsTrigger value="rooms">Rooms</TabsTrigger>
            <TabsTrigger value="users">Users</TabsTrigger>
            <TabsTrigger value="bookings">Bookings</TabsTrigger>
          </TabsList>

          <TabsContent value="overview" className="space-y-6">
            {/* Stats Cards */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
              <Card className="hover:shadow-lg transition-shadow">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium text-primary">Total Rooms</CardTitle>
                  <Building className="h-4 w-4 text-primary" />
                </CardHeader>
                <CardContent>
                  <div className="text-3xl font-bold">{stats.totalRooms}</div>
                  <p className="text-xs text-muted-foreground">
                    <Activity className="inline-block h-3 w-3 mr-1" />
                    {stats.availableRooms} Available • {stats.occupiedRooms} Occupied
                  </p>
                </CardContent>
              </Card>

              <Card className="hover:shadow-lg transition-shadow">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium text-green-600">Active Users</CardTitle>
                  <Users className="h-4 w-4 text-green-600" />
                </CardHeader>
                <CardContent>
                  <div className="text-3xl font-bold">{stats.totalUsers}</div>
                  <p className="text-xs text-muted-foreground">Registered users</p>
                </CardContent>
              </Card>

              <Card className="hover:shadow-lg transition-shadow">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium text-red-600">Today's Bookings</CardTitle>
                  <CalendarDays className="h-4 w-4 text-red-600" />
                </CardHeader>
                <CardContent>
                  <div className="text-3xl font-bold">{stats.todayBookings}</div>
                  <p className="text-xs text-muted-foreground">
                    <Clock className="inline-block h-3 w-3 mr-1" />
                    {stats.pendingBookings} Pending approval
                  </p>
                </CardContent>
              </Card>

              <Card className="hover:shadow-lg transition-shadow">
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                  <CardTitle className="text-sm font-medium text-blue-600">Utilization Rate</CardTitle>
                  <TrendingUp className="h-4 w-4 text-blue-600" />
                </CardHeader>
                <CardContent>
                  <div className="text-3xl font-bold">{stats.utilizationRate}%</div>
                  <p className="text-xs text-green-600">+5% from last week</p>
                </CardContent>
              </Card>
            </div>

            {/* Charts */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <Card className="hover:shadow-lg transition-shadow">
                <CardHeader>
                  <CardTitle className="text-blue-600">Booking Activity</CardTitle>
                  <CardDescription>Hourly booking distribution</CardDescription>
                </CardHeader>
                <CardContent>
                  <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={bookingData}>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="time" />
                      <YAxis />
                      <Tooltip />
                      <Bar dataKey="bookings" fill="#3b82f6" />
                    </BarChart>
                  </ResponsiveContainer>
                </CardContent>
              </Card>

              <Card className="hover:shadow-lg transition-shadow">
                <CardHeader>
                  <CardTitle className="text-green-600">Room Distribution</CardTitle>
                  <CardDescription>Usage by room type</CardDescription>
                </CardHeader>
                <CardContent>
                  <ResponsiveContainer width="100%" height={300}>
                    <PieChart>
                      <Pie
                        data={roomUtilization}
                        cx="50%"
                        cy="50%"
                        outerRadius={80}
                        fill="#8884d8"
                        dataKey="value"
                        label={({ name, value }) => `${name}: ${value}%`}
                      >
                        {roomUtilization.map((entry, index) => (
                          <Cell
                            key={`cell-${index}`}
                            fill={index % 3 === 0 ? "#dc2626" : index % 3 === 1 ? "#3b82f6" : "#059669"}
                          />
                        ))}
                      </Pie>
                      <Tooltip />
                    </PieChart>
                  </ResponsiveContainer>
                </CardContent>
              </Card>
            </div>

            {/* Recent Bookings */}
            <Card className="hover:shadow-lg transition-shadow">
              <CardHeader>
                <CardTitle className="text-red-600">Recent Bookings</CardTitle>
                <CardDescription>Latest reservation requests and status</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {recentBookings.map((booking) => (
                    <div
                      key={booking.id}
                      className="flex items-center justify-between p-4 border rounded-lg hover:bg-muted/50 transition-colors"
                    >
                      <div className="flex items-center space-x-4">
                        <div>
                          <p className="font-medium">{booking.room}</p>
                          <p className="text-sm text-muted-foreground">
                            {booking.user} • {booking.time} • {booking.attendees} attendees
                          </p>
                        </div>
                      </div>
                      <div className="flex items-center space-x-2">
                        {getStatusBadge(booking.status)}
                        {booking.status === "pending" && (
                          <div className="flex space-x-1">
                            <Button size="sm" className="bg-green-600 hover:bg-green-700">
                              Approve
                            </Button>
                            <Button size="sm" className="bg-red-600 hover:bg-red-700">
                              Deny
                            </Button>
                          </div>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </TabsContent>

          <TabsContent value="rooms">
            <RoomManagement />
          </TabsContent>

          <TabsContent value="users">
            <UserManagement />
          </TabsContent>

          <TabsContent value="bookings">
            <BookingManagement />
          </TabsContent>
        </Tabs>
      </div>
    </div>
  )
}
