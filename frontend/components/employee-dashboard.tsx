"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { 
  Calendar, 
  Clock, 
  CheckCircle, 
  BookOpen, 
  AlertCircle, 
  Bell,
  User,
  Settings,
  LogOut,
  Plus,
  Filter
} from "lucide-react"
import EnhancedEmployeeCalendar from "@/components/enhanced-employee-calendar"
import { apiClient } from "@/lib/api"

interface EmployeeStats {
  shiftsThisWeek: number
  pendingTasks: number
  trainingStatus: 'ACTIVE' | 'COMPLETED' | 'NONE'
  leaveBalance: number
}

interface TodayTask {
  id: string
  title: string
  description: string
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED'
  priority: 'LOW' | 'MEDIUM' | 'HIGH'
  dueDate: string
}

// Removed Notification interface - notifications don't exist in backend

export default function EmployeeDashboard() {
  const [stats, setStats] = useState<EmployeeStats>({
    shiftsThisWeek: 0,
    pendingTasks: 0,
    trainingStatus: 'NONE',
    leaveBalance: 0
  })
  const [todayTasks, setTodayTasks] = useState<TodayTask[]>([])
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    fetchEmployeeData()
  }, [])

  const fetchEmployeeData = async () => {
    try {
      setIsLoading(true)
      
      // Fetch employee stats, tasks, and notifications
      // For now, we'll use mock data since we need to implement the backend endpoints
      const mockStats: EmployeeStats = {
        shiftsThisWeek: 5,
        pendingTasks: 3,
        trainingStatus: 'ACTIVE',
        leaveBalance: 12
      }
      
      const mockTasks: TodayTask[] = [
        {
          id: '1',
          title: 'Room Cleaning - Floor 3',
          description: 'Clean all rooms on the 3rd floor',
          status: 'PENDING',
          priority: 'HIGH',
          dueDate: new Date().toISOString()
        },
        {
          id: '2',
          title: 'Equipment Check',
          description: 'Check conference room equipment',
          status: 'IN_PROGRESS',
          priority: 'MEDIUM',
          dueDate: new Date().toISOString()
        },
        {
          id: '3',
          title: 'Safety Training',
          description: 'Complete safety protocol training',
          status: 'PENDING',
          priority: 'LOW',
          dueDate: new Date().toISOString()
        }
      ]
      
      setStats(mockStats)
      setTodayTasks(mockTasks)
      
    } catch (error) {
      console.error('Error fetching employee data:', error)
    } finally {
      setIsLoading(false)
    }
  }

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case 'HIGH': return 'bg-red-100 text-red-800'
      case 'MEDIUM': return 'bg-yellow-100 text-yellow-800'
      case 'LOW': return 'bg-green-100 text-green-800'
      default: return 'bg-gray-100 text-gray-800'
    }
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'COMPLETED': return 'bg-green-100 text-green-800'
      case 'IN_PROGRESS': return 'bg-blue-100 text-blue-800'
      case 'PENDING': return 'bg-yellow-100 text-yellow-800'
      default: return 'bg-gray-100 text-gray-800'
    }
  }

  // Removed notification functions - notifications don't exist in backend

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-2 text-gray-600">Loading dashboard...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <h1 className="text-xl font-semibold text-gray-900">Employee Dashboard</h1>
            </div>
            <div className="flex items-center space-x-4">
              <Button variant="outline" size="sm">
                <Settings className="h-4 w-4 mr-2" />
                Settings
              </Button>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Welcome Section */}
        <div className="mb-8">
          <h2 className="text-2xl font-bold text-gray-900">
            Welcome back, Employee
          </h2>
          <p className="text-gray-600 mt-1">
            Here's what's happening with your schedule today
          </p>
        </div>

        {/* Quick Stats Bar */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Shifts This Week</p>
                  <p className="text-2xl font-bold text-blue-600">{stats.shiftsThisWeek}</p>
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
                  <p className="text-2xl font-bold text-green-600">{stats.pendingTasks}</p>
                </div>
                <CheckCircle className="h-8 w-8 text-green-600" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Training Status</p>
                  <p className="text-lg font-bold text-red-600">{stats.trainingStatus}</p>
                </div>
                <BookOpen className="h-8 w-8 text-red-600" />
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-gray-600">Leave Balance</p>
                  <p className="text-2xl font-bold text-purple-600">{stats.leaveBalance} days</p>
                </div>
                <Calendar className="h-8 w-8 text-purple-600" />
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Main Content Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Calendar Section - Central Element */}
          <div className="lg:col-span-2">
            <Card>
              <CardHeader>
                <div className="flex items-center justify-between">
                  <div>
                    <CardTitle className="flex items-center gap-2">
                      <Calendar className="h-5 w-5" />
                      My Schedule
                    </CardTitle>
                    <CardDescription>
                      View your shifts, tasks, and training sessions
                    </CardDescription>
                  </div>
                  <Button size="sm" className="flex items-center gap-2">
                    <Plus className="h-4 w-4" />
                    Request Leave
                  </Button>
                </div>
              </CardHeader>
              <CardContent>
                <EnhancedEmployeeCalendar />
              </CardContent>
            </Card>
          </div>

          {/* Sidebar */}
          <div className="space-y-6">
            {/* Today's Tasks */}
            <Card>
              <CardHeader>
                <div className="flex items-center justify-between">
                  <CardTitle className="text-lg">Today's Tasks</CardTitle>
                  <Button variant="outline" size="sm">
                    <Filter className="h-4 w-4" />
                  </Button>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                {todayTasks.map((task) => (
                  <div key={task.id} className="border rounded-lg p-4">
                    <div className="flex items-start justify-between mb-2">
                      <h4 className="font-semibold text-sm">{task.title}</h4>
                      <div className="flex gap-2">
                        <Badge className={`text-xs ${getPriorityColor(task.priority)}`}>
                          {task.priority}
                        </Badge>
                        <Badge className={`text-xs ${getStatusColor(task.status)}`}>
                          {task.status}
                        </Badge>
                      </div>
                    </div>
                    <p className="text-sm text-gray-600 mb-2">{task.description}</p>
                    <div className="flex justify-end">
                      <Button size="sm" variant="outline">
                        Update Status
                      </Button>
                    </div>
                  </div>
                ))}
                <Button variant="outline" className="w-full">
                  View All Tasks
                </Button>
              </CardContent>
            </Card>

            {/* Announcements - Using existing backend */}
            <Card>
              <CardHeader>
                <CardTitle className="text-lg flex items-center gap-2">
                  <Bell className="h-5 w-5" />
                  Recent Announcements
                </CardTitle>
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
                <Button variant="outline" className="w-full">
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
                <Button variant="outline" className="w-full justify-start">
                  <User className="h-4 w-4 mr-2" />
                  View Profile
                </Button>
                <Button variant="outline" className="w-full justify-start">
                  <Calendar className="h-4 w-4 mr-2" />
                  Request Leave
                </Button>
                <Button variant="outline" className="w-full justify-start">
                  <Bell className="h-4 w-4 mr-2" />
                  View Announcements
                </Button>
                <Button variant="outline" className="w-full justify-start">
                  <CheckCircle className="h-4 w-4 mr-2" />
                  Update Task Status
                </Button>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  )
}
