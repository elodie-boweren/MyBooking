# Room Reservation System

A comprehensive React-based room reservation system designed to integrate with a Spring Boot backend. This application provides a modern, user-friendly interface for managing office space bookings with role-based access control.

## Features

### 🔐 Authentication & Authorization
- User registration and login
- Role-based access control (Admin, HR, Employee)
- JWT token-based authentication
- Secure password handling

### 🏢 Room Management
- Browse available meeting rooms
- Filter rooms by capacity, floor, and amenities
- Real-time availability status
- Room details with images and amenities

### 📅 Booking System
- Interactive calendar views (Day, Week, Month)
- Easy room booking with conflict detection
- Booking approval workflow
- Email notifications and reminders

### 👥 Admin Dashboard
- Comprehensive analytics and reporting
- User management with role assignment
- Room utilization statistics
- Booking approval and management

### 📊 HR Calendar
- Organization-wide calendar view
- Resource planning and scheduling
- Booking insights and patterns
- Team availability overview

## Technology Stack

### Frontend
- **Next.js 14** - React framework with App Router
- **TypeScript** - Type-safe development
- **Tailwind CSS** - Utility-first styling
- **shadcn/ui** - Modern UI components
- **SWR** - Data fetching and caching
- **Recharts** - Data visualization
- **date-fns** - Date manipulation

### Backend Integration
- **Spring Boot** - Java backend framework
- **REST API** - RESTful web services
- **JWT Authentication** - Secure token-based auth
- **MySQL/PostgreSQL** - Database support

## Project Structure

\`\`\`
├── app/                    # Next.js App Router pages
│   ├── admin/             # Admin dashboard
│   ├── calendar/          # Calendar views
│   ├── login/             # Authentication pages
│   ├── register/          
│   └── rooms/             # Room browsing and booking
├── components/            # Reusable React components
│   ├── admin/             # Admin-specific components
│   ├── calendar/          # Calendar components
│   ├── ui/                # Base UI components (shadcn/ui)
│   └── ...
├── lib/                   # Utility functions and API
│   ├── api.ts             # API client and types
│   ├── api-hooks.ts       # React hooks for API calls
│   └── utils.ts           # Helper functions
└── public/                # Static assets
\`\`\`

## Getting Started

### Prerequisites
- Node.js 18+ 
- npm or yarn
- Spring Boot backend (see API Integration section)

### Installation

1. **Clone the repository**
   \`\`\`bash
   git clone <repository-url>
   cd room-reservation-system
   \`\`\`

2. **Install dependencies**
   \`\`\`bash
   npm install
   # or
   yarn install
   \`\`\`

3. **Environment Configuration**
   Create a `.env.local` file in the root directory:
   \`\`\`env
   NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api
   \`\`\`

4. **Run the development server**
   \`\`\`bash
   npm run dev
   # or
   yarn dev
   \`\`\`

5. **Open your browser**
   Navigate to [http://localhost:3000](http://localhost:3000)

## API Integration

### Spring Boot Backend Requirements

The frontend expects the following REST API endpoints:

#### Authentication Endpoints
\`\`\`
POST /api/auth/login
POST /api/auth/register
POST /api/auth/refresh
POST /api/auth/logout
GET  /api/auth/profile
\`\`\`

#### Room Management Endpoints
\`\`\`
GET    /api/rooms
POST   /api/rooms
GET    /api/rooms/{id}
PUT    /api/rooms/{id}
DELETE /api/rooms/{id}
PATCH  /api/rooms/{id}/availability
\`\`\`

#### Booking Endpoints
\`\`\`
GET    /api/bookings
POST   /api/bookings
GET    /api/bookings/{id}
PUT    /api/bookings/{id}
DELETE /api/bookings/{id}
GET    /api/bookings/user/{userId}
GET    /api/bookings/room/{roomId}
PATCH  /api/bookings/{id}/approve
PATCH  /api/bookings/{id}/reject
\`\`\`

#### Admin Endpoints
\`\`\`
GET    /api/admin/dashboard
GET    /api/admin/stats
GET    /api/admin/users
POST   /api/admin/users
PUT    /api/admin/users/{id}
DELETE /api/admin/users/{id}
PATCH  /api/admin/users/{id}/status
\`\`\`

### Data Models

#### User Model
\`\`\`typescript
interface User {
  id: string
  firstName: string
  lastName: string
  email: string
  department: string
  role: 'admin' | 'employee' | 'hr'
  isActive: boolean
  createdAt: string
  updatedAt: string
}
\`\`\`

#### Room Model
\`\`\`typescript
interface Room {
  id: string
  name: string
  capacity: number
  floor: number
  amenities: string[]
  isAvailable: boolean
  description: string
  imageUrl?: string
  createdAt: string
  updatedAt: string
}
\`\`\`

#### Booking Model
\`\`\`typescript
interface Booking {
  id: string
  roomId: string
  userId: string
  title: string
  description?: string
  date: string
  startTime: string
  endTime: string
  attendees: string[]
  status: 'confirmed' | 'pending' | 'cancelled'
  createdAt: string
  updatedAt: string
}
\`\`\`

## Development

### Mock API
For development without a backend, the application includes mock API endpoints at `/api/mock`. Set the environment variable to use mock data:

\`\`\`env
NEXT_PUBLIC_API_BASE_URL=/api/mock
\`\`\`

### Authentication Flow
1. User submits login credentials
2. Frontend sends POST request to `/api/auth/login`
3. Backend validates credentials and returns JWT token
4. Frontend stores token in localStorage
5. Subsequent API requests include token in Authorization header

### State Management
- **SWR** for server state caching and synchronization
- **React Context** for authentication state
- **Local state** for UI interactions and forms

## Deployment

### Production Build
\`\`\`bash
npm run build
npm start
\`\`\`

### Environment Variables
Set the following environment variables for production:

\`\`\`env
NEXT_PUBLIC_API_BASE_URL=https://your-api-domain.com/api
\`\`\`

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support and questions, please contact the development team or create an issue in the repository.
\`\`\`

```json file="" isHidden
