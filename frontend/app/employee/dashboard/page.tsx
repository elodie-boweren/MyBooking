"use client"

import { useEffect } from "react"
import { useRouter } from "next/navigation"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Calendar, Clock, Users, CheckCircle, AlertCircle, BookOpen, CalendarDays } from "lucide-react"
import EmployeeNavigation from "@/components/employee-navigation"
import { useEmployeeAuth } from "@/components/employee-auth-context"
import EmployeeCalendar from "@/components/employee-calendar"
import { useState } from "react"
import LeaveRequestDialog from "@/components/leave-request-dialog"

export default function EmployeeDashboard() {
  const { employee, isLoading } = useEmployeeAuth()
  const router = useRouter()
  const [showLeaveDialog, setShowLeaveDialog] = useState(false)

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

  const todayStats = {
    shifts: 1,
    tasks: 3,
    trainings: 1,
    announcements: 2,
  }

  const handleLeaveRequest = (request: any) => {
    // Handle leave request submission
    console.log("Leave request submitted:", request)
    // You would typically send this to your API
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <EmployeeNavigation />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Welcome Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Welcome back, {employee.name}</h1>
          <p className="text-gray-600 mt-2">
            {employee.position} • {employee.department}
          </p>
        </div>

        {/* Quick Stats */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Today's Shifts</p>
                  <p className="text-2xl font-bold text-blue-600">{todayStats.shifts}</p>
                </div>
                <Clock className="h-8 w-8 text-blue-600" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Pending Tasks</p>
                  <p className="text-2xl font-bold text-green-600">{todayStats.tasks}</p>
                </div>
                <CheckCircle className="h-8 w-8 text-green-600" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Trainings</p>
                  <p className="text-2xl font-bold text-red-600">{todayStats.trainings}</p>
                </div>
                <BookOpen className="h-8 w-8 text-red-600" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">New Announcements</p>
                  <p className="text-2xl font-bold text-purple-600">{todayStats.announcements}</p>
                </div>
                <AlertCircle className="h-8 w-8 text-purple-600" />
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Main Content Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Calendar Section */}
          <div className="lg:col-span-2">
            <Card>
              <CardHeader>
                <div className="flex items-center justify-between">
                  <div>
                    <CardTitle className="flex items-center gap-2">
                      <Calendar className="h-5 w-5" />
                      My Schedule
                    </CardTitle>
                    <CardDescription>View your shifts, tasks, and training sessions</CardDescription>
                  </div>
                  <Button size="sm" className="flex items-center gap-2" onClick={() => setShowLeaveDialog(true)}>
                    <CalendarDays className="h-4 w-4" />
                    Request Leave
                  </Button>
                </div>
              </CardHeader>
              <CardContent>
                <EmployeeCalendar />
              </CardContent>
            </Card>
          </div>

          {/* Sidebar */}
          <div className="space-y-6">
            {/* Recent Announcements */}
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Recent Announcements</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="border-l-4 border-blue-500 pl-4">
                  <h4 className="font-semibold text-sm">New Safety Protocol</h4>
                  <p className="text-sm text-gray-600">Updated guidelines for equipment handling...</p>
                  <p className="text-xs text-gray-500 mt-1">2 hours ago</p>
                </div>
                <div className="border-l-4 border-green-500 pl-4">
                  <h4 className="font-semibold text-sm">Team Meeting Tomorrow</h4>
                  <p className="text-sm text-gray-600">Monthly operations review at 10 AM...</p>
                  <p className="text-xs text-gray-500 mt-1">1 day ago</p>
                </div>
                <Button variant="outline" size="sm" className="w-full bg-transparent">
                  View All Announcements
                </Button>
              </CardContent>
            </Card>

            {/* Quick Actions */}
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Quick Actions</CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <Button variant="outline" className="w-full justify-start bg-transparent">
                  <Clock className="h-4 w-4 mr-2" />
                  Clock In/Out
                </Button>
                <Button variant="outline" className="w-full justify-start bg-transparent">
                  <Users className="h-4 w-4 mr-2" />
                  View Team Schedule
                </Button>
                <Button variant="outline" className="w-full justify-start bg-transparent">
                  <BookOpen className="h-4 w-4 mr-2" />
                  Training Materials
                </Button>
              </CardContent>
            </Card>
          </div>
        </div>

        {/* Leave Request Dialog */}
        <LeaveRequestDialog open={showLeaveDialog} onOpenChange={setShowLeaveDialog} onSubmit={handleLeaveRequest} />
      </div>
    </div>
  )
}
