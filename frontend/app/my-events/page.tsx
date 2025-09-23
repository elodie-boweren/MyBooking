"use client"

import { useState } from "react"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import {
  Calendar,
  Clock,
  MapPin,
  Users,
  Plus,
  Eye,
  Edit3,
  Trash2,
  PartyPopper,
  CheckCircle,
  XCircle,
} from "lucide-react"

export default function MyEventsPage() {
  const [activeTab, setActiveTab] = useState("registered")

  const registeredEvents = [
    {
      id: "1",
      title: "Tech Talk: Future of AI",
      description: "Join us for an insightful discussion about the future of artificial intelligence",
      date: "2024-01-20",
      time: "14:00 - 16:00",
      location: "Main Auditorium",
      organizer: "Sarah Johnson",
      organizerAvatar: "/diverse-user-avatars.png",
      attendees: 45,
      maxAttendees: 100,
      status: "confirmed",
      category: "Technology",
      image: "/tech-conference.png",
    },
    {
      id: "2",
      title: "Team Building Workshop",
      description: "Interactive workshop focused on improving team collaboration and communication",
      date: "2024-01-25",
      time: "09:00 - 12:00",
      location: "Training Room C",
      organizer: "Mike Chen",
      organizerAvatar: "/diverse-user-avatars.png",
      attendees: 12,
      maxAttendees: 20,
      status: "confirmed",
      category: "Team Building",
      image: "/team-workshop.png",
    },
  ]

  const myEvents = [
    {
      id: "3",
      title: "Monthly Design Review",
      description: "Review and discuss recent design projects and upcoming initiatives",
      date: "2024-01-30",
      time: "15:00 - 17:00",
      location: "Design Studio",
      organizer: "John Doe",
      organizerAvatar: "/diverse-user-avatars.png",
      attendees: 8,
      maxAttendees: 15,
      status: "published",
      category: "Design",
      image: "/design-meeting.jpg",
    },
  ]

  const pastEvents = [
    {
      id: "4",
      title: "Holiday Party 2023",
      description: "Annual company holiday celebration with food, drinks, and entertainment",
      date: "2023-12-15",
      time: "18:00 - 22:00",
      location: "Main Hall",
      organizer: "HR Team",
      organizerAvatar: "/diverse-user-avatars.png",
      attendees: 120,
      maxAttendees: 150,
      status: "completed",
      category: "Social",
      image: "/holiday-party.jpg",
    },
  ]

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "confirmed":
        return (
          <Badge className="bg-green-100 text-green-800 hover:bg-green-100">
            <CheckCircle className="h-3 w-3 mr-1" />
            Registered
          </Badge>
        )
      case "published":
        return (
          <Badge className="bg-blue-100 text-blue-800 hover:bg-blue-100">
            <PartyPopper className="h-3 w-3 mr-1" />
            Published
          </Badge>
        )
      case "completed":
        return (
          <Badge className="bg-gray-100 text-gray-800 hover:bg-gray-100">
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

  const getCategoryColor = (category: string) => {
    const colors: { [key: string]: string } = {
      Technology: "bg-blue-100 text-blue-800",
      "Team Building": "bg-green-100 text-green-800",
      Design: "bg-purple-100 text-purple-800",
      Social: "bg-pink-100 text-pink-800",
      Training: "bg-orange-100 text-orange-800",
    }
    return colors[category] || "bg-gray-100 text-gray-800"
  }

  const EventCard = ({
    event,
    showActions = true,
    isOrganizer = false,
  }: { event: any; showActions?: boolean; isOrganizer?: boolean }) => (
    <Card className="hover:shadow-md transition-shadow">
      <CardContent className="p-0">
        <div className="flex flex-col md:flex-row">
          <div className="w-full md:w-48 h-48 md:h-auto bg-muted flex-shrink-0">
            <img src={event.image || "/placeholder.svg"} alt={event.title} className="w-full h-full object-cover" />
          </div>

          <div className="flex-1 p-6">
            <div className="flex items-start justify-between mb-3">
              <div className="space-y-1">
                <div className="flex items-center space-x-2 mb-2">
                  <Badge className={getCategoryColor(event.category)}>{event.category}</Badge>
                  {getStatusBadge(event.status)}
                </div>
                <h3 className="font-semibold text-xl">{event.title}</h3>
                <p className="text-muted-foreground">{event.description}</p>
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4 text-sm">
              <div className="flex items-center space-x-2">
                <Calendar className="h-4 w-4 text-muted-foreground" />
                <span>{event.date}</span>
              </div>
              <div className="flex items-center space-x-2">
                <Clock className="h-4 w-4 text-muted-foreground" />
                <span>{event.time}</span>
              </div>
              <div className="flex items-center space-x-2">
                <MapPin className="h-4 w-4 text-muted-foreground" />
                <span>{event.location}</span>
              </div>
              <div className="flex items-center space-x-2">
                <Users className="h-4 w-4 text-muted-foreground" />
                <span>
                  {event.attendees}/{event.maxAttendees} attendees
                </span>
              </div>
            </div>

            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-2">
                <Avatar className="h-6 w-6">
                  <AvatarImage src={event.organizerAvatar || "/placeholder.svg"} alt={event.organizer} />
                  <AvatarFallback>{event.organizer[0]}</AvatarFallback>
                </Avatar>
                <span className="text-sm text-muted-foreground">
                  {isOrganizer ? "Organized by you" : `Organized by ${event.organizer}`}
                </span>
              </div>

              {showActions && (
                <div className="flex items-center space-x-2">
                  <Button size="sm" variant="outline">
                    <Eye className="h-4 w-4 mr-2" />
                    View Details
                  </Button>
                  {isOrganizer && (
                    <>
                      <Button size="sm" variant="outline">
                        <Edit3 className="h-4 w-4 mr-2" />
                        Edit
                      </Button>
                      <Button size="sm" variant="outline" className="text-red-600 hover:text-red-700 bg-transparent">
                        <Trash2 className="h-4 w-4 mr-2" />
                        Cancel
                      </Button>
                    </>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  )

  return (
    <div className="container mx-auto px-4 py-8 max-w-6xl">
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-3xl font-bold text-foreground">My Events</h1>
          <p className="text-muted-foreground mt-2">Manage your event registrations and organized events</p>
        </div>
        <Button>
          <Plus className="h-4 w-4 mr-2" />
          Create Event
        </Button>
      </div>

      <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-6">
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="registered">Registered ({registeredEvents.length})</TabsTrigger>
          <TabsTrigger value="organized">My Events ({myEvents.length})</TabsTrigger>
          <TabsTrigger value="past">Past Events ({pastEvents.length})</TabsTrigger>
        </TabsList>

        <TabsContent value="registered" className="space-y-4">
          {registeredEvents.length > 0 ? (
            registeredEvents.map((event) => <EventCard key={event.id} event={event} />)
          ) : (
            <Card>
              <CardContent className="text-center py-12">
                <PartyPopper className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                <h3 className="text-lg font-medium mb-2">No registered events</h3>
                <p className="text-muted-foreground mb-4">You haven't registered for any events yet.</p>
                <Button>Browse Events</Button>
              </CardContent>
            </Card>
          )}
        </TabsContent>

        <TabsContent value="organized" className="space-y-4">
          {myEvents.length > 0 ? (
            myEvents.map((event) => <EventCard key={event.id} event={event} isOrganizer={true} />)
          ) : (
            <Card>
              <CardContent className="text-center py-12">
                <PartyPopper className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                <h3 className="text-lg font-medium mb-2">No organized events</h3>
                <p className="text-muted-foreground mb-4">You haven't created any events yet.</p>
                <Button>
                  <Plus className="h-4 w-4 mr-2" />
                  Create Your First Event
                </Button>
              </CardContent>
            </Card>
          )}
        </TabsContent>

        <TabsContent value="past" className="space-y-4">
          {pastEvents.map((event) => (
            <EventCard key={event.id} event={event} showActions={false} />
          ))}
        </TabsContent>
      </Tabs>
    </div>
  )
}
