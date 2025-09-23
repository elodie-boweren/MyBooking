"use client"

import { Badge } from '@/components/ui/badge'
import { getBadgeVariant, getStatusBadgeClassName, STATUS_STYLES } from '@/lib/style-constants'
import { cn } from '@/lib/utils'

interface StatusBadgeProps {
  entity: keyof typeof STATUS_STYLES
  status: string
  className?: string
  children?: React.ReactNode
}

/**
 * StatusBadge component that preserves the exact styling from your frontend
 * 
 * Usage examples:
 * <StatusBadge entity="room" status="AVAILABLE">Available</StatusBadge>
 * <StatusBadge entity="room" status="OCCUPIED">Occupied</StatusBadge>
 * <StatusBadge entity="reservation" status="CONFIRMED">Confirmed</StatusBadge>
 */
export function StatusBadge({ entity, status, className, children }: StatusBadgeProps) {
  const variant = getBadgeVariant(entity, status)
  const customClassName = getStatusBadgeClassName(entity, status)
  
  // For statuses that need custom styling (like yellow, green), use className
  // For statuses that use shadcn variants (like primary blue, purple), use variant
  const needsCustomStyling = customClassName && !customClassName.includes('bg-primary') && !customClassName.includes('bg-secondary')
  
  if (needsCustomStyling) {
    return (
      <Badge 
        variant="outline" 
        className={cn(customClassName, className)}
      >
        {children || status}
      </Badge>
    )
  }
  
  return (
    <Badge 
      variant={variant as any} 
      className={cn(customClassName, className)}
    >
      {children || status}
    </Badge>
  )
}

// Convenience components for common statuses
export function RoomStatusBadge({ status, className, children }: { status: string; className?: string; children?: React.ReactNode }) {
  return <StatusBadge entity="room" status={status} className={className}>{children}</StatusBadge>
}

export function ReservationStatusBadge({ status, className, children }: { status: string; className?: string; children?: React.ReactNode }) {
  return <StatusBadge entity="reservation" status={status} className={className}>{children}</StatusBadge>
}

export function TaskStatusBadge({ status, className, children }: { status: string; className?: string; children?: React.ReactNode }) {
  return <StatusBadge entity="task" status={status} className={className}>{children}</StatusBadge>
}

export function LeaveRequestStatusBadge({ status, className, children }: { status: string; className?: string; children?: React.ReactNode }) {
  return <StatusBadge entity="leaveRequest" status={status} className={className}>{children}</StatusBadge>
}

export function TrainingStatusBadge({ status, className, children }: { status: string; className?: string; children?: React.ReactNode }) {
  return <StatusBadge entity="training" status={status} className={className}>{children}</StatusBadge>
}

export function EmployeeStatusBadge({ status, className, children }: { status: string; className?: string; children?: React.ReactNode }) {
  return <StatusBadge entity="employee" status={status} className={className}>{children}</StatusBadge>
}

export function PriorityBadge({ priority, className, children }: { priority: string; className?: string; children?: React.ReactNode }) {
  return <StatusBadge entity="priority" status={priority} className={className}>{children}</StatusBadge>
}
