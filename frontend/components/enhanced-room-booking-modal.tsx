"use client"

import { useState, useEffect } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Badge } from "@/components/ui/badge"
import { 
  Calendar, 
  Users, 
  DollarSign, 
  Gift, 
  Star,
  Clock,
  MapPin,
  Bed
} from "lucide-react"
import { loyaltyApi, reservationApi } from "@/lib/api"
import type { Room, LoyaltyAccount } from "@/lib/api"
import { toast } from "sonner"

interface EnhancedRoomBookingModalProps {
  isOpen: boolean
  onClose: () => void
  room: Room
  checkIn: string
  checkOut: string
  numberOfGuests: number
  onBookingSuccess?: () => void
}

export function EnhancedRoomBookingModal({ 
  isOpen, 
  onClose, 
  room, 
  checkIn,
  checkOut,
  numberOfGuests,
  onBookingSuccess 
}: EnhancedRoomBookingModalProps) {
  const [isLoading, setIsLoading] = useState(false)
  const [loyaltyAccount, setLoyaltyAccount] = useState<LoyaltyAccount | null>(null)
  const [loyaltyLoading, setLoyaltyLoading] = useState(true)
  const [pointsToUse, setPointsToUse] = useState(0)
  const [formData, setFormData] = useState({
    checkIn,
    checkOut,
    numberOfGuests,
    specialRequests: ""
  })

  // Load loyalty account
  useEffect(() => {
    if (isOpen) {
      loadLoyaltyAccount()
    }
  }, [isOpen])

  const loadLoyaltyAccount = async () => {
    try {
      setLoyaltyLoading(true)
      const account = await loyaltyApi.getAccount()
      setLoyaltyAccount(account)
    } catch (error: any) {
      console.error("Failed to load loyalty account:", error)
      // Don't show error to user, just disable points functionality
      setLoyaltyAccount(null)
    } finally {
      setLoyaltyLoading(false)
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (formData.numberOfGuests > room.capacity) {
      toast.error(`Maximum capacity is ${room.capacity} guests`)
      return
    }

    if (formData.numberOfGuests < 1) {
      toast.error("At least 1 guest is required")
      return
    }

    setIsLoading(true)

    try {
      // Create reservation with loyalty points
      const reservationData = {
        roomId: room.id,
        checkIn: formData.checkIn,
        checkOut: formData.checkOut,
        numberOfGuests: formData.numberOfGuests,
        pointsUsed: pointsToUse,
        currency: room.currency
      }

      await reservationApi.createReservation(reservationData)
      
      toast.success("Room booked successfully!")
      onBookingSuccess?.()
      onClose()
      
    } catch (error: any) {
      console.error("Booking error:", error)
      toast.error(error.message || "Failed to book room. Please try again.")
    } finally {
      setIsLoading(false)
    }
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  const calculatePricing = () => {
    if (!formData.checkIn || !formData.checkOut) {
      return { nights: 0, subtotal: 0, pointsDiscount: 0, total: 0 }
    }

    const checkInDate = new Date(formData.checkIn)
    const checkOutDate = new Date(formData.checkOut)
    const nights = Math.ceil((checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24))
    
    const subtotal = nights * room.price
    const pointsDiscount = pointsToUse * 0.01 // 1 point = $0.01
    const total = Math.max(0, subtotal - pointsDiscount)
    
    return { nights, subtotal, pointsDiscount, total }
  }

  const { nights, subtotal, pointsDiscount, total } = calculatePricing()
  const maxPoints = loyaltyAccount ? Math.min(loyaltyAccount.balance, Math.floor(subtotal * 100)) : 0

  const getRoomTypeBadge = (roomType: string) => {
    const badges: Record<string, string> = {
      SINGLE: "bg-blue-100 text-blue-800 border-blue-200",
      DOUBLE: "bg-green-100 text-green-800 border-green-200",
      DELUXE: "bg-purple-100 text-purple-800 border-purple-200",
      FAMILY: "bg-orange-100 text-orange-800 border-orange-200"
    }
    return badges[roomType] || "bg-gray-100 text-gray-800 border-gray-200"
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="w-[95vw] max-w-none h-[90vh] max-h-[90vh] overflow-y-auto" style={{ width: '95vw', maxWidth: 'none' }}>
        <DialogHeader className="pb-6">
          <DialogTitle className="text-2xl font-bold text-foreground flex items-center gap-3">
            <Bed className="h-6 w-6 text-primary" />
            Book Room {room.number}
          </DialogTitle>
          <DialogDescription className="text-muted-foreground">
            Complete your room reservation with loyalty points integration
          </DialogDescription>
        </DialogHeader>

        <div className="grid grid-cols-1 xl:grid-cols-4 gap-8">
          {/* Room Details Column */}
          <div className="xl:col-span-1">
            <div className="bg-gradient-to-br from-slate-50 to-gray-50 rounded-lg p-6 border border-slate-200 h-fit">
              <div className="space-y-4">
                <div className="flex items-center gap-3">
                  <h3 className="text-xl font-bold text-foreground">Room {room.number}</h3>
                  <Badge className={`${getRoomTypeBadge(room.roomType)} text-sm px-3 py-1`}>
                    {room.roomType}
                  </Badge>
                </div>
                
                <div className="flex items-center gap-6 text-sm text-muted-foreground">
                  <div className="flex items-center gap-2">
                    <Users className="h-4 w-4" />
                    <span>Capacity: {room.capacity} guests</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <MapPin className="h-4 w-4" />
                    <span>Status: {room.status}</span>
                  </div>
                </div>

                <div className="text-right">
                  <div className="flex items-center gap-2 text-xl font-bold text-foreground">
                    <DollarSign className="h-5 w-5 text-primary" />
                    {room.price} {room.currency}
                  </div>
                  <p className="text-sm text-muted-foreground">per night</p>
                </div>

                {room.description && (
                  <p className="text-muted-foreground text-sm">{room.description}</p>
                )}

                {room.equipment && (
                  <div className="space-y-2">
                    <h4 className="font-semibold text-foreground text-sm">Amenities</h4>
                    <div className="flex flex-wrap gap-1">
                      {room.equipment.split(',').map((item, index) => (
                        <Badge key={index} variant="outline" className="text-xs px-2 py-1">
                          {item.trim()}
                        </Badge>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>

          {/* Booking Form Column */}
          <div className="xl:col-span-1">
            <div className="bg-white rounded-lg border border-slate-200 p-6 h-fit">
              <h4 className="text-lg font-semibold text-foreground mb-6 flex items-center gap-2">
                <Calendar className="h-5 w-5 text-primary" />
                Booking Details
              </h4>
              
              <form onSubmit={handleSubmit} className="space-y-6">
                {/* Date Selection */}
                <div className="space-y-4">
                  <div className="space-y-2">
                    <Label className="text-sm font-semibold text-foreground">Check-in Date</Label>
                    <Input
                      type="date"
                      value={formData.checkIn}
                      onChange={(e) => setFormData(prev => ({ ...prev, checkIn: e.target.value }))}
                      className="h-11"
                      min={new Date().toISOString().split('T')[0]}
                      required
                    />
                  </div>
                  
                  <div className="space-y-2">
                    <Label className="text-sm font-semibold text-foreground">Check-out Date</Label>
                    <Input
                      type="date"
                      value={formData.checkOut}
                      onChange={(e) => setFormData(prev => ({ ...prev, checkOut: e.target.value }))}
                      className="h-11"
                      min={formData.checkIn || new Date().toISOString().split('T')[0]}
                      required
                    />
                  </div>
                </div>

                {/* Number of Guests */}
                <div className="space-y-2">
                  <Label className="text-sm font-semibold text-foreground">Number of Guests</Label>
                  <Input
                    id="numberOfGuests"
                    type="number"
                    min="1"
                    max={room.capacity}
                    value={formData.numberOfGuests}
                    onChange={(e) => setFormData(prev => ({ 
                      ...prev, 
                      numberOfGuests: parseInt(e.target.value) || 1 
                    }))}
                    className="h-11"
                    required
                  />
                  <p className="text-sm text-muted-foreground">
                    Maximum {room.capacity} guests allowed
                  </p>
                </div>

                {/* Special Requests */}
                <div className="space-y-2">
                  <Label className="text-sm font-semibold text-foreground">Special Requests (Optional)</Label>
                  <textarea
                    id="specialRequests"
                    placeholder="Any special dietary requirements, accessibility needs, or other requests..."
                    value={formData.specialRequests}
                    onChange={(e) => setFormData(prev => ({ 
                      ...prev, 
                      specialRequests: e.target.value 
                    }))}
                    className="w-full min-h-[100px] px-3 py-2 border border-input bg-background rounded-md text-sm resize-none focus:ring-2 focus:ring-primary focus:border-transparent"
                  />
                </div>
              </form>
            </div>
          </div>

          {/* Loyalty Points Column */}
          <div className="xl:col-span-1">
            {loyaltyLoading ? (
              <div className="bg-purple-50 rounded-lg p-6 text-center border border-purple-200 h-fit">
                <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-purple-600 mx-auto mb-3"></div>
                <p className="text-sm text-muted-foreground">Loading loyalty points...</p>
              </div>
            ) : loyaltyAccount ? (
              <div className="bg-purple-50 rounded-lg p-6 border border-purple-200 h-fit">
                <div className="flex items-center gap-2 mb-4">
                  <Gift className="h-4 w-4 text-purple-600" />
                  <h3 className="font-semibold text-foreground">Loyalty Points</h3>
                </div>
                
                <div className="space-y-4">
                  <div className="flex justify-between items-center p-3 bg-white rounded border">
                    <span className="text-sm text-muted-foreground">Available:</span>
                    <span className="font-semibold text-purple-600">{loyaltyAccount.balance.toLocaleString()} points</span>
                  </div>
                  
                  <div className="space-y-2">
                    <Label className="text-sm font-medium text-foreground">
                      Use Points (Max: {maxPoints.toLocaleString()})
                    </Label>
                    <Input
                      id="pointsToUse"
                      type="number"
                      min="0"
                      max={maxPoints}
                      value={pointsToUse}
                      onChange={(e) => {
                        const value = parseInt(e.target.value) || 0
                        setPointsToUse(Math.min(Math.max(0, value), maxPoints))
                      }}
                      className="h-11 text-center"
                      placeholder="0"
                    />
                    <div className="flex justify-between items-center">
                      <span className="text-xs text-muted-foreground">0</span>
                      <Button
                        type="button"
                        variant="outline"
                        size="sm"
                        onClick={() => setPointsToUse(maxPoints)}
                        disabled={maxPoints === 0}
                        className="text-xs h-6 px-2"
                      >
                        Use All
                      </Button>
                      <span className="text-xs text-muted-foreground">{maxPoints.toLocaleString()}</span>
                    </div>
                  </div>
                  
                  <div className="bg-white rounded p-2 border text-xs text-muted-foreground">
                    <p>• 1 point = $0.01 discount</p>
                    <p>• Points used: {pointsToUse.toLocaleString()} points</p>
                  </div>
                </div>
              </div>
            ) : (
              <div className="bg-muted/50 rounded-lg p-6 text-center border h-fit">
                <Star className="h-8 w-8 text-muted-foreground mx-auto mb-2" />
                <p className="text-sm text-muted-foreground">Loyalty points not available</p>
              </div>
            )}
          </div>

          {/* Pricing & Actions Column */}
          <div className="xl:col-span-1 space-y-6">
            {/* Pricing Summary */}
            <div className="bg-slate-50 rounded-lg p-6 border">
              <h4 className="font-semibold text-foreground mb-4 flex items-center gap-2">
                <DollarSign className="h-4 w-4 text-primary" />
                Booking Summary
              </h4>
              
              <div className="space-y-3 text-sm">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Room {room.number}</span>
                  <span className="font-medium">{nights} night{nights !== 1 ? 's' : ''}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Price per night:</span>
                  <span className="font-medium">{room.price} {room.currency}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Subtotal:</span>
                  <span className="font-medium">{subtotal.toFixed(2)} {room.currency}</span>
                </div>
                {pointsDiscount > 0 && (
                  <div className="flex justify-between text-green-600">
                    <span>Points discount:</span>
                    <span className="font-semibold">-{pointsDiscount.toFixed(2)} {room.currency}</span>
                  </div>
                )}
                <div className="border-t pt-2">
                  <div className="flex justify-between font-semibold">
                    <span>Total:</span>
                    <span className="text-primary">{total.toFixed(2)} {room.currency}</span>
                  </div>
                </div>
              </div>
            </div>

            {/* Action Buttons */}
            <div className="flex gap-3">
              <Button
                type="button"
                variant="outline"
                onClick={onClose}
                className="flex-1 h-11"
                disabled={isLoading}
              >
                Cancel
              </Button>
              <Button
                onClick={handleSubmit}
                className="flex-1 h-11 bg-primary hover:bg-primary/90"
                disabled={isLoading}
              >
                {isLoading ? "Booking..." : "Confirm Booking"}
              </Button>
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  )
}
