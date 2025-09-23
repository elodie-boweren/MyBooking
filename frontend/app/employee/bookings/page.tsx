"use client"

import { useEffect } from "react"
import { useRouter } from "next/navigation"
import { useEmployeeAuth } from "@/components/employee-auth-context"

export default function EmployeeBookings() {
  const { employee, isLoading } = useEmployeeAuth()
  const router = useRouter()

  useEffect(() => {
    if (!isLoading && !employee) {
      router.push("/employee/login")
    }
  }, [employee, isLoading, router])

  useEffect(() => {
    // Redirect to main bookings/reservations page for now
    if (employee) {
      router.push("/my-reservations")
    }
  }, [employee, router])

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen">Loading...</div>
  }

  return null
}
