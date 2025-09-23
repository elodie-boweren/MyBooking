// API configuration and utility functions for Spring Boot backend integration

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080/api"

// Enhanced API response types matching Spring Boot structure
export interface ApiResponse<T> {
  data?: T
  message?: string
  success?: boolean
  timestamp?: string
  status?: number
  error?: string
  path?: string
}

export interface PaginatedResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  currentPage: number
  pageSize: number
  first: boolean
  last: boolean
  numberOfElements: number
  empty: boolean
  pageable: {
    pageNumber: number
    pageSize: number
    sort: {
      sorted: boolean
      unsorted: boolean
      empty: boolean
    }
    offset: number
    paged: boolean
    unpaged: boolean
  }
  sort: {
    sorted: boolean
    unsorted: boolean
    empty: boolean
  }
}

// Enhanced error handling for Spring Boot responses
export class ApiError extends Error {
  constructor(
    message: string,
    public status: number,
    public response?: any,
    public timestamp?: string,
    public path?: string
  ) {
    super(message)
    this.name = "ApiError"
  }
}

// Enhanced API client with better error handling
class ApiClient {
  private baseURL: string

  constructor(baseURL: string) {
    this.baseURL = baseURL
  }

  private async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const url = `${this.baseURL}${endpoint}`

    // Get token from localStorage
    const token = typeof window !== "undefined" ? localStorage.getItem("token") : null

    const config: RequestInit = {
      headers: {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
        ...options.headers,
      },
      ...options,
    }

    try {
      const response = await fetch(url, config)

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}))
        throw new ApiError(
          errorData.message || `HTTP error! status: ${response.status}`,
          response.status,
          errorData,
          errorData.timestamp,
          errorData.path
        )
      }

      return await response.json()
    } catch (error) {
      if (error instanceof ApiError) {
        throw error
      }
      throw new ApiError("Network error occurred", 0, error)
    }
  }

  async get<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: "GET" })
  }

  async post<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: "POST",
      body: data ? JSON.stringify(data) : undefined,
    })
  }

  async put<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: "PUT",
      body: data ? JSON.stringify(data) : undefined,
    })
  }

  async patch<T>(endpoint: string, data?: any): Promise<T> {
    return this.request<T>(endpoint, {
      method: "PATCH",
      body: data ? JSON.stringify(data) : undefined,
    })
  }

  async delete<T>(endpoint: string): Promise<T> {
    return this.request<T>(endpoint, { method: "DELETE" })
  }
}

// Create API client instance
export const apiClient = new ApiClient(API_BASE_URL)

// API endpoints configuration matching Spring Boot backend
export const API_ENDPOINTS = {
      // Authentication
      AUTH: {
        LOGIN: "/auth/login",
        REGISTER: "/auth/register",
        PROFILE: "/auth/profile",
        CHANGE_PASSWORD: "/auth/change-password",
      },

      // Admin User Management
      ADMIN_USERS: {
        ALL: "/auth/users",
        GET: (id: string) => `/auth/users/${id}`,
        CREATE: "/auth/users",
        UPDATE: (id: string) => `/auth/users/${id}`,
        DELETE: (id: string) => `/auth/users/${id}`,
        BY_ROLE: (role: string) => `/auth/users/role/${role}`,
      },

  // Rooms
  ROOMS: {
    LIST: "/rooms",
    GET: (id: string) => `/rooms/${id}`,
    AVAILABILITY: (id: string) => `/rooms/${id}/availability`,
    SEARCH: "/rooms",
  },

  // Admin Room Management
  ADMIN_ROOMS: {
    ALL: "/rooms",
    GET: (id: string) => `/rooms/${id}`,
    CREATE: "/rooms",
    UPDATE: (id: string) => `/rooms/${id}`,
    DELETE: (id: string) => `/rooms/${id}`,
    SEARCH: "/rooms",
  },

  // Room Photo Management
  ROOM_PHOTOS: {
    ADD: (roomId: string) => `/rooms/${roomId}/photos`,
    GET: (roomId: string) => `/rooms/${roomId}/photos`,
    DELETE: (photoId: string) => `/rooms/photos/${photoId}`,
    SET_PRIMARY: (photoId: string) => `/rooms/photos/${photoId}/primary`,
  },

  // Reservations - Client
  CLIENT_RESERVATIONS: {
    CREATE: "/client/reservations",
    MY: "/client/reservations/my",
    GET: (id: string) => `/client/reservations/${id}`,
    SEARCH: "/client/reservations/search",
    CANCEL: (id: string) => `/client/reservations/${id}/cancel`,
  },

  // Reservations - Admin
  ADMIN_RESERVATIONS: {
    ALL: "/admin/reservations",
    GET: (id: string) => `/admin/reservations/${id}`,
    SEARCH: "/admin/reservations/search",
    UPDATE: (id: string) => `/admin/reservations/${id}`,
    DELETE: (id: string) => `/admin/reservations/${id}`,
  },

  // Events
  EVENTS: {
    LIST: "/events",
    GET: (id: string) => `/events/${id}`,
    CREATE: "/events",
    UPDATE: (id: string) => `/events/${id}`,
    DELETE: (id: string) => `/events/${id}`,
    SEARCH: "/events/search",
  },

  // Event Bookings
  EVENT_BOOKINGS: {
    CREATE: "/event-bookings",
    MY: "/event-bookings/my",
    GET: (id: string) => `/event-bookings/${id}`,
    CANCEL: (id: string) => `/event-bookings/${id}/cancel`,
  },

  // Admin Event Bookings
  ADMIN_EVENT_BOOKINGS: {
    ALL: "/admin/event-bookings",
    GET: (id: string) => `/admin/event-bookings/${id}`,
    SEARCH: "/admin/event-bookings/search",
    UPDATE: (id: string) => `/admin/event-bookings/${id}`,
  },

  // Feedback
  FEEDBACK: {
    CREATE: "/feedback",
    MY: "/feedback/my",
    GET: (id: string) => `/feedback/${id}`,
    UPDATE: (id: string) => `/feedback/${id}`,
    DELETE: (id: string) => `/feedback/${id}`,
  },

  // Admin Feedback
  ADMIN_FEEDBACK: {
    ALL: "/admin/feedback",
    GET: (id: string) => `/admin/feedback/${id}`,
    SEARCH: "/admin/feedback/search",
    STATISTICS: "/admin/feedback/statistics",
    REPLY: (id: string) => `/admin/feedback/${id}/reply`,
  },

  // Loyalty
  LOYALTY: {
    ACCOUNT: "/loyalty/account",
    BALANCE: "/loyalty/balance",
    TRANSACTIONS: "/loyalty/transactions",
    REDEEM: "/loyalty/redeem",
    STATISTICS: "/loyalty/statistics",
  },

  // Admin Loyalty
  ADMIN_LOYALTY: {
    ACCOUNTS: "/admin/loyalty/accounts",
    ACCOUNT: (id: string) => `/admin/loyalty/accounts/${id}`,
    ACCOUNT_BY_USER: (userId: string) => `/admin/loyalty/accounts/user/${userId}`,
    TRANSACTIONS: "/admin/loyalty/transactions",
    STATISTICS: "/admin/loyalty/statistics",
  },

  // Employees
  EMPLOYEES: {
    MY_PROFILE: "/employees/my-profile",
    MY_TASKS: "/employees/my-tasks",
    MY_SHIFTS: "/employees/my-shifts",
    MY_TRAINING: "/employees/my-training",
    MY_LEAVE: "/employees/my-leave",
    TASK_UPDATE: (id: string) => `/employees/tasks/${id}`,
    LEAVE_REQUEST: "/employees/leave-requests",
  },

  // Admin Employees
  ADMIN_EMPLOYEES: {
    ALL: "/admin/employees",
    GET: (id: string) => `/admin/employees/${id}`,
    CREATE: "/admin/employees",
    UPDATE: (id: string) => `/admin/employees/${id}`,
    DELETE: (id: string) => `/admin/employees/${id}`,
    SEARCH: "/admin/employees/search",
    TASKS: "/admin/employees/tasks",
    SHIFTS: "/admin/employees/shifts",
    TRAINING: "/admin/employees/training",
    LEAVE: "/admin/employees/leave",
    STATISTICS: "/admin/employees/statistics",
  },

  // Analytics
  ANALYTICS: {
    DASHBOARD: "/admin/analytics/dashboard",
    REVENUE: "/admin/analytics/revenue",
    REVENUE_TRENDS: "/admin/analytics/revenue/trends",
    OCCUPANCY: "/admin/analytics/occupancy",
    CUSTOMERS: "/admin/analytics/customers",
    EMPLOYEE_PERFORMANCE: "/admin/analytics/employees/performance",
    EMPLOYEE_PRODUCTIVITY: "/admin/analytics/employees/productivity",
    KPIS: "/admin/analytics/kpis",
    SYSTEM_PERFORMANCE: "/admin/analytics/system-performance",
  },

  // Announcements
  ANNOUNCEMENTS: {
    LIST: "/announcements",
    GET: (id: string) => `/announcements/${id}`,
  },

  // Admin Announcements
  ADMIN_ANNOUNCEMENTS: {
    ALL: "/admin/announcements",
    CREATE: "/admin/announcements",
    GET: (id: string) => `/admin/announcements/${id}`,
    UPDATE: (id: string) => `/admin/announcements/${id}`,
    DELETE: (id: string) => `/admin/announcements/${id}`,
  },
} as const

// ==================== CORRECTED TYPE DEFINITIONS ====================

// User interface - CORRECTED with phone, address, birthDate
export interface User {
  id: number
  firstName: string
  lastName: string
  email: string
  phone: string
  address: string
  birthDate: string // LocalDate from backend
  role: "CLIENT" | "EMPLOYEE" | "ADMIN"
  createdAt: string
  updatedAt: string
}

// Room interface - CORRECTED with proper status enum
export interface Room {
  id: number
  number: string // Changed from roomNumber to number
  roomType: "SINGLE" | "DOUBLE" | "DELUXE" | "FAMILY"
  capacity: number
  price: number
  currency: string
  status: "AVAILABLE" | "OCCUPIED" | "OUT_OF_SERVICE" // CORRECTED: OUT_OF_SERVICE instead of MAINTENANCE
  description?: string
  equipment?: string
  createdAt: string
  updatedAt: string
}

// Room Photo interface
export interface RoomPhoto {
  id: number
  photoUrl: string
  caption?: string
  roomId: number
  displayOrder: number
  isPrimary: boolean
  isActive: boolean
  photoType?: string
  fileSize?: number
  fileName?: string
  createdAt: string
  updatedAt: string
}

// Create Room Request interface
export interface CreateRoomRequest {
  number: string
  roomType: "SINGLE" | "DOUBLE" | "DELUXE" | "FAMILY"
  capacity: number
  price: number
  currency: string
  description?: string
}

// Add Room Photo Request interface
export interface AddRoomPhotoRequest {
  photoUrl: string
  caption?: string
  isPrimary?: boolean
}

// Reservation interface - matches backend exactly
export interface Reservation {
  id: number
  checkIn: string
  checkOut: string
  numberOfGuests: number
  totalPrice: number
  currency: string
  status: "CONFIRMED" | "CANCELLED"
  clientId: number
  clientName: string
  clientEmail: string
  roomId: number
  roomNumber: string
  roomType: string
  createdAt: string
  updatedAt: string
}

// Event interface - matches backend exactly
export interface Event {
  id: number
  title: string
  description: string
  eventType: "CONFERENCE" | "WORKSHOP" | "MEETING" | "TRAINING" | "SOCIAL"
  maxCapacity: number
  price: number
  currency: string
  isActive: boolean
  createdAt: string
  updatedAt: string
}

// Event Booking interface - matches backend exactly
export interface EventBooking {
  id: number
  eventId: number
  eventTitle: string
  clientId: number
  clientName: string
  clientEmail: string
  bookingDate: string
  numberOfAttendees: number
  totalPrice: number
  currency: string
  status: "CONFIRMED" | "CANCELLED"
  createdAt: string
  updatedAt: string
}

// Feedback interface - CORRECTED with proper structure
export interface Feedback {
  id: number
  reservationId: number
  reservationNumber: string
  userId: number
  userName: string
  userEmail: string
  rating: number // 1-5
  comment?: string
  createdAt: string
  updatedAt: string
  replies?: FeedbackReply[] // Added feedback replies
  replyCount?: number
}

// Feedback Reply interface - NEW, matches backend exactly
export interface FeedbackReply {
  id: number
  feedbackId: number
  adminUserId: number
  adminUserName: string
  adminUserEmail: string
  message: string
  createdAt: string
  updatedAt: string
}

// Loyalty Account interface - matches backend exactly
export interface LoyaltyAccount {
  id: number
  userId: number
  clientName: string
  clientEmail: string
  pointsBalance: number
  totalPointsEarned: number
  totalPointsRedeemed: number
  createdAt: string
  updatedAt: string
}

// Loyalty Transaction interface - matches backend exactly
export interface LoyaltyTransaction {
  id: number
  accountId: number
  transactionType: "EARN" | "REDEEM"
  points: number
  description: string
  reservationId?: number
  createdAt: string
}

// Employee interface - matches backend exactly
export interface Employee {
  id: number
  userId: number
  employeeNumber: string
  jobTitle: string
  department: string
  hireDate: string
  salary: number
  status: "ACTIVE" | "INACTIVE"
  user: User
  createdAt: string
  updatedAt: string
}

// Task interface - matches backend exactly
export interface Task {
  id: number
  title: string
  description: string
  priority: "LOW" | "MEDIUM" | "HIGH"
  status: "TODO" | "IN_PROGRESS" | "DONE" // CORRECTED: matches TaskStatus enum
  assignedTo: number
  assignedToName: string
  dueDate: string
  createdAt: string
  updatedAt: string
}

// Shift interface - CORRECTED: No status field, matches backend exactly
export interface Shift {
  id: number
  employeeId: number
  employeeName: string
  employeeEmail: string
  startAt: string // LocalDateTime from backend
  endAt: string // LocalDateTime from backend
  // NO STATUS FIELD - matches backend Shift entity
}

// Leave Request interface - matches backend exactly
export interface LeaveRequest {
  id: number
  employeeId: number
  employeeName: string
  startDate: string
  endDate: string
  reason: string
  status: "PENDING" | "APPROVED" | "REJECTED"
  createdAt: string
  updatedAt: string
}

// Training interface - matches backend exactly
export interface Training {
  id: number
  title: string
  description: string
  duration: number
  startDate: string
  endDate: string
  maxParticipants: number
  status: "SCHEDULED" | "IN_PROGRESS" | "COMPLETED" | "CANCELLED"
  createdAt: string
  updatedAt: string
}

// Employee Training interface - matches backend exactly
export interface EmployeeTraining {
  id: number
  employeeId: number
  employeeName: string
  trainingId: number
  trainingTitle: string
  status: "ASSIGNED" | "COMPLETED"
  completionDate?: string
  createdAt: string
  updatedAt: string
}

// Announcement interface - matches backend exactly
export interface Announcement {
  id: number
  title: string
  content: string
  priority: "LOW" | "MEDIUM" | "HIGH"
  isActive: boolean
  createdAt: string
  updatedAt: string
}

// ==================== AUTHENTICATION TYPES ====================

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  firstName: string
  lastName: string
  email: string
  password: string
  phone: string
  address: string
  birthDate: string
  role?: "CLIENT" | "EMPLOYEE"
}

// LoginResponse - CORRECTED to match backend exactly
export interface LoginResponse {
  token: string
  tokenType: string
  userId: number
  email: string
  firstName: string
  lastName: string
  role: string
  expiresAt: string
  client: boolean
  employee: boolean
  admin: boolean
  roleDisplayName: string
  tokenValid: boolean
  fullName: string
}

// ==================== REQUEST/RESPONSE DTOS ====================

export interface CreateReservationRequest {
  roomId: number
  checkIn: string
  checkOut: string
  numberOfGuests: number
}

export interface CreateEventBookingRequest {
  eventId: number
  bookingDate: string
  numberOfAttendees: number
}

export interface CreateFeedbackRequest {
  reservationId: number
  rating: number
  comment?: string
}

export interface CreateFeedbackReplyRequest {
  message: string
}

export interface RedeemPointsRequest {
  points: number
  description: string
}

export interface CreateLeaveRequestRequest {
  startDate: string
  endDate: string
  reason: string
}

export interface UpdateTaskRequest {
  status: "TODO" | "IN_PROGRESS" | "DONE"
  comment?: string
}

// ==================== ANALYTICS DTOS ====================

export interface DashboardData {
  revenueSummary: Record<string, any>
  occupancySummary: Record<string, any>
  customerInsights: Record<string, any>
  employeePerformance: Record<string, any>
  operationalKPIs: Record<string, any>
  systemPerformance: Record<string, any>
}

export interface RevenueAnalytics {
  roomRevenue: number
  eventRevenue: number
  totalRevenue: number
  revenueByService: Record<string, number>
  avgRevenuePerCustomer: number
}

export interface OccupancyMetrics {
  roomOccupancyRate: number
  occupiedRoomNights: number
  totalRoomNights: number
  installationUtilizationRate: number
  usedInstallations: number
  totalInstallations: number
}

export interface CustomerInsights {
  totalCustomers: number
  activeLoyaltyUsers: number
  loyaltyEngagementRate: number
  customerFeedback: Record<string, any>
}

export interface EmployeePerformance {
  taskMetrics: Record<string, any>
  trainingMetrics: Record<string, any>
  leaveMetrics: Record<string, any>
}

export interface EmployeeProductivity {
  totalEmployees: number
  activeEmployees: number
  activeEmployeeRate: number
  taskProductivity: Record<string, any>
}

export interface OperationalKPIs {
  totalRooms: number
  totalInstallations: number
  totalEmployees: number
  totalCustomers: number
  serviceMetrics: Record<string, any>
  resourceUtilization: Record<string, any>
}

export interface SystemPerformance {
  bookingSuccessRate: number
  responseMetrics: Record<string, any>
  healthIndicators: Record<string, any>
}
