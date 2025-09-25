"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Badge } from "@/components/ui/badge"
import { Calendar, Clock, Users, MapPin, DollarSign, X } from "lucide-react"
import { eventBookingApi, EventBookingCreateRequest } from "@/lib/api"
import { toast } from "sonner"

interface EventBookingModalProps {
  isOpen: boolean
  onClose: () => void
  event: {
    id: number
    name: string
    eventType: string
    startAt: string
    endAt: string
    price: number
    currency: string
    capacity: number
    installationName: string
    description?: string
  }
  onBookingSuccess?: () => void
}

export function EventBookingModal({ 
  isOpen, 
  onClose, 
  event, 
  onBookingSuccess 
}: EventBookingModalProps) {
  const [isLoading, setIsLoading] = useState(false)
  const [formData, setFormData] = useState({
    participants: 1,
    specialRequests: ""
  })

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (formData.participants > event.capacity) {
      toast.error(`Maximum capacity is ${event.capacity} participants`)
      return
    }

    if (formData.participants < 1) {
      toast.error("At least 1 participant is required")
      return
    }

    setIsLoading(true)

    try {
      // Create event booking without reservation (standalone event booking)
      const bookingRequest: EventBookingCreateRequest = {
        eventId: event.id,
        reservationId: null, // No reservation required for standalone event bookings
        numberOfParticipants: formData.participants,
        specialRequests: formData.specialRequests
      }

      await eventBookingApi.createBooking(bookingRequest)
      
      toast.success("Event booked successfully!")
      onBookingSuccess?.()
      onClose()
      
      // Reset form
      setFormData({
        participants: 1,
        specialRequests: ""
      })
      
    } catch (error: any) {
      console.error("Booking error:", error)
      toast.error(error.message || "Failed to book event. Please try again.")
    } finally {
      setIsLoading(false)
    }
  }

  const formatDateTime = (dateString: string) => {
    return new Date(dateString).toLocaleString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  const getEventTypeBadge = (eventType: string) => {
    const badges: Record<string, string> = {
      WEDDING: "bg-pink-100 text-pink-800 border-pink-200",
      CONFERENCE: "bg-blue-100 text-blue-800 border-blue-200",
      BIRTHDAY: "bg-yellow-100 text-yellow-800 border-yellow-200",
      CORPORATE: "bg-purple-100 text-purple-800 border-purple-200",
      SPA: "bg-green-100 text-green-800 border-green-200",
      FITNESS: "bg-orange-100 text-orange-800 border-orange-200"
    }
    return badges[eventType] || "bg-gray-100 text-gray-800 border-gray-200"
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="text-2xl font-bold text-foreground">
            Book Event
          </DialogTitle>
          <DialogDescription>
            Complete your event booking by providing the required information
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-6">
          {/* Event Details Card */}
          <div className="bg-muted/50 rounded-lg p-4 space-y-3">
            <div className="flex items-start justify-between">
              <div>
                <h3 className="text-lg font-semibold text-foreground">{event.name}</h3>
                <Badge className={`mt-1 ${getEventTypeBadge(event.eventType)}`}>
                  {event.eventType}
                </Badge>
              </div>
              <div className="text-right">
                <div className="flex items-center gap-1 text-lg font-semibold text-foreground">
                  <DollarSign className="h-4 w-4" />
                  {event.price} {event.currency}
                </div>
                <p className="text-sm text-muted-foreground">per person</p>
              </div>
            </div>

            {event.description && (
              <p className="text-muted-foreground text-sm">{event.description}</p>
            )}

            <div className="grid grid-cols-2 gap-4 text-sm">
              <div className="flex items-center gap-2 text-muted-foreground">
                <Calendar className="h-4 w-4" />
                <span>{formatDateTime(event.startAt)}</span>
              </div>
              <div className="flex items-center gap-2 text-muted-foreground">
                <Clock className="h-4 w-4" />
                <span>Duration: {Math.round((new Date(event.endAt).getTime() - new Date(event.startAt).getTime()) / (1000 * 60 * 60))}h</span>
              </div>
              <div className="flex items-center gap-2 text-muted-foreground">
                <Users className="h-4 w-4" />
                <span>Capacity: {event.capacity} people</span>
              </div>
              <div className="flex items-center gap-2 text-muted-foreground">
                <MapPin className="h-4 w-4" />
                <span>{event.installationName}</span>
              </div>
            </div>
          </div>

          {/* Booking Form */}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="participants" className="text-sm font-medium">
                Number of Participants *
              </Label>
              <Input
                id="participants"
                type="number"
                min="1"
                max={event.capacity}
                value={formData.participants}
                onChange={(e) => setFormData(prev => ({ 
                  ...prev, 
                  participants: parseInt(e.target.value) || 1 
                }))}
                className="w-full"
                required
              />
              <p className="text-xs text-muted-foreground">
                Maximum {event.capacity} participants allowed
              </p>
            </div>

            <div className="space-y-2">
              <Label htmlFor="specialRequests" className="text-sm font-medium">
                Special Requests (Optional)
              </Label>
              <Textarea
                id="specialRequests"
                placeholder="Any special dietary requirements, accessibility needs, or other requests..."
                value={formData.specialRequests}
                onChange={(e) => setFormData(prev => ({ 
                  ...prev, 
                  specialRequests: e.target.value 
                }))}
                className="min-h-[80px]"
              />
            </div>

            {/* Booking Summary */}
            <div className="bg-muted/30 rounded-lg p-4 space-y-2">
              <h4 className="font-semibold text-foreground">Booking Summary</h4>
              <div className="flex justify-between text-sm">
                <span className="text-muted-foreground">Participants:</span>
                <span className="font-medium">{formData.participants}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-muted-foreground">Price per person:</span>
                <span className="font-medium">{event.price} {event.currency}</span>
              </div>
              <div className="flex justify-between text-sm font-semibold border-t pt-2">
                <span>Total:</span>
                <span>{formData.participants * event.price} {event.currency}</span>
              </div>
            </div>

            {/* Action Buttons */}
            <div className="flex gap-3 pt-4">
              <Button
                type="button"
                variant="outline"
                onClick={onClose}
                className="flex-1"
                disabled={isLoading}
              >
                Cancel
              </Button>
              <Button
                type="submit"
                className="flex-1 bg-primary hover:bg-primary/90"
                disabled={isLoading}
              >
                {isLoading ? "Booking..." : "Confirm Booking"}
              </Button>
            </div>
          </form>
        </div>
      </DialogContent>
    </Dialog>
  )
}
