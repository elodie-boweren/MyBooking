"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Textarea } from "@/components/ui/textarea"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { ArrowLeft, MessageSquare, Star, Plus, Calendar, User } from "lucide-react"
import Link from "next/link"
import { feedbackApi, reservationApi } from "@/lib/api"
import type { Feedback, Reservation } from "@/lib/api"
import { toast } from "sonner"

export default function MyFeedbackPage() {
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([])
  const [reservations, setReservations] = useState<Reservation[]>([])
  const [loading, setLoading] = useState(true)
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)
  const [selectedReservation, setSelectedReservation] = useState<Reservation | null>(null)

  // Form state for new feedback
  const [formData, setFormData] = useState({
    rating: 5,
    comment: ""
  })

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        // Fetch user's feedback
        const feedbackData = await feedbackApi.getUserFeedbacks()
        setFeedbacks(feedbackData)
        
        // Fetch user's reservations for feedback submission
        const reservationData = await reservationApi.getUserReservations()
        setReservations(reservationData)
      } catch (error) {
        console.error('Failed to fetch feedback data:', error)
        toast.error("Failed to load feedback data")
      } finally {
        setLoading(false)
      }
    }

    fetchData()
  }, [])

  const handleSubmitFeedback = async () => {
    if (!selectedReservation) {
      toast.error("Please select a reservation")
      return
    }

    try {
      await feedbackApi.createFeedback({
        reservationId: selectedReservation.id,
        rating: formData.rating,
        comment: formData.comment
      })
      
      toast.success("Feedback submitted successfully!")
      setIsCreateDialogOpen(false)
      setFormData({ rating: 5, comment: "" })
      setSelectedReservation(null)
      
      // Refresh feedback list
      const feedbackData = await feedbackApi.getUserFeedbacks()
      setFeedbacks(feedbackData)
    } catch (error) {
      console.error('Failed to submit feedback:', error)
      toast.error("Failed to submit feedback")
    }
  }

  const getRatingStars = (rating: number) => {
    return Array.from({ length: 5 }, (_, i) => (
      <Star
        key={i}
        className={`h-4 w-4 ${
          i < rating ? "text-yellow-400 fill-current" : "text-gray-300"
        }`}
      />
    ))
  }


  const getStatusBadge = (status: string) => {
    const statusColors: { [key: string]: string } = {
      PENDING: "bg-yellow-100 text-yellow-800",
      APPROVED: "bg-green-100 text-green-800",
      REJECTED: "bg-red-100 text-red-800"
    }
    
    return (
      <Badge className={statusColors[status] || "bg-gray-100 text-gray-800"}>
        {status}
      </Badge>
    )
  }

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8 max-w-6xl">
        <div className="text-center py-8">Loading feedback data...</div>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-6xl">
      <div className="flex items-center justify-between mb-8">
        <div className="flex items-center gap-4">
          <Link href="/rooms">
            <Button variant="outline" size="sm">
              <ArrowLeft className="h-4 w-4 mr-2" />
              Back to Dashboard
            </Button>
          </Link>
          <div>
            <h1 className="text-3xl font-bold text-foreground">My Feedback</h1>
            <p className="text-muted-foreground mt-2">View and submit feedback for your stays</p>
          </div>
        </div>
        
        <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
          <DialogTrigger asChild>
            <Button>
              <Plus className="h-4 w-4 mr-2" />
              Submit Feedback
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>Submit New Feedback</DialogTitle>
              <DialogDescription>
                Share your experience to help us improve our services
              </DialogDescription>
            </DialogHeader>
            
            <div className="space-y-6">
              {/* Reservation Selection */}
              <div className="space-y-2">
                <Label htmlFor="reservation">Select Reservation</Label>
                <Select onValueChange={(value) => {
                  const reservation = reservations.find(r => r.id.toString() === value)
                  setSelectedReservation(reservation || null)
                }}>
                  <SelectTrigger>
                    <SelectValue placeholder="Choose a reservation to provide feedback for" />
                  </SelectTrigger>
                  <SelectContent>
                    {reservations.map((reservation) => (
                      <SelectItem key={reservation.id} value={reservation.id.toString()}>
                        Room {reservation.roomNumber} - {new Date(reservation.checkInDate).toLocaleDateString()} to {new Date(reservation.checkOutDate).toLocaleDateString()}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>

              {/* Rating */}
              <div className="space-y-2">
                <Label>Rating</Label>
                <div className="flex items-center gap-2">
                  {Array.from({ length: 5 }, (_, i) => (
                    <button
                      key={i}
                      type="button"
                      onClick={() => setFormData(prev => ({ ...prev, rating: i + 1 }))}
                      className="focus:outline-none"
                    >
                      <Star
                        className={`h-6 w-6 ${
                          i < formData.rating ? "text-yellow-400 fill-current" : "text-gray-300"
                        }`}
                      />
                    </button>
                  ))}
                  <span className="ml-2 text-sm text-muted-foreground">
                    {formData.rating} out of 5 stars
                  </span>
                </div>
              </div>


              {/* Comment */}
              <div className="space-y-2">
                <Label htmlFor="comment">Comments</Label>
                <Textarea
                  id="comment"
                  placeholder="Share your detailed feedback..."
                  value={formData.comment}
                  onChange={(e) => setFormData(prev => ({ ...prev, comment: e.target.value }))}
                  rows={4}
                />
              </div>

              <div className="flex justify-end gap-3">
                <Button variant="outline" onClick={() => setIsCreateDialogOpen(false)}>
                  Cancel
                </Button>
                <Button onClick={handleSubmitFeedback}>
                  Submit Feedback
                </Button>
              </div>
            </div>
          </DialogContent>
        </Dialog>
      </div>

      {/* Feedback List */}
      {feedbacks.length === 0 ? (
        <Card>
          <CardContent className="text-center py-12">
            <MessageSquare className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
            <h3 className="text-lg font-semibold mb-2">No feedback submitted yet</h3>
            <p className="text-muted-foreground mb-4">
              Share your experience to help us improve our services
            </p>
            <Button onClick={() => setIsCreateDialogOpen(true)}>
              <Plus className="h-4 w-4 mr-2" />
              Submit Your First Feedback
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-4">
          {feedbacks.map((feedback) => (
            <Card key={feedback.id}>
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="space-y-2">
                    <div className="flex items-center gap-2">
                      <h3 className="font-semibold">Room {feedback.reservation?.roomNumber}</h3>
                      {getStatusBadge(feedback.status)}
                    </div>
                    <div className="flex items-center gap-2 text-sm text-muted-foreground">
                      <Calendar className="h-4 w-4" />
                      {new Date(feedback.createdAt).toLocaleDateString()}
                      <User className="h-4 w-4 ml-4" />
                      {feedback.reservation?.clientName}
                    </div>
                  </div>
                  <div className="flex items-center gap-1">
                    {getRatingStars(feedback.rating)}
                  </div>
                </div>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground mb-4">{feedback.comment}</p>
                
                {feedback.reply && (
                  <div className="bg-muted/50 p-4 rounded-lg">
                    <div className="flex items-center gap-2 mb-2">
                      <MessageSquare className="h-4 w-4" />
                      <span className="font-medium text-sm">Hotel Response</span>
                    </div>
                    <p className="text-sm">{feedback.reply}</p>
                    <p className="text-xs text-muted-foreground mt-2">
                      Replied on {new Date(feedback.updatedAt).toLocaleDateString()}
                    </p>
                  </div>
                )}
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
