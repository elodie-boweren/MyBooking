"use client"

import { useState, useEffect } from "react"
import { useParams, useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Separator } from "@/components/ui/separator"
import { Calendar } from "@/components/ui/calendar"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Label } from "@/components/ui/label"
import { Users, Wifi, Monitor, Coffee, ArrowLeft, MapPin, Star, ChevronLeft, ChevronRight } from "lucide-react"
import { Navigation } from "@/components/navigation"

interface Room {
  id: string
  name: string
  capacity: number
  floor: number
  amenities: string[]
  isAvailable: boolean
  nextAvailable?: string
  images: string[]
  description: string
  detailedDescription: string
  pricePerHour: number
  rating: number
  reviews: number
}

export default function RoomDetailsPage() {
  const params = useParams()
  const router = useRouter()
  const [room, setRoom] = useState<Room | null>(null)
  const [selectedDate, setSelectedDate] = useState<Date | undefined>(new Date())
  const [selectedTime, setSelectedTime] = useState("")
  const [duration, setDuration] = useState("")
  const [meetingTitle, setMeetingTitle] = useState("")
  const [meetingDescription, setMeetingDescription] = useState("")
  const [currentImageIndex, setCurrentImageIndex] = useState(0)

  // Mock room data - replace with API call
  useEffect(() => {
    const mockRoom: Room = {
      id: params.id as string,
      name: "Executive Boardroom",
      capacity: 20,
      floor: 3,
      amenities: ["Large Display", "Video Conference", "Coffee Machine", "WiFi", "Whiteboard", "Sound System"],
      isAvailable: true,
      images: [
        "/executive-boardroom.png",
        "/modern-conference-room.png",
        "/creative-workspace.png",
        "/modern-training-room.png",
      ],
      description: "Premium boardroom for executive meetings and important presentations",
      detailedDescription:
        "This spacious executive boardroom features a stunning city view and is equipped with state-of-the-art technology. Perfect for board meetings, client presentations, and important business discussions. The room includes a large conference table that seats up to 20 people comfortably, with ergonomic chairs and professional lighting.",
      pricePerHour: 75,
      rating: 4.8,
      reviews: 124,
    }
    setRoom(mockRoom)
  }, [params.id])

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

  const timeSlots = [
    "09:00",
    "09:30",
    "10:00",
    "10:30",
    "11:00",
    "11:30",
    "12:00",
    "12:30",
    "13:00",
    "13:30",
    "14:00",
    "14:30",
    "15:00",
    "15:30",
    "16:00",
    "16:30",
    "17:00",
    "17:30",
  ]

  const durations = ["30 min", "1 hour", "1.5 hours", "2 hours", "3 hours", "4 hours"]

  const handleBooking = () => {
    // Handle booking logic here
    console.log("Booking:", { selectedDate, selectedTime, duration, meetingTitle, meetingDescription })
    router.push("/my-reservations")
  }

  const nextImage = () => {
    if (room) {
      setCurrentImageIndex((prev) => (prev + 1) % room.images.length)
    }
  }

  const prevImage = () => {
    if (room) {
      setCurrentImageIndex((prev) => (prev - 1 + room.images.length) % room.images.length)
    }
  }

  if (!room) {
    return <div>Loading...</div>
  }

  return (
    <div className="min-h-screen bg-background">
      <Navigation />

      <div className="container mx-auto px-4 py-8">
        {/* Back Button */}
        <Button variant="ghost" onClick={() => router.back()} className="mb-6 flex items-center space-x-2">
          <ArrowLeft className="h-4 w-4" />
          <span>Back to Rooms</span>
        </Button>

        <div className="grid lg:grid-cols-2 gap-8">
          {/* Left Column - Images and Details */}
          <div className="space-y-6">
            {/* Image Gallery */}
            <div className="relative">
              <div className="aspect-video rounded-lg overflow-hidden bg-muted">
                <img
                  src={room.images[currentImageIndex] || "/placeholder.svg"}
                  alt={room.name}
                  className="w-full h-full object-cover"
                />
              </div>

              {room.images.length > 1 && (
                <>
                  <Button
                    variant="outline"
                    size="icon"
                    className="absolute left-4 top-1/2 transform -translate-y-1/2 bg-white/80 hover:bg-white"
                    onClick={prevImage}
                  >
                    <ChevronLeft className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="outline"
                    size="icon"
                    className="absolute right-4 top-1/2 transform -translate-y-1/2 bg-white/80 hover:bg-white"
                    onClick={nextImage}
                  >
                    <ChevronRight className="h-4 w-4" />
                  </Button>

                  {/* Image indicators */}
                  <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex space-x-2">
                    {room.images.map((_, index) => (
                      <button
                        key={index}
                        className={`w-2 h-2 rounded-full ${index === currentImageIndex ? "bg-white" : "bg-white/50"}`}
                        onClick={() => setCurrentImageIndex(index)}
                      />
                    ))}
                  </div>
                </>
              )}
            </div>

            {/* Room Details */}
            <Card>
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div>
                    <CardTitle className="text-2xl">{room.name}</CardTitle>
                    <CardDescription className="flex items-center space-x-4 mt-2">
                      <span className="flex items-center">
                        <MapPin className="h-4 w-4 mr-1" />
                        Floor {room.floor}
                      </span>
                      <span className="flex items-center">
                        <Users className="h-4 w-4 mr-1" />
                        Up to {room.capacity} people
                      </span>
                      <span className="flex items-center">
                        <Star className="h-4 w-4 mr-1 text-yellow-500" />
                        {room.rating} ({room.reviews} reviews)
                      </span>
                    </CardDescription>
                  </div>
                  <Badge variant={room.isAvailable ? "default" : "secondary"}>
                    {room.isAvailable ? "Available" : "Occupied"}
                  </Badge>
                </div>
              </CardHeader>

              <CardContent className="space-y-4">
                <div>
                  <h3 className="font-semibold mb-2">Description</h3>
                  <p className="text-muted-foreground">{room.detailedDescription}</p>
                </div>

                <Separator />

                <div>
                  <h3 className="font-semibold mb-3">Amenities</h3>
                  <div className="grid grid-cols-2 gap-2">
                    {room.amenities.map((amenity) => (
                      <div key={amenity} className="flex items-center space-x-2">
                        {getAmenityIcon(amenity)}
                        <span className="text-sm">{amenity}</span>
                      </div>
                    ))}
                  </div>
                </div>

                <Separator />

                <div className="flex items-center justify-between">
                  <span className="text-lg font-semibold">${room.pricePerHour}/hour</span>
                  <span className="text-sm text-muted-foreground">Professional rate</span>
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Right Column - Booking Form */}
          <div className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle>Book This Room</CardTitle>
                <CardDescription>Select your preferred date and time</CardDescription>
              </CardHeader>

              <CardContent className="space-y-6">
                {/* Date Selection */}
                <div>
                  <Label className="text-sm font-medium">Select Date</Label>
                  <Calendar
                    mode="single"
                    selected={selectedDate}
                    onSelect={setSelectedDate}
                    disabled={(date) => date < new Date()}
                    className="rounded-md border mt-2"
                  />
                </div>

                {/* Time and Duration */}
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Label htmlFor="time">Start Time</Label>
                    <Select value={selectedTime} onValueChange={setSelectedTime}>
                      <SelectTrigger>
                        <SelectValue placeholder="Select time" />
                      </SelectTrigger>
                      <SelectContent>
                        {timeSlots.map((time) => (
                          <SelectItem key={time} value={time}>
                            {time}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>

                  <div>
                    <Label htmlFor="duration">Duration</Label>
                    <Select value={duration} onValueChange={setDuration}>
                      <SelectTrigger>
                        <SelectValue placeholder="Duration" />
                      </SelectTrigger>
                      <SelectContent>
                        {durations.map((dur) => (
                          <SelectItem key={dur} value={dur}>
                            {dur}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                </div>

                {/* Meeting Details */}
                <div>
                  <Label htmlFor="title">Meeting Title</Label>
                  <Input
                    id="title"
                    placeholder="Enter meeting title"
                    value={meetingTitle}
                    onChange={(e) => setMeetingTitle(e.target.value)}
                  />
                </div>

                <div>
                  <Label htmlFor="description">Description (Optional)</Label>
                  <Textarea
                    id="description"
                    placeholder="Meeting agenda or additional notes"
                    value={meetingDescription}
                    onChange={(e) => setMeetingDescription(e.target.value)}
                    rows={3}
                  />
                </div>

                {/* Booking Summary */}
                {selectedDate && selectedTime && duration && (
                  <div className="bg-muted p-4 rounded-lg">
                    <h4 className="font-medium mb-2">Booking Summary</h4>
                    <div className="space-y-1 text-sm text-muted-foreground">
                      <div className="flex justify-between">
                        <span>Date:</span>
                        <span>{selectedDate.toLocaleDateString()}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Time:</span>
                        <span>
                          {selectedTime} ({duration})
                        </span>
                      </div>
                      <div className="flex justify-between">
                        <span>Room:</span>
                        <span>{room.name}</span>
                      </div>
                      <Separator className="my-2" />
                      <div className="flex justify-between font-medium text-foreground">
                        <span>Total:</span>
                        <span>${room.pricePerHour * (duration === "30 min" ? 0.5 : Number.parseInt(duration))}</span>
                      </div>
                    </div>
                  </div>
                )}

                <Button
                  className="w-full"
                  onClick={handleBooking}
                  disabled={!selectedDate || !selectedTime || !duration || !meetingTitle}
                >
                  Book Room
                </Button>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  )
}
