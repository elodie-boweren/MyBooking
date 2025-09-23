"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Badge } from "@/components/ui/badge"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Textarea } from "@/components/ui/textarea"
import { Checkbox } from "@/components/ui/checkbox"
import { Plus, Edit, Trash2, Users, MapPin } from "lucide-react"
import { useToast } from "@/hooks/use-toast"

interface Room {
  id: string
  name: string
  capacity: number
  floor: number
  amenities: string[]
  isAvailable: boolean
  description: string
  image?: string
}

export function RoomManagement() {
  const [rooms, setRooms] = useState<Room[]>([])
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [editingRoom, setEditingRoom] = useState<Room | null>(null)
  const [formData, setFormData] = useState({
    name: "",
    capacity: "",
    floor: "",
    description: "",
    amenities: [] as string[],
  })
  const { toast } = useToast()

  const availableAmenities = [
    "Projector",
    "Whiteboard",
    "Video Conference",
    "WiFi",
    "TV Screen",
    "Coffee Machine",
    "Sound System",
    "Microphone",
    "Power Outlets",
    "Natural Light",
    "Air Conditioning",
  ]

  // Mock data - replace with API call
  useEffect(() => {
    const mockRooms: Room[] = [
      {
        id: "1",
        name: "Conference Room A",
        capacity: 12,
        floor: 1,
        amenities: ["Projector", "Whiteboard", "Video Conference", "WiFi"],
        isAvailable: true,
        description: "Spacious conference room perfect for team meetings and presentations",
      },
      {
        id: "2",
        name: "Meeting Room B",
        capacity: 6,
        floor: 2,
        amenities: ["TV Screen", "Whiteboard", "WiFi"],
        isAvailable: false,
        description: "Intimate meeting space ideal for small team discussions",
      },
      {
        id: "3",
        name: "Executive Boardroom",
        capacity: 20,
        floor: 3,
        amenities: ["Large Display", "Video Conference", "Coffee Machine", "WiFi"],
        isAvailable: true,
        description: "Premium boardroom for executive meetings and important presentations",
      },
    ]
    setRooms(mockRooms)
  }, [])

  const handleInputChange = (field: string, value: string | string[]) => {
    setFormData((prev) => ({ ...prev, [field]: value }))
  }

  const handleAmenityChange = (amenity: string, checked: boolean) => {
    setFormData((prev) => ({
      ...prev,
      amenities: checked ? [...prev.amenities, amenity] : prev.amenities.filter((a) => a !== amenity),
    }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    try {
      const roomData = {
        name: formData.name,
        capacity: Number.parseInt(formData.capacity),
        floor: Number.parseInt(formData.floor),
        description: formData.description,
        amenities: formData.amenities,
        isAvailable: true,
      }

      if (editingRoom) {
        // Update existing room
        const response = await fetch(`/api/admin/rooms/${editingRoom.id}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
          body: JSON.stringify(roomData),
        })

        if (response.ok) {
          toast({
            title: "Room updated",
            description: "Room details have been successfully updated",
          })
          setRooms((prev) => prev.map((room) => (room.id === editingRoom.id ? { ...room, ...roomData } : room)))
        }
      } else {
        // Create new room
        const response = await fetch("/api/admin/rooms", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
          body: JSON.stringify(roomData),
        })

        if (response.ok) {
          const newRoom = await response.json()
          toast({
            title: "Room created",
            description: "New room has been successfully added",
          })
          setRooms((prev) => [...prev, newRoom])
        }
      }

      // Reset form
      setFormData({
        name: "",
        capacity: "",
        floor: "",
        description: "",
        amenities: [],
      })
      setIsAddDialogOpen(false)
      setEditingRoom(null)
    } catch (error) {
      toast({
        title: "Error",
        description: "Something went wrong. Please try again.",
        variant: "destructive",
      })
    }
  }

  const handleEdit = (room: Room) => {
    setEditingRoom(room)
    setFormData({
      name: room.name,
      capacity: room.capacity.toString(),
      floor: room.floor.toString(),
      description: room.description,
      amenities: room.amenities,
    })
    setIsAddDialogOpen(true)
  }

  const handleDelete = async (roomId: string) => {
    if (!confirm("Are you sure you want to delete this room?")) return

    try {
      const response = await fetch(`/api/admin/rooms/${roomId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      })

      if (response.ok) {
        toast({
          title: "Room deleted",
          description: "Room has been successfully removed",
        })
        setRooms((prev) => prev.filter((room) => room.id !== roomId))
      }
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to delete room. Please try again.",
        variant: "destructive",
      })
    }
  }

  const toggleRoomAvailability = async (roomId: string, isAvailable: boolean) => {
    try {
      const response = await fetch(`/api/admin/rooms/${roomId}/availability`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
        body: JSON.stringify({ isAvailable: !isAvailable }),
      })

      if (response.ok) {
        setRooms((prev) => prev.map((room) => (room.id === roomId ? { ...room, isAvailable: !isAvailable } : room)))
        toast({
          title: "Room status updated",
          description: `Room is now ${!isAvailable ? "available" : "unavailable"}`,
        })
      }
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to update room status",
        variant: "destructive",
      })
    }
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-foreground">Room Management</h2>
          <p className="text-muted-foreground">Add, edit, and manage meeting rooms</p>
        </div>
        <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
          <DialogTrigger asChild>
            <Button
              onClick={() => {
                setEditingRoom(null)
                setFormData({
                  name: "",
                  capacity: "",
                  floor: "",
                  description: "",
                  amenities: [],
                })
              }}
            >
              <Plus className="h-4 w-4 mr-2" />
              Add Room
            </Button>
          </DialogTrigger>
          <DialogContent className="sm:max-w-[500px]">
            <DialogHeader>
              <DialogTitle>{editingRoom ? "Edit Room" : "Add New Room"}</DialogTitle>
              <DialogDescription>{editingRoom ? "Update room details" : "Create a new meeting room"}</DialogDescription>
            </DialogHeader>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="space-y-2">
                <Label htmlFor="name">Room Name</Label>
                <Input
                  id="name"
                  value={formData.name}
                  onChange={(e) => handleInputChange("name", e.target.value)}
                  placeholder="Conference Room A"
                  required
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="capacity">Capacity</Label>
                  <Input
                    id="capacity"
                    type="number"
                    value={formData.capacity}
                    onChange={(e) => handleInputChange("capacity", e.target.value)}
                    placeholder="12"
                    min="1"
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="floor">Floor</Label>
                  <Select value={formData.floor} onValueChange={(value) => handleInputChange("floor", value)}>
                    <SelectTrigger>
                      <SelectValue placeholder="Select floor" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="1">Floor 1</SelectItem>
                      <SelectItem value="2">Floor 2</SelectItem>
                      <SelectItem value="3">Floor 3</SelectItem>
                      <SelectItem value="4">Floor 4</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="description">Description</Label>
                <Textarea
                  id="description"
                  value={formData.description}
                  onChange={(e) => handleInputChange("description", e.target.value)}
                  placeholder="Room description and features"
                  rows={3}
                />
              </div>

              <div className="space-y-2">
                <Label>Amenities</Label>
                <div className="grid grid-cols-2 gap-2">
                  {availableAmenities.map((amenity) => (
                    <div key={amenity} className="flex items-center space-x-2">
                      <Checkbox
                        id={amenity}
                        checked={formData.amenities.includes(amenity)}
                        onCheckedChange={(checked) => handleAmenityChange(amenity, checked as boolean)}
                      />
                      <Label htmlFor={amenity} className="text-sm">
                        {amenity}
                      </Label>
                    </div>
                  ))}
                </div>
              </div>

              <div className="flex justify-end space-x-2 pt-4">
                <Button type="button" variant="outline" onClick={() => setIsAddDialogOpen(false)}>
                  Cancel
                </Button>
                <Button type="submit">{editingRoom ? "Update Room" : "Create Room"}</Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {rooms.map((room) => (
          <Card key={room.id}>
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle className="text-lg">{room.name}</CardTitle>
                <Badge variant={room.isAvailable ? "default" : "secondary"}>
                  {room.isAvailable ? "Available" : "Unavailable"}
                </Badge>
              </div>
              <CardDescription>
                <div className="flex items-center space-x-4 text-sm">
                  <div className="flex items-center">
                    <Users className="h-4 w-4 mr-1" />
                    {room.capacity} people
                  </div>
                  <div className="flex items-center">
                    <MapPin className="h-4 w-4 mr-1" />
                    Floor {room.floor}
                  </div>
                </div>
              </CardDescription>
            </CardHeader>

            <CardContent>
              <div className="space-y-4">
                <p className="text-sm text-muted-foreground">{room.description}</p>

                <div>
                  <h4 className="text-sm font-medium mb-2">Amenities</h4>
                  <div className="flex flex-wrap gap-1">
                    {room.amenities.map((amenity) => (
                      <Badge key={amenity} variant="outline" className="text-xs">
                        {amenity}
                      </Badge>
                    ))}
                  </div>
                </div>

                <div className="flex justify-between items-center pt-2">
                  <Button variant="outline" size="sm" onClick={() => toggleRoomAvailability(room.id, room.isAvailable)}>
                    {room.isAvailable ? "Disable" : "Enable"}
                  </Button>
                  <div className="flex space-x-1">
                    <Button variant="outline" size="sm" onClick={() => handleEdit(room)}>
                      <Edit className="h-4 w-4" />
                    </Button>
                    <Button variant="outline" size="sm" onClick={() => handleDelete(room.id)}>
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
}
