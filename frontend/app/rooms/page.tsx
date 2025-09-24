"use client"

import { RoomSearch } from '@/components/room-search'
import { ProtectedRoute } from '@/components/protected-route'

export default function RoomsPage() {
  const handleRoomSelect = (room: any) => {
    // TODO: Implement room selection and booking flow
    console.log('Selected room:', room)
    alert(`Room ${room.number} selected! Booking functionality will be implemented next.`)
  }

  return (
    <ProtectedRoute requiredRole="CLIENT">
      <div className="min-h-screen bg-background">
        <div className="container mx-auto px-4 py-8">
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-foreground mb-2">Find Your Perfect Room</h1>
            <p className="text-muted-foreground">
              Search and book from our selection of comfortable and luxurious rooms
            </p>
          </div>
          
          <RoomSearch onRoomSelect={handleRoomSelect} />
        </div>
      </div>
    </ProtectedRoute>
  )
}