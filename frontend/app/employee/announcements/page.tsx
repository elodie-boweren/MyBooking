"use client"

import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Textarea } from "@/components/ui/textarea"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import { Bell, MessageSquare, Clock, AlertTriangle, Info, CheckCircle, Send, Eye, Users } from "lucide-react"
import EmployeeNavigation from "@/components/employee-navigation"
import { useEmployeeAuth } from "@/components/employee-auth-context"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"

interface Announcement {
  id: string
  title: string
  content: string
  type: "info" | "urgent" | "success" | "warning"
  author: string
  authorRole: string
  createdAt: string
  isRead: boolean
  requiresResponse: boolean
  responses?: AnnouncementResponse[]
  readBy?: string[]
}

interface AnnouncementResponse {
  id: string
  employeeId: string
  employeeName: string
  content: string
  createdAt: string
}

const mockAnnouncements: Announcement[] = [
  {
    id: "1",
    title: "New Safety Protocol Implementation",
    content:
      "Effective immediately, all employees must follow the updated safety protocols when handling equipment. Please review the attached guidelines and confirm your understanding by responding to this announcement.",
    type: "urgent",
    author: "Sarah Johnson",
    authorRole: "Safety Manager",
    createdAt: "2024-01-15T10:30:00Z",
    isRead: false,
    requiresResponse: true,
    responses: [
      {
        id: "r1",
        employeeId: "emp2",
        employeeName: "Mike Chen",
        content: "Understood. I've reviewed the new protocols and will implement them immediately.",
        createdAt: "2024-01-15T11:00:00Z",
      },
    ],
    readBy: ["emp2", "emp3"],
  },
  {
    id: "2",
    title: "Monthly Team Meeting - Tomorrow 10 AM",
    content:
      "Don't forget about our monthly operations review meeting tomorrow at 10 AM in Conference Room A. We'll be discussing Q1 performance metrics and upcoming projects.",
    type: "info",
    author: "David Wilson",
    authorRole: "Operations Manager",
    createdAt: "2024-01-14T16:45:00Z",
    isRead: true,
    requiresResponse: false,
    readBy: ["emp1", "emp2", "emp3", "emp4"],
  },
  {
    id: "3",
    title: "System Maintenance Completed Successfully",
    content:
      "The scheduled system maintenance has been completed successfully. All systems are now fully operational. Thank you for your patience during the maintenance window.",
    type: "success",
    author: "IT Department",
    authorRole: "System Administrator",
    createdAt: "2024-01-14T08:00:00Z",
    isRead: true,
    requiresResponse: false,
    readBy: ["emp1", "emp2", "emp3", "emp4", "emp5"],
  },
  {
    id: "4",
    title: "Upcoming Training Session - Equipment Handling",
    content:
      "Mandatory training session on new equipment handling procedures scheduled for Friday, January 19th at 2 PM. Please ensure your attendance as this training is required for all operations staff.",
    type: "warning",
    author: "HR Department",
    authorRole: "Training Coordinator",
    createdAt: "2024-01-13T14:20:00Z",
    isRead: false,
    requiresResponse: true,
    responses: [],
    readBy: ["emp3"],
  },
]

export default function EmployeeAnnouncements() {
  const { employee, isLoading } = useEmployeeAuth()
  const router = useRouter()
  const [announcements, setAnnouncements] = useState<Announcement[]>(mockAnnouncements)
  const [selectedAnnouncement, setSelectedAnnouncement] = useState<Announcement | null>(null)
  const [showAnnouncementDialog, setShowAnnouncementDialog] = useState(false)
  const [responseText, setResponseText] = useState("")

  useEffect(() => {
    if (!isLoading && !employee) {
      router.push("/employee/login")
    }
  }, [employee, isLoading, router])

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen">Loading...</div>
  }

  if (!employee) {
    return null
  }

  const getAnnouncementIcon = (type: Announcement["type"]) => {
    switch (type) {
      case "urgent":
        return <AlertTriangle className="h-5 w-5 text-red-600" />
      case "warning":
        return <AlertTriangle className="h-5 w-5 text-yellow-600" />
      case "success":
        return <CheckCircle className="h-5 w-5 text-green-600" />
      case "info":
      default:
        return <Info className="h-5 w-5 text-blue-600" />
    }
  }

  const getAnnouncementColor = (type: Announcement["type"]) => {
    switch (type) {
      case "urgent":
        return "border-l-red-500 bg-red-50"
      case "warning":
        return "border-l-yellow-500 bg-yellow-50"
      case "success":
        return "border-l-green-500 bg-green-50"
      case "info":
      default:
        return "border-l-blue-500 bg-blue-50"
    }
  }

  const handleAnnouncementClick = (announcement: Announcement) => {
    setSelectedAnnouncement(announcement)
    setShowAnnouncementDialog(true)

    // Mark as read
    if (!announcement.isRead) {
      setAnnouncements((prev) =>
        prev.map((a) =>
          a.id === announcement.id ? { ...a, isRead: true, readBy: [...(a.readBy || []), employee.employeeId] } : a,
        ),
      )
    }
  }

  const handleSubmitResponse = () => {
    if (!selectedAnnouncement || !responseText.trim()) return

    const newResponse: AnnouncementResponse = {
      id: `r${Date.now()}`,
      employeeId: employee.employeeId,
      employeeName: employee.name,
      content: responseText.trim(),
      createdAt: new Date().toISOString(),
    }

    setAnnouncements((prev) =>
      prev.map((a) =>
        a.id === selectedAnnouncement.id ? { ...a, responses: [...(a.responses || []), newResponse] } : a,
      ),
    )

    setResponseText("")
    setShowAnnouncementDialog(false)
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString)
    return date.toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    })
  }

  const unreadCount = announcements.filter((a) => !a.isRead).length

  return (
    <div className="min-h-screen bg-gray-50">
      <EmployeeNavigation />

      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
                <Bell className="h-8 w-8 text-blue-600" />
                Announcements
              </h1>
              <p className="text-gray-600 mt-2">Stay updated with company news and important information</p>
            </div>
            {unreadCount > 0 && (
              <Badge variant="destructive" className="text-sm">
                {unreadCount} unread
              </Badge>
            )}
          </div>
        </div>

        {/* Announcements List */}
        <div className="space-y-4">
          {announcements.map((announcement) => (
            <Card
              key={announcement.id}
              className={`cursor-pointer transition-all hover:shadow-md border-l-4 ${getAnnouncementColor(announcement.type)} ${
                !announcement.isRead ? "ring-2 ring-blue-200" : ""
              }`}
              onClick={() => handleAnnouncementClick(announcement)}
            >
              <CardContent className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex items-start gap-4 flex-1">
                    {getAnnouncementIcon(announcement.type)}
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-2">
                        <h3 className={`font-semibold ${!announcement.isRead ? "text-gray-900" : "text-gray-700"}`}>
                          {announcement.title}
                        </h3>
                        {!announcement.isRead && <div className="w-2 h-2 bg-blue-600 rounded-full"></div>}
                      </div>
                      <p className="text-gray-600 text-sm mb-3 line-clamp-2">{announcement.content}</p>
                      <div className="flex items-center gap-4 text-xs text-gray-500">
                        <div className="flex items-center gap-1">
                          <Clock className="h-3 w-3" />
                          {formatDate(announcement.createdAt)}
                        </div>
                        <div>
                          By {announcement.author} • {announcement.authorRole}
                        </div>
                        {announcement.readBy && (
                          <div className="flex items-center gap-1">
                            <Eye className="h-3 w-3" />
                            {announcement.readBy.length} read
                          </div>
                        )}
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center gap-2 ml-4">
                    {announcement.requiresResponse && (
                      <Badge variant="outline" className="text-xs">
                        Response Required
                      </Badge>
                    )}
                    {announcement.responses && announcement.responses.length > 0 && (
                      <div className="flex items-center gap-1 text-xs text-gray-500">
                        <MessageSquare className="h-3 w-3" />
                        {announcement.responses.length}
                      </div>
                    )}
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* Announcement Details Dialog */}
        <Dialog open={showAnnouncementDialog} onOpenChange={setShowAnnouncementDialog}>
          <DialogContent className="max-w-2xl max-h-[80vh] overflow-y-auto">
            <DialogHeader>
              <DialogTitle className="flex items-center gap-2">
                {selectedAnnouncement && getAnnouncementIcon(selectedAnnouncement.type)}
                {selectedAnnouncement?.title}
              </DialogTitle>
              <DialogDescription>
                By {selectedAnnouncement?.author} • {selectedAnnouncement?.authorRole} •{" "}
                {selectedAnnouncement && formatDate(selectedAnnouncement.createdAt)}
              </DialogDescription>
            </DialogHeader>

            {selectedAnnouncement && (
              <div className="space-y-6">
                {/* Content */}
                <div>
                  <p className="text-gray-700 leading-relaxed">{selectedAnnouncement.content}</p>
                </div>

                {/* Read Status */}
                {selectedAnnouncement.readBy && (
                  <div className="bg-gray-50 p-4 rounded-lg">
                    <div className="flex items-center gap-2 mb-2">
                      <Users className="h-4 w-4 text-gray-600" />
                      <span className="text-sm font-medium text-gray-700">
                        Read by {selectedAnnouncement.readBy.length} employees
                      </span>
                    </div>
                  </div>
                )}

                {/* Existing Responses */}
                {selectedAnnouncement.responses && selectedAnnouncement.responses.length > 0 && (
                  <div>
                    <h4 className="font-semibold text-gray-900 mb-3">Responses</h4>
                    <div className="space-y-3">
                      {selectedAnnouncement.responses.map((response) => (
                        <div key={response.id} className="bg-gray-50 p-4 rounded-lg">
                          <div className="flex items-start gap-3">
                            <Avatar className="h-8 w-8">
                              <AvatarFallback className="text-xs">
                                {response.employeeName
                                  .split(" ")
                                  .map((n) => n[0])
                                  .join("")}
                              </AvatarFallback>
                            </Avatar>
                            <div className="flex-1">
                              <div className="flex items-center gap-2 mb-1">
                                <span className="font-medium text-sm">{response.employeeName}</span>
                                <span className="text-xs text-gray-500">{formatDate(response.createdAt)}</span>
                              </div>
                              <p className="text-sm text-gray-700">{response.content}</p>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                )}

                {/* Response Form */}
                {selectedAnnouncement.requiresResponse && (
                  <div className="border-t pt-4">
                    <h4 className="font-semibold text-gray-900 mb-3">Your Response</h4>
                    <div className="space-y-3">
                      <Textarea
                        placeholder="Type your response here..."
                        value={responseText}
                        onChange={(e) => setResponseText(e.target.value)}
                        rows={3}
                      />
                      <div className="flex justify-end">
                        <Button
                          onClick={handleSubmitResponse}
                          disabled={!responseText.trim()}
                          className="flex items-center gap-2"
                        >
                          <Send className="h-4 w-4" />
                          Send Response
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
    </div>
  )
}
