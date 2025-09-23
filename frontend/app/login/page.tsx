"use client"

import Link from "next/link"
import { CalendarDays } from "lucide-react"
import { LoginForm } from "@/components/auth/login-form"
import { COMPONENT_TEMPLATES } from "@/lib/style-constants"

export default function LoginPage() {
  return (
    <div className={COMPONENT_TEMPLATES.pageContainer}>
      <div className="flex items-center justify-center min-h-screen p-4">
        <div className="w-full max-w-md">
          {/* Header */}
          <div className="text-center mb-8">
            <Link href="/" className="inline-flex items-center space-x-2 mb-6">
              <CalendarDays className="h-8 w-8 text-primary" />
              <span className="text-2xl font-bold text-foreground">MyBooking</span>
            </Link>
          </div>

          <LoginForm />
        </div>
      </div>
    </div>
  )
}
