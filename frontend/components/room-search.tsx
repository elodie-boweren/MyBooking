"use client"

import { useState, useEffect } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { 
  Building2, 
  Search, 
  Filter, 
  Calendar,
  Users,
  DollarSign,
  Bed,
  Star,
  Wifi,
  Car,
  Coffee
} from 'lucide-react'
import { COMPONENT_TEMPLATES } from '@/lib/style-constants'
import { apiClient, API_ENDPOINTS, Room } from '@/lib/api'

// Helper function to get placeholder image based on room type - 4 UNIQUE HIGH-QUALITY HOTEL ROOM IMAGES
const getPlaceholderImage = (roomType: string): string => {
  const images = {
    SINGLE: 'https://images.pexels.com/photos/271624/pexels-photo-271624.jpeg?w=600&h=400&fit=crop&auto=format&q=95', // Modern single room
    DOUBLE: 'https://images.pexels.com/photos/271743/pexels-photo-271743.jpeg?w=600&h=400&fit=crop&auto=format&q=95', // Elegant double room
    DELUXE: 'https://images.pexels.com/photos/279746/pexels-photo-279746.jpeg?w=600&h=400&fit=crop&auto=format&q=95', // Luxury deluxe suite
    FAMILY: 'https://images.pexels.com/photos/210265/pexels-photo-210265.jpeg?w=600&h=400&fit=crop&auto=format&q=95'  // Spacious family room
  }
  return images[roomType as keyof typeof images] || images.SINGLE
}

interface RoomSearchProps {
  onRoomSelect?: (room: Room) => void
}

export function RoomSearch({ onRoomSelect }: RoomSearchProps) {
  const [rooms, setRooms] = useState<Room[]>([])
  const [loading, setLoading] = useState(false)
  const [searchParams, setSearchParams] = useState({
    checkIn: '',
    checkOut: '',
    guests: 1,
    roomType: 'all',
    maxPrice: ''
  })

  const handleSearch = async () => {
    if (!searchParams.checkIn || !searchParams.checkOut) {
      alert('Please select check-in and check-out dates')
      return
    }

    try {
      setLoading(true)
      const response = await apiClient.get<any>(API_ENDPOINTS.ROOMS.SEARCH, {
        checkIn: searchParams.checkIn,
        checkOut: searchParams.checkOut,
        minCapacity: searchParams.guests,
        roomType: searchParams.roomType !== 'all' ? searchParams.roomType : undefined,
        maxPrice: searchParams.maxPrice ? parseFloat(searchParams.maxPrice) : undefined
      })
      
      // Handle paginated response from Spring Boot
      const roomsData = response.content || response
      setRooms(Array.isArray(roomsData) ? roomsData : [])
    } catch (error) {
      console.error('Failed to search rooms:', error)
      // Fallback to mock data
      const mockRooms: Room[] = [
        {
          id: 1,
          number: '101',
          roomType: 'SINGLE',
          capacity: 1,
          price: 120,
          currency: 'USD',
          status: 'AVAILABLE',
          description: 'Comfortable single room with city view',
          equipment: 'WiFi, TV, Mini-bar',
          createdAt: '2024-01-01T00:00:00Z',
          updatedAt: '2024-01-01T00:00:00Z'
        },
        {
          id: 2,
          number: '102',
          roomType: 'DOUBLE',
          capacity: 2,
          price: 180,
          currency: 'USD',
          status: 'AVAILABLE',
          description: 'Spacious double room with balcony',
          equipment: 'WiFi, TV, Mini-bar, Balcony',
          createdAt: '2024-01-01T00:00:00Z',
          updatedAt: '2024-01-01T00:00:00Z'
        },
        {
          id: 3,
          number: '201',
          roomType: 'DELUXE',
          capacity: 2,
          price: 250,
          currency: 'USD',
          status: 'AVAILABLE',
          description: 'Luxurious deluxe suite',
          equipment: 'WiFi, TV, Mini-bar, Jacuzzi, Balcony',
          createdAt: '2024-01-01T00:00:00Z',
          updatedAt: '2024-01-01T00:00:00Z'
        }
      ]
      setRooms(mockRooms)
    } finally {
      setLoading(false)
    }
  }

  const getRoomTypeIcon = (type: string) => {
    switch (type) {
      case 'SINGLE':
        return '🛏️'
      case 'DOUBLE':
        return '🛏️🛏️'
      case 'DELUXE':
        return '👑'
      case 'FAMILY':
        return '👨‍👩‍👧‍👦'
      default:
        return '🏨'
    }
  }

  const getRoomTypeName = (type: string) => {
    switch (type) {
      case 'SINGLE':
        return 'Single Room'
      case 'DOUBLE':
        return 'Double Room'
      case 'DELUXE':
        return 'Deluxe Suite'
      case 'FAMILY':
        return 'Family Room'
      default:
        return 'Standard Room'
    }
  }

  const getEquipmentIcons = (equipment: string) => {
    const icons = []
    if (equipment?.includes('WiFi')) icons.push(<Wifi key="wifi" className="h-4 w-4" />)
    if (equipment?.includes('TV')) icons.push(<Star key="tv" className="h-4 w-4" />)
    if (equipment?.includes('Parking')) icons.push(<Car key="parking" className="h-4 w-4" />)
    if (equipment?.includes('Coffee')) icons.push(<Coffee key="coffee" className="h-4 w-4" />)
    return icons
  }

  return (
    <div className="space-y-6">
      {/* Search Form */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <Search className="h-5 w-5" />
            <span>Find Your Perfect Room</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
            <div>
              <label className="block text-sm font-medium mb-2">Check-in Date</label>
              <Input
                type="date"
                value={searchParams.checkIn}
                onChange={(e) => setSearchParams(prev => ({ ...prev, checkIn: e.target.value }))}
                min={new Date().toISOString().split('T')[0]}
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Check-out Date</label>
              <Input
                type="date"
                value={searchParams.checkOut}
                onChange={(e) => setSearchParams(prev => ({ ...prev, checkOut: e.target.value }))}
                min={searchParams.checkIn || new Date().toISOString().split('T')[0]}
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Guests</label>
              <select
                value={searchParams.guests}
                onChange={(e) => setSearchParams(prev => ({ ...prev, guests: parseInt(e.target.value) }))}
                className="w-full px-3 py-2 border border-border rounded-md bg-background text-foreground"
              >
                {[1, 2, 3, 4, 5, 6].map(num => (
                  <option key={num} value={num}>{num} guest{num > 1 ? 's' : ''}</option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Room Type</label>
              <select
                value={searchParams.roomType}
                onChange={(e) => setSearchParams(prev => ({ ...prev, roomType: e.target.value }))}
                className="w-full px-3 py-2 border border-border rounded-md bg-background text-foreground"
              >
                <option value="all">All Types</option>
                <option value="SINGLE">Single</option>
                <option value="DOUBLE">Double</option>
                <option value="DELUXE">Deluxe</option>
                <option value="FAMILY">Family</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Max Price</label>
              <Input
                type="number"
                placeholder="No limit"
                value={searchParams.maxPrice}
                onChange={(e) => setSearchParams(prev => ({ ...prev, maxPrice: e.target.value }))}
              />
            </div>
          </div>
          <div className="mt-4">
            <Button onClick={handleSearch} disabled={loading} className="w-full md:w-auto">
              {loading ? (
                <>
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                  Searching...
                </>
              ) : (
                <>
                  <Search className="h-4 w-4 mr-2" />
                  Search Rooms
                </>
              )}
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Search Results */}
      {(rooms || []).length > 0 && (
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-xl font-semibold">
              Available Rooms ({(rooms || []).length})
            </h2>
            <div className="flex items-center space-x-2">
              <Filter className="h-4 w-4 text-muted-foreground" />
              <span className="text-sm text-muted-foreground">Filter results</span>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {rooms.map((room) => (
              <Card key={room.id} className={COMPONENT_TEMPLATES.cardHover}>
                {/* Room Image */}
                <div className="relative h-48 w-full overflow-hidden rounded-t-lg bg-muted">
                  <img
                    src={getPlaceholderImage(room.roomType)}
                    alt={`Room ${room.number}`}
                    className="w-full h-full object-cover"
                    loading="lazy"
                    onError={(e) => {
                      // Reliable Pexels fallback images for each room type - ALL UNIQUE
                      const fallbacks = {
                        SINGLE: [
                          'https://images.pexels.com/photos/164595/pexels-photo-164595.jpeg?w=600&h=400&fit=crop&auto=format&q=95',
                          'https://images.pexels.com/photos/90317/pexels-photo-90317.jpeg?w=600&h=400&fit=crop&auto=format&q=95'
                        ],
                        DOUBLE: [
                          'https://images.pexels.com/photos/279746/pexels-photo-279746.jpeg?w=600&h=400&fit=crop&auto=format&q=95',
                          'https://images.pexels.com/photos/237371/pexels-photo-237371.jpeg?w=600&h=400&fit=crop&auto=format&q=95'
                        ],
                        DELUXE: [
                          'https://images.pexels.com/photos/271618/pexels-photo-271618.jpeg?w=600&h=400&fit=crop&auto=format&q=95',
                          'https://images.pexels.com/photos/276671/pexels-photo-276671.jpeg?w=600&h=400&fit=crop&auto=format&q=95'
                        ],
                        FAMILY: [
                          'https://images.pexels.com/photos/276724/pexels-photo-276724.jpeg?w=600&h=400&fit=crop&auto=format&q=95',
                          'https://images.pexels.com/photos/271816/pexels-photo-271816.jpeg?w=600&h=400&fit=crop&auto=format&q=95'
                        ]
                      }
                      
                      const roomTypeFallbacks = fallbacks[room.roomType as keyof typeof fallbacks] || fallbacks.SINGLE
                      const currentSrc = e.currentTarget.src
                      const fallbackIndex = roomTypeFallbacks.findIndex(fallback => fallback !== currentSrc)
                      
                      if (fallbackIndex !== -1) {
                        e.currentTarget.src = roomTypeFallbacks[fallbackIndex]
                      } else {
                        // If all fallbacks fail, show a beautiful placeholder
                        e.currentTarget.style.display = 'none'
                        e.currentTarget.parentElement!.innerHTML = `
                          <div class="w-full h-full flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100 text-muted-foreground">
                            <div class="text-center">
                              <div class="text-6xl mb-4">🏨</div>
                              <div class="text-lg font-semibold">${room.roomType} Room</div>
                              <div class="text-sm">Room ${room.number}</div>
                            </div>
                          </div>
                        `
                      }
                    }}
                  />
                  <div className="absolute top-2 right-2">
                    <Badge variant="default">
                      Available
                    </Badge>
                  </div>
                </div>
                <CardHeader className="pb-3">
                  <div className="flex items-center space-x-3">
                    <div className="text-2xl">{getRoomTypeIcon(room.roomType)}</div>
                    <div>
                      <CardTitle className="text-lg">Room {room.number}</CardTitle>
                      <p className="text-sm text-muted-foreground">
                        {getRoomTypeName(room.roomType)}
                      </p>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-3">
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-muted-foreground">Price per night:</span>
                      <span className="text-xl font-bold text-primary">
                        ${room.price} {room.currency}
                      </span>
                    </div>
                    <div className="flex items-center space-x-2">
                      <Users className="h-4 w-4 text-muted-foreground" />
                      <span className="text-sm">{room.capacity} guest{room.capacity > 1 ? 's' : ''}</span>
                    </div>
                    {room.description && (
                      <p className="text-sm text-muted-foreground">
                        {room.description}
                      </p>
                    )}
                    {room.equipment && (
                      <div className="flex items-center space-x-2">
                        <span className="text-sm text-muted-foreground">Amenities:</span>
                        <div className="flex space-x-1">
                          {getEquipmentIcons(room.equipment)}
                        </div>
                      </div>
                    )}
                  </div>
                  <div className="mt-4">
                    <Button 
                      className="w-full"
                      onClick={() => onRoomSelect?.(room)}
                    >
                      <Calendar className="h-4 w-4 mr-2" />
                      Book This Room
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      )}

      {/* No Results */}
      {(rooms || []).length === 0 && !loading && (
        <Card>
          <CardContent className="text-center py-8">
            <Building2 className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <h3 className="text-lg font-medium text-foreground mb-2">No rooms available</h3>
            <p className="text-muted-foreground">
              Try adjusting your search criteria or dates
            </p>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
