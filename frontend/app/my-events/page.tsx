"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { ArrowLeft, Calendar, Clock, Users, MapPin, Star, PartyPopper } from "lucide-react"
import Link from "next/link"
import { eventApi } from "@/lib/api"
import type { EventBooking } from "@/lib/api"
import { toast } from "sonner"

export default function MyEventsPage() {
  const [eventBookings, setEventBookings] = useState<EventBooking[]>([])
  const [loading, setLoading] = useState(true)
  const [selectedStatus, setSelectedStatus] = useState("all")

  useEffect(() => {
    const fetchEventBookings = async () => {
      try {
        setLoading(true)
        const bookings = await eventApi.getUserEventBookings()
        setEventBookings(bookings)
      } catch (error) {
        console.error('Failed to fetch event bookings:', error)
        toast.error("Failed to load event bookings")
      } finally {
        setLoading(false)
      }
    }

    fetchEventBookings()
  }, [])

  const filteredBookings = eventBookings.filter(booking => {
    if (selectedStatus !== "all" && booking.status !== selectedStatus) {
      return false
    }
    return true
  })

  const getStatusBadge = (status: string) => {
    const statusColors: { [key: string]: string } = {
      CONFIRMED: "bg-green-100 text-green-800",
      PENDING: "bg-yellow-100 text-yellow-800",
      CANCELLED: "bg-red-100 text-red-800",
      COMPLETED: "bg-blue-100 text-blue-800"
    }
    
    return (
      <Badge className={statusColors[status] || "bg-gray-100 text-gray-800"}>
        {status}
      </Badge>
    )
  }

  const getEventTypeBadge = (eventType: string) => {
    const typeColors: { [key: string]: string } = {
      SPA: "bg-purple-100 text-purple-800",
      CONFERENCE: "bg-blue-100 text-blue-800",
      YOGA_CLASS: "bg-green-100 text-green-800",
      FITNESS: "bg-orange-100 text-orange-800",
      WEDDING: "bg-pink-100 text-pink-800"
    }
    
    return (
      <Badge className={typeColors[eventType] || "bg-gray-100 text-gray-800"}>
        {eventType}
      </Badge>
    )
  }

  const formatDateTime = (dateTime: string) => {
    return new Date(dateTime).toLocaleString()
  }

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8 max-w-6xl">
        <div className="text-center py-8">Loading event bookings...</div>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-6xl">
      <div className="flex items-center justify-between mb-8">
        <div className="flex items-center gap-4">
          <Link href="/">
            <Button variant="outline" size="sm">
              <ArrowLeft className="h-4 w-4 mr-2" />
              Back to Home
            </Button>
          </Link>
          <div>
            <h1 className="text-3xl font-bold text-foreground">My Events</h1>
            <p className="text-muted-foreground mt-2">View and manage your event bookings</p>
          </div>
        </div>
        
        <Link href="/events">
          <Button>
            <PartyPopper className="h-4 w-4 mr-2" />
            Browse Events
          </Button>
        </Link>
      </div>

      {/* Statistics */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-2">
              <Calendar className="h-6 w-6 text-blue-600" />
            </div>
            <CardTitle className="text-2xl">{eventBookings.length}</CardTitle>
            <CardDescription>Total Bookings</CardDescription>
          </CardHeader>
        </Card>

        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mb-2">
              <Star className="h-6 w-6 text-green-600" />
            </div>
            <CardTitle className="text-2xl">
              {eventBookings.filter(b => b.status === "CONFIRMED").length}
            </CardTitle>
            <CardDescription>Confirmed</CardDescription>
          </CardHeader>
        </Card>

        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-yellow-100 rounded-full flex items-center justify-center mb-2">
              <Clock className="h-6 w-6 text-yellow-600" />
            </div>
            <CardTitle className="text-2xl">
              {eventBookings.filter(b => b.status === "PENDING").length}
            </CardTitle>
            <CardDescription>Pending</CardDescription>
          </CardHeader>
        </Card>

        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-2">
              <Users className="h-6 w-6 text-blue-600" />
            </div>
            <CardTitle className="text-2xl">
              {eventBookings.filter(b => b.status === "COMPLETED").length}
            </CardTitle>
            <CardDescription>Completed</CardDescription>
          </CardHeader>
        </Card>
      </div>

      <Tabs defaultValue="bookings" className="space-y-6">
        <TabsList>
          <TabsTrigger value="bookings">My Bookings</TabsTrigger>
          <TabsTrigger value="upcoming">Upcoming Events</TabsTrigger>
        </TabsList>

        <TabsContent value="bookings" className="space-y-6">
          {/* Filters */}
          <div className="flex gap-4">
            <Select value={selectedStatus} onValueChange={setSelectedStatus}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder="Filter by status" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Bookings</SelectItem>
                <SelectItem value="CONFIRMED">Confirmed</SelectItem>
                <SelectItem value="PENDING">Pending</SelectItem>
                <SelectItem value="CANCELLED">Cancelled</SelectItem>
                <SelectItem value="COMPLETED">Completed</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {/* Bookings List */}
          {filteredBookings.length === 0 ? (
            <Card>
              <CardContent className="text-center py-12">
                <PartyPopper className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
                <h3 className="text-lg font-semibold mb-2">No event bookings found</h3>
                <p className="text-muted-foreground mb-4">
                  You haven't booked any events yet. Explore our exciting events!
                </p>
                <Link href="/events">
                  <Button>
                    <PartyPopper className="h-4 w-4 mr-2" />
                    Browse Events
                  </Button>
                </Link>
              </CardContent>
            </Card>
          ) : (
            <div className="space-y-4">
              {filteredBookings.map((booking) => (
                <Card key={booking.id}>
                  <CardHeader>
                    <div className="flex items-start justify-between">
                      <div className="space-y-2">
                        <div className="flex items-center gap-2">
                          <h3 className="font-semibold text-lg">{booking.eventName}</h3>
                          {getEventTypeBadge(booking.eventType)}
                          {getStatusBadge(booking.status)}
                        </div>
                        <p className="text-muted-foreground">{booking.eventDescription}</p>
                      </div>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <div className="space-y-2">
                        <div className="flex items-center gap-2 text-sm">
                          <Clock className="h-4 w-4" />
                          <span>{formatDateTime(booking.startAt)}</span>
                        </div>
                        <div className="flex items-center gap-2 text-sm">
                          <Clock className="h-4 w-4" />
                          <span>Ends: {formatDateTime(booking.endAt)}</span>
                        </div>
                        <div className="flex items-center gap-2 text-sm">
                          <Users className="h-4 w-4" />
                          <span>{booking.participants} participants</span>
                        </div>
                      </div>
                      <div className="space-y-2">
                        <div className="flex items-center gap-2 text-sm">
                          <MapPin className="h-4 w-4" />
                          <span>{booking.installationName}</span>
                        </div>
                        <div className="flex items-center gap-2 text-sm">
                          <span className="font-medium">Total Cost: {booking.totalCost} {booking.currency}</span>
                        </div>
                        <div className="flex items-center gap-2 text-sm text-muted-foreground">
                          <Calendar className="h-4 w-4" />
                          <span>Booked on {new Date(booking.createdAt).toLocaleDateString()}</span>
                        </div>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </TabsContent>

        <TabsContent value="upcoming" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Upcoming Events</CardTitle>
              <CardDescription>
                Your confirmed events happening soon
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-center py-8">
                <Calendar className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
                <h3 className="text-lg font-semibold">Upcoming Events Feature</h3>
                <p className="text-muted-foreground">This feature will show your upcoming confirmed events.</p>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}