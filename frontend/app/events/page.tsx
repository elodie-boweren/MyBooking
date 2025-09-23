"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Calendar } from "@/components/ui/calendar"
import { PartyPopper, Plus, CalendarIcon, Users, MapPin, Clock, Search } from "lucide-react"
// import { Navigation } from "@/components/navigation"

interface Event {
  id: string
  title: string
  description: string
  date: string
  time: string
  duration: string
  location: string
  organizer: string
  attendees: number
  maxAttendees: number
  category: string
  isRegistered: boolean
}

export default function EventsPage() {
  const [events, setEvents] = useState<Event[]>([])
  const [filteredEvents, setFilteredEvents] = useState<Event[]>([])
  const [searchTerm, setSearchTerm] = useState("")
  const [categoryFilter, setCategoryFilter] = useState("all")
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false)

  // New event form state
  const [newEvent, setNewEvent] = useState({
    title: "",
    description: "",
    date: undefined as Date | undefined,
    time: "",
    duration: "",
    location: "",
    maxAttendees: "",
    category: "",
  })

  // Mock events data
  useEffect(() => {
    const mockEvents: Event[] = [
      {
        id: "1",
        title: "Team Building Workshop",
        description: "Interactive workshop focused on improving team collaboration and communication skills.",
        date: "2024-01-15",
        time: "14:00",
        duration: "2 hours",
        location: "Training Room",
        organizer: "HR Department",
        attendees: 12,
        maxAttendees: 25,
        category: "Team Building",
        isRegistered: false,
      },
      {
        id: "2",
        title: "Product Launch Presentation",
        description: "Exciting presentation of our new product line with demos and Q&A session.",
        date: "2024-01-18",
        time: "10:00",
        duration: "1.5 hours",
        location: "Executive Boardroom",
        organizer: "Product Team",
        attendees: 45,
        maxAttendees: 50,
        category: "Business",
        isRegistered: true,
      },
      {
        id: "3",
        title: "Wellness Wednesday: Yoga Session",
        description: "Relaxing yoga session to help reduce stress and improve workplace wellness.",
        date: "2024-01-17",
        time: "12:00",
        duration: "1 hour",
        location: "Creative Studio",
        organizer: "Wellness Committee",
        attendees: 8,
        maxAttendees: 15,
        category: "Wellness",
        isRegistered: false,
      },
      {
        id: "4",
        title: "Tech Talk: AI in Business",
        description: "Insightful discussion about implementing AI solutions in modern business practices.",
        date: "2024-01-20",
        time: "16:00",
        duration: "1 hour",
        location: "Conference Room A",
        organizer: "Tech Team",
        attendees: 28,
        maxAttendees: 30,
        category: "Technology",
        isRegistered: true,
      },
    ]
    setEvents(mockEvents)
    setFilteredEvents(mockEvents)
  }, [])

  // Filter events
  useEffect(() => {
    const filtered = events.filter((event) => {
      const matchesSearch =
        event.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
        event.description.toLowerCase().includes(searchTerm.toLowerCase())
      const matchesCategory = categoryFilter === "all" || event.category === categoryFilter
      return matchesSearch && matchesCategory
    })
    setFilteredEvents(filtered)
  }, [events, searchTerm, categoryFilter])

  const handleRegister = (eventId: string) => {
    setEvents(
      events.map((event) =>
        event.id === eventId
          ? {
              ...event,
              isRegistered: !event.isRegistered,
              attendees: event.isRegistered ? event.attendees - 1 : event.attendees + 1,
            }
          : event,
      ),
    )
  }

  const handleCreateEvent = () => {
    if (newEvent.title && newEvent.date && newEvent.time && newEvent.location) {
      const event: Event = {
        id: Date.now().toString(),
        title: newEvent.title,
        description: newEvent.description,
        date: newEvent.date.toISOString().split("T")[0],
        time: newEvent.time,
        duration: newEvent.duration,
        location: newEvent.location,
        organizer: "You",
        attendees: 1,
        maxAttendees: Number.parseInt(newEvent.maxAttendees) || 10,
        category: newEvent.category,
        isRegistered: true,
      }
      setEvents([...events, event])
      setIsCreateModalOpen(false)
      setNewEvent({
        title: "",
        description: "",
        date: undefined,
        time: "",
        duration: "",
        location: "",
        maxAttendees: "",
        category: "",
      })
    }
  }

  const categories = ["Team Building", "Business", "Wellness", "Technology", "Training", "Social"]

  return (
    <div className="min-h-screen bg-background">
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-3xl font-bold text-foreground mb-2">Company Events</h1>
            <p className="text-muted-foreground">Discover and join exciting workplace events</p>
          </div>

          <Dialog open={isCreateModalOpen} onOpenChange={setIsCreateModalOpen}>
            <DialogTrigger asChild>
              <Button className="flex items-center space-x-2">
                <Plus className="h-4 w-4" />
                <span>Create Event</span>
              </Button>
            </DialogTrigger>
            <DialogContent className="max-w-md">
              <DialogHeader>
                <DialogTitle>Create New Event</DialogTitle>
                <DialogDescription>Organize a new event for your colleagues</DialogDescription>
              </DialogHeader>

              <div className="space-y-4">
                <div>
                  <Label htmlFor="event-title">Event Title</Label>
                  <Input
                    id="event-title"
                    placeholder="Enter event title"
                    value={newEvent.title}
                    onChange={(e) => setNewEvent({ ...newEvent, title: e.target.value })}
                  />
                </div>

                <div>
                  <Label htmlFor="event-description">Description</Label>
                  <Textarea
                    id="event-description"
                    placeholder="Describe your event"
                    value={newEvent.description}
                    onChange={(e) => setNewEvent({ ...newEvent, description: e.target.value })}
                    rows={3}
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Label>Date</Label>
                    <Calendar
                      mode="single"
                      selected={newEvent.date}
                      onSelect={(date) => setNewEvent({ ...newEvent, date })}
                      disabled={(date) => date < new Date()}
                      className="rounded-md border"
                    />
                  </div>

                  <div className="space-y-4">
                    <div>
                      <Label htmlFor="event-time">Time</Label>
                      <Input
                        id="event-time"
                        type="time"
                        value={newEvent.time}
                        onChange={(e) => setNewEvent({ ...newEvent, time: e.target.value })}
                      />
                    </div>

                    <div>
                      <Label htmlFor="event-duration">Duration</Label>
                      <Select
                        value={newEvent.duration}
                        onValueChange={(value) => setNewEvent({ ...newEvent, duration: value })}
                      >
                        <SelectTrigger>
                          <SelectValue placeholder="Duration" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="30 min">30 minutes</SelectItem>
                          <SelectItem value="1 hour">1 hour</SelectItem>
                          <SelectItem value="1.5 hours">1.5 hours</SelectItem>
                          <SelectItem value="2 hours">2 hours</SelectItem>
                          <SelectItem value="3 hours">3 hours</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>

                    <div>
                      <Label htmlFor="event-location">Location</Label>
                      <Input
                        id="event-location"
                        placeholder="Event location"
                        value={newEvent.location}
                        onChange={(e) => setNewEvent({ ...newEvent, location: e.target.value })}
                      />
                    </div>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Label htmlFor="max-attendees">Max Attendees</Label>
                    <Input
                      id="max-attendees"
                      type="number"
                      placeholder="20"
                      value={newEvent.maxAttendees}
                      onChange={(e) => setNewEvent({ ...newEvent, maxAttendees: e.target.value })}
                    />
                  </div>

                  <div>
                    <Label htmlFor="event-category">Category</Label>
                    <Select
                      value={newEvent.category}
                      onValueChange={(value) => setNewEvent({ ...newEvent, category: value })}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder="Select category" />
                      </SelectTrigger>
                      <SelectContent>
                        {categories.map((category) => (
                          <SelectItem key={category} value={category}>
                            {category}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                </div>

                <Button onClick={handleCreateEvent} className="w-full">
                  Create Event
                </Button>
              </div>
            </DialogContent>
          </Dialog>
        </div>

        {/* Search and Filters */}
        <div className="flex flex-col md:flex-row gap-4 mb-8">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
            <Input
              placeholder="Search events..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10"
            />
          </div>

          <Select value={categoryFilter} onValueChange={setCategoryFilter}>
            <SelectTrigger className="w-[180px]">
              <SelectValue placeholder="All Categories" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">All Categories</SelectItem>
              {categories.map((category) => (
                <SelectItem key={category} value={category}>
                  {category}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        {/* Events Grid */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredEvents.map((event) => (
            <Card key={event.id} className="hover:shadow-lg transition-shadow">
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <CardTitle className="text-lg">{event.title}</CardTitle>
                    <CardDescription className="mt-1">Organized by {event.organizer}</CardDescription>
                  </div>
                  <Badge variant="outline">{event.category}</Badge>
                </div>
              </CardHeader>

              <CardContent className="space-y-4">
                <p className="text-sm text-muted-foreground line-clamp-2">{event.description}</p>

                <div className="space-y-2 text-sm">
                  <div className="flex items-center space-x-2 text-muted-foreground">
                    <CalendarIcon className="h-4 w-4" />
                    <span>{new Date(event.date).toLocaleDateString()}</span>
                  </div>

                  <div className="flex items-center space-x-2 text-muted-foreground">
                    <Clock className="h-4 w-4" />
                    <span>
                      {event.time} ({event.duration})
                    </span>
                  </div>

                  <div className="flex items-center space-x-2 text-muted-foreground">
                    <MapPin className="h-4 w-4" />
                    <span>{event.location}</span>
                  </div>

                  <div className="flex items-center space-x-2 text-muted-foreground">
                    <Users className="h-4 w-4" />
                    <span>
                      {event.attendees}/{event.maxAttendees} attendees
                    </span>
                  </div>
                </div>

                <Button
                  className="w-full"
                  variant={event.isRegistered ? "outline" : "default"}
                  onClick={() => handleRegister(event.id)}
                  disabled={!event.isRegistered && event.attendees >= event.maxAttendees}
                >
                  {event.isRegistered
                    ? "Registered ✓"
                    : event.attendees >= event.maxAttendees
                      ? "Event Full"
                      : "Register"}
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* No Results */}
        {filteredEvents.length === 0 && (
          <div className="text-center py-12">
            <PartyPopper className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <p className="text-muted-foreground">No events match your current filters.</p>
            <Button
              variant="outline"
              onClick={() => {
                setSearchTerm("")
                setCategoryFilter("all")
              }}
              className="mt-4"
            >
              Clear Filters
            </Button>
          </div>
        )}
      </div>
    </div>
  )
}
