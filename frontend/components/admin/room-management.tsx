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
  Plus,
  Edit,
  Trash2,
  Eye,
  Bed,
  Users,
  DollarSign,
  Calendar,
  AlertCircle
} from 'lucide-react'
import { COMPONENT_TEMPLATES } from '@/lib/style-constants'
import { apiClient, API_ENDPOINTS, Room, CreateRoomRequest, AddRoomPhotoRequest } from '@/lib/api'

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080/api"

export function RoomManagement() {
  const [rooms, setRooms] = useState<Room[]>([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedType, setSelectedType] = useState<string>('all')
  const [selectedStatus, setSelectedStatus] = useState<string>('all')
  const [showCreateForm, setShowCreateForm] = useState(false)
  const [showEditForm, setShowEditForm] = useState(false)
  const [creating, setCreating] = useState(false)
  const [editing, setEditing] = useState(false)
  const [showViewModal, setShowViewModal] = useState(false)
  const [selectedRoom, setSelectedRoom] = useState<Room | null>(null)
  const [currentImageIndex, setCurrentImageIndex] = useState(0)
  const [formData, setFormData] = useState<CreateRoomRequest>({
    number: '',
    roomType: 'SINGLE',
    capacity: 1,
    price: 0,
    currency: 'EUR',
    description: ''
  })
  const [editFormData, setEditFormData] = useState<CreateRoomRequest>({
    number: '',
    roomType: 'SINGLE',
    capacity: 1,
    price: 0,
    currency: 'EUR',
    description: ''
  })

  useEffect(() => {
    fetchRooms()
  }, [])

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

  // Helper function to get room gallery images - 12 UNIQUE HIGH-QUALITY IMAGES (3 per room type)
  const getRoomGallery = (roomType: string): string[] => {
    const galleries = {
      SINGLE: [
        'https://images.pexels.com/photos/271624/pexels-photo-271624.jpeg?w=800&h=600&fit=crop&auto=format&q=95', // Single room main view
        'https://images.pexels.com/photos/164595/pexels-photo-164595.jpeg?w=800&h=600&fit=crop&auto=format&q=95', // Single room workspace
        'https://images.pexels.com/photos/90317/pexels-photo-90317.jpeg?w=800&h=600&fit=crop&auto=format&q=95'   // Single room bathroom
      ],
      DOUBLE: [
        'https://images.pexels.com/photos/271743/pexels-photo-271743.jpeg?w=800&h=600&fit=crop&auto=format&q=95', // Double room main view
        'https://images.pexels.com/photos/279746/pexels-photo-279746.jpeg?w=800&h=600&fit=crop&auto=format&q=95', // Double room seating area
        'https://images.pexels.com/photos/237371/pexels-photo-237371.jpeg?w=800&h=600&fit=crop&auto=format&q=95'  // Double room balcony
      ],
      DELUXE: [
        'https://images.pexels.com/photos/210265/pexels-photo-210265.jpeg?w=800&h=600&fit=crop&auto=format&q=95', // Deluxe suite main area
        'https://images.pexels.com/photos/271618/pexels-photo-271618.jpeg?w=800&h=600&fit=crop&auto=format&q=95', // Deluxe suite bedroom
        'https://images.pexels.com/photos/276671/pexels-photo-276671.jpeg?w=800&h=600&fit=crop&auto=format&q=95'  // Deluxe suite living
      ],
      FAMILY: [
        'https://images.pexels.com/photos/271619/pexels-photo-271619.jpeg?w=800&h=600&fit=crop&auto=format&q=95', // Family room main area
        'https://images.pexels.com/photos/276724/pexels-photo-276724.jpeg?w=800&h=600&fit=crop&auto=format&q=95', // Family room kids area
        'https://images.pexels.com/photos/271816/pexels-photo-271816.jpeg?w=800&h=600&fit=crop&auto=format&q=95'  // Family room dining
      ]
    }
    return galleries[roomType as keyof typeof galleries] || galleries.SINGLE
  }

  // Helper function to add room photo
  const addRoomPhoto = async (roomId: number, roomType: string): Promise<void> => {
    try {
      const photoData: AddRoomPhotoRequest = {
        photoUrl: getPlaceholderImage(roomType),
        caption: `${roomType} room - Auto-generated placeholder image`,
        isPrimary: true
      }
      await apiClient.post(API_ENDPOINTS.ROOM_PHOTOS.ADD(roomId.toString()), photoData)
    } catch (error) {
      console.error('Failed to add room photo:', error)
      // Don't throw error - room creation should succeed even if photo fails
    }
  }

  const fetchRooms = async () => {
    try {
      setLoading(true)
      const response = await apiClient.get<any>(API_ENDPOINTS.ADMIN_ROOMS.ALL)
      
      // Handle paginated response from Spring Boot
      const roomsData = response.content || response
      setRooms(Array.isArray(roomsData) ? roomsData : [])
    } catch (error) {
      console.error('Failed to fetch rooms:', error)
      // Fallback to mock data if API fails
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
          status: 'OCCUPIED',
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
          status: 'OUT_OF_SERVICE',
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

  const filteredRooms = (rooms || []).filter(room => {
    const matchesSearch = room.number.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         room.description?.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesType = selectedType === 'all' || room.roomType === selectedType
    const matchesStatus = selectedStatus === 'all' || room.status === selectedStatus
    return matchesSearch && matchesType && matchesStatus
  })

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'AVAILABLE':
        return <Bed className="h-4 w-4 text-green-600" />
      case 'OCCUPIED':
        return <Users className="h-4 w-4 text-blue-600" />
      case 'OUT_OF_SERVICE':
        return <AlertCircle className="h-4 w-4 text-red-600" />
      default:
        return <Bed className="h-4 w-4 text-gray-600" />
    }
  }

  const getStatusBadgeVariant = (status: string) => {
    switch (status) {
      case 'AVAILABLE':
        return 'default'
      case 'OCCUPIED':
        return 'secondary'
      case 'OUT_OF_SERVICE':
        return 'destructive'
      default:
        return 'outline'
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

  const handleEditRoom = (roomId: number) => {
    const room = rooms.find(r => r.id === roomId)
    if (room) {
      setEditFormData({
        number: room.number,
        roomType: room.roomType,
        capacity: room.capacity,
        price: room.price,
        currency: room.currency,
        description: room.description || ''
      })
      setSelectedRoom(room)
      setShowEditForm(true)
    }
  }

  const handleDeleteRoom = async (roomId: number) => {
    const room = rooms.find(r => r.id === roomId)
    if (!room) return

    const confirmMessage = `Are you sure you want to delete Room ${room.number}?\n\nThis will mark the room as "Out of Service" and it will no longer be available for bookings.`
    
    if (!confirm(confirmMessage)) {
      return
    }

    try {
      // Make DELETE request with proper error handling
      const response = await fetch(`${API_BASE_URL}/rooms/${roomId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
        },
      })

      if (!response.ok) {
        const errorText = await response.text()
        throw new Error(`HTTP ${response.status}: ${errorText}`)
      }
      
      // Show success message
      alert(`Room ${room.number} has been deleted successfully!`)
      
      // Refresh the room list
      fetchRooms()
    } catch (error: any) {
      console.error('Failed to delete room:', error)
      const errorMessage = error?.message || 'Network error occurred'
      alert(`Failed to delete Room ${room.number}: ${errorMessage}`)
    }
  }

  const handleRestoreRoom = async (roomId: number) => {
    const room = rooms.find(r => r.id === roomId)
    if (!room) return

    const confirmMessage = `Are you sure you want to restore Room ${room.number}?\n\nThis will make the room available for bookings again.`
    
    if (!confirm(confirmMessage)) {
      return
    }

    try {
      // Update room status to AVAILABLE
      const roomUpdateData = {
        id: room.id,
        number: room.number,
        roomType: room.roomType,
        capacity: room.capacity,
        price: room.price,
        currency: room.currency,
        status: 'AVAILABLE', // Restore to available
        description: room.description,
        equipment: room.equipment,
        createdAt: room.createdAt,
        updatedAt: new Date().toISOString()
      }

      // Update the room
      await apiClient.put(API_ENDPOINTS.ADMIN_ROOMS.UPDATE(roomId.toString()), roomUpdateData)

      // Show success message
      alert(`Room ${room.number} has been restored successfully!`)
      
      // Refresh the room list
      fetchRooms()
    } catch (error: any) {
      console.error('Failed to restore room:', error)
      const errorMessage = error?.response?.data?.message || error?.message || 'Unknown error'
      alert(`Failed to restore Room ${room.number}: ${errorMessage}`)
    }
  }

  const handleCreateRoom = async (e: React.FormEvent) => {
    e.preventDefault()
    if (creating) return

    try {
      setCreating(true)
      
      // Create the room
      const newRoom = await apiClient.post<Room>(API_ENDPOINTS.ADMIN_ROOMS.CREATE, formData)
      
      // Add placeholder image
      await addRoomPhoto(newRoom.id, formData.roomType)
      
      // Reset form and close modal
      setFormData({
        number: '',
        roomType: 'SINGLE',
        capacity: 1,
        price: 0,
        currency: 'EUR',
        description: ''
      })
      setShowCreateForm(false)
      
      // Refresh room list
      fetchRooms()
      
      alert('Room created successfully!')
    } catch (error) {
      console.error('Failed to create room:', error)
      alert('Failed to create room. Please try again.')
    } finally {
      setCreating(false)
    }
  }

  const handleFormChange = (field: keyof CreateRoomRequest, value: string | number) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }))
  }

  const handleEditFormChange = (field: keyof CreateRoomRequest, value: string | number) => {
    setEditFormData(prev => ({
      ...prev,
      [field]: value
    }))
  }

  const handleUpdateRoom = async (e: React.FormEvent) => {
    e.preventDefault()
    if (editing || !selectedRoom) return

    try {
      setEditing(true)

      // Create Room object with all required fields
      const roomUpdateData = {
        id: selectedRoom.id,
        number: editFormData.number,
        roomType: editFormData.roomType,
        capacity: editFormData.capacity,
        price: editFormData.price,
        currency: editFormData.currency,
        status: selectedRoom.status, // Keep existing status
        description: editFormData.description,
        equipment: selectedRoom.equipment, // Keep existing equipment
        createdAt: selectedRoom.createdAt,
        updatedAt: new Date().toISOString()
      }

      // Update the room
      await apiClient.put(API_ENDPOINTS.ADMIN_ROOMS.UPDATE(selectedRoom.id.toString()), roomUpdateData)

      // Reset form and close modal
      setEditFormData({
        number: '',
        roomType: 'SINGLE',
        capacity: 1,
        price: 0,
        currency: 'EUR',
        description: ''
      })
      setShowEditForm(false)
      setSelectedRoom(null)

      // Refresh room list
      fetchRooms()

      alert('Room updated successfully!')
    } catch (error: any) {
      console.error('Failed to update room:', error)
      const errorMessage = error?.response?.data?.message || error?.message || 'Unknown error'
      alert(`Failed to update room: ${errorMessage}`)
    } finally {
      setEditing(false)
    }
  }

  const handleViewRoom = (roomId: number) => {
    const room = rooms.find(r => r.id === roomId)
    if (room) {
      setSelectedRoom(room)
      setCurrentImageIndex(0)
      setShowViewModal(true)
    }
  }

  if (loading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div className="h-8 bg-muted rounded w-1/4 animate-pulse"></div>
          <div className="h-10 bg-muted rounded w-32 animate-pulse"></div>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[...Array(6)].map((_, i) => (
            <Card key={i} className="animate-pulse">
              <CardHeader>
                <div className="h-4 bg-muted rounded w-3/4"></div>
              </CardHeader>
              <CardContent>
                <div className="space-y-2">
                  <div className="h-3 bg-muted rounded w-full"></div>
                  <div className="h-3 bg-muted rounded w-2/3"></div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-foreground">Room Management</h1>
          <p className="text-muted-foreground">
            Manage hotel rooms, availability, and pricing
          </p>
        </div>
        <Button onClick={() => setShowCreateForm(true)}>
              <Plus className="h-4 w-4 mr-2" />
              Add Room
            </Button>
      </div>

      {/* Filters */}
      <Card>
        <CardContent className="pt-6">
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                <Input
                  placeholder="Search rooms by number or description..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div className="flex gap-2">
              <select
                value={selectedType}
                onChange={(e) => setSelectedType(e.target.value)}
                className="px-3 py-2 border border-border rounded-md bg-background text-foreground"
              >
                <option value="all">All Types</option>
                <option value="SINGLE">Single</option>
                <option value="DOUBLE">Double</option>
                <option value="DELUXE">Deluxe</option>
                <option value="FAMILY">Family</option>
              </select>
              <select
                value={selectedStatus}
                onChange={(e) => setSelectedStatus(e.target.value)}
                className="px-3 py-2 border border-border rounded-md bg-background text-foreground"
              >
                <option value="all">All Status</option>
                <option value="AVAILABLE">Available</option>
                <option value="OCCUPIED">Occupied</option>
                <option value="OUT_OF_SERVICE">Out of Service</option>
              </select>
              <Button variant="outline">
                <Filter className="h-4 w-4 mr-2" />
                Filter
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Rooms Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredRooms.map((room) => (
          <Card key={room.id} className={`${COMPONENT_TEMPLATES.cardHover} ${room.status === 'OUT_OF_SERVICE' ? 'border-red-200 bg-red-50/30' : ''}`}>
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
                <Badge variant={getStatusBadgeVariant(room.status)}>
                  {room.status}
                </Badge>
              </div>
            </div>
            <CardHeader className="pb-3">
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  <div className="text-2xl">{getRoomTypeIcon(room.roomType)}</div>
                  <div>
                    <CardTitle className={`text-lg ${room.status === 'OUT_OF_SERVICE' ? 'text-red-600' : ''}`}>
                      Room {room.number}
                      {room.status === 'OUT_OF_SERVICE' && <span className="ml-2 text-xs text-red-500">(Deleted)</span>}
                    </CardTitle>
                    <p className="text-sm text-muted-foreground capitalize">
                      {room.roomType.toLowerCase()} • {room.capacity} guest{room.capacity > 1 ? 's' : ''}
                    </p>
                  </div>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-muted-foreground">Price:</span>
                  <span className="text-lg font-semibold">
                    ${room.price} {room.currency}
                  </span>
                </div>
                <div className="flex items-center space-x-2">
                  <span className="text-sm text-muted-foreground">Status:</span>
                  <div className="flex items-center space-x-1">
                    {getStatusIcon(room.status)}
                    <span className="text-sm capitalize">{room.status.toLowerCase()}</span>
                  </div>
                </div>
                {room.description && (
                  <div className="text-sm text-muted-foreground">
                    {room.description}
                  </div>
                )}
                {room.equipment && (
                  <div className="text-xs text-muted-foreground">
                    <strong>Equipment:</strong> {room.equipment}
                  </div>
                )}
              </div>
              <div className="flex gap-2 mt-4">
                <Button 
                  variant="outline" 
                  size="sm" 
                  className="flex-1"
                  onClick={() => handleViewRoom(room.id)}
                >
                  <Eye className="h-4 w-4 mr-2" />
                  View
                </Button>
                {room.status === 'OUT_OF_SERVICE' ? (
                  <Button 
                    variant="outline" 
                    size="sm" 
                    className="flex-1 text-green-600 hover:text-green-700"
                    onClick={() => handleRestoreRoom(room.id)}
                  >
                    <AlertCircle className="h-4 w-4 mr-2" />
                    Restore
                  </Button>
                ) : (
                  <Button 
                    variant="outline" 
                    size="sm" 
                    className="flex-1"
                    onClick={() => handleEditRoom(room.id)}
                  >
                    <Edit className="h-4 w-4 mr-2" />
                    Edit
                  </Button>
                )}
                {room.status !== 'OUT_OF_SERVICE' && (
                  <Button 
                    variant="outline"
                    size="sm" 
                    className="flex-1"
                    onClick={() => handleDeleteRoom(room.id)}
                  >
                    <Trash2 className="h-4 w-4 mr-2" />
                    Delete
                  </Button>
                )}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Empty State */}
      {filteredRooms.length === 0 && (
        <Card>
          <CardContent className="text-center py-8">
            <Building2 className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <h3 className="text-lg font-medium text-foreground mb-2">No rooms found</h3>
            <p className="text-muted-foreground">
              {searchTerm || selectedType !== 'all' || selectedStatus !== 'all'
                ? 'Try adjusting your search or filter criteria'
                : 'No rooms have been created yet'
              }
            </p>
          </CardContent>
        </Card>
      )}

      {/* Create Room Form Modal */}
      {showCreateForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-full max-w-md mx-4">
            <CardHeader>
              <CardTitle>Create New Room</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleCreateRoom} className="space-y-4">
                <div>
                  <label className="text-sm font-medium">Room Number</label>
                  <Input
                    value={formData.number}
                    onChange={(e) => handleFormChange('number', e.target.value)}
                    placeholder="e.g., 101"
                    required
                  />
                </div>

                <div>
                  <label className="text-sm font-medium">Room Type</label>
                  <select
                    value={formData.roomType}
                    onChange={(e) => handleFormChange('roomType', e.target.value)}
                    className="w-full p-2 border rounded-md"
                    required
                  >
                    <option value="SINGLE">Single</option>
                    <option value="DOUBLE">Double</option>
                    <option value="DELUXE">Deluxe</option>
                    <option value="FAMILY">Family</option>
                  </select>
              </div>

                <div>
                  <label className="text-sm font-medium">Capacity</label>
                  <Input
                    type="number"
                    min="1"
                    max="10"
                    value={formData.capacity}
                    onChange={(e) => handleFormChange('capacity', parseInt(e.target.value))}
                    required
                  />
                </div>

                <div>
                  <label className="text-sm font-medium">Price (€)</label>
                  <Input
                    type="number"
                    step="0.01"
                    min="0"
                    value={formData.price}
                    onChange={(e) => handleFormChange('price', parseFloat(e.target.value))}
                    placeholder="0.00"
                    required
                  />
                </div>

                <div>
                  <label className="text-sm font-medium">Currency</label>
                  <select
                    value={formData.currency}
                    onChange={(e) => handleFormChange('currency', e.target.value)}
                    className="w-full p-2 border rounded-md"
                    required
                  >
                    <option value="EUR">EUR</option>
                    <option value="USD">USD</option>
                    <option value="GBP">GBP</option>
                  </select>
                </div>

                <div>
                  <label className="text-sm font-medium">Description</label>
                  <textarea
                    value={formData.description}
                    onChange={(e) => handleFormChange('description', e.target.value)}
                    placeholder="Room description..."
                    className="w-full p-2 border rounded-md h-20"
                  />
                </div>

                <div className="flex gap-2">
                  <Button 
                    type="button"
                    variant="outline" 
                    onClick={() => setShowCreateForm(false)}
                    className="flex-1"
                    disabled={creating}
                  >
                    Cancel
                  </Button>
                  <Button 
                    type="submit"
                    className="flex-1"
                    disabled={creating}
                  >
                    {creating ? 'Creating...' : 'Create Room'}
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        </div>
      )}

      {/* Edit Room Form Modal */}
      {showEditForm && selectedRoom && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-full max-w-md mx-4">
            <CardHeader>
              <CardTitle>Edit Room {selectedRoom.number}</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleUpdateRoom} className="space-y-4">
                <div>
                  <label className="text-sm font-medium">Room Number</label>
                  <Input
                    value={editFormData.number}
                    onChange={(e) => handleEditFormChange('number', e.target.value)}
                    placeholder="e.g., 101"
                    required
                  />
                </div>

                <div>
                  <label className="text-sm font-medium">Room Type</label>
                  <select
                    value={editFormData.roomType}
                    onChange={(e) => handleEditFormChange('roomType', e.target.value)}
                    className="w-full p-2 border rounded-md"
                    required
                  >
                    <option value="SINGLE">Single</option>
                    <option value="DOUBLE">Double</option>
                    <option value="DELUXE">Deluxe</option>
                    <option value="FAMILY">Family</option>
                  </select>
                </div>

                <div>
                  <label className="text-sm font-medium">Capacity</label>
                  <Input
                    type="number"
                    min="1"
                    max="10"
                    value={editFormData.capacity}
                    onChange={(e) => handleEditFormChange('capacity', parseInt(e.target.value))}
                    required
                  />
                </div>

                <div>
                  <label className="text-sm font-medium">Price (€)</label>
                  <Input
                    type="number"
                    step="0.01"
                    min="0"
                    value={editFormData.price}
                    onChange={(e) => handleEditFormChange('price', parseFloat(e.target.value))}
                    placeholder="0.00"
                    required
                  />
                </div>

                <div>
                  <label className="text-sm font-medium">Currency</label>
                  <select
                    value={editFormData.currency}
                    onChange={(e) => handleEditFormChange('currency', e.target.value)}
                    className="w-full p-2 border rounded-md"
                    required
                  >
                    <option value="EUR">EUR</option>
                    <option value="USD">USD</option>
                    <option value="GBP">GBP</option>
                  </select>
                </div>

                <div>
                  <label className="text-sm font-medium">Description</label>
                  <textarea
                    value={editFormData.description}
                    onChange={(e) => handleEditFormChange('description', e.target.value)}
                    placeholder="Room description..."
                    className="w-full p-2 border rounded-md h-20"
                  />
                </div>

                <div className="flex gap-2">
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => {
                      setShowEditForm(false)
                      setSelectedRoom(null)
                    }}
                    className="flex-1"
                    disabled={editing}
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    className="flex-1"
                    disabled={editing}
                  >
                    {editing ? 'Updating...' : 'Update Room'}
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        </div>
      )}

      {/* Room View Modal with Gallery */}
      {showViewModal && selectedRoom && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <Card className="w-full max-w-4xl max-h-[90vh] overflow-hidden">
            <CardHeader className="pb-3">
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle className="text-2xl">Room {selectedRoom.number}</CardTitle>
                  <p className="text-muted-foreground capitalize">
                    {selectedRoom.roomType.toLowerCase()} • {selectedRoom.capacity} guest{selectedRoom.capacity > 1 ? 's' : ''}
                  </p>
                </div>
                <Button 
                  variant="outline" 
                  onClick={() => setShowViewModal(false)}
                >
                  Close
                </Button>
              </div>
            </CardHeader>
            <CardContent className="space-y-6">
              {/* Main Image Display */}
              <div className="relative">
                <img
                  src={getRoomGallery(selectedRoom.roomType)[currentImageIndex]}
                  alt={`Room ${selectedRoom.number} - Image ${currentImageIndex + 1}`}
                  className="w-full h-96 object-cover rounded-lg"
                />
                {/* Navigation arrows */}
                {getRoomGallery(selectedRoom.roomType).length > 1 && (
                  <>
                    <Button
                      variant="outline"
                      size="sm"
                      className="absolute left-2 top-1/2 transform -translate-y-1/2"
                      onClick={() => setCurrentImageIndex(prev => 
                        prev === 0 ? getRoomGallery(selectedRoom.roomType).length - 1 : prev - 1
                      )}
                    >
                      ←
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      className="absolute right-2 top-1/2 transform -translate-y-1/2"
                      onClick={() => setCurrentImageIndex(prev => 
                        prev === getRoomGallery(selectedRoom.roomType).length - 1 ? 0 : prev + 1
                      )}
                    >
                      →
                    </Button>
                  </>
                )}
                {/* Image counter */}
                <div className="absolute bottom-2 right-2 bg-black bg-opacity-50 text-white px-2 py-1 rounded text-sm">
                  {currentImageIndex + 1} / {getRoomGallery(selectedRoom.roomType).length}
                </div>
              </div>

              {/* Thumbnail Gallery */}
              {getRoomGallery(selectedRoom.roomType).length > 1 && (
                <div className="flex gap-2 overflow-x-auto">
                  {getRoomGallery(selectedRoom.roomType).map((image, index) => (
                    <button
                      key={index}
                      onClick={() => setCurrentImageIndex(index)}
                      className={`flex-shrink-0 w-20 h-16 rounded overflow-hidden border-2 ${
                        currentImageIndex === index ? 'border-primary' : 'border-transparent'
                      }`}
                    >
                      <img
                        src={image}
                        alt={`Thumbnail ${index + 1}`}
                        className="w-full h-full object-cover"
                      />
                    </button>
                  ))}
                </div>
              )}

              {/* Room Details */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-4">
              <div className="flex items-center justify-between">
                    <span className="text-sm text-muted-foreground">Price per night:</span>
                    <span className="text-2xl font-bold text-primary">
                      ${selectedRoom.price} {selectedRoom.currency}
                    </span>
                  </div>
                  <div className="flex items-center space-x-2">
                    <span className="text-sm text-muted-foreground">Status:</span>
                    <Badge variant={getStatusBadgeVariant(selectedRoom.status)}>
                      {selectedRoom.status}
                    </Badge>
                  </div>
                  <div className="flex items-center space-x-2">
                    <Users className="h-4 w-4 text-muted-foreground" />
                    <span className="text-sm">{selectedRoom.capacity} guest{selectedRoom.capacity > 1 ? 's' : ''}</span>
                  </div>
                </div>
                <div className="space-y-4">
                  {selectedRoom.description && (
                    <div>
                      <h4 className="font-medium mb-2">Description</h4>
                      <p className="text-sm text-muted-foreground">{selectedRoom.description}</p>
                    </div>
                  )}
                  {selectedRoom.equipment && (
                    <div>
                      <h4 className="font-medium mb-2">Equipment</h4>
                      <p className="text-sm text-muted-foreground">{selectedRoom.equipment}</p>
                  </div>
                  )}
                </div>
              </div>
            </CardContent>
          </Card>
      </div>
      )}
    </div>
  )
}