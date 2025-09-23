"use client"

import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { CalendarDays, Plus, Clock, CheckCircle, XCircle, Calendar, User } from "lucide-react"
import EmployeeNavigation from "@/components/employee-navigation"
import { useEmployeeAuth } from "@/components/employee-auth-context"
import LeaveRequestDialog from "@/components/leave-request-dialog"

interface LeaveRequest {
  id: string
  employeeId: string
  employeeName: string
  type: "vacation" | "sick" | "personal" | "emergency"
  startDate: string
  endDate: string
  reason: string
  status: "pending" | "approved" | "rejected"
  submittedAt: string
  approvedBy?: string
  approvedAt?: string
  comments?: string
}

const mockLeaveRequests: LeaveRequest[] = [
  {
    id: "1",
    employeeId: "EMP001",
    employeeName: "John Smith",
    type: "vacation",
    startDate: "2024-02-15",
    endDate: "2024-02-19",
    reason: "Family vacation to Hawaii",
    status: "approved",
    submittedAt: "2024-01-10T09:00:00Z",
    approvedBy: "Sarah Johnson",
    approvedAt: "2024-01-11T14:30:00Z",
  },
  {
    id: "2",
    employeeId: "EMP001",
    employeeName: "John Smith",
    type: "sick",
    startDate: "2024-01-08",
    endDate: "2024-01-09",
    reason: "Flu symptoms",
    status: "approved",
    submittedAt: "2024-01-08T07:30:00Z",
    approvedBy: "Sarah Johnson",
    approvedAt: "2024-01-08T08:00:00Z",
  },
  {
    id: "3",
    employeeId: "EMP001",
    employeeName: "John Smith",
    type: "personal",
    startDate: "2024-03-01",
    endDate: "2024-03-01",
    reason: "Doctor's appointment",
    status: "pending",
    submittedAt: "2024-01-14T16:20:00Z",
  },
]

export default function EmployeeLeave() {
  const { employee, isLoading } = useEmployeeAuth()
  const router = useRouter()
  const [leaveRequests, setLeaveRequests] = useState<LeaveRequest[]>(mockLeaveRequests)
  const [showRequestDialog, setShowRequestDialog] = useState(false)

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

  const handleSubmitRequest = (
    request: Omit<LeaveRequest, "id" | "employeeId" | "employeeName" | "status" | "submittedAt">,
  ) => {
    const newRequest: LeaveRequest = {
      id: `req_${Date.now()}`,
      employeeId: employee.employeeId,
      employeeName: employee.name,
      status: "pending",
      submittedAt: new Date().toISOString(),
      ...request,
    }

    setLeaveRequests((prev) => [newRequest, ...prev])
  }

  const getStatusIcon = (status: LeaveRequest["status"]) => {
    switch (status) {
      case "approved":
        return <CheckCircle className="h-4 w-4 text-green-600" />
      case "rejected":
        return <XCircle className="h-4 w-4 text-red-600" />
      case "pending":
      default:
        return <Clock className="h-4 w-4 text-yellow-600" />
    }
  }

  const getStatusColor = (status: LeaveRequest["status"]) => {
    switch (status) {
      case "approved":
        return "bg-green-100 text-green-800"
      case "rejected":
        return "bg-red-100 text-red-800"
      case "pending":
      default:
        return "bg-yellow-100 text-yellow-800"
    }
  }

  const getLeaveTypeColor = (type: LeaveRequest["type"]) => {
    switch (type) {
      case "vacation":
        return "bg-blue-100 text-blue-800"
      case "sick":
        return "bg-red-100 text-red-800"
      case "personal":
        return "bg-purple-100 text-purple-800"
      case "emergency":
        return "bg-orange-100 text-orange-800"
      default:
        return "bg-gray-100 text-gray-800"
    }
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
      year: "numeric",
    })
  }

  const calculateDays = (startDate: string, endDate: string) => {
    const start = new Date(startDate)
    const end = new Date(endDate)
    const diffTime = Math.abs(end.getTime() - start.getTime())
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1
    return diffDays
  }

  const stats = {
    pending: leaveRequests.filter((r) => r.status === "pending").length,
    approved: leaveRequests.filter((r) => r.status === "approved").length,
    totalDays: leaveRequests
      .filter((r) => r.status === "approved")
      .reduce((sum, r) => sum + calculateDays(r.startDate, r.endDate), 0),
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <EmployeeNavigation />

      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 flex items-center gap-3">
                <CalendarDays className="h-8 w-8 text-blue-600" />
                Leave Requests
              </h1>
              <p className="text-gray-600 mt-2">Manage your time off requests and view approval status</p>
            </div>
            <Button onClick={() => setShowRequestDialog(true)} className="flex items-center gap-2">
              <Plus className="h-4 w-4" />
              New Request
            </Button>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Pending Requests</p>
                  <p className="text-2xl font-bold text-yellow-600">{stats.pending}</p>
                </div>
                <Clock className="h-8 w-8 text-yellow-600" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Approved Requests</p>
                  <p className="text-2xl font-bold text-green-600">{stats.approved}</p>
                </div>
                <CheckCircle className="h-8 w-8 text-green-600" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Days Taken</p>
                  <p className="text-2xl font-bold text-blue-600">{stats.totalDays}</p>
                </div>
                <Calendar className="h-8 w-8 text-blue-600" />
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Leave Requests List */}
        <Card>
          <CardHeader>
            <CardTitle>Your Leave Requests</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {leaveRequests.map((request) => (
                <div
                  key={request.id}
                  className="border border-gray-200 rounded-lg p-4 hover:shadow-sm transition-shadow"
                >
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <div className="flex items-center gap-3 mb-2">
                        <Badge className={getLeaveTypeColor(request.type)}>
                          {request.type.charAt(0).toUpperCase() + request.type.slice(1)}
                        </Badge>
                        <Badge className={getStatusColor(request.status)}>
                          <div className="flex items-center gap-1">
                            {getStatusIcon(request.status)}
                            {request.status.charAt(0).toUpperCase() + request.status.slice(1)}
                          </div>
                        </Badge>
                      </div>

                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-3">
                        <div>
                          <p className="text-sm font-medium text-gray-900">
                            {formatDate(request.startDate)} - {formatDate(request.endDate)}
                          </p>
                          <p className="text-xs text-gray-500">
                            {calculateDays(request.startDate, request.endDate)} day
                            {calculateDays(request.startDate, request.endDate) !== 1 ? "s" : ""}
                          </p>
                        </div>
                        <div>
                          <p className="text-sm text-gray-600">{request.reason}</p>
                        </div>
                      </div>

                      <div className="flex items-center gap-4 text-xs text-gray-500">
                        <span>Submitted {formatDate(request.submittedAt)}</span>
                        {request.approvedBy && (
                          <span className="flex items-center gap-1">
                            <User className="h-3 w-3" />
                            Approved by {request.approvedBy}
                          </span>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              ))}

              {leaveRequests.length === 0 && (
                <div className="text-center py-8">
                  <CalendarDays className="h-12 w-12 text-gray-400 mx-auto mb-4" />
                  <p className="text-gray-500">No leave requests yet</p>
                  <Button onClick={() => setShowRequestDialog(true)} className="mt-4 flex items-center gap-2">
                    <Plus className="h-4 w-4" />
                    Submit Your First Request
                  </Button>
                </div>
              )}
            </div>
          </CardContent>
        </Card>

        {/* Leave Request Dialog */}
        <LeaveRequestDialog
          open={showRequestDialog}
          onOpenChange={setShowRequestDialog}
          onSubmit={handleSubmitRequest}
        />
      </div>
    </div>
  )
}
