"use client"

import { useAuth, useIsClient, useIsEmployee, useIsAdmin } from '@/lib/auth-context'
import { useRouter } from 'next/navigation'
import { useEffect, ReactNode } from 'react'
import { COMPONENT_TEMPLATES } from '@/lib/style-constants'

// Protected route props
interface ProtectedRouteProps {
  children: ReactNode
  requiredRole?: 'CLIENT' | 'EMPLOYEE' | 'ADMIN'
  fallbackPath?: string
  showLoading?: boolean
}

// Loading component
function AuthLoading() {
  return (
    <div className={COMPONENT_TEMPLATES.pageContainer}>
      <div className={COMPONENT_TEMPLATES.pageContent}>
        <div className="flex items-center justify-center min-h-[400px]">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto mb-4"></div>
            <p className="text-muted-foreground">Authenticating...</p>
          </div>
        </div>
      </div>
    </div>
  )
}

// Unauthorized component
function Unauthorized({ requiredRole, fallbackPath }: { requiredRole?: string; fallbackPath?: string }) {
  const router = useRouter()
  
  useEffect(() => {
    if (fallbackPath) {
      router.push(fallbackPath)
    }
  }, [router, fallbackPath])

  return (
    <div className={COMPONENT_TEMPLATES.pageContainer}>
      <div className={COMPONENT_TEMPLATES.pageContent}>
        <div className="flex items-center justify-center min-h-[400px]">
          <div className="text-center">
            <div className="text-6xl mb-4">🔒</div>
            <h1 className="text-2xl font-bold text-foreground mb-2">Access Denied</h1>
            <p className="text-muted-foreground mb-4">
              {requiredRole 
                ? `This page requires ${requiredRole} access.`
                : 'You need to be logged in to access this page.'
              }
            </p>
            <button
              onClick={() => router.push('/login')}
              className="bg-primary text-primary-foreground px-4 py-2 rounded-md hover:bg-primary/90 transition-colors"
            >
              Go to Login
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}

// Main protected route component
export function ProtectedRoute({ 
  children, 
  requiredRole, 
  fallbackPath = '/login',
  showLoading = true 
}: ProtectedRouteProps) {
  const { isAuthenticated, isLoading } = useAuth()
  const isClient = useIsClient()
  const isEmployee = useIsEmployee()
  const isAdmin = useIsAdmin()

  // Show loading state
  if (isLoading && showLoading) {
    return <AuthLoading />
  }

  // Check authentication
  if (!isAuthenticated) {
    return <Unauthorized fallbackPath={fallbackPath} />
  }

  // Check role-based access
  if (requiredRole) {
    const hasRequiredRole = 
      (requiredRole === 'CLIENT' && isClient) ||
      (requiredRole === 'EMPLOYEE' && isEmployee) ||
      (requiredRole === 'ADMIN' && isAdmin)

    if (!hasRequiredRole) {
      return <Unauthorized requiredRole={requiredRole} fallbackPath={fallbackPath} />
    }
  }

  // Render protected content
  return <>{children}</>
}

// Convenience components for specific roles
export function ClientRoute({ children, fallbackPath }: { children: ReactNode; fallbackPath?: string }) {
  return (
    <ProtectedRoute requiredRole="CLIENT" fallbackPath={fallbackPath}>
      {children}
    </ProtectedRoute>
  )
}

export function EmployeeRoute({ children, fallbackPath }: { children: ReactNode; fallbackPath?: string }) {
  return (
    <ProtectedRoute requiredRole="EMPLOYEE" fallbackPath={fallbackPath}>
      {children}
    </ProtectedRoute>
  )
}

export function AdminRoute({ children, fallbackPath }: { children: ReactNode; fallbackPath?: string }) {
  return (
    <ProtectedRoute requiredRole="ADMIN" fallbackPath={fallbackPath}>
      {children}
    </ProtectedRoute>
  )
}

// Higher-order component for protecting pages
export function withAuth<T extends object>(
  Component: React.ComponentType<T>,
  requiredRole?: 'CLIENT' | 'EMPLOYEE' | 'ADMIN'
) {
  return function AuthenticatedComponent(props: T) {
    return (
      <ProtectedRoute requiredRole={requiredRole}>
        <Component {...props} />
      </ProtectedRoute>
    )
  }
}
