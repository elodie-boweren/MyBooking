"use client"

import {
  format,
  startOfWeek,
  endOfWeek,
  eachDayOfInterval,
  isSameDay,
  startOfMonth,
  endOfMonth,
  eachWeekOfInterval,
} from "date-fns"
import { cn } from "@/lib/utils"

interface CalendarBooking {
  id: string
  roomId: string
  roomName: string
  roomFloor: number
  title: string
  description?: string
  startTime: string
  endTime: string
  date: string
  userName: string
  userEmail: string
  attendees: number
  status: "confirmed" | "pending" | "cancelled"
  color: string
}

interface CalendarViewProps {
  viewMode: "day" | "week" | "month"
  selectedDate: Date
  bookings: CalendarBooking[]
  onBookingClick: (booking: CalendarBooking) => void
}

export function CalendarView({ viewMode, selectedDate, bookings, onBookingClick }: CalendarViewProps) {
  const getBookingsForDate = (date: Date) => {
    const dateStr = format(date, "yyyy-MM-dd")
    return bookings.filter((booking) => booking.date === dateStr)
  }

  const renderDayView = () => {
    const dayBookings = getBookingsForDate(selectedDate)
    const hours = Array.from({ length: 24 }, (_, i) => i)

    return (
      <div className="space-y-4">
        <div className="grid grid-cols-1 gap-2">
          {hours.map((hour) => {
            const hourStr = hour.toString().padStart(2, "0") + ":00"
            const hourBookings = dayBookings.filter((booking) => {
              const startHour = Number.parseInt(booking.startTime.split(":")[0])
              const endHour = Number.parseInt(booking.endTime.split(":")[0])
              return hour >= startHour && hour < endHour
            })

            return (
              <div key={hour} className="flex border-b border-border pb-2">
                <div className="w-16 text-sm text-muted-foreground font-medium">{hourStr}</div>
                <div className="flex-1 space-y-1">
                  {hourBookings.map((booking) => (
                    <div
                      key={booking.id}
                      className="p-2 rounded-md cursor-pointer hover:opacity-80 transition-opacity"
                      style={{ backgroundColor: booking.color + "20", borderLeft: `3px solid ${booking.color}` }}
                      onClick={() => onBookingClick(booking)}
                    >
                      <div className="font-medium text-sm">{booking.title}</div>
                      <div className="text-xs text-muted-foreground">
                        {booking.roomName} • {booking.startTime} - {booking.endTime}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )
          })}
        </div>
      </div>
    )
  }

  const renderWeekView = () => {
    const weekStart = startOfWeek(selectedDate, { weekStartsOn: 1 })
    const weekEnd = endOfWeek(selectedDate, { weekStartsOn: 1 })
    const weekDays = eachDayOfInterval({ start: weekStart, end: weekEnd })
    const hours = Array.from({ length: 24 }, (_, i) => i)

    return (
      <div className="overflow-x-auto">
        <div className="min-w-[800px]">
          {/* Header */}
          <div className="grid grid-cols-8 gap-1 mb-4">
            <div className="w-16"></div>
            {weekDays.map((day) => (
              <div key={day.toISOString()} className="text-center p-2">
                <div className="font-medium">{format(day, "EEE")}</div>
                <div
                  className={cn(
                    "text-sm",
                    isSameDay(day, new Date()) ? "text-primary font-bold" : "text-muted-foreground",
                  )}
                >
                  {format(day, "dd")}
                </div>
              </div>
            ))}
          </div>

          {/* Time slots */}
          <div className="space-y-1">
            {hours.map((hour) => {
              const hourStr = hour.toString().padStart(2, "0") + ":00"
              return (
                <div key={hour} className="grid grid-cols-8 gap-1 border-b border-border pb-1">
                  <div className="w-16 text-sm text-muted-foreground font-medium py-2">{hourStr}</div>
                  {weekDays.map((day) => {
                    const dayBookings = getBookingsForDate(day).filter((booking) => {
                      const startHour = Number.parseInt(booking.startTime.split(":")[0])
                      const endHour = Number.parseInt(booking.endTime.split(":")[0])
                      return hour >= startHour && hour < endHour
                    })

                    return (
                      <div key={day.toISOString()} className="min-h-[40px] p-1">
                        {dayBookings.map((booking) => (
                          <div
                            key={booking.id}
                            className="p-1 rounded text-xs cursor-pointer hover:opacity-80 transition-opacity mb-1"
                            style={{ backgroundColor: booking.color + "20", borderLeft: `2px solid ${booking.color}` }}
                            onClick={() => onBookingClick(booking)}
                          >
                            <div className="font-medium truncate">{booking.title}</div>
                            <div className="text-muted-foreground truncate">{booking.roomName}</div>
                          </div>
                        ))}
                      </div>
                    )
                  })}
                </div>
              )
            })}
          </div>
        </div>
      </div>
    )
  }

  const renderMonthView = () => {
    const monthStart = startOfMonth(selectedDate)
    const monthEnd = endOfMonth(selectedDate)
    const calendarStart = startOfWeek(monthStart, { weekStartsOn: 1 })
    const calendarEnd = endOfWeek(monthEnd, { weekStartsOn: 1 })
    const weeks = eachWeekOfInterval({ start: calendarStart, end: calendarEnd }, { weekStartsOn: 1 })

    return (
      <div className="space-y-4">
        {/* Header */}
        <div className="grid grid-cols-7 gap-1">
          {["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"].map((day) => (
            <div key={day} className="text-center font-medium text-muted-foreground p-2">
              {day}
            </div>
          ))}
        </div>

        {/* Calendar Grid */}
        <div className="space-y-1">
          {weeks.map((weekStart) => {
            const weekDays = eachDayOfInterval({ start: weekStart, end: endOfWeek(weekStart, { weekStartsOn: 1 }) })
            return (
              <div key={weekStart.toISOString()} className="grid grid-cols-7 gap-1">
                {weekDays.map((day) => {
                  const dayBookings = getBookingsForDate(day)
                  const isCurrentMonth = day.getMonth() === selectedDate.getMonth()
                  const isToday = isSameDay(day, new Date())

                  return (
                    <div
                      key={day.toISOString()}
                      className={cn(
                        "min-h-[120px] p-2 border border-border rounded-md",
                        !isCurrentMonth && "bg-muted/30 text-muted-foreground",
                        isToday && "bg-primary/10 border-primary",
                      )}
                    >
                      <div className={cn("font-medium text-sm mb-2", isToday && "text-primary")}>
                        {format(day, "d")}
                      </div>
                      <div className="space-y-1">
                        {dayBookings.slice(0, 3).map((booking) => (
                          <div
                            key={booking.id}
                            className="p-1 rounded text-xs cursor-pointer hover:opacity-80 transition-opacity"
                            style={{ backgroundColor: booking.color + "20", borderLeft: `2px solid ${booking.color}` }}
                            onClick={() => onBookingClick(booking)}
                          >
                            <div className="font-medium truncate">{booking.title}</div>
                            <div className="text-muted-foreground truncate">{booking.startTime}</div>
                          </div>
                        ))}
                        {dayBookings.length > 3 && (
                          <div className="text-xs text-muted-foreground">+{dayBookings.length - 3} more</div>
                        )}
                      </div>
                    </div>
                  )
                })}
              </div>
            )
          })}
        </div>
      </div>
    )
  }

  switch (viewMode) {
    case "day":
      return renderDayView()
    case "week":
      return renderWeekView()
    case "month":
      return renderMonthView()
    default:
      return renderWeekView()
  }
}
