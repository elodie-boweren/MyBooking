"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { ArrowLeft, Plus, Users, BookOpen, Calendar, CheckCircle, Clock, AlertCircle } from "lucide-react"
import { adminTrainingApi, adminEmployeesApi, Training, EmployeeTraining, Employee, EmployeeTrainingCreateRequest, UpdateTrainingStatusRequest, TrainingCreateRequest } from "@/lib/api"
import { toast } from "sonner"

export default function AdminTrainingManagement() {
  const [trainings, setTrainings] = useState<Training[]>([])
  const [employeeTrainings, setEmployeeTrainings] = useState<EmployeeTraining[]>([])
  const [employees, setEmployees] = useState<Employee[]>([])
  const [loading, setLoading] = useState(false)
  const [isLoadingEmployees, setIsLoadingEmployees] = useState(false)
  const [isAssignDialogOpen, setIsAssignDialogOpen] = useState(false)
  const [isCreateTrainingDialogOpen, setIsCreateTrainingDialogOpen] = useState(false)
  const [selectedTraining, setSelectedTraining] = useState<Training | null>(null)
  const [selectedEmployee, setSelectedEmployee] = useState<Employee | null>(null)
  const [newTraining, setNewTraining] = useState({
    title: '',
    startDate: new Date().toISOString().split('T')[0], // Today's date
    endDate: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0] // 7 days from now
  })

  // Load data on component mount
  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    setLoading(true)
    try {
      console.log("🔄 Loading training data...")
      
      // Load trainings and employees separately to identify which one is failing
      let trainingsResponse, employeesResponse
      
      try {
        console.log("📚 Fetching trainings...")
        trainingsResponse = await adminTrainingApi.getAllTrainings()
        console.log("✅ Trainings response:", trainingsResponse)
        setTrainings(trainingsResponse.content)
      } catch (error) {
        console.error("❌ Error loading trainings:", error)
        toast.error("Failed to load training programs")
        setTrainings([])
      }
      
      try {
        console.log("👥 Fetching employees...")
        setIsLoadingEmployees(true)
        employeesResponse = await adminEmployeesApi.getAllEmployees()
        console.log("✅ Employees response:", employeesResponse)
        // Ensure employees is an array
        if (Array.isArray(employeesResponse)) {
          setEmployees(employeesResponse)
        } else {
          console.error('Employees response is not an array:', employeesResponse)
          setEmployees([])
          toast.error('Invalid employees data received.')
        }
      } catch (error) {
        console.error("❌ Error loading employees:", error)
        toast.error("Failed to load employees")
        setEmployees([])
      } finally {
        setIsLoadingEmployees(false)
      }
      
      // Load employee trainings for the first training if available
      if (trainingsResponse && trainingsResponse.content.length > 0) {
        console.log("🔄 Loading employee trainings for training:", trainingsResponse.content[0].id)
        loadEmployeeTrainings(trainingsResponse.content[0].id)
      }
    } catch (error) {
      console.error("❌ General error loading data:", error)
      toast.error("Failed to load training data")
    } finally {
      setLoading(false)
    }
  }

  const loadEmployeeTrainings = async (trainingId: number) => {
    try {
      const response = await adminTrainingApi.getEmployeeTrainings(trainingId)
      setEmployeeTrainings(response.content)
    } catch (error) {
      console.error("Error loading employee trainings:", error)
      toast.error("Failed to load employee trainings")
    }
  }

  const handleCreateTraining = async () => {
    if (!newTraining.title || !newTraining.startDate || !newTraining.endDate) {
      toast.error("Please fill in all training fields")
      return
    }

    // Validate date range
    if (new Date(newTraining.startDate) >= new Date(newTraining.endDate)) {
      toast.error("Start date must be before end date")
      return
    }

    // Validate start date is not in the past
    if (new Date(newTraining.startDate) < new Date()) {
      toast.error("Start date cannot be in the past")
      return
    }

    try {
      const request: TrainingCreateRequest = {
        title: newTraining.title,
        startDate: newTraining.startDate,
        endDate: newTraining.endDate
      }

      await adminTrainingApi.createTraining(request)
      toast.success(`Training "${newTraining.title}" created successfully`)
      
      // Reset form and refresh data
      setNewTraining({ 
        title: '', 
        startDate: new Date().toISOString().split('T')[0], 
        endDate: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0] 
      })
      loadData()
      setIsCreateTrainingDialogOpen(false)
    } catch (error: any) {
      console.error("Error creating training:", error)
      
      // Handle specific error messages
      if (error.message?.includes("overlaps")) {
        toast.error("Training dates overlap with existing training. Please choose different dates.")
      } else if (error.message?.includes("past")) {
        toast.error("Training start date cannot be in the past")
      } else {
        toast.error(`Failed to create training: ${error.message || "Unknown error"}`)
      }
    }
  }

  const handleAssignTraining = async () => {
    if (!selectedTraining || !selectedEmployee) {
      toast.error("Please select both training and employee")
      return
    }

    try {
      const request: EmployeeTrainingCreateRequest = {
        employeeId: selectedEmployee.userId,
        trainingId: selectedTraining.id
      }

      await adminTrainingApi.assignTraining(request)
      toast.success(`Training "${selectedTraining.title}" assigned to ${selectedEmployee.firstName} ${selectedEmployee.lastName}`)
      
      // Refresh data
      loadData()
      setIsAssignDialogOpen(false)
      setSelectedTraining(null)
      setSelectedEmployee(null)
    } catch (error) {
      console.error("Error assigning training:", error)
      toast.error("Failed to assign training")
    }
  }

  const handleUpdateTrainingStatus = async (employeeTraining: EmployeeTraining, newStatus: "ASSIGNED" | "IN_PROGRESS" | "COMPLETED") => {
    try {
      const request: UpdateTrainingStatusRequest = { status: newStatus }
      await adminTrainingApi.updateTrainingStatus(employeeTraining.employeeId, employeeTraining.trainingId, request)
      
      toast.success(`Training status updated to ${newStatus}`)
      loadData()
    } catch (error) {
      console.error("Error updating training status:", error)
      toast.error("Failed to update training status")
    }
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case "ASSIGNED":
        return "bg-blue-100 text-blue-800 border-blue-200"
      case "IN_PROGRESS":
        return "bg-yellow-100 text-yellow-800 border-yellow-200"
      case "COMPLETED":
        return "bg-green-100 text-green-800 border-green-200"
      default:
        return "bg-gray-100 text-gray-800 border-gray-200"
    }
  }

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "ASSIGNED":
        return <AlertCircle className="h-4 w-4" />
      case "IN_PROGRESS":
        return <Clock className="h-4 w-4" />
      case "COMPLETED":
        return <CheckCircle className="h-4 w-4" />
      default:
        return <AlertCircle className="h-4 w-4" />
    }
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>
          <p className="text-muted-foreground">Loading training data...</p>
        </div>
      </div>
    )
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
            <h1 className="text-2xl font-bold text-foreground">Training Management</h1>
            <p className="text-muted-foreground">Assign and manage employee training programs</p>
          </div>
        </div>
        
        <div className="flex gap-2">
          <Button 
            variant="outline" 
            onClick={loadData}
            className="flex items-center gap-2"
          >
            <ArrowLeft className="h-4 w-4" />
            Refresh Data
          </Button>
          
          <Dialog open={isCreateTrainingDialogOpen} onOpenChange={setIsCreateTrainingDialogOpen}>
            <DialogTrigger asChild>
              <Button 
                variant="outline" 
                className="flex items-center gap-2"
                onClick={() => setIsCreateTrainingDialogOpen(true)}
              >
                <BookOpen className="h-4 w-4" />
                Create Training
              </Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Create New Training Program</DialogTitle>
                <DialogDescription>
                  Create a new training program that can be assigned to employees
                </DialogDescription>
              </DialogHeader>
              
              <div className="space-y-4">
                <div>
                  <Label htmlFor="training-title">Training Title</Label>
                  <Input
                    id="training-title"
                    value={newTraining.title}
                    onChange={(e) => setNewTraining({ ...newTraining, title: e.target.value })}
                    placeholder="Enter training title"
                  />
                </div>
                
                <div>
                  <Label htmlFor="start-date">Start Date</Label>
                  <Input
                    id="start-date"
                    type="date"
                    value={newTraining.startDate}
                    onChange={(e) => setNewTraining({ ...newTraining, startDate: e.target.value })}
                  />
                </div>
                
                <div>
                  <Label htmlFor="end-date">End Date</Label>
                  <Input
                    id="end-date"
                    type="date"
                    value={newTraining.endDate}
                    onChange={(e) => setNewTraining({ ...newTraining, endDate: e.target.value })}
                  />
                </div>
              </div>
              
              <DialogFooter>
                <Button variant="outline" onClick={() => setIsCreateTrainingDialogOpen(false)}>
                  Cancel
                </Button>
                <Button onClick={handleCreateTraining}>
                  Create Training
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
          
          <Dialog open={isAssignDialogOpen} onOpenChange={setIsAssignDialogOpen}>
            <DialogTrigger asChild>
              <Button 
                className="flex items-center gap-2"
                onClick={() => setIsAssignDialogOpen(true)}
              >
                <Plus className="h-4 w-4" />
                Assign Training
              </Button>
            </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Assign Training</DialogTitle>
              <DialogDescription>
                Select a training program and assign it to an employee
              </DialogDescription>
            </DialogHeader>
            
            <div className="space-y-4">
              <div>
                <Label htmlFor="training-select">Training Program</Label>
                <Select onValueChange={(value) => {
                  const training = trainings.find(t => t.id.toString() === value)
                  setSelectedTraining(training || null)
                }}>
                  <SelectTrigger>
                    <SelectValue placeholder="Select a training program" />
                  </SelectTrigger>
                  <SelectContent>
                    {trainings.map((training) => (
                      <SelectItem key={training.id} value={training.id.toString()}>
                        {training.title}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              
              <div>
                <Label htmlFor="employee-select">Employee</Label>
                <Select onValueChange={(value) => {
                  const employee = employees.find(e => e.userId.toString() === value)
                  setSelectedEmployee(employee || null)
                }}>
                  <SelectTrigger>
                    <SelectValue placeholder={isLoadingEmployees ? "Loading employees..." : "Select an employee"} />
                  </SelectTrigger>
                  <SelectContent>
                    {isLoadingEmployees ? (
                      <SelectItem value="loading" disabled>
                        Loading employees...
                      </SelectItem>
                    ) : employees.length === 0 ? (
                      <SelectItem value="no-employees" disabled>
                        No employees available
                      </SelectItem>
                    ) : (
                      employees.map((employee) => (
                        <SelectItem key={employee.userId} value={employee.userId.toString()}>
                          {employee.firstName} {employee.lastName} ({employee.email})
                        </SelectItem>
                      ))
                    )}
                  </SelectContent>
                </Select>
              </div>
            </div>
            
            <DialogFooter>
              <Button variant="outline" onClick={() => setIsAssignDialogOpen(false)}>
                Cancel
              </Button>
              <Button onClick={handleAssignTraining}>
                Assign Training
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
        </div>
      </div>

      {/* Debug Information */}
      <Card className="bg-yellow-50 border-yellow-200">
        <CardHeader>
          <CardTitle className="text-sm">Debug Information</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="text-sm space-y-1">
            <p>📚 Trainings loaded: {trainings.length}</p>
            <p>👥 Employees loaded: {employees.length}</p>
            <p>📋 Employee trainings: {employeeTrainings.length}</p>
            <p>🔄 Loading: {loading ? 'Yes' : 'No'}</p>
          </div>
        </CardContent>
      </Card>

      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Trainings</CardTitle>
            <BookOpen className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{trainings.length}</div>
            <p className="text-xs text-muted-foreground">Available programs</p>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Assigned</CardTitle>
            <AlertCircle className="h-4 w-4 text-blue-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {employeeTrainings.filter(et => et.status === "ASSIGNED").length}
            </div>
            <p className="text-xs text-muted-foreground">Pending assignments</p>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">In Progress</CardTitle>
            <Clock className="h-4 w-4 text-yellow-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {employeeTrainings.filter(et => et.status === "IN_PROGRESS").length}
            </div>
            <p className="text-xs text-muted-foreground">Currently active</p>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Completed</CardTitle>
            <CheckCircle className="h-4 w-4 text-green-600" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {employeeTrainings.filter(et => et.status === "COMPLETED").length}
            </div>
            <p className="text-xs text-muted-foreground">Finished training</p>
          </CardContent>
        </Card>
      </div>

      {/* Training Programs */}
      <Card>
        <CardHeader>
          <CardTitle>Available Training Programs</CardTitle>
          <CardDescription>Select a training to view assigned employees</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {trainings.map((training) => (
              <Card 
                key={training.id} 
                className={`cursor-pointer transition-colors ${
                  selectedTraining?.id === training.id ? 'ring-2 ring-primary' : 'hover:bg-muted'
                }`}
                onClick={() => {
                  setSelectedTraining(training)
                  loadEmployeeTrainings(training.id)
                }}
              >
                <CardHeader className="pb-3">
                  <CardTitle className="text-lg">{training.title}</CardTitle>
                  <CardDescription>
                    <div className="flex items-center gap-2">
                      <Calendar className="h-4 w-4" />
                      {new Date(training.startDate).toLocaleDateString()} - {new Date(training.endDate).toLocaleDateString()}
                    </div>
                  </CardDescription>
                </CardHeader>
              </Card>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Employee Training Assignments */}
      {selectedTraining && (
        <Card>
          <CardHeader>
            <CardTitle>Training Assignments - {selectedTraining.title}</CardTitle>
            <CardDescription>
              Manage employee assignments for this training program
            </CardDescription>
          </CardHeader>
          <CardContent>
            {employeeTrainings.length === 0 ? (
              <div className="text-center py-8">
                <BookOpen className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
                <p className="text-muted-foreground">No employees assigned to this training yet</p>
              </div>
            ) : (
              <div className="space-y-4">
                {employeeTrainings.map((employeeTraining) => (
                  <div key={`${employeeTraining.employeeId}-${employeeTraining.trainingId}`} className="flex items-center justify-between p-4 border rounded-lg">
                    <div className="flex items-center gap-4">
                      <div>
                        <h4 className="font-medium">{employeeTraining.employeeName}</h4>
                        <p className="text-sm text-muted-foreground">{employeeTraining.employeeEmail}</p>
                      </div>
                      <Badge className={getStatusColor(employeeTraining.status)}>
                        <div className="flex items-center gap-1">
                          {getStatusIcon(employeeTraining.status)}
                          {employeeTraining.status}
                        </div>
                      </Badge>
                    </div>
                    
                    <div className="flex items-center gap-2">
                      <Select
                        value={employeeTraining.status}
                        onValueChange={(value: "ASSIGNED" | "IN_PROGRESS" | "COMPLETED") => 
                          handleUpdateTrainingStatus(employeeTraining, value)
                        }
                      >
                        <SelectTrigger className="w-40">
                          <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="ASSIGNED">Assigned</SelectItem>
                          <SelectItem value="IN_PROGRESS">In Progress</SelectItem>
                          <SelectItem value="COMPLETED">Completed</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>
      )}
    </div>
  )
}
