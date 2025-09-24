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
  Coffee,
  CreditCard,
  CheckCircle
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
  
  // Booking state
  const [showBookingForm, setShowBookingForm] = useState(false)
  const [selectedRoom, setSelectedRoom] = useState<Room | null>(null)
  const [booking, setBooking] = useState(false)
  const [bookingData, setBookingData] = useState({
    checkIn: '',
    checkOut: '',
    numberOfGuests: 1,
    pointsUsed: 0
  })

  // Fetch all available rooms on component mount
  useEffect(() => {
    fetchAllRooms()
  }, [])

  const fetchAllRooms = async () => {
    try {
      setLoading(true)
      
      // If dates are provided, use the search endpoint with availability checking
      if (searchParams.checkIn && searchParams.checkOut) {
        await handleSearch()
        return
      }
      
      const response = await apiClient.get<any>(API_ENDPOINTS.ROOMS.SEARCH)
      
      // Handle paginated response from Spring Boot
      const roomsData = response.content || response
      const allRooms = Array.isArray(roomsData) ? roomsData : []
      
      // Filter out OUT_OF_SERVICE rooms for client interface - clients should only see bookable rooms
      let availableRooms = allRooms.filter((room: Room) => room.status === 'AVAILABLE')
      
      // Apply additional client-side filtering
      if (searchParams.maxPrice) {
        const maxPrice = parseFloat(searchParams.maxPrice)
        availableRooms = availableRooms.filter((room: Room) => room.price <= maxPrice)
      }
      
      if (searchParams.roomType !== 'all') {
        availableRooms = availableRooms.filter((room: Room) => room.roomType === searchParams.roomType)
      }
      
      if (searchParams.guests > 1) {
        availableRooms = availableRooms.filter((room: Room) => room.capacity >= searchParams.guests)
      }
      
      setRooms(availableRooms)
    } catch (error) {
      console.error('Failed to fetch rooms:', error)
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
          price: 280,
          currency: 'USD',
          status: 'AVAILABLE',
          description: 'Luxurious deluxe suite with panoramic view',
          equipment: 'WiFi, TV, Mini-bar, Balcony, Jacuzzi',
          createdAt: '2024-01-01T00:00:00Z',
          updatedAt: '2024-01-01T00:00:00Z'
        },
        {
          id: 4,
          number: '301',
          roomType: 'FAMILY',
          capacity: 4,
          price: 350,
          currency: 'USD',
          status: 'AVAILABLE',
          description: 'Spacious family room perfect for families',
          equipment: 'WiFi, TV, Mini-bar, Balcony, Kitchenette',
          createdAt: '2024-01-01T00:00:00Z',
          updatedAt: '2024-01-01T00:00:00Z'
        }
      ]
      setRooms(mockRooms)
    } finally {
      setLoading(false)
    }
  }

  // Booking functions
  const handleBookRoom = (room: Room) => {
    setSelectedRoom(room)
    setBookingData({
      checkIn: searchParams.checkIn || '',
      checkOut: searchParams.checkOut || '',
      numberOfGuests: searchParams.guests,
      pointsUsed: 0
    })
    setShowBookingForm(true)
  }

  const handleBookingSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (booking || !selectedRoom) return

    try {
      setBooking(true)
      
      // Create reservation request
      const reservationData = {
        roomId: selectedRoom.id,
        checkIn: bookingData.checkIn,
        checkOut: bookingData.checkOut,
        numberOfGuests: bookingData.numberOfGuests,
        pointsUsed: bookingData.pointsUsed || 0,
        currency: selectedRoom.currency
      }

      // Submit booking
      await apiClient.post(API_ENDPOINTS.CLIENT_RESERVATIONS.CREATE, reservationData)
      
      // Success
      alert(`Booking confirmed! You have successfully booked ${selectedRoom.number} for ${bookingData.numberOfGuests} guests.`)
      
      // Reset form and close modal
      setShowBookingForm(false)
      setSelectedRoom(null)
      setBookingData({
        checkIn: '',
        checkOut: '',
        numberOfGuests: 1,
        pointsUsed: 0
      })
      
      // Refresh rooms to update availability
      fetchAllRooms()
      
    } catch (error: any) {
      console.error('Failed to book room:', error)
      const errorMessage = error?.response?.data?.message || error?.message || 'Unknown error'
      alert(`Failed to book room: ${errorMessage}`)
    } finally {
      setBooking(false)
    }
  }

  const handleBookingChange = (field: string, value: string | number) => {
    setBookingData(prev => ({
      ...prev,
      [field]: value
    }))
  }

  // Calculate total price for booking
  const calculateTotalPrice = () => {
    if (!selectedRoom || !bookingData.checkIn || !bookingData.checkOut) {
      return { nights: 0, subtotal: 0, tax: 0, total: 0 }
    }

    const checkIn = new Date(bookingData.checkIn)
    const checkOut = new Date(bookingData.checkOut)
    const nights = Math.ceil((checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24))
    
    const subtotal = nights * selectedRoom.price
    const tax = subtotal * 0.1 // 10% tax
    const total = subtotal + tax

    return { nights, subtotal, tax, total }
  }

  const handleSearch = async () => {
    try {
      setLoading(true)
      
      // If no dates provided, show all available rooms
      if (!searchParams.checkIn || !searchParams.checkOut) {
        await fetchAllRooms()
        return
      }

      // Build query parameters
      const queryParams = new URLSearchParams()
      queryParams.append('checkIn', searchParams.checkIn)
      queryParams.append('checkOut', searchParams.checkOut)
      queryParams.append('minCapacity', searchParams.guests.toString())
      if (searchParams.roomType !== 'all') {
        queryParams.append('roomType', searchParams.roomType)
      }
      if (searchParams.maxPrice) {
        queryParams.append('maxPrice', searchParams.maxPrice)
      }

      const response = await apiClient.get<any>(`${API_ENDPOINTS.ROOMS.SEARCH}?${queryParams.toString()}`)
      
      // Handle paginated response from Spring Boot
      const roomsData = response.content || response
      const allRooms = Array.isArray(roomsData) ? roomsData : []
      
      // Filter out OUT_OF_SERVICE rooms for client interface - clients should only see bookable rooms
      let availableRooms = allRooms.filter((room: Room) => room.status === 'AVAILABLE')
      
      // Apply additional client-side filtering
      if (searchParams.maxPrice) {
        const maxPrice = parseFloat(searchParams.maxPrice)
        availableRooms = availableRooms.filter((room: Room) => room.price <= maxPrice)
      }
      
      if (searchParams.roomType !== 'all') {
        availableRooms = availableRooms.filter((room: Room) => room.roomType === searchParams.roomType)
      }
      
      if (searchParams.guests > 1) {
        availableRooms = availableRooms.filter((room: Room) => room.capacity >= searchParams.guests)
      }
      
      setRooms(availableRooms)
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
                  {searchParams.checkIn && searchParams.checkOut ? 'Search Rooms' : 'Show All Rooms'}
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
            <div>
              <h2 className="text-xl font-semibold">
                Available Rooms ({(rooms || []).length})
              </h2>
              {searchParams.checkIn && searchParams.checkOut && (
                <p className="text-sm text-muted-foreground">
                  Available from {new Date(searchParams.checkIn).toLocaleDateString()} to {new Date(searchParams.checkOut).toLocaleDateString()}
                </p>
              )}
            </div>
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
                      onClick={() => handleBookRoom(room)}
                    >
                      <CreditCard className="h-4 w-4 mr-2" />
                      Book Now
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
              {searchParams.checkIn && searchParams.checkOut 
                ? 'Try adjusting your search criteria or dates'
                : 'Select check-in and check-out dates to see room availability'
              }
            </p>
            {!searchParams.checkIn || !searchParams.checkOut ? (
              <p className="text-sm text-muted-foreground mt-2">
                💡 Tip: Select dates to check real-time availability and avoid booking conflicts
              </p>
            ) : null}
          </CardContent>
        </Card>
      )}

      {/* Booking Form Modal */}
      {showBookingForm && selectedRoom && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-full max-w-md mx-4">
            <CardHeader>
              <CardTitle className="flex items-center space-x-2">
                <CreditCard className="h-5 w-5" />
                <span>Book Room {selectedRoom.number}</span>
              </CardTitle>
              <p className="text-sm text-muted-foreground">
                Complete your booking for {getRoomTypeName(selectedRoom.roomType)}
              </p>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleBookingSubmit} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm font-medium">Check-in Date</label>
                    <Input
                      type="date"
                      value={bookingData.checkIn}
                      onChange={(e) => handleBookingChange('checkIn', e.target.value)}
                      required
                    />
                  </div>
                  <div>
                    <label className="text-sm font-medium">Check-out Date</label>
                    <Input
                      type="date"
                      value={bookingData.checkOut}
                      onChange={(e) => handleBookingChange('checkOut', e.target.value)}
                      required
                    />
                  </div>
                </div>

                <div>
                  <label className="text-sm font-medium">Number of Guests</label>
                  <Input
                    type="number"
                    min="1"
                    max={selectedRoom.capacity}
                    value={bookingData.numberOfGuests}
                    onChange={(e) => handleBookingChange('numberOfGuests', parseInt(e.target.value))}
                    required
                  />
                  <p className="text-xs text-muted-foreground mt-1">
                    Maximum capacity: {selectedRoom.capacity} guests
                  </p>
                </div>

                <div>
                  <label className="text-sm font-medium">Loyalty Points to Use (Optional)</label>
                  <Input
                    type="number"
                    min="0"
                    placeholder="0"
                    value={bookingData.pointsUsed}
                    onChange={(e) => handleBookingChange('pointsUsed', parseInt(e.target.value) || 0)}
                  />
                  <p className="text-xs text-muted-foreground mt-1">
                    Enter 0 if you don't want to use loyalty points
                  </p>
                </div>

                <div className="bg-muted p-3 rounded-md">
                  {(() => {
                    const { nights, subtotal, tax, total } = calculateTotalPrice()
                    return (
                      <div className="space-y-2">
                        <div className="flex justify-between items-center">
                          <span className="text-sm font-medium">Price per night:</span>
                          <span className="text-sm">${selectedRoom.price} {selectedRoom.currency}</span>
                        </div>
                        <div className="flex justify-between items-center">
                          <span className="text-sm font-medium">Number of nights:</span>
                          <span className="text-sm">{nights} night{nights !== 1 ? 's' : ''}</span>
                        </div>
                        <div className="flex justify-between items-center">
                          <span className="text-sm font-medium">Subtotal:</span>
                          <span className="text-sm">${subtotal.toFixed(2)} {selectedRoom.currency}</span>
                        </div>
                        <div className="flex justify-between items-center">
                          <span className="text-sm font-medium">Tax (10%):</span>
                          <span className="text-sm">${tax.toFixed(2)} {selectedRoom.currency}</span>
                        </div>
                        <div className="border-t pt-2">
                          <div className="flex justify-between items-center">
                            <span className="text-lg font-bold">Total Price:</span>
                            <span className="text-lg font-bold text-primary">
                              ${total.toFixed(2)} {selectedRoom.currency}
                            </span>
                          </div>
                        </div>
                        <p className="text-xs text-muted-foreground mt-1">
                          For {bookingData.numberOfGuests} guest{bookingData.numberOfGuests > 1 ? 's' : ''} • {nights} night{nights !== 1 ? 's' : ''}
                        </p>
                      </div>
                    )
                  })()}
                </div>

                <div className="flex gap-2">
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => {
                      setShowBookingForm(false)
                      setSelectedRoom(null)
                    }}
                    className="flex-1"
                    disabled={booking}
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    className="flex-1"
                    disabled={booking}
                  >
                    {booking ? (
                      <>
                        <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                        Booking...
                      </>
                    ) : (
                      <>
                        <CheckCircle className="h-4 w-4 mr-2" />
                        Confirm Booking
                      </>
                    )}
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  )
}
