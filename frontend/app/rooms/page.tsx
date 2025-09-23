"use client"

import { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Badge } from "@/components/ui/badge"
import { Users, Wifi, Monitor, Coffee, Search } from "lucide-react"
import { RoomBookingModal } from "@/components/room-booking-modal"

interface Room {
  id: string
  name: string
  capacity: number
  floor: number
  amenities: string[]
  isAvailable: boolean
  nextAvailable?: string
  image: string
  description: string
}

export default function RoomsPage() {
  const [rooms, setRooms] = useState<Room[]>([])
  const [filteredRooms, setFilteredRooms] = useState<Room[]>([])
  const [searchTerm, setSearchTerm] = useState("")
  const [capacityFilter, setCapacityFilter] = useState("allSizes")
  const [floorFilter, setFloorFilter] = useState("allFloors")
  const [availabilityFilter, setAvailabilityFilter] = useState("allRooms")
  const [selectedRoom, setSelectedRoom] = useState<Room | null>(null)
  const [isBookingModalOpen, setIsBookingModalOpen] = useState(false)
  const router = useRouter()

  // Mock data - replace with API call to Spring Boot backend
  useEffect(() => {
    const mockRooms: Room[] = [
      {
        id: "1",
        name: "Conference Room A",
        capacity: 12,
        floor: 1,
        amenities: ["Projector", "Whiteboard", "Video Conference", "WiFi"],
        isAvailable: true,
        image: "/modern-conference-room.png",
        description: "Spacious conference room perfect for team meetings and presentations",
      },
      {
        id: "2",
        name: "Meeting Room B",
        capacity: 6,
        floor: 2,
        amenities: ["TV Screen", "Whiteboard", "WiFi"],
        isAvailable: false,
        nextAvailable: "2:00 PM",
        image: "/small-meeting-room.png",
        description: "Intimate meeting space ideal for small team discussions",
      },
      {
        id: "3",
        name: "Executive Boardroom",
        capacity: 20,
        floor: 3,
        amenities: ["Large Display", "Video Conference", "Coffee Machine", "WiFi"],
        isAvailable: true,
        image: "/executive-boardroom.png",
        description: "Premium boardroom for executive meetings and important presentations",
      },
      {
        id: "4",
        name: "Creative Studio",
        capacity: 8,
        floor: 2,
        amenities: ["Whiteboard", "Sticky Notes", "WiFi"],
        isAvailable: true,
        image: "/creative-workspace.png",
        description: "Open creative space designed for brainstorming and collaborative work",
      },
      {
        id: "5",
        name: "Phone Booth 1",
        capacity: 1,
        floor: 1,
        amenities: ["WiFi", "Power Outlet"],
        isAvailable: false,
        nextAvailable: "11:30 AM",
        image: "/placeholder-dvh33.png",
        description: "Private space for confidential calls and focused work",
      },
      {
        id: "6",
        name: "Training Room",
        capacity: 25,
        floor: 1,
        amenities: ["Projector", "Sound System", "Microphone", "WiFi"],
        isAvailable: true,
        image: "/modern-training-room.png",
        description: "Large training facility equipped for workshops and seminars",
      },
    ]
    console.log("[v0] Loading rooms data:", mockRooms)
    setRooms(mockRooms)
    setFilteredRooms(mockRooms)
  }, [])

  // Filter rooms based on search and filters
  useEffect(() => {
    console.log("[v0] Applying filters:", { searchTerm, capacityFilter, floorFilter, availabilityFilter })
    const filtered = rooms.filter((room) => {
      const matchesSearch =
        room.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        room.description.toLowerCase().includes(searchTerm.toLowerCase())
      const matchesCapacity =
        capacityFilter === "allSizes" ||
        (capacityFilter === "small" && room.capacity <= 6) ||
        (capacityFilter === "medium" && room.capacity > 6 && room.capacity <= 15) ||
        (capacityFilter === "large" && room.capacity > 15)
      const matchesFloor = floorFilter === "allFloors" || room.floor.toString() === floorFilter
      const matchesAvailability =
        availabilityFilter === "allRooms" ||
        (availabilityFilter === "available" && room.isAvailable) ||
        (availabilityFilter === "occupied" && !room.isAvailable)

      return matchesSearch && matchesCapacity && matchesFloor && matchesAvailability
    })
    console.log("[v0] Filtered rooms:", filtered)
    setFilteredRooms(filtered)
  }, [rooms, searchTerm, capacityFilter, floorFilter, availabilityFilter])

  const getAmenityIcon = (amenity: string) => {
    switch (amenity.toLowerCase()) {
      case "wifi":
        return <Wifi className="h-4 w-4" />
      case "projector":
      case "tv screen":
      case "large display":
        return <Monitor className="h-4 w-4" />
      case "coffee machine":
        return <Coffee className="h-4 w-4" />
      default:
        return null
    }
  }

  const handleBookRoom = (room: Room) => {
    setSelectedRoom(room)
    setIsBookingModalOpen(true)
  }

  return (
    <div className="min-h-screen bg-background">
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-foreground mb-2">Available Rooms</h1>
          <p className="text-muted-foreground">Find and book the perfect space for your meeting</p>
        </div>

        {/* Search and Filters */}
        <div className="mb-8 space-y-4">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
              <Input
                placeholder="Search rooms..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
            <div className="flex gap-2">
              <Select value={capacityFilter} onValueChange={setCapacityFilter}>
                <SelectTrigger className="w-[140px]">
                  <SelectValue placeholder="Capacity" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="allSizes">All Sizes</SelectItem>
                  <SelectItem value="small">Small (1-6)</SelectItem>
                  <SelectItem value="medium">Medium (7-15)</SelectItem>
                  <SelectItem value="large">Large (16+)</SelectItem>
                </SelectContent>
              </Select>

              <Select value={floorFilter} onValueChange={setFloorFilter}>
                <SelectTrigger className="w-[100px]">
                  <SelectValue placeholder="Floor" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="allFloors">All Floors</SelectItem>
                  <SelectItem value="1">Floor 1</SelectItem>
                  <SelectItem value="2">Floor 2</SelectItem>
                  <SelectItem value="3">Floor 3</SelectItem>
                </SelectContent>
              </Select>

              <Select value={availabilityFilter} onValueChange={setAvailabilityFilter}>
                <SelectTrigger className="w-[130px]">
                  <SelectValue placeholder="Status" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="allRooms">All Rooms</SelectItem>
                  <SelectItem value="available">Available</SelectItem>
                  <SelectItem value="occupied">Occupied</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </div>

        <div className="mb-4">
          <p className="text-sm text-muted-foreground">
            Showing {filteredRooms.length} of {rooms.length} rooms
          </p>
        </div>

        {/* Room Grid */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredRooms.map((room) => (
            <Card
              key={room.id}
              className="overflow-hidden hover:shadow-lg transition-all duration-200 cursor-pointer hover:scale-[1.02]"
              onClick={() => {
                console.log("[v0] Navigating to room:", room.id)
                router.push(`/rooms/${room.id}`)
              }}
            >
              <div className="relative">
                <img
                  src={room.image || "/placeholder.svg"}
                  alt={room.name}
                  className="w-full h-48 object-cover"
                  onError={(e) => {
                    console.log("[v0] Image failed to load:", room.image)
                    e.currentTarget.src = "/modern-meeting-room.png"
                  }}
                />
                <div className="absolute top-4 right-4">
                  <Badge variant={room.isAvailable ? "default" : "secondary"}>
                    {room.isAvailable ? "Available" : "Occupied"}
                  </Badge>
                </div>
              </div>

              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="text-lg">{room.name}</CardTitle>
                  <div className="flex items-center text-muted-foreground">
                    <Users className="h-4 w-4 mr-1" />
                    <span className="text-sm">{room.capacity}</span>
                  </div>
                </div>
                <CardDescription>
                  Floor {room.floor} • {room.description}
                </CardDescription>
              </CardHeader>

              <CardContent>
                <div className="space-y-4">
                  {/* Amenities */}
                  <div>
                    <h4 className="text-sm font-medium text-foreground mb-2">Amenities</h4>
                    <div className="flex flex-wrap gap-2">
                      {room.amenities.map((amenity) => (
                        <div key={amenity} className="flex items-center gap-1 text-xs text-muted-foreground">
                          {getAmenityIcon(amenity)}
                          <span>{amenity}</span>
                        </div>
                      ))}
                    </div>
                  </div>

                  {/* Next Available */}
                  {!room.isAvailable && room.nextAvailable && (
                    <div className="text-sm text-muted-foreground">Next available: {room.nextAvailable}</div>
                  )}

                  {/* Book Button */}
                  <Button
                    className="w-full"
                    disabled={!room.isAvailable}
                    onClick={(e) => {
                      e.stopPropagation()
                      console.log("[v0] Opening booking modal for room:", room.name)
                      handleBookRoom(room)
                    }}
                  >
                    {room.isAvailable ? "Book Room" : "Currently Occupied"}
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* No Results */}
        {filteredRooms.length === 0 && (
          <div className="text-center py-12">
            <p className="text-muted-foreground">No rooms match your current filters.</p>
            <Button
              variant="outline"
              onClick={() => {
                setSearchTerm("")
                setCapacityFilter("allSizes")
                setFloorFilter("allFloors")
                setAvailabilityFilter("allRooms")
              }}
              className="mt-4"
            >
              Clear Filters
            </Button>
          </div>
        )}
      </div>

      {/* Booking Modal */}
      <RoomBookingModal
        room={selectedRoom}
        isOpen={isBookingModalOpen}
        onClose={() => {
          setIsBookingModalOpen(false)
          setSelectedRoom(null)
        }}
      />
    </div>
  )
}
