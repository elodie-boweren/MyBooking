"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { 
  CheckCircle, 
  Clock, 
  AlertCircle,
  Filter,
  Search,
  RefreshCw,
  Plus,
  Edit,
  Calendar,
  User,
  Users,
  BarChart3,
  ArrowLeft,
  Trash2
} from "lucide-react"
import { adminShiftApi, adminEmployeesApi, AdminShift, CreateShiftRequest, UpdateShiftRequest, Employee } from "@/lib/api"

interface ShiftFormData {
  userId: number
  startAt: string
  endAt: string
}

interface ShiftUpdateFormData {
  startAt: string
  endAt: string
}

export default function AdminShiftManagement() {
  const [shifts, setShifts] = useState<AdminShift[]>([])
  const [employees, setEmployees] = useState<Employee[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [isLoadingEmployees, setIsLoadingEmployees] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [showCreateDialog, setShowCreateDialog] = useState(false)
  const [showUpdateDialog, setShowUpdateDialog] = useState(false)
  const [selectedShift, setSelectedShift] = useState<AdminShift | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [searchTerm, setSearchTerm] = useState("")
  
  const [shiftForm, setShiftForm] = useState<ShiftFormData>({
    userId: 0,
    startAt: "",
    endAt: ""
  })
  
  const [updateForm, setUpdateForm] = useState<ShiftUpdateFormData>({
    startAt: "",
    endAt: ""
  })

  // Fetch shifts on component mount
  useEffect(() => {
    fetchShifts()
    fetchEmployees()
  }, [])

  const fetchShifts = async () => {
    try {
      setIsLoading(true)
      setError(null)
      
      const response = await adminShiftApi.getAllShifts()
      setShifts(response.content)
    } catch (error) {
      console.error('Error fetching shifts:', error)
      setError('Failed to load shifts. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  const fetchEmployees = async () => {
    try {
      setIsLoadingEmployees(true)
      const employees = await adminEmployeesApi.getAllEmployees()
      if (Array.isArray(employees)) {
        setEmployees(employees)
      } else {
        console.error('Employees response is not an array:', employees)
        setEmployees([])
        setError('Invalid employees data received.')
      }
    } catch (error) {
      console.error('Error fetching employees:', error)
      setError('Failed to load employees. Please try again.')
    } finally {
      setIsLoadingEmployees(false)
    }
  }

  const handleCreateShift = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!shiftForm.userId || !shiftForm.startAt || !shiftForm.endAt) {
      setError('Employee, start time, and end time are required.')
      return
    }

    try {
      setIsSubmitting(true)
      setError(null)
      
      const request: CreateShiftRequest = {
        employeeId: shiftForm.userId,
        startAt: shiftForm.startAt,
        endAt: shiftForm.endAt
      }
      
      await adminShiftApi.createShift(request)
      
      // Reset form and close dialog
      setShiftForm({ userId: 0, startAt: "", endAt: "" })
      setShowCreateDialog(false)
      
      // Refresh shifts list
      fetchShifts()
    } catch (error) {
      console.error('Error creating shift:', error)
      setError('Failed to create shift. Please try again.')
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleUpdateShift = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!selectedShift) return

    try {
      setIsSubmitting(true)
      setError(null)
      
      const request: UpdateShiftRequest = {
        startAt: updateForm.startAt,
        endAt: updateForm.endAt
      }
      
      await adminShiftApi.updateShift(selectedShift.id, request)
      
      // Close dialog and refresh
      setShowUpdateDialog(false)
      setSelectedShift(null)
      fetchShifts()
    } catch (error) {
      console.error('Error updating shift:', error)
      setError('Failed to update shift. Please try again.')
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleDeleteShift = async (shiftId: number) => {
    if (!confirm('Are you sure you want to delete this shift?')) return

    try {
      await adminShiftApi.deleteShift(shiftId)
      fetchShifts()
    } catch (error) {
      console.error('Error deleting shift:', error)
      setError('Failed to delete shift. Please try again.')
    }
  }

  const openUpdateDialog = (shift: AdminShift) => {
    setSelectedShift(shift)
    setUpdateForm({
      startAt: shift.startAt,
      endAt: shift.endAt
    })
    setShowUpdateDialog(true)
  }

  const formatDateTime = (dateTimeString: string) => {
    return new Date(dateTimeString).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  const formatDate = (dateTimeString: string) => {
    return new Date(dateTimeString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    })
  }

  const formatTime = (dateTimeString: string) => {
    return new Date(dateTimeString).toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  const getShiftDuration = (startAt: string, endAt: string) => {
    const start = new Date(startAt)
    const end = new Date(endAt)
    const durationMs = end.getTime() - start.getTime()
    const hours = Math.floor(durationMs / (1000 * 60 * 60))
    const minutes = Math.floor((durationMs % (1000 * 60 * 60)) / (1000 * 60))
    return `${hours}h ${minutes}m`
  }

  const filteredShifts = shifts.filter(shift => {
    const matchesSearch = shift.employeeName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                        shift.employeeEmail.toLowerCase().includes(searchTerm.toLowerCase())
    return matchesSearch
  })

  const stats = {
    total: shifts.length,
    today: shifts.filter(s => formatDate(s.startAt) === formatDate(new Date().toISOString())).length,
    thisWeek: shifts.filter(s => {
      const shiftDate = new Date(s.startAt)
      const now = new Date()
      const weekStart = new Date(now.setDate(now.getDate() - now.getDay()))
      const weekEnd = new Date(weekStart.getTime() + 7 * 24 * 60 * 60 * 1000)
      return shiftDate >= weekStart && shiftDate < weekEnd
    }).length
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button 
            variant="outline" 
            size="sm"
            onClick={() => window.location.href = '/admin'}
            className="flex items-center gap-2"
          >
            <ArrowLeft className="h-4 w-4" />
            Back to Dashboard
          </Button>
          <div>
            <h2 className="text-2xl font-bold text-gray-900">Shift Management</h2>
            <p className="text-gray-600 mt-1">Schedule and manage employee shifts</p>
          </div>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={fetchShifts} disabled={isLoading}>
            <RefreshCw className={`h-4 w-4 ${isLoading ? 'animate-spin' : ''}`} />
          </Button>
          <Button onClick={() => setShowCreateDialog(true)} className="flex items-center gap-2">
            <Plus className="h-4 w-4" />
            Assign Shift
          </Button>
        </div>
      </div>

      {/* Error Alert */}
      {error && (
        <Alert variant="destructive">
          <AlertCircle className="h-4 w-4" />
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      )}

      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Total Shifts</p>
                <p className="text-2xl font-bold text-blue-600">{stats.total}</p>
              </div>
              <Calendar className="h-8 w-8 text-blue-600" />
            </div>
          </CardContent>
        </Card>
        
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Today</p>
                <p className="text-2xl font-bold text-green-600">{stats.today}</p>
              </div>
              <Clock className="h-8 w-8 text-green-600" />
            </div>
          </CardContent>
        </Card>
        
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">This Week</p>
                <p className="text-2xl font-bold text-purple-600">{stats.thisWeek}</p>
              </div>
              <BarChart3 className="h-8 w-8 text-purple-600" />
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Search */}
      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2 flex-1">
          <Search className="h-4 w-4" />
          <Input
            placeholder="Search shifts by employee name or email..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="max-w-sm"
          />
        </div>
      </div>

      {/* Shifts List */}
      {isLoading ? (
        <div className="flex items-center justify-center py-8">
          <div className="text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-2 text-gray-600">Loading shifts...</p>
          </div>
        </div>
      ) : filteredShifts.length === 0 ? (
        <Card>
          <CardContent className="p-8 text-center">
            <Calendar className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">No shifts found</h3>
            <p className="text-gray-600 mb-4">
              {searchTerm ? 'No shifts match your search criteria.' : 'No shifts have been assigned yet.'}
            </p>
            <Button onClick={() => setShowCreateDialog(true)} className="flex items-center gap-2">
              <Plus className="h-4 w-4" />
              Assign First Shift
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="grid gap-4">
          {filteredShifts.map((shift) => (
            <Card key={shift.id} className="hover:shadow-md transition-shadow">
              <CardContent className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <h3 className="font-semibold text-lg">{shift.employeeName}</h3>
                      <Badge variant="outline" className="text-blue-600">
                        {shift.employeeEmail}
                      </Badge>
                    </div>
                    
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm text-gray-600 mb-4">
                      <div className="flex items-center gap-2">
                        <Calendar className="h-4 w-4" />
                        <span><strong>Date:</strong> {formatDate(shift.startAt)}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <Clock className="h-4 w-4" />
                        <span><strong>Time:</strong> {formatTime(shift.startAt)} - {formatTime(shift.endAt)}</span>
                      </div>
                      <div className="flex items-center gap-2">
                        <BarChart3 className="h-4 w-4" />
                        <span><strong>Duration:</strong> {getShiftDuration(shift.startAt, shift.endAt)}</span>
                      </div>
                    </div>
                  </div>
                  
                  <div className="flex items-center gap-2">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => openUpdateDialog(shift)}
                      className="flex items-center gap-2"
                    >
                      <Edit className="h-4 w-4" />
                      Edit
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleDeleteShift(shift.id)}
                      className="flex items-center gap-2 text-red-600 hover:text-red-700"
                    >
                      <Trash2 className="h-4 w-4" />
                      Delete
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {/* Create Shift Dialog */}
      <Dialog open={showCreateDialog} onOpenChange={setShowCreateDialog}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Assign New Shift</DialogTitle>
            <DialogDescription>
              Create a new shift assignment for an employee.
            </DialogDescription>
          </DialogHeader>
          
          <form onSubmit={handleCreateShift} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="userId">Employee</Label>
              <select
                id="userId"
                value={shiftForm.userId}
                onChange={(e) => setShiftForm(prev => ({ ...prev, userId: parseInt(e.target.value) }))}
                className="w-full px-3 py-2 border border-gray-300 rounded-md"
                required
                disabled={isLoadingEmployees}
              >
                <option value={0}>
                  {isLoadingEmployees ? 'Loading employees...' : 'Select an employee'}
                </option>
                {employees.map(employee => (
                  <option key={employee.userId} value={employee.userId}>
                    {employee.firstName} {employee.lastName} ({employee.email})
                  </option>
                ))}
              </select>
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="startAt">Start Time</Label>
              <Input
                id="startAt"
                type="datetime-local"
                value={shiftForm.startAt}
                onChange={(e) => setShiftForm(prev => ({ ...prev, startAt: e.target.value }))}
                required
              />
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="endAt">End Time</Label>
              <Input
                id="endAt"
                type="datetime-local"
                value={shiftForm.endAt}
                onChange={(e) => setShiftForm(prev => ({ ...prev, endAt: e.target.value }))}
                required
              />
            </div>
            
            <div className="flex justify-end gap-2">
              <Button 
                type="button" 
                variant="outline" 
                onClick={() => setShowCreateDialog(false)}
                disabled={isSubmitting}
              >
                Cancel
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Creating...' : 'Create Shift'}
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>

      {/* Update Shift Dialog */}
      <Dialog open={showUpdateDialog} onOpenChange={setShowUpdateDialog}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Update Shift</DialogTitle>
            <DialogDescription>
              Update the shift timing for {selectedShift?.employeeName}.
            </DialogDescription>
          </DialogHeader>
          
          <form onSubmit={handleUpdateShift} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="updateStartAt">Start Time</Label>
              <Input
                id="updateStartAt"
                type="datetime-local"
                value={updateForm.startAt}
                onChange={(e) => setUpdateForm(prev => ({ ...prev, startAt: e.target.value }))}
                required
              />
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="updateEndAt">End Time</Label>
              <Input
                id="updateEndAt"
                type="datetime-local"
                value={updateForm.endAt}
                onChange={(e) => setUpdateForm(prev => ({ ...prev, endAt: e.target.value }))}
                required
              />
            </div>
            
            <div className="flex justify-end gap-2">
              <Button 
                type="button" 
                variant="outline" 
                onClick={() => setShowUpdateDialog(false)}
                disabled={isSubmitting}
              >
                Cancel
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? 'Updating...' : 'Update Shift'}
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  )
}
