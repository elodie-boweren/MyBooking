// Mock API endpoints for development and testing
// Remove this file when connecting to actual Spring Boot backend

import { type NextRequest, NextResponse } from "next/server"

// Mock data
const mockUsers = [
  {
    id: "1",
    firstName: "John",
    lastName: "Doe",
    email: "john.doe@company.com",
    department: "Engineering",
    role: "employee",
    isActive: true,
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-01T00:00:00Z",
  },
  {
    id: "2",
    firstName: "Jane",
    lastName: "Smith",
    email: "jane.smith@company.com",
    department: "Marketing",
    role: "admin",
    isActive: true,
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-01T00:00:00Z",
  },
]

const mockRooms = [
  {
    id: "1",
    name: "Conference Room A",
    capacity: 12,
    floor: 1,
    amenities: ["Projector", "Whiteboard", "Video Conference", "WiFi"],
    isAvailable: true,
    description: "Spacious conference room perfect for team meetings and presentations",
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-01T00:00:00Z",
  },
  {
    id: "2",
    name: "Meeting Room B",
    capacity: 6,
    floor: 2,
    amenities: ["TV Screen", "Whiteboard", "WiFi"],
    isAvailable: true,
    description: "Intimate meeting space ideal for small team discussions",
    createdAt: "2024-01-01T00:00:00Z",
    updatedAt: "2024-01-01T00:00:00Z",
  },
]

const mockBookings = [
  {
    id: "1",
    roomId: "1",
    userId: "1",
    title: "Team Standup",
    description: "Daily team synchronization meeting",
    date: "2024-01-22",
    startTime: "09:00",
    endTime: "09:30",
    attendees: ["john.doe@company.com", "jane.smith@company.com"],
    status: "confirmed",
    createdAt: "2024-01-20T00:00:00Z",
    updatedAt: "2024-01-20T00:00:00Z",
  },
]

export async function GET(request: NextRequest) {
  const { searchParams } = new URL(request.url)
  const endpoint = searchParams.get("endpoint")

  // Simulate API delay
  await new Promise((resolve) => setTimeout(resolve, 500))

  switch (endpoint) {
    case "rooms":
      return NextResponse.json({ data: mockRooms, success: true })

    case "users":
      return NextResponse.json({ data: mockUsers, success: true })

    case "bookings":
      return NextResponse.json({ data: mockBookings, success: true })

    case "dashboard-stats":
      return NextResponse.json({
        data: {
          totalRooms: 12,
          totalUsers: 156,
          totalBookings: 89,
          todayBookings: 28,
          utilizationRate: 73,
          availableRooms: 7,
          occupiedRooms: 5,
          pendingBookings: 3,
        },
        success: true,
      })

    default:
      return NextResponse.json({ message: "Endpoint not found", success: false }, { status: 404 })
  }
}

export async function POST(request: NextRequest) {
  const { searchParams } = new URL(request.url)
  const endpoint = searchParams.get("endpoint")
  const body = await request.json()

  // Simulate API delay
  await new Promise((resolve) => setTimeout(resolve, 500))

  switch (endpoint) {
    case "login":
      // Mock login validation
      if (body.email === "admin@company.com" && body.password === "password") {
        return NextResponse.json({
          data: {
            token: "mock-jwt-token",
            refreshToken: "mock-refresh-token",
            user: mockUsers[1], // Return admin user
          },
          success: true,
        })
      } else if (body.email === "john.doe@company.com" && body.password === "password") {
        return NextResponse.json({
          data: {
            token: "mock-jwt-token",
            refreshToken: "mock-refresh-token",
            user: mockUsers[0], // Return regular user
          },
          success: true,
        })
      } else {
        return NextResponse.json({ message: "Invalid credentials", success: false }, { status: 401 })
      }

    case "register":
      // Mock registration
      const newUser = {
        id: Date.now().toString(),
        ...body,
        isActive: true,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      }
      return NextResponse.json({ data: newUser, success: true })

    case "create-room":
      const newRoom = {
        id: Date.now().toString(),
        ...body,
        isAvailable: true,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      }
      return NextResponse.json({ data: newRoom, success: true })

    case "create-booking":
      const newBooking = {
        id: Date.now().toString(),
        userId: "1", // Mock current user
        ...body,
        status: "pending",
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      }
      return NextResponse.json({ data: newBooking, success: true })

    default:
      return NextResponse.json({ message: "Endpoint not found", success: false }, { status: 404 })
  }
}
