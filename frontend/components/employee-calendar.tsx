"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Card, CardContent } from "@/components/ui/card"
import { Textarea } from "@/components/ui/textarea"
import { Input } from "@/components/ui/input"
import {
  ChevronLeft,
  ChevronRight,
  Clock,
  Users,
  BookOpen,
  Camera,
  MessageSquare,
  Send,
  Paperclip,
  CheckCircle,
  X,
} from "lucide-react"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"

interface TaskReply {
  id: string
  content: string
  createdAt: string
  attachments?: string[]
}

interface CalendarEvent {
  id: string
  title: string
  type: "shift" | "task" | "training"
  startTime: string
  endTime: string
  description?: string
  location?: string
  assignedBy?: string
  status?: "pending" | "completed" | "in-progress"
  canReply?: boolean
  replies?: TaskReply[]
}

const mockEvents: CalendarEvent[] = [
  {
    id: "1",
    title: "Morning Shift",
    type: "shift",
    startTime: "09:00",
    endTime: "17:00",
    location: "Main Floor",
    status: "in-progress",
  },
  {
    id: "2",
    title: "Equipment Maintenance Check",
    type: "task",
    startTime: "10:30",
    endTime: "11:30",
    description:
      "Perform routine maintenance on conference room AV equipment. Check all connections, test projectors, and ensure audio systems are functioning properly.",
    assignedBy: "Sarah Johnson",
    status: "pending",
    canReply: true,
    replies: [
      {
        id: "r1",
        content: "Started the maintenance check. All projectors are working fine. Will test audio systems next.",
        createdAt: "2024-01-15T10:45:00Z",
        attachments: ["maintenance-checklist.jpg"],
      },
    ],
  },
  {
    id: "3",
    title: "Safety Training Session",
    type: "training",
    startTime: "14:00",
    endTime: "15:30",
    location: "Training Room B",
    description: "Updated safety protocols and emergency procedures",
    status: "pending",
  },
  {
    id: "4",
    title: "Room Setup - Board Meeting",
    type: "task",
    startTime: "16:00",
    endTime: "16:30",
    description:
      "Set up Executive Conference Room for board meeting. Arrange seating for 12 people, test presentation equipment, and prepare refreshment station.",
    assignedBy: "Mike Chen",
    status: "pending",
    canReply: true,
    replies: [],
  },
]

export default function EmployeeCalendar() {
  const [selectedDate, setSelectedDate] = useState(new Date())
  const [selectedEvent, setSelectedEvent] = useState<CalendarEvent | null>(null)
  const [showEventDialog, setShowEventDialog] = useState(false)
  const [events, setEvents] = useState<CalendarEvent[]>(mockEvents)
  const [replyText, setReplyText] = useState("")
  const [attachments, setAttachments] = useState<File[]>([])

  const getEventColor = (type: CalendarEvent["type"]) => {
    switch (type) {
      case "shift":
        return "bg-blue-100 text-blue-800 border-blue-200"
      case "task":
        return "bg-green-100 text-green-800 border-green-200"
      case "training":
        return "bg-red-100 text-red-800 border-red-200"
      default:
        return "bg-gray-100 text-gray-800 border-gray-200"
    }
  }

  const getEventIcon = (type: CalendarEvent["type"]) => {
    switch (type) {
      case "shift":
        return <Clock className="h-4 w-4" />
      case "task":
        return <Users className="h-4 w-4" />
      case "training":
        return <BookOpen className="h-4 w-4" />
      default:
        return null
    }
  }

  const handleEventClick = (event: CalendarEvent) => {
    setSelectedEvent(event)
    setShowEventDialog(true)
  }

  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      setAttachments((prev) => [...prev, ...Array.from(e.target.files!)])
    }
  }

  const removeAttachment = (index: number) => {
    setAttachments((prev) => prev.filter((_, i) => i !== index))
  }

  const handleSubmitReply = () => {
    if (!selectedEvent || !replyText.trim()) return

    const newReply: TaskReply = {
      id: `reply_${Date.now()}`,
      content: replyText.trim(),
      createdAt: new Date().toISOString(),
      attachments: attachments.map((file) => file.name),
    }

    setEvents((prev) =>
      prev.map((event) =>
        event.id === selectedEvent.id ? { ...event, replies: [...(event.replies || []), newReply] } : event,
      ),
    )

    setReplyText("")
    setAttachments([])

    // Update selected event to show new reply
    setSelectedEvent((prev) =>
      prev
        ? {
            ...prev,
            replies: [...(prev.replies || []), newReply],
          }
        : null,
    )
  }

  const markTaskComplete = () => {
    if (!selectedEvent) return

    setEvents((prev) =>
      prev.map((event) => (event.id === selectedEvent.id ? { ...event, status: "completed" as const } : event)),
    )

    setSelectedEvent((prev) => (prev ? { ...prev, status: "completed" as const } : null))
  }

  const formatDate = (date: Date) => {
    return date.toLocaleDateString("en-US", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
    })
  }

  const formatReplyDate = (dateString: string) => {
    const date = new Date(dateString)
    return date.toLocaleString("en-US", {
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    })
  }

  return (
    <div className="space-y-4">
      {/* Calendar Header */}
      <div className="flex items-center justify-between">
        <h3 className="text-lg font-semibold">{formatDate(selectedDate)}</h3>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <ChevronLeft className="h-4 w-4" />
          </Button>
          <Button variant="outline" size="sm">
            Today
          </Button>
          <Button variant="outline" size="sm">
            <ChevronRight className="h-4 w-4" />
          </Button>
        </div>
      </div>

      {/* Legend */}
      <div className="flex items-center gap-4 text-sm">
        <div className="flex items-center gap-2">
          <div className="w-3 h-3 bg-blue-500 rounded"></div>
          <span>Shifts</span>
        </div>
        <div className="flex items-center gap-2">
          <div className="w-3 h-3 bg-green-500 rounded"></div>
          <span>Tasks</span>
        </div>
        <div className="flex items-center gap-2">
          <div className="w-3 h-3 bg-red-500 rounded"></div>
          <span>Training</span>
        </div>
      </div>

      {/* Events List */}
      <div className="space-y-3">
        {events.map((event) => (
          <Card
            key={event.id}
            className={`cursor-pointer transition-all hover:shadow-md ${getEventColor(event.type)} border`}
            onClick={() => handleEventClick(event)}
          >
            <CardContent className="p-4">
              <div className="flex items-start justify-between">
                <div className="flex items-start gap-3">
                  {getEventIcon(event.type)}
                  <div>
                    <h4 className="font-semibold">{event.title}</h4>
                    <p className="text-sm opacity-80">
                      {event.startTime} - {event.endTime}
                    </p>
                    {event.location && <p className="text-sm opacity-70 mt-1">{event.location}</p>}
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  {event.status && (
                    <Badge
                      variant={event.status === "completed" ? "default" : "secondary"}
                      className={`text-xs ${event.status === "completed" ? "bg-green-600" : ""}`}
                    >
                      {event.status}
                    </Badge>
                  )}
                  {event.canReply && <MessageSquare className="h-4 w-4 opacity-60" />}
                  {event.replies && event.replies.length > 0 && (
                    <Badge variant="outline" className="text-xs">
                      {event.replies.length} replies
                    </Badge>
                  )}
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Enhanced Event Details Dialog */}
      <Dialog open={showEventDialog} onOpenChange={setShowEventDialog}>
        <DialogContent className="max-w-2xl max-h-[80vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle className="flex items-center gap-2">
              {selectedEvent && getEventIcon(selectedEvent.type)}
              {selectedEvent?.title}
            </DialogTitle>
            <DialogDescription>
              {selectedEvent?.startTime} - {selectedEvent?.endTime}
              {selectedEvent?.location && ` • ${selectedEvent.location}`}
            </DialogDescription>
          </DialogHeader>

          {selectedEvent && (
            <div className="space-y-6">
              {selectedEvent.description && (
                <div>
                  <h4 className="font-semibold text-sm mb-2">Description</h4>
                  <p className="text-sm text-gray-600">{selectedEvent.description}</p>
                </div>
              )}

              {selectedEvent.assignedBy && (
                <div>
                  <h4 className="font-semibold text-sm mb-2">Assigned By</h4>
                  <p className="text-sm text-gray-600">{selectedEvent.assignedBy}</p>
                </div>
              )}

              {selectedEvent.replies && selectedEvent.replies.length > 0 && (
                <div>
                  <h4 className="font-semibold text-sm mb-3">Task Updates</h4>
                  <div className="space-y-3">
                    {selectedEvent.replies.map((reply) => (
                      <div key={reply.id} className="bg-gray-50 p-4 rounded-lg">
                        <div className="flex items-start gap-3">
                          <Avatar className="h-8 w-8">
                            <AvatarFallback className="text-xs">JS</AvatarFallback>
                          </Avatar>
                          <div className="flex-1">
                            <div className="flex items-center gap-2 mb-2">
                              <span className="font-medium text-sm">John Smith</span>
                              <span className="text-xs text-gray-500">{formatReplyDate(reply.createdAt)}</span>
                            </div>
                            <p className="text-sm text-gray-700 mb-2">{reply.content}</p>
                            {reply.attachments && reply.attachments.length > 0 && (
                              <div className="flex flex-wrap gap-2">
                                {reply.attachments.map((attachment, index) => (
                                  <div
                                    key={index}
                                    className="flex items-center gap-1 bg-blue-100 text-blue-800 px-2 py-1 rounded text-xs"
                                  >
                                    <Paperclip className="h-3 w-3" />
                                    {attachment}
                                  </div>
                                ))}
                              </div>
                            )}
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              {selectedEvent.canReply && selectedEvent.type === "task" && (
                <div className="border-t pt-4">
                  <div className="flex items-center justify-between mb-3">
                    <h4 className="font-semibold text-sm">Task Update</h4>
                    {selectedEvent.status !== "completed" && (
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={markTaskComplete}
                        className="flex items-center gap-2 bg-transparent"
                      >
                        <CheckCircle className="h-4 w-4" />
                        Mark Complete
                      </Button>
                    )}
                  </div>

                  <div className="space-y-3">
                    <Textarea
                      placeholder="Provide an update on this task..."
                      value={replyText}
                      onChange={(e) => setReplyText(e.target.value)}
                      rows={3}
                    />

                    {/* File Upload */}
                    <div className="flex items-center gap-2">
                      <Input
                        type="file"
                        multiple
                        accept="image/*,.pdf,.doc,.docx"
                        onChange={handleFileUpload}
                        className="hidden"
                        id="file-upload"
                      />
                      <label htmlFor="file-upload">
                        <Button variant="outline" size="sm" className="flex items-center gap-2 bg-transparent" asChild>
                          <span>
                            <Camera className="h-4 w-4" />
                            Add Photos
                          </span>
                        </Button>
                      </label>
                    </div>

                    {/* Attachment Preview */}
                    {attachments.length > 0 && (
                      <div className="flex flex-wrap gap-2">
                        {attachments.map((file, index) => (
                          <div
                            key={index}
                            className="flex items-center gap-1 bg-blue-100 text-blue-800 px-2 py-1 rounded text-xs"
                          >
                            <Paperclip className="h-3 w-3" />
                            {file.name}
                            <button onClick={() => removeAttachment(index)}>
                              <X className="h-3 w-3 hover:text-red-600" />
                            </button>
                          </div>
                        ))}
                      </div>
                    )}

                    <div className="flex justify-end">
                      <Button
                        onClick={handleSubmitReply}
                        disabled={!replyText.trim()}
                        className="flex items-center gap-2"
                      >
                        <Send className="h-4 w-4" />
                        Send Update
                      </Button>
                    </div>
                  </div>
                </div>
              )}
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  )
}
