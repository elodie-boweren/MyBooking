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
  MapPin,
  PartyPopper,
} from "lucide-react"

export function Navigation() {
  const router = useRouter()
  const pathname = usePathname()

  const user = {
    firstName: "John",
    lastName: "Doe",
    email: "john.doe@company.com",
    role: "employee",
    loyaltyPoints: 1250,
    avatar: "/diverse-user-avatars.png",
  }

  const handleLogout = () => {
    localStorage.removeItem("token")
    localStorage.removeItem("user")
    router.push("/login")
  }

  const navItems = [
    { href: "/rooms", label: "Rooms", icon: CalendarDays },
    { href: "/events", label: "Events", icon: PartyPopper },
    { href: "/facilities", label: "Facilities", icon: MapPin },
    ...(user.role === "admin" ? [{ href: "/admin", label: "Admin", icon: LayoutDashboard }] : []),
  ]

  return (
    <header className="border-b border-border bg-card sticky top-0 z-50">
      <div className="container mx-auto px-4 py-4 flex items-center justify-between">
        <div className="flex items-center space-x-8">
          <Link href="/rooms" className="flex items-center space-x-2">
            <CalendarDays className="h-8 w-8 text-primary" />
            <span className="text-2xl font-bold text-foreground">RoomReserve</span>
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
          <div className="hidden sm:flex items-center space-x-2 px-3 py-1 bg-secondary/10 rounded-full">
            <Star className="h-4 w-4 text-secondary" />
            <span className="text-sm font-medium text-secondary">{user.loyaltyPoints}</span>
          </div>

          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="ghost" className="relative h-10 w-10 rounded-full">
                <Avatar className="h-10 w-10">
                  <AvatarImage src={user.avatar || "/placeholder.svg"} alt={user.firstName} />
                  <AvatarFallback>
                    {user.firstName[0]}
                    {user.lastName[0]}
                  </AvatarFallback>
                </Avatar>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent className="w-64" align="end" forceMount>
              <div className="flex items-center justify-start gap-2 p-2">
                <div className="flex flex-col space-y-1 leading-none">
                  <p className="font-medium">
                    {user.firstName} {user.lastName}
                  </p>
                  <p className="w-[200px] truncate text-sm text-muted-foreground">{user.email}</p>
                  <div className="flex items-center space-x-2 mt-2">
                    <Badge variant="secondary" className="flex items-center space-x-1">
                      <Star className="h-3 w-3" />
                      <span>{user.loyaltyPoints} Points</span>
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
        </div>
      </div>
    </header>
  )
}
