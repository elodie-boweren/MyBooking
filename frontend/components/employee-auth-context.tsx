"use client"

import { createContext, useContext, useState, useEffect, type ReactNode } from "react"

interface Employee {
  id: string
  name: string
  email: string
  role: "employee" | "manager"
  department: string
  employeeId: string
  position: string
  manager?: string
  startDate: string
}

interface EmployeeAuthContextType {
  employee: Employee | null
  login: (email: string, password: string) => Promise<boolean>
  logout: () => void
  isLoading: boolean
}

const EmployeeAuthContext = createContext<EmployeeAuthContextType | undefined>(undefined)

export function EmployeeAuthProvider({ children }: { children: ReactNode }) {
  const [employee, setEmployee] = useState<Employee | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const storedEmployee = localStorage.getItem("employee")
    if (storedEmployee) {
      setEmployee(JSON.parse(storedEmployee))
    }
    setIsLoading(false)
  }, [])

  const login = async (email: string, password: string): Promise<boolean> => {
    // Mock authentication
    if (email === "employee@company.com" && password === "employee123") {
      const employeeData: Employee = {
        id: "2",
        name: "John Smith",
        email: "employee@company.com",
        role: "employee",
        department: "Operations",
        employeeId: "EMP001",
        position: "Operations Specialist",
        manager: "Sarah Johnson",
        startDate: "2023-01-15",
      }

      localStorage.setItem("employee", JSON.stringify(employeeData))
      setEmployee(employeeData)
      return true
    }
    return false
  }

  const logout = () => {
    localStorage.removeItem("employee")
    setEmployee(null)
  }

  return (
    <EmployeeAuthContext.Provider value={{ employee, login, logout, isLoading }}>
      {children}
    </EmployeeAuthContext.Provider>
  )
}

export function useEmployeeAuth() {
  const context = useContext(EmployeeAuthContext)
  if (context === undefined) {
    throw new Error("useEmployeeAuth must be used within an EmployeeAuthProvider")
  }
  return context
}
