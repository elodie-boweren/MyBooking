"use client"

import { useState } from "react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Star, TrendingUp, Calendar, Gift, Award, Plus, Minus, Download } from "lucide-react"

export default function LoyaltyHistoryPage() {
  const [selectedPeriod, setSelectedPeriod] = useState("all")
  const [selectedType, setSelectedType] = useState("all")

  const currentPoints = 1250
  const totalEarned = 2840
  const totalRedeemed = 1590

  const transactions = [
    {
      id: "1",
      type: "earned",
      points: 50,
      description: "Room booking - Executive Boardroom",
      date: "2024-01-15",
      time: "14:30",
      category: "booking",
      reference: "BK-2024-001",
    },
    {
      id: "2",
      type: "earned",
      points: 25,
      description: "Event attendance - Tech Talk: Future of AI",
      date: "2024-01-12",
      time: "16:00",
      category: "event",
      reference: "EV-2024-005",
    },
    {
      id: "3",
      type: "redeemed",
      points: -100,
      description: "Priority booking privilege",
      date: "2024-01-10",
      time: "09:15",
      category: "reward",
      reference: "RW-2024-003",
    },
    {
      id: "4",
      type: "earned",
      points: 75,
      description: "Room booking - Modern Conference Room A (3 hours)",
      date: "2024-01-08",
      time: "11:00",
      category: "booking",
      reference: "BK-2024-002",
    },
    {
      id: "5",
      type: "bonus",
      points: 200,
      description: "Monthly loyalty bonus",
      date: "2024-01-01",
      time: "00:00",
      category: "bonus",
      reference: "BN-2024-001",
    },
    {
      id: "6",
      type: "redeemed",
      points: -150,
      description: "Extended booking hours privilege",
      date: "2023-12-28",
      time: "15:45",
      category: "reward",
      reference: "RW-2023-012",
    },
    {
      id: "7",
      type: "earned",
      points: 30,
      description: "Event organization - Design Workshop",
      date: "2023-12-20",
      time: "17:30",
      category: "event",
      reference: "EV-2023-018",
    },
    {
      id: "8",
      type: "earned",
      points: 40,
      description: "Room booking - Creative Workspace",
      date: "2023-12-15",
      time: "10:20",
      category: "booking",
      reference: "BK-2023-045",
    },
  ]

  const rewards = [
    {
      id: "1",
      name: "Priority Booking",
      description: "Book popular rooms 24 hours before general availability",
      cost: 100,
      status: "active",
      validUntil: "2024-02-15",
    },
    {
      id: "2",
      name: "Extended Hours",
      description: "Book rooms for up to 8 hours instead of standard 4 hours",
      cost: 150,
      status: "active",
      validUntil: "2024-01-28",
    },
    {
      id: "3",
      name: "VIP Room Access",
      description: "Access to executive boardrooms and premium facilities",
      cost: 500,
      status: "available",
      validUntil: null,
    },
    {
      id: "4",
      name: "Free Catering",
      description: "Complimentary refreshments for your next meeting",
      cost: 300,
      status: "available",
      validUntil: null,
    },
  ]

  const getTransactionIcon = (type: string) => {
    switch (type) {
      case "earned":
        return <Plus className="h-4 w-4 text-green-600" />
      case "redeemed":
        return <Minus className="h-4 w-4 text-red-600" />
      case "bonus":
        return <Gift className="h-4 w-4 text-blue-600" />
      default:
        return <Star className="h-4 w-4 text-gray-600" />
    }
  }

  const getTransactionColor = (type: string) => {
    switch (type) {
      case "earned":
        return "text-green-600"
      case "redeemed":
        return "text-red-600"
      case "bonus":
        return "text-blue-600"
      default:
        return "text-gray-600"
    }
  }

  const getCategoryBadge = (category: string) => {
    const colors: { [key: string]: string } = {
      booking: "bg-blue-100 text-blue-800",
      event: "bg-green-100 text-green-800",
      reward: "bg-purple-100 text-purple-800",
      bonus: "bg-orange-100 text-orange-800",
    }
    return (
      <Badge className={colors[category] || "bg-gray-100 text-gray-800"}>
        {category.charAt(0).toUpperCase() + category.slice(1)}
      </Badge>
    )
  }

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "active":
        return <Badge className="bg-green-100 text-green-800 hover:bg-green-100">Active</Badge>
      case "available":
        return <Badge variant="outline">Available</Badge>
      case "expired":
        return <Badge className="bg-red-100 text-red-800 hover:bg-red-100">Expired</Badge>
      default:
        return <Badge variant="secondary">{status}</Badge>
    }
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-6xl">
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-3xl font-bold text-foreground">Loyalty Points History</h1>
          <p className="text-muted-foreground mt-2">Track your points earnings, redemptions, and rewards</p>
        </div>
        <Button variant="outline">
          <Download className="h-4 w-4 mr-2" />
          Export History
        </Button>
      </div>

      {/* Points Overview */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-primary/10 rounded-full flex items-center justify-center mb-2">
              <Star className="h-6 w-6 text-primary" />
            </div>
            <CardTitle className="text-2xl">{currentPoints.toLocaleString()}</CardTitle>
            <CardDescription>Current Balance</CardDescription>
          </CardHeader>
        </Card>

        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mb-2">
              <TrendingUp className="h-6 w-6 text-green-600" />
            </div>
            <CardTitle className="text-2xl">{totalEarned.toLocaleString()}</CardTitle>
            <CardDescription>Total Earned</CardDescription>
          </CardHeader>
        </Card>

        <Card>
          <CardHeader className="text-center">
            <div className="mx-auto w-12 h-12 bg-red-100 rounded-full flex items-center justify-center mb-2">
              <Award className="h-6 w-6 text-red-600" />
            </div>
            <CardTitle className="text-2xl">{totalRedeemed.toLocaleString()}</CardTitle>
            <CardDescription>Total Redeemed</CardDescription>
          </CardHeader>
        </Card>
      </div>

      <Tabs defaultValue="transactions" className="space-y-6">
        <TabsList className="grid w-full grid-cols-2">
          <TabsTrigger value="transactions">Transaction History</TabsTrigger>
          <TabsTrigger value="rewards">My Rewards</TabsTrigger>
        </TabsList>

        <TabsContent value="transactions" className="space-y-6">
          {/* Filters */}
          <Card>
            <CardContent className="p-4">
              <div className="flex flex-col md:flex-row gap-4">
                <div className="flex-1">
                  <Select value={selectedPeriod} onValueChange={setSelectedPeriod}>
                    <SelectTrigger>
                      <SelectValue placeholder="Select period" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All Time</SelectItem>
                      <SelectItem value="30">Last 30 Days</SelectItem>
                      <SelectItem value="90">Last 3 Months</SelectItem>
                      <SelectItem value="365">Last Year</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="flex-1">
                  <Select value={selectedType} onValueChange={setSelectedType}>
                    <SelectTrigger>
                      <SelectValue placeholder="Transaction type" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All Types</SelectItem>
                      <SelectItem value="earned">Earned</SelectItem>
                      <SelectItem value="redeemed">Redeemed</SelectItem>
                      <SelectItem value="bonus">Bonus</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Transaction List */}
          <Card>
            <CardHeader>
              <CardTitle>Recent Transactions</CardTitle>
              <CardDescription>Your latest points activity</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {transactions.map((transaction) => (
                  <div
                    key={transaction.id}
                    className="flex items-center justify-between p-4 border rounded-lg hover:bg-muted/50 transition-colors"
                  >
                    <div className="flex items-center space-x-4">
                      <div className="w-10 h-10 rounded-full bg-muted flex items-center justify-center">
                        {getTransactionIcon(transaction.type)}
                      </div>
                      <div className="space-y-1">
                        <p className="font-medium">{transaction.description}</p>
                        <div className="flex items-center space-x-2 text-sm text-muted-foreground">
                          <Calendar className="h-3 w-3" />
                          <span>
                            {transaction.date} at {transaction.time}
                          </span>
                          <span>•</span>
                          <span>{transaction.reference}</span>
                        </div>
                      </div>
                    </div>
                    <div className="flex items-center space-x-3">
                      {getCategoryBadge(transaction.category)}
                      <div className={`text-lg font-semibold ${getTransactionColor(transaction.type)}`}>
                        {transaction.points > 0 ? "+" : ""}
                        {transaction.points}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="rewards" className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {rewards.map((reward) => (
              <Card key={reward.id} className="hover:shadow-md transition-shadow">
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="space-y-1">
                      <CardTitle className="text-lg">{reward.name}</CardTitle>
                      <CardDescription>{reward.description}</CardDescription>
                    </div>
                    {getStatusBadge(reward.status)}
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-2">
                      <Star className="h-4 w-4 text-primary" />
                      <span className="font-semibold">{reward.cost} points</span>
                    </div>
                    {reward.status === "available" ? (
                      <Button size="sm" disabled={currentPoints < reward.cost}>
                        {currentPoints < reward.cost ? "Insufficient Points" : "Redeem"}
                      </Button>
                    ) : reward.status === "active" ? (
                      <div className="text-sm text-muted-foreground">Valid until {reward.validUntil}</div>
                    ) : null}
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>
      </Tabs>
    </div>
  )
}
