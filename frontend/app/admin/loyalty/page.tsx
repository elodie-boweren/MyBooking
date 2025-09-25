"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { 
  ArrowLeft, 
  Star, 
  TrendingUp, 
  Award, 
  Download, 
  Plus, 
  Minus, 
  Search,
  Filter,
  Users,
  DollarSign
} from "lucide-react"
import Link from "next/link"
import { loyaltyApi } from "@/lib/api"
import type { LoyaltyAccount, LoyaltyTransaction } from "@/lib/api"
import { toast } from "sonner"

export default function AdminLoyaltyPage() {
  const [accounts, setAccounts] = useState<LoyaltyAccount[]>([])
  const [transactions, setTransactions] = useState<LoyaltyTransaction[]>([])
  const [loading, setLoading] = useState(true)
  const [searchTerm, setSearchTerm] = useState("")
  const [selectedType, setSelectedType] = useState("all")
  const [selectedPeriod, setSelectedPeriod] = useState("all")

  useEffect(() => {
    const fetchLoyaltyData = async () => {
      try {
        setLoading(true)
        // Fetch all loyalty accounts
        const accountsData = await loyaltyApi.getAllAccounts()
        setAccounts(accountsData)
        
        // Fetch all transactions
        const transactionsData = await loyaltyApi.getAllTransactions()
        setTransactions(transactionsData)
      } catch (error) {
        console.error('Failed to fetch loyalty data:', error)
        toast.error("Failed to load loyalty data")
      } finally {
        setLoading(false)
      }
    }

    fetchLoyaltyData()
  }, [])

  const filteredAccounts = accounts.filter(account => 
    account.userName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    account.userEmail.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const filteredTransactions = transactions.filter(transaction => {
    if (selectedType !== "all" && transaction.type !== selectedType) {
      return false
    }
    if (selectedPeriod !== "all") {
      const transactionDate = new Date(transaction.createdAt)
      const now = new Date()
      
      switch (selectedPeriod) {
        case "week":
          const weekAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
          return transactionDate >= weekAgo
        case "month":
          const monthAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
          return transactionDate >= monthAgo
        case "quarter":
          const quarterAgo = new Date(now.getTime() - 90 * 24 * 60 * 60 * 1000)
          return transactionDate >= quarterAgo
        default:
          return true
      }
    }
    return true
  })

  const getTransactionIcon = (type: string) => {
    switch (type) {
      case "EARN":
        return <Plus className="h-4 w-4 text-green-600" />
      case "REDEEM":
        return <Minus className="h-4 w-4 text-red-600" />
      default:
        return <Star className="h-4 w-4" />
    }
  }

  const getTransactionBadge = (type: string) => {
    switch (type) {
      case "EARN":
        return <Badge variant="default" className="bg-green-100 text-green-800">Earned</Badge>
      case "REDEEM":
        return <Badge variant="destructive">Redeemed</Badge>
      default:
        return <Badge variant="secondary">{type}</Badge>
    }
  }

  const totalPointsEarned = transactions
    .filter(t => t.type === "EARN")
    .reduce((sum, t) => sum + t.points, 0)

  const totalPointsRedeemed = transactions
    .filter(t => t.type === "REDEEM")
    .reduce((sum, t) => sum + t.points, 0)

  const activeAccounts = accounts.filter(account => account.balance > 0).length

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8 max-w-7xl">
        <div className="text-center py-8">Loading loyalty data...</div>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-7xl">
      <div className="flex items-center justify-between mb-8">
        <div className="flex items-center gap-4">
          <Link href="/admin">
            <Button variant="outline" size="sm">
              <ArrowLeft className="h-4 w-4 mr-2" />
              Back to Admin
            </Button>
          </Link>
          <div>
            <h1 className="text-3xl font-bold text-foreground">Loyalty Management</h1>
            <p className="text-muted-foreground mt-2">Manage client loyalty points and transactions</p>
          </div>
        </div>
        <Button variant="outline">
          <Download className="h-4 w-4 mr-2" />
          Export Data
        </Button>
      </div>

      {/* Overview Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center mb-2">
              <Users className="h-6 w-6 text-primary" />
            </div>
            <CardTitle className="text-2xl">{accounts.length}</CardTitle>
            <CardDescription>Total Accounts</CardDescription>
          </CardHeader>
        </Card>

        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mb-2">
              <TrendingUp className="h-6 w-6 text-green-600" />
            </div>
            <CardTitle className="text-2xl">{totalPointsEarned.toLocaleString()}</CardTitle>
            <CardDescription>Points Earned</CardDescription>
          </CardHeader>
        </Card>

        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-red-100 rounded-full flex items-center justify-center mb-2">
              <Award className="h-6 w-6 text-red-600" />
            </div>
            <CardTitle className="text-2xl">{totalPointsRedeemed.toLocaleString()}</CardTitle>
            <CardDescription>Points Redeemed</CardDescription>
          </CardHeader>
        </Card>

        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center mb-2">
              <Star className="h-6 w-6 text-blue-600" />
            </div>
            <CardTitle className="text-2xl">{activeAccounts}</CardTitle>
            <CardDescription>Active Accounts</CardDescription>
          </CardHeader>
        </Card>
      </div>

      <Tabs defaultValue="accounts" className="space-y-6">
        <TabsList>
          <TabsTrigger value="accounts">Client Accounts</TabsTrigger>
          <TabsTrigger value="transactions">All Transactions</TabsTrigger>
        </TabsList>

        <TabsContent value="accounts" className="space-y-6">
          {/* Search and Filters */}
          <div className="flex gap-4">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Search by name or email..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
          </div>

          {/* Accounts List */}
          <Card>
            <CardHeader>
              <CardTitle>Client Loyalty Accounts</CardTitle>
              <CardDescription>
                {filteredAccounts.length} accounts found
              </CardDescription>
            </CardHeader>
            <CardContent>
              {filteredAccounts.length === 0 ? (
                <div className="text-center py-8">
                  <Users className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
                  <h3 className="text-lg font-semibold">No accounts found</h3>
                  <p className="text-muted-foreground">No loyalty accounts match your search criteria.</p>
                </div>
              ) : (
                <div className="space-y-4">
                  {filteredAccounts.map((account) => (
                    <div key={account.id} className="flex items-center justify-between p-4 border rounded-lg">
                      <div className="flex items-center gap-4">
                        <div className="w-10 h-10 bg-primary/10 rounded-full flex items-center justify-center">
                          <span className="text-sm font-semibold text-primary">
                            {account.userName.charAt(0).toUpperCase()}
                          </span>
                        </div>
                        <div>
                          <p className="font-medium">{account.userName}</p>
                          <p className="text-sm text-muted-foreground">{account.userEmail}</p>
                        </div>
                      </div>
                      <div className="flex items-center gap-6">
                        <div className="text-right">
                          <p className="text-sm text-muted-foreground">Current Balance</p>
                          <p className="text-lg font-semibold text-primary">{account.balance.toLocaleString()} points</p>
                        </div>
                        <div className="text-right">
                          <p className="text-sm text-muted-foreground">Account ID</p>
                          <p className="text-sm font-medium">#{account.id}</p>
                        </div>
                        <div className="text-right">
                          <p className="text-sm text-muted-foreground">Created</p>
                          <p className="text-sm font-medium">{new Date(account.createdAt).toLocaleDateString()}</p>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="transactions" className="space-y-6">
          {/* Filters */}
          <div className="flex gap-4">
            <Select value={selectedType} onValueChange={setSelectedType}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder="Filter by Type" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Transactions</SelectItem>
                <SelectItem value="EARN">Earned Points</SelectItem>
                <SelectItem value="REDEEM">Redeemed Points</SelectItem>
              </SelectContent>
            </Select>

            <Select value={selectedPeriod} onValueChange={setSelectedPeriod}>
              <SelectTrigger className="w-48">
                <SelectValue placeholder="Filter by Period" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Time</SelectItem>
                <SelectItem value="week">This Week</SelectItem>
                <SelectItem value="month">This Month</SelectItem>
                <SelectItem value="quarter">This Quarter</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {/* Transactions List */}
          <Card>
            <CardHeader>
              <CardTitle>All Transactions</CardTitle>
              <CardDescription>
                {filteredTransactions.length} transactions found
              </CardDescription>
            </CardHeader>
            <CardContent>
              {filteredTransactions.length === 0 ? (
                <div className="text-center py-8">
                  <Star className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
                  <h3 className="text-lg font-semibold">No transactions found</h3>
                  <p className="text-muted-foreground">No transactions match your filter criteria.</p>
                </div>
              ) : (
                <div className="space-y-4">
                  {filteredTransactions.map((transaction) => (
                    <div key={transaction.id} className="flex items-center justify-between p-4 border rounded-lg">
                      <div className="flex items-center gap-3">
                        {getTransactionIcon(transaction.type)}
                        <div>
                          <p className="font-medium">{transaction.type === "EARN" ? "Points Earned" : "Points Redeemed"}</p>
                          <p className="text-sm text-muted-foreground">
                            Client: {transaction.userName} ({transaction.userEmail})
                          </p>
                          <p className="text-sm text-muted-foreground">
                            {new Date(transaction.createdAt).toLocaleDateString()} at{" "}
                            {new Date(transaction.createdAt).toLocaleTimeString()}
                          </p>
                        </div>
                      </div>
                      <div className="flex items-center gap-3">
                        {getTransactionBadge(transaction.type)}
                        <div className="text-right">
                          <p className={`font-semibold ${
                            transaction.type === "EARN" ? "text-green-600" : "text-red-600"
                          }`}>
                            {transaction.type === "EARN" ? "+" : "-"}{Math.abs(transaction.points)}
                          </p>
                          <p className="text-sm text-muted-foreground">points</p>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
