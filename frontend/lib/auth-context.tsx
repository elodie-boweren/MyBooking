"use client"

import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react'
import { apiClient, API_ENDPOINTS, LoginRequest, RegisterRequest, LoginResponse, User } from './api'

// Auth state interface
interface AuthState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
  role: 'CLIENT' | 'EMPLOYEE' | 'ADMIN' | null
}

// Auth context interface
interface AuthContextType extends AuthState {
  login: (credentials: LoginRequest) => Promise<{ success: boolean; error?: string }>
  register: (userData: RegisterRequest) => Promise<{ success: boolean; error?: string }>
  logout: () => void
  refreshToken: () => Promise<boolean>
  updateUser: (user: User) => void
}

// Create auth context
const AuthContext = createContext<AuthContextType | undefined>(undefined)

// Auth provider props
interface AuthProviderProps {
  children: ReactNode
}

// Auth provider component
export function AuthProvider({ children }: AuthProviderProps) {
  const [authState, setAuthState] = useState<AuthState>({
    user: null,
    token: null,
    isAuthenticated: false,
    isLoading: true,
    role: null,
  })

  // Initialize auth state from localStorage
  useEffect(() => {
    const initializeAuth = async () => {
      try {
        const token = localStorage.getItem('token')
        const userStr = localStorage.getItem('user')
        
        if (token && userStr) {
          const user = JSON.parse(userStr)
          
          // Verify token is still valid by fetching user profile
          try {
            const response = await apiClient.get<User>(API_ENDPOINTS.AUTH.PROFILE)
            
            setAuthState({
              user: response,
              token,
              isAuthenticated: true,
              isLoading: false,
              role: response.role as 'CLIENT' | 'EMPLOYEE' | 'ADMIN',
            })
          } catch (error) {
            // Token is invalid, clear storage
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            setAuthState({
              user: null,
              token: null,
              isAuthenticated: false,
              isLoading: false,
              role: null,
            })
          }
        } else {
          setAuthState(prev => ({ ...prev, isLoading: false }))
        }
      } catch (error) {
        console.error('Auth initialization error:', error)
        setAuthState({
          user: null,
          token: null,
          isAuthenticated: false,
          isLoading: false,
          role: null,
        })
      }
    }

    initializeAuth()
  }, [])

  // Login function
  const login = async (credentials: LoginRequest): Promise<{ success: boolean; error?: string }> => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true }))
      
      const response = await apiClient.post<LoginResponse>(API_ENDPOINTS.AUTH.LOGIN, credentials)
      
      // Store token and user data
      localStorage.setItem('token', response.token)
      localStorage.setItem('user', JSON.stringify({
        id: response.userId,
        email: response.email,
        firstName: response.firstName,
        lastName: response.lastName,
        role: response.role,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      }))

      // Update auth state
      setAuthState({
        user: {
          id: response.userId,
          email: response.email,
          firstName: response.firstName,
          lastName: response.lastName,
          phone: '', // Will be updated from profile
          address: '', // Will be updated from profile
          birthDate: '', // Will be updated from profile
          role: response.role as 'CLIENT' | 'EMPLOYEE' | 'ADMIN',
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        },
        token: response.token,
        isAuthenticated: true,
        isLoading: false,
        role: response.role as 'CLIENT' | 'EMPLOYEE' | 'ADMIN',
      })

      return { success: true }
    } catch (error: any) {
      setAuthState(prev => ({ ...prev, isLoading: false }))
      
      const errorMessage = error?.response?.data?.message || error?.message || 'Login failed'
      return { success: false, error: errorMessage }
    }
  }

  // Register function
  const register = async (userData: RegisterRequest): Promise<{ success: boolean; error?: string }> => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true }))
      
      const response = await apiClient.post<LoginResponse>(API_ENDPOINTS.AUTH.REGISTER, userData)
      
      // Store token and user data
      localStorage.setItem('token', response.token)
      localStorage.setItem('user', JSON.stringify({
        id: response.userId,
        email: response.email,
        firstName: response.firstName,
        lastName: response.lastName,
        role: response.role,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      }))

      // Update auth state
      setAuthState({
        user: {
          id: response.userId,
          email: response.email,
          firstName: response.firstName,
          lastName: response.lastName,
          phone: userData.phone,
          address: userData.address,
          birthDate: userData.birthDate,
          role: response.role as 'CLIENT' | 'EMPLOYEE' | 'ADMIN',
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        },
        token: response.token,
        isAuthenticated: true,
        isLoading: false,
        role: response.role as 'CLIENT' | 'EMPLOYEE' | 'ADMIN',
      })

      return { success: true }
    } catch (error: any) {
      setAuthState(prev => ({ ...prev, isLoading: false }))
      
      const errorMessage = error?.response?.data?.message || error?.message || 'Registration failed'
      return { success: false, error: errorMessage }
    }
  }

  // Logout function
  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    
    setAuthState({
      user: null,
      token: null,
      isAuthenticated: false,
      isLoading: false,
      role: null,
    })
  }

  // Refresh token function
  const refreshToken = async (): Promise<boolean> => {
    try {
      const response = await apiClient.get<User>(API_ENDPOINTS.AUTH.PROFILE)
      
      // Update user data
      localStorage.setItem('user', JSON.stringify(response))
      
      setAuthState(prev => ({
        ...prev,
        user: response,
        role: response.role as 'CLIENT' | 'EMPLOYEE' | 'ADMIN',
      }))
      
      return true
    } catch (error) {
      console.error('Token refresh failed:', error)
      logout()
      return false
    }
  }

  // Update user function
  const updateUser = (user: User) => {
    localStorage.setItem('user', JSON.stringify(user))
    setAuthState(prev => ({
      ...prev,
      user,
      role: user.role as 'CLIENT' | 'EMPLOYEE' | 'ADMIN',
    }))
  }

  const contextValue: AuthContextType = {
    ...authState,
    login,
    register,
    logout,
    refreshToken,
    updateUser,
  }

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  )
}

// Custom hook to use auth context
export function useAuth(): AuthContextType {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

// Helper hooks for specific roles
export function useIsClient(): boolean {
  const { role } = useAuth()
  return role === 'CLIENT'
}

export function useIsEmployee(): boolean {
  const { role } = useAuth()
  return role === 'EMPLOYEE'
}

export function useIsAdmin(): boolean {
  const { role } = useAuth()
  return role === 'ADMIN'
}

export function useRequireAuth() {
  const { isAuthenticated, isLoading } = useAuth()
  return { isAuthenticated, isLoading }
}
