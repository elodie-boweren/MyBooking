// Centralized style constants for consistent UI design

export const STYLE_CONSTANTS = {
  colors: {
    primary: 'hsl(var(--primary))',
    secondary: 'hsl(var(--secondary))',
    accent: 'hsl(var(--accent))',
    muted: 'hsl(var(--muted))',
    success: 'hsl(142, 76%, 36%)',
    warning: 'hsl(38, 92%, 50%)',
    error: 'hsl(0, 84%, 60%)',
    info: 'hsl(221, 83%, 53%)',
  },
  spacing: {
    xs: '0.5rem',
    sm: '0.75rem',
    md: '1rem',
    lg: '1.5rem',
    xl: '2rem',
    '2xl': '3rem',
  },
  borderRadius: {
    sm: '0.375rem',
    md: '0.5rem',
    lg: '0.75rem',
    xl: '1rem',
  },
  shadows: {
    sm: '0 1px 2px 0 rgb(0 0 0 / 0.05)',
    md: '0 4px 6px -1px rgb(0 0 0 / 0.1)',
    lg: '0 10px 15px -3px rgb(0 0 0 / 0.1)',
    xl: '0 20px 25px -5px rgb(0 0 0 / 0.1)',
  },
  transitions: {
    fast: '150ms ease-in-out',
    normal: '200ms ease-in-out',
    slow: '300ms ease-in-out',
  },
  breakpoints: {
    sm: '640px',
    md: '768px',
    lg: '1024px',
    xl: '1280px',
    '2xl': '1536px',
  },
}

// Reusable component templates - MATCHING EXACT FRONTEND STYLING
export const COMPONENT_TEMPLATES = {
  // Page layouts - matching your exact patterns
  pageContainer: 'min-h-screen bg-background',
  pageContent: 'container mx-auto px-4 py-8',
  pageHeader: 'mb-8',
  pageTitle: 'text-3xl font-bold text-foreground mb-2',
  pageSubtitle: 'text-muted-foreground',
  
  // Cards - matching your exact card styling
  card: 'bg-card text-card-foreground border border-border rounded-lg shadow-sm',
  cardHover: 'overflow-hidden hover:shadow-lg transition-all duration-200 cursor-pointer hover:scale-[1.02]',
  cardHeader: 'flex flex-row items-center justify-between space-y-0 pb-2',
  cardTitle: 'text-sm font-medium text-primary',
  cardContent: 'text-3xl font-bold',
  cardDescription: 'text-xs text-muted-foreground',
  
  // Buttons - matching your exact button styling
  button: 'inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-all disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg:not([class*=\'size-\'])]:size-4 shrink-0 [&_svg]:shrink-0 outline-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px]',
  buttonPrimary: 'bg-primary text-primary-foreground shadow-xs hover:bg-primary/90',
  buttonSecondary: 'bg-secondary text-secondary-foreground shadow-xs hover:bg-secondary/80',
  buttonOutline: 'border bg-background shadow-xs hover:bg-accent hover:text-accent-foreground',
  buttonDestructive: 'bg-destructive text-white shadow-xs hover:bg-destructive/90',
  
  // Inputs - matching your exact input styling
  input: 'flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50',
  inputWithIcon: 'pl-10', // For inputs with left icons
  
  // Badges - matching your exact badge styling
  badge: 'inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 [&>svg]:size-3 gap-1 [&>svg]:pointer-events-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive transition-[color,box-shadow] overflow-hidden',
  
  // Navigation - matching your exact navigation styling
  navHeader: 'border-b border-border bg-card sticky top-0 z-50',
  navContainer: 'container mx-auto px-4 py-4 flex items-center justify-between',
  navBrand: 'flex items-center space-x-2',
  navBrandIcon: 'h-8 w-8 text-primary',
  navBrandText: 'text-2xl font-bold text-foreground',
  navItems: 'hidden md:flex items-center space-x-6',
  navItem: 'flex items-center space-x-2 px-3 py-2 rounded-md text-sm font-medium transition-colors',
  navItemActive: 'bg-primary text-primary-foreground',
  navItemInactive: 'text-muted-foreground hover:text-foreground hover:bg-muted',
  
  // Tables - matching your exact table styling
  table: 'w-full caption-bottom text-sm',
  tableHeader: 'border-b',
  tableBody: '[&_tr:last-child]:border-0',
  tableRow: 'border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted',
  tableHead: 'h-12 px-4 text-left align-middle font-medium text-muted-foreground [&:has([role=checkbox])]:pr-0',
  tableCell: 'p-4 align-middle [&:has([role=checkbox])]:pr-0',
  
  // Grid layouts - matching your exact grid patterns
  gridCards: 'grid md:grid-cols-2 lg:grid-cols-3 gap-6',
  gridStats: 'grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6',
  gridCharts: 'grid grid-cols-1 lg:grid-cols-2 gap-6',
  
  // Search and filters - matching your exact patterns
  searchContainer: 'mb-8 space-y-4',
  searchRow: 'flex flex-col md:flex-row gap-4',
  searchInput: 'relative flex-1',
  searchIcon: 'absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4',
  filterRow: 'flex gap-2',
  
  // Icons - matching your exact icon sizing
  iconSmall: 'h-3 w-3',
  iconMedium: 'h-4 w-4',
  iconLarge: 'h-8 w-8',
  iconInline: 'inline-block',
}

// Status-specific styling - MATCHING ACTUAL FRONTEND COLORS
export const STATUS_STYLES = {
  // Room status - Using actual frontend colors
  room: {
    AVAILABLE: 'bg-primary text-primary-foreground', // Cyan-600 (#0891b2) - Blue marine
    OCCUPIED: 'bg-secondary text-secondary-foreground', // Purple (#8b5cf6)
    OUT_OF_SERVICE: 'bg-yellow-100 text-yellow-800 border-yellow-200',
  },
  // Reservation status - Using actual frontend colors from admin page
  reservation: {
    CONFIRMED: 'bg-green-100 text-green-800 border-green-200',
    CANCELLED: 'bg-red-100 text-red-800 border-red-200',
  },
  // Event booking status
  eventBooking: {
    CONFIRMED: 'bg-green-100 text-green-800 border-green-200',
    CANCELLED: 'bg-red-100 text-red-800 border-red-200',
  },
  // Task status
  task: {
    TODO: 'bg-gray-100 text-gray-800 border-gray-200',
    IN_PROGRESS: 'bg-primary text-primary-foreground', // Using primary blue
    DONE: 'bg-green-100 text-green-800 border-green-200',
  },
  // Leave request status
  leaveRequest: {
    PENDING: 'bg-yellow-100 text-yellow-800 border-yellow-200',
    APPROVED: 'bg-green-100 text-green-800 border-green-200',
    REJECTED: 'bg-red-100 text-red-800 border-red-200',
  },
  // Training status
  training: {
    SCHEDULED: 'bg-primary text-primary-foreground', // Using primary blue
    IN_PROGRESS: 'bg-yellow-100 text-yellow-800 border-yellow-200',
    COMPLETED: 'bg-green-100 text-green-800 border-green-200',
    CANCELLED: 'bg-red-100 text-red-800 border-red-200',
  },
  // Employee training status
  employeeTraining: {
    ASSIGNED: 'bg-primary text-primary-foreground', // Using primary blue
    COMPLETED: 'bg-green-100 text-green-800 border-green-200',
  },
  // Employee status
  employee: {
    ACTIVE: 'bg-green-100 text-green-800 border-green-200',
    INACTIVE: 'bg-red-100 text-red-800 border-red-200',
  },
  // Priority levels
  priority: {
    LOW: 'bg-gray-100 text-gray-800 border-gray-200',
    MEDIUM: 'bg-yellow-100 text-yellow-800 border-yellow-200',
    HIGH: 'bg-red-100 text-red-800 border-red-200',
  },
  // Announcement priority
  announcement: {
    LOW: 'bg-gray-100 text-gray-800 border-gray-200',
    MEDIUM: 'bg-primary text-primary-foreground', // Using primary blue
    HIGH: 'bg-red-100 text-red-800 border-red-200',
  },
}

// Icon mappings for different entities
export const ENTITY_ICONS = {
  room: {
    SINGLE: '🛏️',
    DOUBLE: '🛏️🛏️',
    DELUXE: '🏨',
    FAMILY: '👨‍👩‍👧‍👦',
  },
  event: {
    CONFERENCE: '🏢',
    WORKSHOP: '🔧',
    MEETING: '👥',
    TRAINING: '📚',
    SOCIAL: '🎉',
  },
  user: {
    CLIENT: '👤',
    EMPLOYEE: '👨‍💼',
    ADMIN: '👑',
  },
  status: {
    success: '✅',
    warning: '⚠️',
    error: '❌',
    info: 'ℹ️',
  },
}

// Animation classes
export const ANIMATIONS = {
  fadeIn: 'animate-in fade-in duration-300',
  slideIn: 'animate-in slide-in-from-bottom-4 duration-300',
  scaleIn: 'animate-in zoom-in-95 duration-200',
  slideUp: 'animate-in slide-in-from-top-2 duration-200',
  slideDown: 'animate-in slide-in-from-bottom-2 duration-200',
}

// Layout constants
export const LAYOUT = {
  headerHeight: '4rem',
  sidebarWidth: '16rem',
  sidebarCollapsedWidth: '4rem',
  contentPadding: '1.5rem',
  maxContentWidth: '1200px',
}

// Form validation styles
export const FORM_STYLES = {
  error: 'border-red-500 focus:border-red-500 focus:ring-red-500',
  success: 'border-green-500 focus:border-green-500 focus:ring-green-500',
  warning: 'border-yellow-500 focus:border-yellow-500 focus:ring-yellow-500',
}

// Responsive grid classes
export const GRID_CLASSES = {
  '1': 'grid-cols-1',
  '2': 'grid-cols-1 md:grid-cols-2',
  '3': 'grid-cols-1 md:grid-cols-2 lg:grid-cols-3',
  '4': 'grid-cols-1 md:grid-cols-2 lg:grid-cols-4',
  '5': 'grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5',
  '6': 'grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6',
}

// Badge variant utilities - MATCHING ACTUAL FRONTEND USAGE
export const BADGE_VARIANTS = {
  // Room status - matches your frontend exactly
  room: {
    AVAILABLE: 'default', // Uses bg-primary (Cyan-600/blue marine)
    OCCUPIED: 'secondary', // Uses bg-secondary (Purple)
    OUT_OF_SERVICE: 'outline', // Custom styling needed
  },
  // Reservation status
  reservation: {
    CONFIRMED: 'default', // Can use primary or custom green
    CANCELLED: 'destructive', // Uses destructive (red)
  },
  // Event booking status
  eventBooking: {
    CONFIRMED: 'default',
    CANCELLED: 'destructive',
  },
  // Task status
  task: {
    TODO: 'outline',
    IN_PROGRESS: 'default', // Uses primary blue
    DONE: 'default', // Can use primary or custom green
  },
  // Leave request status
  leaveRequest: {
    PENDING: 'outline', // Custom yellow styling
    APPROVED: 'default', // Custom green styling
    REJECTED: 'destructive',
  },
  // Training status
  training: {
    SCHEDULED: 'default', // Uses primary blue
    IN_PROGRESS: 'outline', // Custom yellow styling
    COMPLETED: 'default', // Custom green styling
    CANCELLED: 'destructive',
  },
  // Employee training status
  employeeTraining: {
    ASSIGNED: 'default', // Uses primary blue
    COMPLETED: 'default', // Custom green styling
  },
  // Employee status
  employee: {
    ACTIVE: 'default', // Custom green styling
    INACTIVE: 'destructive',
  },
  // Priority levels
  priority: {
    LOW: 'outline',
    MEDIUM: 'outline', // Custom yellow styling
    HIGH: 'destructive',
  },
  // Announcement priority
  announcement: {
    LOW: 'outline',
    MEDIUM: 'default', // Uses primary blue
    HIGH: 'destructive',
  },
}

// Helper function to get Badge variant for any status
export const getBadgeVariant = (entity: keyof typeof BADGE_VARIANTS, status: string) => {
  const entityVariants = BADGE_VARIANTS[entity] as Record<string, string>
  return entityVariants[status] || 'outline'
}

// Helper function to get custom className for status badges that need custom colors
export const getStatusBadgeClassName = (entity: keyof typeof STATUS_STYLES, status: string) => {
  const entityStyles = STATUS_STYLES[entity] as Record<string, string>
  return entityStyles[status] || ''
}

// Common utility classes - MATCHING EXACT FRONTEND PATTERNS
export const UTILITY_CLASSES = {
  // Text - matching your exact text patterns
  textMuted: 'text-muted-foreground',
  textPrimary: 'text-primary',
  textSecondary: 'text-secondary-foreground',
  textForeground: 'text-foreground',
  textCardForeground: 'text-card-foreground',
  
  // Background - matching your exact background patterns
  bgMuted: 'bg-muted',
  bgCard: 'bg-card',
  bgPrimary: 'bg-primary',
  bgSecondary: 'bg-secondary',
  bgBackground: 'bg-background',
  
  // Border - matching your exact border patterns
  borderDefault: 'border border-border',
  borderMuted: 'border border-muted',
  borderB: 'border-b',
  borderT: 'border-t',
  borderL: 'border-l',
  borderR: 'border-r',
  
  // Spacing - matching your exact spacing patterns
  pCard: 'p-6',
  pCardSm: 'p-4',
  pCardLg: 'p-8',
  px4: 'px-4',
  py8: 'py-8',
  py4: 'py-4',
  py2: 'py-2',
  mb8: 'mb-8',
  mb2: 'mb-2',
  spaceY4: 'space-y-4',
  spaceY6: 'space-y-6',
  spaceX2: 'space-x-2',
  spaceX6: 'space-x-6',
  spaceX8: 'space-x-8',
  
  // Flexbox - matching your exact flex patterns
  flexCenter: 'flex items-center justify-center',
  flexBetween: 'flex items-center justify-between',
  flexStart: 'flex items-center justify-start',
  flexEnd: 'flex items-center justify-end',
  flexCol: 'flex flex-col',
  flexRow: 'flex flex-row',
  itemsCenter: 'items-center',
  justifyBetween: 'justify-between',
  
  // Grid - matching your exact grid patterns
  gridCards: 'grid md:grid-cols-2 lg:grid-cols-3 gap-6',
  gridStats: 'grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6',
  gridCharts: 'grid grid-cols-1 lg:grid-cols-2 gap-6',
  gridCols1: 'grid-cols-1',
  gridCols2: 'grid-cols-2',
  gridCols3: 'grid-cols-3',
  gridCols4: 'grid-cols-4',
  
  // Responsive - matching your exact responsive patterns
  hiddenMd: 'hidden md:flex',
  flexColMd: 'flex-col md:flex-row',
  gridCols1Md: 'grid-cols-1 md:grid-cols-2',
  gridCols2Lg: 'md:grid-cols-2 lg:grid-cols-3',
  gridCols4Lg: 'md:grid-cols-2 lg:grid-cols-4',
  
  // Hover effects - matching your exact hover patterns
  hoverCard: 'hover:shadow-lg transition-all duration-200',
  hoverScale: 'hover:scale-[1.02] transition-all duration-200',
  hoverOpacity: 'hover:opacity-80 transition-opacity duration-200',
  hoverShadow: 'hover:shadow-lg transition-shadow',
  hoverBg: 'hover:bg-muted',
  hoverText: 'hover:text-foreground',
  
  // Transitions - matching your exact transition patterns
  transitionAll: 'transition-all duration-200',
  transitionColors: 'transition-colors',
  transitionShadow: 'transition-shadow',
  transitionTransform: 'transition-transform',
  
  // Positioning - matching your exact positioning patterns
  sticky: 'sticky top-0 z-50',
  absolute: 'absolute',
  relative: 'relative',
  top4: 'top-4',
  right4: 'right-4',
  left3: 'left-3',
  topHalf: 'top-1/2',
  transform: 'transform -translate-y-1/2',
  
  // Sizing - matching your exact sizing patterns
  wFull: 'w-full',
  h10: 'h-10',
  h48: 'h-48',
  h12: 'h-12',
  wFit: 'w-fit',
  size9: 'size-9',
  
  // Rounded - matching your exact rounded patterns
  roundedMd: 'rounded-md',
  roundedLg: 'rounded-lg',
  roundedFull: 'rounded-full',
  
  // Shadow - matching your exact shadow patterns
  shadowSm: 'shadow-sm',
  shadowXs: 'shadow-xs',
  shadowLg: 'shadow-lg',
  
  // Gap - matching your exact gap patterns
  gap2: 'gap-2',
  gap4: 'gap-4',
  gap6: 'gap-6',
}

// Business card effects - MATCHING EXACT FRONTEND CSS
export const BUSINESS_EFFECTS = {
  cardHover: 'business-card-hover',
  gradientRed: 'business-gradient-red',
  gradientBlue: 'business-gradient-blue', 
  gradientGreen: 'business-gradient-green',
}

// Custom CSS classes from your globals.css
export const CUSTOM_CLASSES = {
  // Business card hover effect
  businessCardHover: 'transition: all 0.3s ease; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)',
  businessCardHoverActive: 'box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05); transform: translateY(-2px)',
  
  // Business gradients
  businessGradientRed: 'background: linear-gradient(135deg, #dc2626 0%, #b91c1c 100%)',
  businessGradientBlue: 'background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%)',
  businessGradientGreen: 'background: linear-gradient(135deg, #059669 0%, #047857 100%)',
}
