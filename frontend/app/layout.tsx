import type React from "react"
import type { Metadata } from "next"
import "./globals.css"
import { ClientLayout } from "./client-layout"
import { AuthProvider } from "@/lib/auth-context"

export const metadata: Metadata = {
  title: "MyBooking - Professional Room Booking Solution",
  description: "Streamline your hotel management and bookingwith our comprehensive room reservation system",
  generator: "v0.app",
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <AuthProvider>
      <ClientLayout>{children}</ClientLayout>
    </AuthProvider>
  )
}
