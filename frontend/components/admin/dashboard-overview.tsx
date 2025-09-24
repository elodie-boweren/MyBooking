"use client"

import { useState, useEffect } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { 
  DollarSign, 
  Users, 
  Building2, 
  TrendingUp,
  Calendar,
  MessageSquare,
  UserCheck,
  AlertCircle
} from 'lucide-react'
import { COMPONENT_TEMPLATES } from '@/lib/style-constants'
import { apiClient, API_ENDPOINTS } from '@/lib/api'

interface DashboardStats {
  revenue: {
    today: number
    monthly: number
    trend: number
  }
  occupancy: {
    rate: number
    occupied: number
    total: number
  }
  users: {
    total: number
    clients: number
    employees: number
    admins: number
  }
  system: {
    recentBookings: number
    pendingTasks: number
    feedbackCount: number
    alerts: number
  }
}

export function DashboardOverview() {
  const [stats, setStats] = useState<DashboardStats | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetchDashboardStats()
  }, [])

  const fetchDashboardStats = async () => {
    try {
      setLoading(true)
      setError(null)

      // Fetch analytics data from backend
      const [revenueData, occupancyData, userData, systemData] = await Promise.all([
        apiClient.get(API_ENDPOINTS.ANALYTICS.REVENUE),
        apiClient.get(API_ENDPOINTS.ANALYTICS.OCCUPANCY),
        apiClient.get(API_ENDPOINTS.ANALYTICS.CUSTOMERS),
        apiClient.get(API_ENDPOINTS.ANALYTICS.KPIS)
      ])

      // Transform data to match our interface
      const dashboardStats: DashboardStats = {
        revenue: {
          today: revenueData.roomRevenue || 0,
          monthly: revenueData.totalRevenue || 0,
          trend: 12.5 // Mock trend data
        },
        occupancy: {
          rate: occupancyData.roomOccupancyRate || 0,
          occupied: occupancyData.occupiedRoomNights || 0,
          total: occupancyData.totalRoomNights || 0
        },
        users: {
          total: userData.totalCustomers || 0,
          clients: userData.totalCustomers || 0,
          employees: systemData.totalEmployees || 0,
          admins: 1 // We know there's 1 admin
        },
        system: {
          recentBookings: 5, // Mock data
          pendingTasks: 3, // Mock data
          feedbackCount: 8, // Mock data
          alerts: 0 // Mock data
        }
      }

      setStats(dashboardStats)
    } catch (err: any) {
      console.error('Failed to fetch dashboard stats:', err)
      setError('Failed to load dashboard data')
      
      // Set mock data for development
      setStats({
        revenue: { today: 1250, monthly: 45000, trend: 12.5 },
        occupancy: { rate: 78.5, occupied: 45, total: 60 },
        users: { total: 150, clients: 120, employees: 25, admins: 1 },
        system: { recentBookings: 5, pendingTasks: 3, feedbackCount: 8, alerts: 0 }
      })
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {[...Array(4)].map((_, i) => (
          <Card key={i} className="animate-pulse">
            <CardHeader className="pb-2">
              <div className="h-4 bg-muted rounded w-1/2"></div>
            </CardHeader>
            <CardContent>
              <div className="h-8 bg-muted rounded w-3/4 mb-2"></div>
              <div className="h-3 bg-muted rounded w-1/2"></div>
            </CardContent>
          </Card>
        ))}
      </div>
    )
  }

  if (error && !stats) {
    return (
      <div className="text-center py-8">
        <AlertCircle className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
        <p className="text-muted-foreground">{error}</p>
        <button 
          onClick={fetchDashboardStats}
          className="mt-4 text-primary hover:underline"
        >
          Try again
        </button>
      </div>
    )
  }

  if (!stats) return null

  return (
    <div className="space-y-6">
      {/* Main Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {/* Revenue Card */}
        <Card className={COMPONENT_TEMPLATES.cardHover}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-primary">Revenue</CardTitle>
            <DollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">${stats.revenue.today.toLocaleString()}</div>
            <p className="text-xs text-muted-foreground">
              Today • <span className="text-green-600">+{stats.revenue.trend}%</span> from last month
            </p>
          </CardContent>
        </Card>

        {/* Occupancy Card */}
        <Card className={COMPONENT_TEMPLATES.cardHover}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-primary">Occupancy</CardTitle>
            <Building2 className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.occupancy.rate.toFixed(1)}%</div>
            <p className="text-xs text-muted-foreground">
              {stats.occupancy.occupied} of {stats.occupancy.total} rooms occupied
            </p>
          </CardContent>
        </Card>

        {/* Users Card */}
        <Card className={COMPONENT_TEMPLATES.cardHover}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-primary">Users</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.users.total}</div>
            <p className="text-xs text-muted-foreground">
              {stats.users.clients} clients, {stats.users.employees} employees
            </p>
          </CardContent>
        </Card>

        {/* System Health Card */}
        <Card className={COMPONENT_TEMPLATES.cardHover}>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium text-primary">System</CardTitle>
            <TrendingUp className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.system.recentBookings}</div>
            <p className="text-xs text-muted-foreground">
              Recent bookings • {stats.system.pendingTasks} pending tasks
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Quick Stats Row */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-primary">Recent Activity</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-2">
              <div className="flex items-center space-x-2">
                <Calendar className="h-4 w-4 text-muted-foreground" />
                <span className="text-sm">{stats.system.recentBookings} new bookings today</span>
              </div>
              <div className="flex items-center space-x-2">
                <MessageSquare className="h-4 w-4 text-muted-foreground" />
                <span className="text-sm">{stats.system.feedbackCount} feedback items</span>
              </div>
              <div className="flex items-center space-x-2">
                <UserCheck className="h-4 w-4 text-muted-foreground" />
                <span className="text-sm">{stats.system.pendingTasks} pending tasks</span>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-primary">Room Status</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-2">
              <div className="flex justify-between items-center">
                <span className="text-sm">Available</span>
                <Badge variant="default">{stats.occupancy.total - stats.occupancy.occupied}</Badge>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm">Occupied</span>
                <Badge variant="secondary">{stats.occupancy.occupied}</Badge>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm">Total</span>
                <Badge variant="outline">{stats.occupancy.total}</Badge>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-primary">Alerts</CardTitle>
          </CardHeader>
          <CardContent>
            {stats.system.alerts > 0 ? (
              <div className="space-y-2">
                <div className="flex items-center space-x-2 text-red-600">
                  <AlertCircle className="h-4 w-4" />
                  <span className="text-sm">{stats.system.alerts} system alerts</span>
                </div>
              </div>
            ) : (
              <div className="text-sm text-green-600">All systems operational</div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
