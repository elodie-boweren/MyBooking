import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { CalendarDays, Users, Shield } from "lucide-react"

export default function HomePage() {
  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="border-b border-border bg-card">
        <div className="container mx-auto px-4 py-4 flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <CalendarDays className="h-8 w-8 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">RoomReserve</h1>
          </div>
          <div className="flex items-center space-x-4">
            <Link href="/login">
              <Button variant="outline">Login</Button>
            </Link>
            <Link href="/register">
              <Button>Get Started</Button>
            </Link>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="py-20 px-4">
        <div className="container mx-auto text-center">
          <h2 className="text-4xl md:text-6xl font-bold text-foreground mb-6 text-balance">
            Streamline Your Office Space Management
          </h2>
          <p className="text-xl text-muted-foreground mb-8 max-w-2xl mx-auto text-pretty">
            Effortlessly book meeting rooms, manage schedules, and optimize your workspace with our comprehensive room
            reservation system.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link href="/register">
              <Button size="lg" className="w-full sm:w-auto">
                Start Booking Rooms
              </Button>
            </Link>
            <Link href="/rooms">
              <Button variant="outline" size="lg" className="w-full sm:w-auto bg-transparent">
                View Available Rooms
              </Button>
            </Link>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-16 px-4 bg-muted/50">
        <div className="container mx-auto">
          <h3 className="text-3xl font-bold text-center text-foreground mb-12">
            Everything You Need for Room Management
          </h3>
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            <Card>
              <CardHeader>
                <CalendarDays className="h-12 w-12 text-primary mb-4" />
                <CardTitle>Easy Booking</CardTitle>
                <CardDescription>Book rooms instantly with our intuitive calendar interface</CardDescription>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">
                  Select your preferred time slot, room capacity, and amenities with just a few clicks.
                </p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <Users className="h-12 w-12 text-primary mb-4" />
                <CardTitle>Team Management</CardTitle>
                <CardDescription>Manage user access and permissions efficiently</CardDescription>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">
                  Control who can book rooms, set approval workflows, and manage team schedules.
                </p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <Shield className="h-12 w-12 text-primary mb-4" />
                <CardTitle>Admin Dashboard</CardTitle>
                <CardDescription>Comprehensive analytics and room utilization insights</CardDescription>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">
                  Track usage patterns, generate reports, and optimize your office space allocation.
                </p>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-border bg-card py-8 px-4">
        <div className="container mx-auto text-center">
          <p className="text-muted-foreground">© 2024 RoomReserve. Built for modern office management.</p>
        </div>
      </footer>
    </div>
  )
}
