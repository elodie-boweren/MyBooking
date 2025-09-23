"use client"

import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Separator } from "@/components/ui/separator"
import { CalendarDays, Users, MapPin, Mail, User, MessageSquare } from "lucide-react"
import { format } from "date-fns"

interface CalendarBooking {
  id: string
  roomId: string
  roomName: string
  roomFloor: number
  title: string
  description?: string
  startTime: string
  endTime: string
  date: string
  userName: string
  userEmail: string
  attendees: number
  status: "confirmed" | "pending" | "cancelled"
  color: string
}

interface BookingDetailsProps {
  booking: CalendarBooking | null
  isOpen: boolean
  onClose: () => void
}

export function BookingDetails({ booking, isOpen, onClose }: BookingDetailsProps) {
  if (!booking) return null

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "confirmed":
        return <Badge className="bg-green-100 text-green-800">Confirmed</Badge>
      case "pending":
        return <Badge variant="secondary">Pending Approval</Badge>
      case "cancelled":
        return <Badge variant="destructive">Cancelled</Badge>
      default:
        return <Badge variant="outline">{status}</Badge>
    }
  }

  const formatDate = (dateStr: string) => {
    return format(new Date(dateStr), "EEEE, MMMM dd, yyyy")
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <div className="flex items-center justify-between">
            <DialogTitle className="text-xl">{booking.title}</DialogTitle>
            {getStatusBadge(booking.status)}
          </div>
          <DialogDescription>Booking details and information</DialogDescription>
        </DialogHeader>

        <div className="space-y-6">
          {/* Date and Time */}
          <div className="space-y-3">
            <div className="flex items-center space-x-3">
              <CalendarDays className="h-5 w-5 text-muted-foreground" />
              <div>
                <p className="font-medium">{formatDate(booking.date)}</p>
                <p className="text-sm text-muted-foreground">
                  {booking.startTime} - {booking.endTime}
                </p>
              </div>
            </div>

            <div className="flex items-center space-x-3">
              <MapPin className="h-5 w-5 text-muted-foreground" />
              <div>
                <p className="font-medium">{booking.roomName}</p>
                <p className="text-sm text-muted-foreground">Floor {booking.roomFloor}</p>
              </div>
            </div>

            <div className="flex items-center space-x-3">
              <Users className="h-5 w-5 text-muted-foreground" />
              <div>
                <p className="font-medium">{booking.attendees} attendees</p>
                <p className="text-sm text-muted-foreground">Expected participants</p>
              </div>
            </div>
          </div>

          <Separator />

          {/* Organizer */}
          <div className="space-y-3">
            <h4 className="font-medium flex items-center">
              <User className="h-4 w-4 mr-2" />
              Organizer
            </h4>
            <div className="pl-6">
              <p className="font-medium">{booking.userName}</p>
              <p className="text-sm text-muted-foreground flex items-center">
                <Mail className="h-3 w-3 mr-1" />
                {booking.userEmail}
              </p>
            </div>
          </div>

          {/* Description */}
          {booking.description && (
            <>
              <Separator />
              <div className="space-y-3">
                <h4 className="font-medium flex items-center">
                  <MessageSquare className="h-4 w-4 mr-2" />
                  Description
                </h4>
                <p className="text-sm text-muted-foreground pl-6">{booking.description}</p>
              </div>
            </>
          )}

          <Separator />

          {/* Actions */}
          <div className="flex justify-end space-x-2">
            <Button variant="outline" onClick={onClose}>
              Close
            </Button>
            {booking.status === "confirmed" && (
              <Button variant="outline">
                <Mail className="h-4 w-4 mr-2" />
                Send Reminder
              </Button>
            )}
          </div>
        </div>
      </DialogContent>
    </Dialog>
  )
}
