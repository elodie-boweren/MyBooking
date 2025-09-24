"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { 
  Calendar, 
  Clock, 
  CheckCircle, 
  XCircle, 
  AlertCircle,
  Plus,
  Filter,
  Search,
  RefreshCw,
  ArrowLeft
} from "lucide-react"
import { employeeApi, EmployeeLeaveRequest, CreateLeaveRequestRequest } from "@/lib/api"

interface LeaveRequestFormData {
  fromDate: string
  toDate: string
  reason: string
}

export default function EmployeeLeaveRequests() {
  const [leaveRequests, setLeaveRequests] = useState<EmployeeLeaveRequest[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [showCreateDialog, setShowCreateDialog] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [filterStatus, setFilterStatus] = useState<string>("all")
  const [searchTerm, setSearchTerm] = useState("")
  
  const [formData, setFormData] = useState<LeaveRequestFormData>({
    fromDate: "",
    toDate: "",
    reason: ""
  })

  // Fetch leave requests on component mount
  useEffect(() => {
    fetchLeaveRequests()
  }, [])

  const fetchLeaveRequests = async () => {
    try {
      setIsLoading(true)
      setError(null)
      
      const response = await employeeApi.getLeaveRequests()
      setLeaveRequests(response.content)
    } catch (error) {
      console.error('Error fetching leave requests:', error)
      setError('Failed to load leave requests. Please try again.')
    } finally {
      setIsLoading(false)
    }
  }

  const handleCreateLeaveRequest = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!formData.fromDate || !formData.toDate || !formData.reason.trim()) {
      setError('Please fill in all fields')
      return
    }

    if (new Date(formData.fromDate) > new Date(formData.toDate)) {
      setError('End date must be after start date')
      return
    }

    try {
      setIsSubmitting(true)
      setError(null)
      
      const request: CreateLeaveRequestRequest = {
        fromDate: formData.fromDate,
        toDate: formData.toDate,
        reason: formData.reason.trim()
      }
      
      await employeeApi.createLeaveRequest(request)
      
      // Reset form and close dialog
      setFormData({ fromDate: "", toDate: "", reason: "" })
      setShowCreateDialog(false)
      
      // Refresh leave requests
      await fetchLeaveRequests()
    } catch (error) {
      console.error('Error creating leave request:', error)
      setError('Failed to create leave request. Please try again.')
    } finally {
      setIsSubmitting(false)
    }
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'APPROVED': return 'bg-green-100 text-green-800'
      case 'REJECTED': return 'bg-red-100 text-red-800'
      case 'PENDING': return 'bg-yellow-100 text-yellow-800'
      default: return 'bg-gray-100 text-gray-800'
    }
  }

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'APPROVED': return <CheckCircle className="h-4 w-4 text-green-600" />
      case 'REJECTED': return <XCircle className="h-4 w-4 text-red-600" />
      case 'PENDING': return <Clock className="h-4 w-4 text-yellow-600" />
      default: return <AlertCircle className="h-4 w-4 text-gray-600" />
    }
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    })
  }

  const calculateDays = (fromDate: string, toDate: string) => {
    const start = new Date(fromDate)
    const end = new Date(toDate)
    const diffTime = Math.abs(end.getTime() - start.getTime())
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1
    return diffDays
  }

  // Filter and search leave requests
  const filteredRequests = leaveRequests.filter(request => {
    const matchesStatus = filterStatus === "all" || request.status === filterStatus
    const matchesSearch = searchTerm === "" || 
      request.reason.toLowerCase().includes(searchTerm.toLowerCase()) ||
      request.fromDate.includes(searchTerm) ||
      request.toDate.includes(searchTerm)
    
    return matchesStatus && matchesSearch
  })

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button 
            variant="outline" 
            size="sm"
            onClick={() => window.location.href = '/employee/dashboard'}
            className="flex items-center gap-2"
          >
            <ArrowLeft className="h-4 w-4" />
            Back to Dashboard
          </Button>
          <div>
            <h2 className="text-2xl font-bold text-gray-900">Leave Requests</h2>
            <p className="text-gray-600 mt-1">Manage your leave requests and track their status</p>
          </div>
        </div>
        <Dialog open={showCreateDialog} onOpenChange={setShowCreateDialog}>
          <DialogTrigger asChild>
            <Button className="flex items-center gap-2">
              <Plus className="h-4 w-4" />
              Request Leave
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-md">
            <DialogHeader>
              <DialogTitle>Create Leave Request</DialogTitle>
              <DialogDescription>
                Submit a new leave request. Please provide accurate dates and a clear reason.
              </DialogDescription>
            </DialogHeader>
            
            <form onSubmit={handleCreateLeaveRequest} className="space-y-4">
              {error && (
                <Alert variant="destructive">
                  <AlertCircle className="h-4 w-4" />
                  <AlertDescription>{error}</AlertDescription>
                </Alert>
              )}
              
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="fromDate">From Date</Label>
                  <Input
                    id="fromDate"
                    type="date"
                    value={formData.fromDate}
                    onChange={(e) => setFormData(prev => ({ ...prev, fromDate: e.target.value }))}
                    required
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="toDate">To Date</Label>
                  <Input
                    id="toDate"
                    type="date"
                    value={formData.toDate}
                    onChange={(e) => setFormData(prev => ({ ...prev, toDate: e.target.value }))}
                    required
                  />
                </div>
              </div>
              
              <div className="space-y-2">
                <Label htmlFor="reason">Reason</Label>
                <Textarea
                  id="reason"
                  placeholder="Please provide a reason for your leave request..."
                  value={formData.reason}
                  onChange={(e) => setFormData(prev => ({ ...prev, reason: e.target.value }))}
                  rows={3}
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
                  {isSubmitting ? (
                    <>
                      <RefreshCw className="h-4 w-4 mr-2 animate-spin" />
                      Submitting...
                    </>
                  ) : (
                    'Submit Request'
                  )}
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      {/* Filters and Search */}
      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2">
          <Filter className="h-4 w-4" />
          <select
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value)}
            className="px-3 py-2 border border-gray-300 rounded-md text-sm"
          >
            <option value="all">All Status</option>
            <option value="PENDING">Pending</option>
            <option value="APPROVED">Approved</option>
            <option value="REJECTED">Rejected</option>
          </select>
        </div>
        
        <div className="flex items-center gap-2 flex-1">
          <Search className="h-4 w-4" />
          <Input
            placeholder="Search leave requests..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="max-w-sm"
          />
        </div>
        
        <Button variant="outline" size="sm" onClick={fetchLeaveRequests} disabled={isLoading}>
          <RefreshCw className={`h-4 w-4 ${isLoading ? 'animate-spin' : ''}`} />
        </Button>
      </div>

      {/* Leave Requests List */}
      {isLoading ? (
        <div className="text-center py-8">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Loading leave requests...</p>
        </div>
      ) : error ? (
        <Alert variant="destructive">
          <AlertCircle className="h-4 w-4" />
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      ) : filteredRequests.length === 0 ? (
        <div className="text-center py-8 text-gray-500">
          <Calendar className="h-12 w-12 mx-auto mb-4 opacity-50" />
          <p>No leave requests found</p>
          {searchTerm && <p className="text-sm mt-2">Try adjusting your search criteria</p>}
        </div>
      ) : (
        <div className="space-y-4">
          {filteredRequests.map((request) => (
            <Card key={request.id} className="hover:shadow-md transition-shadow">
              <CardContent className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-3 mb-2">
                      <h3 className="font-semibold text-lg">
                        {formatDate(request.fromDate)} - {formatDate(request.toDate)}
                      </h3>
                      <Badge className={getStatusColor(request.status)}>
                        {getStatusIcon(request.status)}
                        <span className="ml-1">{request.status}</span>
                      </Badge>
                    </div>
                    
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm text-gray-600">
                      <div>
                        <span className="font-medium">Duration:</span> {calculateDays(request.fromDate, request.toDate)} days
                      </div>
                      <div>
                        <span className="font-medium">Submitted:</span> {formatDate(request.createdAt)}
                      </div>
                      <div>
                        <span className="font-medium">Employee:</span> {request.employeeName}
                      </div>
                    </div>
                    
                    <div className="mt-3">
                      <span className="font-medium text-sm">Reason:</span>
                      <p className="text-sm text-gray-700 mt-1">{request.reason}</p>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
