"use client"
import Link from "next/link"
import { useRouter, usePathname } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Badge } from "@/components/ui/badge"
import {
  CalendarDays,
  LayoutDashboard,
  LogOut,
  User,
  Settings,
  Star,
  BookOpen,
  PartyPopper,
  CreditCard,
  MessageSquare,
  Gift,
} from "lucide-react"
import { useAuth } from "@/components/auth-provider"
import { useIsAdmin, useIsEmployee } from "@/lib/auth-context"
import { useState, useEffect } from "react"
import { loyaltyApi } from "@/lib/api"
import type { LoyaltyAccount } from "@/lib/api"

export function Navigation() {
  const router = useRouter()
  const pathname = usePathname()
  const { user, logout, isLoading, isAuthenticated } = useAuth()
  const isAdmin = user?.role === 'ADMIN'
  const isEmployee = user?.role === 'EMPLOYEE'
  const [loyaltyAccount, setLoyaltyAccount] = useState<LoyaltyAccount | null>(null)
  const [loyaltyLoading, setLoyaltyLoading] = useState(false)

  // Debug authentication state
  console.log('Navigation Debug:', {
    isAuthenticated,
    isLoading,
    user: user ? { 
      id: user.id, 
      email: user.email, 
      name: user.name,
      role: user.role,
      fullUser: user
    } : null,
    loyaltyAccount: loyaltyAccount ? { balance: loyaltyAccount.balance } : null,
    loyaltyLoading
  })
  
  // Debug loyalty points specifically
  console.log('Loyalty Debug:', {
    loyaltyAccount,
    loyaltyLoading,
    balance: loyaltyAccount?.balance,
    pointsBalance: loyaltyAccount?.pointsBalance,
    userRole: user?.role,
    isClient: user?.role === 'CLIENT' || user?.role === 'client',
    shouldFetch: user && (user.role === 'CLIENT' || user.role === 'client') && isAuthenticated
  })

  const handleLogout = () => {
    logout()
    router.push("/login")
  }

  // Fetch loyalty points for clients
  useEffect(() => {
    const fetchLoyaltyPoints = async () => {
      console.log('fetchLoyaltyPoints called:', {
        user: !!user,
        userRole: user?.role,
        isAuthenticated,
        shouldFetch: user && (user.role === 'CLIENT' || user.role === 'client') && isAuthenticated
      })
      
      if (user && (user.role === 'CLIENT' || user.role === 'client') && isAuthenticated) {
        console.log('Fetching loyalty points...')
        setLoyaltyLoading(true)
        try {
          const account = await loyaltyApi.getAccount()
          console.log('Loyalty account fetched:', account)
          setLoyaltyAccount(account)
        } catch (error) {
          console.error('Failed to fetch loyalty points:', error)
          setLoyaltyAccount(null)
        } finally {
          setLoyaltyLoading(false)
        }
      } else {
        console.log('Not fetching loyalty points - conditions not met')
      }
    }

    // Only fetch if user is authenticated and not loading
    if (isAuthenticated && !isLoading) {
      fetchLoyaltyPoints()
    }
  }, [user, isAuthenticated, isLoading])

  const navItems = [
    { href: "/rooms", label: "Rooms", icon: CalendarDays },
    { href: "/events", label: "Events", icon: PartyPopper },
    ...(isAdmin ? [{ href: "/admin", label: "Admin", icon: LayoutDashboard }] : []),
  ]

  return (
    <header className="border-b border-border bg-card sticky top-0 z-50">
      <div className="container mx-auto px-4 py-4 flex items-center justify-between">
        <div className="flex items-center space-x-8">
          <Link href="/rooms" className="flex items-center space-x-2">
            <CalendarDays className="h-8 w-8 text-primary" />
            <span className="text-2xl font-bold text-foreground">MyBooking</span>
          </Link>

          <nav className="hidden md:flex items-center space-x-6">
            {navItems.map((item) => {
              const Icon = item.icon
              const isActive = pathname === item.href
              return (
                <Link
                  key={item.href}
                  href={item.href}
                  className={`flex items-center space-x-2 px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                    isActive
                      ? "bg-primary text-primary-foreground"
                      : "text-muted-foreground hover:text-foreground hover:bg-muted"
                  }`}
                >
                  <Icon className="h-4 w-4" />
                  <span>{item.label}</span>
                </Link>
              )
            })}
          </nav>
        </div>

        <div className="flex items-center space-x-4">
          {isAuthenticated && user ? (
            <>
              <div className="hidden sm:flex items-center space-x-2 px-3 py-1 bg-secondary/10 rounded-full">
                <Star className="h-4 w-4 text-secondary" />
                <span className="text-sm font-medium text-secondary">
                  {loyaltyLoading ? "Loading..." : 
                   loyaltyAccount ? loyaltyAccount.balance : 
                   "0"}
                </span>
              </div>

              <DropdownMenu>
                <DropdownMenuTrigger className="relative h-10 w-10 rounded-full border-0 bg-transparent hover:bg-muted focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2">
                  <Avatar className="h-10 w-10">
                    <AvatarImage src="/placeholder.svg" alt={user.firstName || 'User'} />
                    <AvatarFallback>
                      {user.firstName ? user.firstName[0] : 'U'}
                    </AvatarFallback>
                  </Avatar>
                </DropdownMenuTrigger>
                <DropdownMenuContent className="w-64" align="end" forceMount>
                  <div className="flex items-center justify-start gap-2 p-2">
                    <div className="flex flex-col space-y-1 leading-none">
                      <p className="font-medium">
                        {user.firstName} {user.lastName}
                      </p>
                      <p className="w-[200px] truncate text-sm text-muted-foreground">{user.email || 'No email'}</p>
                      <div className="flex items-center space-x-2 mt-2">
                        <Badge variant="secondary" className="flex items-center space-x-1">
                          <Star className="h-3 w-3" />
                          <span>
                            {loyaltyLoading ? "Loading..." : 
                             loyaltyAccount ? `${loyaltyAccount.balance} Points` : 
                             "0 Points"}
                          </span>
                        </Badge>
                      </div>
                    </div>
                  </div>
              <DropdownMenuSeparator />

              <DropdownMenuItem asChild>
                <Link href="/profile" className="flex items-center">
                  <User className="mr-2 h-4 w-4" />
                  <span>Profile & Preferences</span>
                </Link>
              </DropdownMenuItem>

              <DropdownMenuItem asChild>
                <Link href="/my-reservations" className="flex items-center">
                  <BookOpen className="mr-2 h-4 w-4" />
                  <span>My Reservations</span>
                </Link>
              </DropdownMenuItem>

              <DropdownMenuItem asChild>
                <Link href="/my-events" className="flex items-center">
                  <PartyPopper className="mr-2 h-4 w-4" />
                  <span>My Events</span>
                </Link>
              </DropdownMenuItem>

              <DropdownMenuItem asChild>
                <Link href="/my-loyalty" className="flex items-center">
                  <Gift className="mr-2 h-4 w-4" />
                  <span>Loyalty Points</span>
                </Link>
              </DropdownMenuItem>


              <DropdownMenuItem>
                <Settings className="mr-2 h-4 w-4" />
                <span>Settings</span>
              </DropdownMenuItem>

              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={handleLogout}>
                <LogOut className="mr-2 h-4 w-4" />
                <span>Log out</span>
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
            </>
          ) : (
            <div className="flex items-center space-x-2">
              <Button variant="outline" asChild>
                <Link href="/login">Login</Link>
              </Button>
              <Button asChild>
                <Link href="/register">Register</Link>
              </Button>
            </div>
          )}
        </div>
      </div>
    </header>
  )
}
