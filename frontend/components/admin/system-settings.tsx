"use client"

import { useState, useEffect } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Switch } from '@/components/ui/switch'
import { Separator } from '@/components/ui/separator'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { 
  ArrowLeft,
  Settings,
  Building2,
  Mail,
  Shield,
  Bell,
  Database,
  Save,
  RefreshCw,
  AlertTriangle,
  CheckCircle
} from 'lucide-react'
import { useRouter } from 'next/navigation'
import { toast } from 'sonner'

interface SystemSettings {
  hotel: {
    name: string
    address: string
    phone: string
    email: string
    website: string
    description: string
  }
  notifications: {
    emailEnabled: boolean
    smsEnabled: boolean
    pushEnabled: boolean
    maintenanceAlerts: boolean
    bookingAlerts: boolean
    systemAlerts: boolean
  }
  security: {
    sessionTimeout: number
    maxLoginAttempts: number
    twoFactorRequired: boolean
    passwordExpiry: number
  }
  system: {
    maintenanceMode: boolean
    autoBackup: boolean
    logRetention: number
    debugMode: boolean
  }
}

export function SystemSettings() {
  const router = useRouter()
  const [settings, setSettings] = useState<SystemSettings>({
    hotel: {
      name: 'Overlook Hotel',
      address: '123 Mountain View Drive, Colorado Springs, CO 80906',
      phone: '+1 (555) 123-4567',
      email: 'info@overlookhotel.com',
      website: 'https://overlookhotel.com',
      description: 'A luxury mountain resort offering world-class accommodations and services.'
    },
    notifications: {
      emailEnabled: true,
      smsEnabled: false,
      pushEnabled: true,
      maintenanceAlerts: true,
      bookingAlerts: true,
      systemAlerts: true
    },
    security: {
      sessionTimeout: 24,
      maxLoginAttempts: 5,
      twoFactorRequired: false,
      passwordExpiry: 90
    },
    system: {
      maintenanceMode: false,
      autoBackup: true,
      logRetention: 30,
      debugMode: false
    }
  })
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    fetchSettings()
  }, [])

  const fetchSettings = async () => {
    setLoading(true)
    try {
      // TODO: Replace with real API call
      await new Promise(resolve => setTimeout(resolve, 1000)) // Simulate API call
      // Settings are already initialized with default values
    } catch (error) {
      console.error('Failed to fetch settings:', error)
      toast.error('Failed to load system settings')
    } finally {
      setLoading(false)
    }
  }

  const handleSave = async () => {
    setSaving(true)
    try {
      // TODO: Replace with real API call
      await new Promise(resolve => setTimeout(resolve, 1000)) // Simulate API call
      toast.success('Settings saved successfully!')
    } catch (error) {
      console.error('Failed to save settings:', error)
      toast.error('Failed to save settings. Please try again.')
    } finally {
      setSaving(false)
    }
  }

  const handleReset = () => {
    if (confirm('Are you sure you want to reset all settings to default values?')) {
      fetchSettings()
      toast.success('Settings reset to defaults')
    }
  }

  if (loading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Button 
              variant="outline" 
              size="sm"
              onClick={() => router.push('/admin')}
              className="flex items-center gap-2"
            >
              <ArrowLeft className="h-4 w-4" />
              Back to Dashboard
            </Button>
            <div>
              <h1 className="text-3xl font-bold text-foreground">System Settings</h1>
              <p className="text-muted-foreground">Configure hotel management system settings</p>
            </div>
          </div>
        </div>
        
        <div className="flex items-center justify-center py-12">
          <div className="flex items-center gap-2">
            <RefreshCw className="h-5 w-5 animate-spin" />
            <span>Loading settings...</span>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button 
            variant="outline" 
            size="sm"
            onClick={() => router.push('/admin')}
            className="flex items-center gap-2"
          >
            <ArrowLeft className="h-4 w-4" />
            Back to Dashboard
          </Button>
          <div>
            <h1 className="text-3xl font-bold text-foreground">System Settings</h1>
            <p className="text-muted-foreground">Configure hotel management system settings</p>
          </div>
        </div>
        
        <div className="flex items-center gap-2">
          <Button 
            variant="outline" 
            size="sm"
            onClick={handleReset}
            className="flex items-center gap-2"
          >
            <RefreshCw className="h-4 w-4" />
            Reset
          </Button>
          <Button 
            onClick={handleSave} 
            disabled={saving}
            className="flex items-center gap-2"
          >
            {saving ? (
              <>
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2" />
                Saving...
              </>
            ) : (
              <>
                <Save className="h-4 w-4" />
                Save Settings
              </>
            )}
          </Button>
        </div>
      </div>

      <Tabs defaultValue="hotel" className="space-y-6">
        <TabsList className="grid w-full grid-cols-4">
          <TabsTrigger value="hotel">Hotel Info</TabsTrigger>
          <TabsTrigger value="notifications">Notifications</TabsTrigger>
          <TabsTrigger value="security">Security</TabsTrigger>
          <TabsTrigger value="system">System</TabsTrigger>
        </TabsList>

        <TabsContent value="hotel" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Building2 className="h-5 w-5" />
                Hotel Information
              </CardTitle>
              <CardDescription>
                Configure basic hotel information and contact details
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="hotelName">Hotel Name</Label>
                  <Input
                    id="hotelName"
                    value={settings.hotel.name}
                    onChange={(e) => setSettings(prev => ({
                      ...prev,
                      hotel: { ...prev.hotel, name: e.target.value }
                    }))}
                    placeholder="Enter hotel name"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="hotelPhone">Phone Number</Label>
                  <Input
                    id="hotelPhone"
                    value={settings.hotel.phone}
                    onChange={(e) => setSettings(prev => ({
                      ...prev,
                      hotel: { ...prev.hotel, phone: e.target.value }
                    }))}
                    placeholder="Enter phone number"
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="hotelAddress">Address</Label>
                <Textarea
                  id="hotelAddress"
                  value={settings.hotel.address}
                  onChange={(e) => setSettings(prev => ({
                    ...prev,
                    hotel: { ...prev.hotel, address: e.target.value }
                  }))}
                  placeholder="Enter hotel address"
                  rows={3}
                />
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="hotelEmail">Email Address</Label>
                  <Input
                    id="hotelEmail"
                    type="email"
                    value={settings.hotel.email}
                    onChange={(e) => setSettings(prev => ({
                      ...prev,
                      hotel: { ...prev.hotel, email: e.target.value }
                    }))}
                    placeholder="Enter email address"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="hotelWebsite">Website</Label>
                  <Input
                    id="hotelWebsite"
                    value={settings.hotel.website}
                    onChange={(e) => setSettings(prev => ({
                      ...prev,
                      hotel: { ...prev.hotel, website: e.target.value }
                    }))}
                    placeholder="Enter website URL"
                  />
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="hotelDescription">Description</Label>
                <Textarea
                  id="hotelDescription"
                  value={settings.hotel.description}
                  onChange={(e) => setSettings(prev => ({
                    ...prev,
                    hotel: { ...prev.hotel, description: e.target.value }
                  }))}
                  placeholder="Enter hotel description"
                  rows={4}
                />
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="notifications" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Bell className="h-5 w-5" />
                Notification Settings
              </CardTitle>
              <CardDescription>
                Configure notification preferences for the system
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label htmlFor="emailEnabled">Email Notifications</Label>
                    <p className="text-sm text-muted-foreground">
                      Send notifications via email
                    </p>
                  </div>
                  <Switch
                    id="emailEnabled"
                    checked={settings.notifications.emailEnabled}
                    onCheckedChange={(checked) => setSettings(prev => ({
                      ...prev,
                      notifications: { ...prev.notifications, emailEnabled: checked }
                    }))}
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label htmlFor="smsEnabled">SMS Notifications</Label>
                    <p className="text-sm text-muted-foreground">
                      Send urgent notifications via SMS
                    </p>
                  </div>
                  <Switch
                    id="smsEnabled"
                    checked={settings.notifications.smsEnabled}
                    onCheckedChange={(checked) => setSettings(prev => ({
                      ...prev,
                      notifications: { ...prev.notifications, smsEnabled: checked }
                    }))}
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label htmlFor="pushEnabled">Push Notifications</Label>
                    <p className="text-sm text-muted-foreground">
                      Send push notifications to mobile devices
                    </p>
                  </div>
                  <Switch
                    id="pushEnabled"
                    checked={settings.notifications.pushEnabled}
                    onCheckedChange={(checked) => setSettings(prev => ({
                      ...prev,
                      notifications: { ...prev.notifications, pushEnabled: checked }
                    }))}
                  />
                </div>

                <Separator />

                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label htmlFor="maintenanceAlerts">Maintenance Alerts</Label>
                    <p className="text-sm text-muted-foreground">
                      Receive alerts for system maintenance
                    </p>
                  </div>
                  <Switch
                    id="maintenanceAlerts"
                    checked={settings.notifications.maintenanceAlerts}
                    onCheckedChange={(checked) => setSettings(prev => ({
                      ...prev,
                      notifications: { ...prev.notifications, maintenanceAlerts: checked }
                    }))}
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label htmlFor="bookingAlerts">Booking Alerts</Label>
                    <p className="text-sm text-muted-foreground">
                      Receive alerts for new bookings and cancellations
                    </p>
                  </div>
                  <Switch
                    id="bookingAlerts"
                    checked={settings.notifications.bookingAlerts}
                    onCheckedChange={(checked) => setSettings(prev => ({
                      ...prev,
                      notifications: { ...prev.notifications, bookingAlerts: checked }
                    }))}
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label htmlFor="systemAlerts">System Alerts</Label>
                    <p className="text-sm text-muted-foreground">
                      Receive alerts for system errors and issues
                    </p>
                  </div>
                  <Switch
                    id="systemAlerts"
                    checked={settings.notifications.systemAlerts}
                    onCheckedChange={(checked) => setSettings(prev => ({
                      ...prev,
                      notifications: { ...prev.notifications, systemAlerts: checked }
                    }))}
                  />
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="security" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Shield className="h-5 w-5" />
                Security Settings
              </CardTitle>
              <CardDescription>
                Configure security and authentication settings
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="sessionTimeout">Session Timeout (hours)</Label>
                  <Input
                    id="sessionTimeout"
                    type="number"
                    value={settings.security.sessionTimeout}
                    onChange={(e) => setSettings(prev => ({
                      ...prev,
                      security: { ...prev.security, sessionTimeout: parseInt(e.target.value) }
                    }))}
                    min="1"
                    max="168"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="maxLoginAttempts">Max Login Attempts</Label>
                  <Input
                    id="maxLoginAttempts"
                    type="number"
                    value={settings.security.maxLoginAttempts}
                    onChange={(e) => setSettings(prev => ({
                      ...prev,
                      security: { ...prev.security, maxLoginAttempts: parseInt(e.target.value) }
                    }))}
                    min="3"
                    max="10"
                  />
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="passwordExpiry">Password Expiry (days)</Label>
                  <Input
                    id="passwordExpiry"
                    type="number"
                    value={settings.security.passwordExpiry}
                    onChange={(e) => setSettings(prev => ({
                      ...prev,
                      security: { ...prev.security, passwordExpiry: parseInt(e.target.value) }
                    }))}
                    min="30"
                    max="365"
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="twoFactorRequired">Two-Factor Authentication</Label>
                  <div className="flex items-center space-x-2">
                    <Switch
                      id="twoFactorRequired"
                      checked={settings.security.twoFactorRequired}
                      onCheckedChange={(checked) => setSettings(prev => ({
                        ...prev,
                        security: { ...prev.security, twoFactorRequired: checked }
                      }))}
                    />
                    <span className="text-sm text-muted-foreground">
                      {settings.security.twoFactorRequired ? 'Required' : 'Optional'}
                    </span>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="system" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Settings className="h-5 w-5" />
                System Configuration
              </CardTitle>
              <CardDescription>
                Configure system-wide settings and maintenance options
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label htmlFor="maintenanceMode">Maintenance Mode</Label>
                    <p className="text-sm text-muted-foreground">
                      Temporarily disable the system for maintenance
                    </p>
                  </div>
                  <Switch
                    id="maintenanceMode"
                    checked={settings.system.maintenanceMode}
                    onCheckedChange={(checked) => setSettings(prev => ({
                      ...prev,
                      system: { ...prev.system, maintenanceMode: checked }
                    }))}
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label htmlFor="autoBackup">Automatic Backup</Label>
                    <p className="text-sm text-muted-foreground">
                      Automatically backup system data daily
                    </p>
                  </div>
                  <Switch
                    id="autoBackup"
                    checked={settings.system.autoBackup}
                    onCheckedChange={(checked) => setSettings(prev => ({
                      ...prev,
                      system: { ...prev.system, autoBackup: checked }
                    }))}
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <Label htmlFor="debugMode">Debug Mode</Label>
                    <p className="text-sm text-muted-foreground">
                      Enable detailed logging for troubleshooting
                    </p>
                  </div>
                  <Switch
                    id="debugMode"
                    checked={settings.system.debugMode}
                    onCheckedChange={(checked) => setSettings(prev => ({
                      ...prev,
                      system: { ...prev.system, debugMode: checked }
                    }))}
                  />
                </div>
              </div>

              <Separator />

              <div className="space-y-2">
                <Label htmlFor="logRetention">Log Retention (days)</Label>
                <Input
                  id="logRetention"
                  type="number"
                  value={settings.system.logRetention}
                  onChange={(e) => setSettings(prev => ({
                    ...prev,
                    system: { ...prev.system, logRetention: parseInt(e.target.value) }
                  }))}
                  min="7"
                  max="365"
                />
                <p className="text-sm text-muted-foreground">
                  How long to keep system logs before automatic deletion
                </p>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
