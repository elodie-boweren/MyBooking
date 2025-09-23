import type React from "react"
import { EmployeeAuthProvider } from "@/components/employee-auth-context"

export default function EmployeeLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return <EmployeeAuthProvider>{children}</EmployeeAuthProvider>
}
