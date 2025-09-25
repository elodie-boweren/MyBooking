"use client"

import { createContext, useContext, useState, useEffect, type ReactNode } from "react"
import { authApi } from "@/lib/api"
import type { User } from "@/lib/api"

interface AuthContextType {
  user: User | null
  login: (email: string, password: string) => Promise<boolean>
  register: (firstName: string, lastName: string, email: string, password: string) => Promise<boolean>
  logout: () => void
  isLoading: boolean
  isAuthenticated: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // Check for existing session
    const initializeAuth = async () => {
      try {
        const token = localStorage.getItem('token')
        const userStr = localStorage.getItem('user')
        
        if (token && userStr) {
          const user = JSON.parse(userStr)
          setUser(user)
          
          // Optionally verify token is still valid by fetching user profile
          // For now, we'll trust the stored user data to avoid unnecessary API calls
          try {
            // Only verify if we're in development or if the token is old
            const tokenAge = Date.now() - (user.createdAt ? new Date(user.createdAt).getTime() : 0)
            if (tokenAge > 24 * 60 * 60 * 1000) { // 24 hours
              const response = await authApi.getProfile()
              setUser(response)
            }
          } catch (error) {
            console.log('Token verification failed, but keeping user logged in')
            // Don't clear the session immediately, let the user try to use the app
          }
        }
      } catch (error) {
        console.error('Auth initialization error:', error)
        setUser(null)
      } finally {
        setIsLoading(false)
      }
    }

    initializeAuth()
  }, [])

  const login = async (email: string, password: string): Promise<boolean> => {
    setIsLoading(true)
    try {
      const response = await authApi.login({ email, password })
      
      // Store token and user data
      localStorage.setItem('token', response.token)
      localStorage.setItem('user', JSON.stringify(response.user))
      
      setUser(response.user)
      return true
    } catch (error) {
      console.error('Login error:', error)
      return false
    } finally {
      setIsLoading(false)
    }
  }

  const register = async (firstName: string, lastName: string, email: string, password: string): Promise<boolean> => {
    setIsLoading(true)
    try {
      const response = await authApi.register({
        firstName,
        lastName,
        email,
        password
      })
      
      // Auto-login after registration
      return await login(email, password)
    } catch (error) {
      console.error('Registration error:', error)
      return false
    } finally {
      setIsLoading(false)
    }
  }

  const logout = () => {
    setUser(null)
    localStorage.removeItem("user")
    localStorage.removeItem("token")
  }

  return <AuthContext.Provider value={{ user, login, register, logout, isLoading, isAuthenticated: !!user }}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}
