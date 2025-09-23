"use client"

import { useState } from "react"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Calendar, Clock, MapPin, Users, Edit3, Trash2, Eye, CheckCircle, XCircle, AlertCircle } from "lucide-react"

export default function MyReservationsPage() {
  const [activeTab, setActiveTab] = useState("upcoming")

  const upcomingReservations = [
    {
      id: "1",
      roomName: "Modern Conference Room A",
      roomImage: "/modern-conference-room.png",
      date: "2024-01-15",
      time: "09:00 - 11:00",
      duration: "2 hours",
      attendees: 8,
      purpose: "Team Sprint Planning",
      status: "confirmed",
      location: "Floor 3, Building A",
    },
    {
      id: "2",
      roomName: "Executive Boardroom",
      roomImage: "/executive-boardroom.png",
      date: "2024-01-18",
      time: "14:00 - 16:30",
      duration: "2.5 hours",
      attendees: 12,
      purpose: "Quarterly Review Meeting",
      status: "confirmed",
      location: "Floor 5, Building A",
    },
    {
      id: "3",
      roomName: "Creative Workspace",
      roomImage: "/creative-workspace.png",
      date: "2024-01-22",
      time: "10:00 - 12:00",
      duration: "2 hours",
      attendees: 6,
      purpose: "Design Workshop",
      status: "pending",
      location: "Floor 2, Building B",
    },
  ]

  const pastReservations = [
    {
      id: "4",
      roomName: "Small Meeting Room B",
      roomImage: "/small-meeting-room.png",
      date: "2024-01-10",
      time: "15:00 - 16:00",
      duration: "1 hour",
      attendees: 4,
      purpose: "Client Call",
      status: "completed",
      location: "Floor 2, Building A",
    },
    {
      id: "5",
      roomName: "Training Room",
      roomImage: "/modern-training-room.png",
      date: "2024-01-08",
      time: "09:00 - 17:00",
      duration: "8 hours",
      attendees: 20,
      purpose: "New Employee Orientation",
      status: "completed",
      location: "Floor 1, Building C",
    },
  ]

  const cancelledReservations = [
    {
      id: "6",
      roomName: "Modern Conference Room A",
      roomImage: "/modern-conference-room.png",
      date: "2024-01-12",
      time: "13:00 - 15:00",
      duration: "2 hours",
      attendees: 6,
      purpose: "Project Kickoff",
      status: "cancelled",
      location: "Floor 3, Building A",
      cancelReason: "Meeting postponed",
    },
  ]

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "confirmed":
        return (
          <Badge className="bg-green-100 text-green-800 hover:bg-green-100">
            <CheckCircle className="h-3 w-3 mr-1" />
            Confirmed
          </Badge>
        )
      case "pending":
        return (
          <Badge className="bg-yellow-100 text-yellow-800 hover:bg-yellow-100">
            <AlertCircle className="h-3 w-3 mr-1" />
            Pending
          </Badge>
        )
      case "completed":
        return (
          <Badge className="bg-blue-100 text-blue-800 hover:bg-blue-100">
            <CheckCircle className="h-3 w-3 mr-1" />
            Completed
          </Badge>
        )
      case "cancelled":
        return (
          <Badge className="bg-red-100 text-red-800 hover:bg-red-100">
            <XCircle className="h-3 w-3 mr-1" />
            Cancelled
          </Badge>
        )
      default:
        return <Badge variant="secondary">{status}</Badge>
    }
  }

  const ReservationCard = ({ reservation, showActions = true }: { reservation: any; showActions?: boolean }) => (
    <Card className="hover:shadow-md transition-shadow">
      <CardContent className="p-6">
        <div className="flex items-start space-x-4">
          <div className="w-20 h-20 rounded-lg overflow-hidden bg-muted flex-shrink-0">
            <img
              src={reservation.roomImage || "/placeholder.svg"}
              alt={reservation.roomName}
              className="w-full h-full object-cover"
            />
          </div>

          <div className="flex-1 min-w-0">
            <div className="flex items-start justify-between">
              <div className="space-y-1">
                <h3 className="font-semibold text-lg">{reservation.roomName}</h3>
                <p className="text-muted-foreground">{reservation.purpose}</p>
              </div>
              {getStatusBadge(reservation.status)}
            </div>

            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-4 text-sm">
              <div className="flex items-center space-x-2">
                <Calendar className="h-4 w-4 text-muted-foreground" />
                <span>{reservation.date}</span>
              </div>
              <div className="flex items-center space-x-2">
                <Clock className="h-4 w-4 text-muted-foreground" />
                <span>{reservation.time}</span>
              </div>
              <div className="flex items-center space-x-2">
                <Users className="h-4 w-4 text-muted-foreground" />
                <span>{reservation.attendees} people</span>
              </div>
              <div className="flex items-center space-x-2">
                <MapPin className="h-4 w-4 text-muted-foreground" />
                <span>{reservation.location}</span>
              </div>
            </div>

            {reservation.cancelReason && (
              <div className="mt-3 p-2 bg-red-50 border border-red-200 rounded text-sm text-red-700">
                <strong>Cancellation reason:</strong> {reservation.cancelReason}
              </div>
            )}

            {showActions && reservation.status !== "completed" && reservation.status !== "cancelled" && (
              <div className="flex items-center space-x-2 mt-4">
                <Button size="sm" variant="outline">
                  <Eye className="h-4 w-4 mr-2" />
                  View Details
                </Button>
                <Button size="sm" variant="outline">
                  <Edit3 className="h-4 w-4 mr-2" />
                  Modify
                </Button>
                <Button size="sm" variant="outline" className="text-red-600 hover:text-red-700 bg-transparent">
                  <Trash2 className="h-4 w-4 mr-2" />
                  Cancel
                </Button>
              </div>
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  )

  return (
    <div className="container mx-auto px-4 py-8 max-w-6xl">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-foreground">My Reservations</h1>
        <p className="text-muted-foreground mt-2">Manage your room bookings and view reservation history</p>
      </div>

      <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-6">
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="upcoming">Upcoming ({upcomingReservations.length})</TabsTrigger>
          <TabsTrigger value="past">Past ({pastReservations.length})</TabsTrigger>
          <TabsTrigger value="cancelled">Cancelled ({cancelledReservations.length})</TabsTrigger>
        </TabsList>

        <TabsContent value="upcoming" className="space-y-4">
          {upcomingReservations.length > 0 ? (
            upcomingReservations.map((reservation) => (
              <ReservationCard key={reservation.id} reservation={reservation} />
            ))
          ) : (
            <Card>
              <CardContent className="text-center py-12">
                <Calendar className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                <h3 className="text-lg font-medium mb-2">No upcoming reservations</h3>
                <p className="text-muted-foreground mb-4">You don't have any upcoming room bookings.</p>
                <Button>Book a Room</Button>
              </CardContent>
            </Card>
          )}
        </TabsContent>

        <TabsContent value="past" className="space-y-4">
          {pastReservations.map((reservation) => (
            <ReservationCard key={reservation.id} reservation={reservation} showActions={false} />
          ))}
        </TabsContent>

        <TabsContent value="cancelled" className="space-y-4">
          {cancelledReservations.map((reservation) => (
            <ReservationCard key={reservation.id} reservation={reservation} showActions={false} />
          ))}
        </TabsContent>
      </Tabs>
    </div>
  )
}
