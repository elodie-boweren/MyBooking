"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import {
  Search,
  MapPin,
  Clock,
  Users,
  Wifi,
  Car,
  Coffee,
  Utensils,
  Dumbbell,
  Printer,
  Phone,
  Shield,
} from "lucide-react"
import { Navigation } from "@/components/navigation"

interface Facility {
  id: string
  name: string
  type: string
  location: string
  floor: number
  capacity?: number
  amenities: string[]
  availability: string
  description: string
  image: string
  isBookable: boolean
}

export default function FacilitiesPage() {
  const [facilities, setFacilities] = useState<Facility[]>([])
  const [filteredFacilities, setFilteredFacilities] = useState<Facility[]>([])
  const [searchTerm, setSearchTerm] = useState("")
  const [typeFilter, setTypeFilter] = useState("all")
  const [floorFilter, setFloorFilter] = useState("all")

  // Mock facilities data
  useEffect(() => {
    const mockFacilities: Facility[] = [
      {
        id: "1",
        name: "Main Cafeteria",
        type: "Dining",
        location: "Ground Floor",
        floor: 0,
        capacity: 150,
        amenities: ["Food Service", "WiFi", "Seating Area", "Microwave"],
        availability: "6:00 AM - 8:00 PM",
        description: "Spacious cafeteria with diverse food options and comfortable seating areas.",
        image: "/modern-cafeteria.jpg",
        isBookable: false,
      },
      {
        id: "2",
        name: "Fitness Center",
        type: "Recreation",
        location: "Basement Level",
        floor: -1,
        capacity: 30,
        amenities: ["Exercise Equipment", "Lockers", "Showers", "Towel Service"],
        availability: "5:00 AM - 10:00 PM",
        description: "Fully equipped fitness center with modern exercise equipment and changing facilities.",
        image: "/modern-gym-fitness-center.jpg",
        isBookable: true,
      },
      {
        id: "3",
        name: "Parking Garage",
        type: "Parking",
        location: "Underground",
        floor: -2,
        capacity: 200,
        amenities: ["Security Cameras", "Electric Charging", "Reserved Spots", "24/7 Access"],
        availability: "24/7",
        description: "Secure underground parking with electric vehicle charging stations.",
        image: "/underground-parking-garage.png",
        isBookable: true,
      },
      {
        id: "4",
        name: "Print & Copy Center",
        type: "Business Services",
        location: "Floor 1",
        floor: 1,
        amenities: ["High-Speed Printing", "Scanning", "Binding", "Lamination"],
        availability: "7:00 AM - 7:00 PM",
        description: "Professional printing and copying services with various finishing options.",
        image: "/modern-print-center-office.jpg",
        isBookable: false,
      },
      {
        id: "5",
        name: "Quiet Work Pods",
        type: "Work Space",
        location: "Floor 2",
        floor: 2,
        capacity: 1,
        amenities: ["Sound Isolation", "Power Outlets", "Adjustable Lighting", "WiFi"],
        availability: "24/7",
        description: "Individual soundproof pods perfect for focused work and private calls.",
        image: "/modern-work-pod-office-booth.jpg",
        isBookable: true,
      },
      {
        id: "6",
        name: "Rooftop Terrace",
        type: "Recreation",
        location: "Rooftop",
        floor: 5,
        capacity: 50,
        amenities: ["Outdoor Seating", "City Views", "WiFi", "Weather Protection"],
        availability: "6:00 AM - 10:00 PM",
        description: "Beautiful rooftop terrace with panoramic city views, perfect for breaks and informal meetings.",
        image: "/rooftop-terrace-office-building.jpg",
        isBookable: true,
      },
    ]
    setFacilities(mockFacilities)
    setFilteredFacilities(mockFacilities)
  }, [])

  // Filter facilities
  useEffect(() => {
    const filtered = facilities.filter((facility) => {
      const matchesSearch =
        facility.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        facility.description.toLowerCase().includes(searchTerm.toLowerCase())
      const matchesType = typeFilter === "all" || facility.type === typeFilter
      const matchesFloor = floorFilter === "all" || facility.floor.toString() === floorFilter
      return matchesSearch && matchesType && matchesFloor
    })
    setFilteredFacilities(filtered)
  }, [facilities, searchTerm, typeFilter, floorFilter])

  const getTypeIcon = (type: string) => {
    switch (type) {
      case "Dining":
        return <Utensils className="h-5 w-5" />
      case "Recreation":
        return <Dumbbell className="h-5 w-5" />
      case "Parking":
        return <Car className="h-5 w-5" />
      case "Business Services":
        return <Printer className="h-5 w-5" />
      case "Work Space":
        return <Phone className="h-5 w-5" />
      default:
        return <MapPin className="h-5 w-5" />
    }
  }

  const getAmenityIcon = (amenity: string) => {
    switch (amenity.toLowerCase()) {
      case "wifi":
        return <Wifi className="h-4 w-4" />
      case "security cameras":
      case "24/7 access":
        return <Shield className="h-4 w-4" />
      case "food service":
      case "coffee machine":
        return <Coffee className="h-4 w-4" />
      default:
        return null
    }
  }

  const facilityTypes = ["Dining", "Recreation", "Parking", "Business Services", "Work Space"]
  const floors = ["-2", "-1", "0", "1", "2", "3", "4", "5"]

  return (
    <div className="min-h-screen bg-background">
      <Navigation />

      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-foreground mb-2">Office Facilities</h1>
          <p className="text-muted-foreground">Explore all available facilities and amenities in our building</p>
        </div>

        {/* Search and Filters */}
        <div className="flex flex-col md:flex-row gap-4 mb-8">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
            <Input
              placeholder="Search facilities..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="pl-10"
            />
          </div>

          <div className="flex gap-2">
            <Select value={typeFilter} onValueChange={setTypeFilter}>
              <SelectTrigger className="w-[160px]">
                <SelectValue placeholder="All Types" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Types</SelectItem>
                {facilityTypes.map((type) => (
                  <SelectItem key={type} value={type}>
                    {type}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>

            <Select value={floorFilter} onValueChange={setFloorFilter}>
              <SelectTrigger className="w-[120px]">
                <SelectValue placeholder="All Floors" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Floors</SelectItem>
                {floors.map((floor) => (
                  <SelectItem key={floor} value={floor}>
                    {floor === "-2" ? "B2" : floor === "-1" ? "B1" : floor === "0" ? "Ground" : `Floor ${floor}`}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        </div>

        {/* Facilities Grid */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredFacilities.map((facility) => (
            <Card key={facility.id} className="overflow-hidden hover:shadow-lg transition-shadow">
              <div className="relative">
                <img
                  src={facility.image || "/placeholder.svg"}
                  alt={facility.name}
                  className="w-full h-48 object-cover"
                />
                <div className="absolute top-4 left-4">
                  <Badge variant="secondary" className="flex items-center space-x-1">
                    {getTypeIcon(facility.type)}
                    <span>{facility.type}</span>
                  </Badge>
                </div>
                {facility.isBookable && (
                  <div className="absolute top-4 right-4">
                    <Badge variant="default">Bookable</Badge>
                  </div>
                )}
              </div>

              <CardHeader>
                <CardTitle className="text-lg">{facility.name}</CardTitle>
                <CardDescription className="flex items-center space-x-4">
                  <span className="flex items-center">
                    <MapPin className="h-4 w-4 mr-1" />
                    {facility.location}
                  </span>
                  {facility.capacity && (
                    <span className="flex items-center">
                      <Users className="h-4 w-4 mr-1" />
                      {facility.capacity}
                    </span>
                  )}
                </CardDescription>
              </CardHeader>

              <CardContent className="space-y-4">
                <p className="text-sm text-muted-foreground">{facility.description}</p>

                <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                  <Clock className="h-4 w-4" />
                  <span>{facility.availability}</span>
                </div>

                <div>
                  <h4 className="text-sm font-medium text-foreground mb-2">Amenities</h4>
                  <div className="flex flex-wrap gap-2">
                    {facility.amenities.map((amenity) => (
                      <div key={amenity} className="flex items-center gap-1 text-xs text-muted-foreground">
                        {getAmenityIcon(amenity)}
                        <span>{amenity}</span>
                      </div>
                    ))}
                  </div>
                </div>

                {facility.isBookable && <Button className="w-full">Book Facility</Button>}
              </CardContent>
            </Card>
          ))}
        </div>

        {/* No Results */}
        {filteredFacilities.length === 0 && (
          <div className="text-center py-12">
            <MapPin className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <p className="text-muted-foreground">No facilities match your current filters.</p>
            <Button
              variant="outline"
              onClick={() => {
                setSearchTerm("")
                setTypeFilter("all")
                setFloorFilter("all")
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
