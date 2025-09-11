### Exemples d'API — MyBooking (JSON)

Conventions:
- En-têtes: `Authorization: Bearer <token>` quand nécessaire, `Content-Type: application/json`.
- Dates au format ISO `YYYY-MM-DD`; timestamps ISO8601.

## Authentification

POST /api/v1/auth/register (request)
```json
{ "firstName": "John", "lastName": "Doe", "email": "john@doe.com", "password": "Secret123!", "birthdate": "1991-04-15", "phone": "+33123456789", "address": "12 rue Exemple, Paris" }
```

POST /api/v1/auth/register (response)
```json
{ "id": 101, "email": "john@doe.com" }
```

POST /api/v1/auth/login (request)
```json
{ "email": "john@doe.com", "password": "Secret123!" }
```

POST /api/v1/auth/login (response)
```json
{ "accessToken": "jwt...", "refreshToken": "jwt...", "role": "CLIENT" }
```

GET /api/v1/auth/profile (response)
```json
{ "id": 101, "email": "john@doe.com", "role": "CLIENT" }
```

POST /api/v1/auth/password/forgot (request)
```json
{ "email": "john@doe.com" }
```

POST /api/v1/auth/password/reset (request)
```json
{ "token": "reset-token", "newPassword": "NewSecret123!" }
```

## Client — Profil & Notifications

GET /api/v1/client/profile (response)
```json
{ "id": 101, "firstName": "John", "lastName": "Doe", "email": "john@doe.com", "birthdate": "1991-04-15", "phone": "+33123456789", "address": "12 rue Exemple, Paris" }
```

PUT /api/v1/client/profile (request)
```json
{ "firstName": "John", "lastName": "Doe", "birthdate": "1991-04-15", "phone": "+33123456789", "address": "12 rue Exemple, Paris" }
```

PUT /api/v1/client/password (request)
```json
{ "currentPassword": "Secret123!", "newPassword": "NewSecret123!" }
```

GET /api/v1/client/notifications/preferences (response)
```json
{ "email": true, "sms": false }
```

PUT /api/v1/client/notifications/preferences (request)
```json
{ "email": true, "sms": false }
```

## Chambres

GET /api/v1/rooms?checkIn=2025-10-01&checkOut=2025-10-05&capacity=2&roomType=DOUBLE&page=0&size=10 (response)
```json
{
  "items": [
    {
      "id": 42,
      "roomType": "DOUBLE",
      "capacity": 2,
      "price": 120.0,
      "currency": "EUR",
      "equipments": ["WiFi", "TV"],
      "status": "AVAILABLE"
    }
  ],
  "page": 0, "size": 10, "total": 1
}
```

GET /api/v1/rooms/42 (response)
```json
{ "id": 42, "roomType": "DOUBLE", "capacity": 2, "price": 120.0, "currency": "EUR", "equipments": ["WiFi", "TV"], "description": "Chambre double lumineuse", "photos": ["/img/42-1.jpg"] }
```

GET /api/v1/rooms/42/availability?from=2025-10-01&to=2025-10-31 (response)
```json
{ "roomId": 42, "available": true, "slots": [ { "from": "2025-10-01", "to": "2025-10-05" } ] }
```

## Réservations (Client)

POST /api/v1/reservations (request)
```json
{ "roomId": 42, "checkIn": "2025-10-01", "checkOut": "2025-10-05", "guests": 2, "usePoints": 100 }
```

POST /api/v1/reservations (response)
```json
{ "id": 123, "roomId": 42, "checkIn": "2025-10-01", "checkOut": "2025-10-05", "status": "CONFIRMED", "total": 480.0, "currency": "EUR" }
```

GET /api/v1/client/reservations?scope=upcoming&page=0&size=10 (response)
```json
{
  "items": [
    { "id": 123, "roomId": 42, "checkIn": "2025-10-01", "checkOut": "2025-10-05", "status": "CONFIRMED", "total": 480.0, "currency": "EUR" }
  ],
  "page": 0, "size": 10, "total": 1
}
```

GET /api/v1/reservations/123 (response)
```json
{ "id": 123, "roomId": 42, "checkIn": "2025-10-01", "checkOut": "2025-10-05", "status": "CONFIRMED", "guests": 2, "total": 480.0, "currency": "EUR" }
```

PUT /api/v1/reservations/123 (request)
```json
{ "checkOut": "2025-10-06" }
```

DELETE /api/v1/reservations/123 (response)
```json
{ "id": 123, "status": "CANCELLED" }
```

## Espace Employé (Employee Workspace)

PUT /api/v1/employee/rooms/42/status (request)
```json
{ "status": "PROPRE", "note": "Inspection terminée" }
```

GET /api/v1/employee/reservations/search?query=Doe&date=2025-10-01 (response)
```json
{ "items": [ { "id": 123, "customerName": "John Doe", "roomId": 42, "checkIn": "2025-10-01", "checkOut": "2025-10-05", "status": "CONFIRMED" } ] }
```

GET /api/v1/employee/shifts (response)
```json
{ "items": [ { "id": 11, "start": "2025-10-01T08:00:00Z", "end": "2025-10-01T16:00:00Z" } ] }
```

POST /api/v1/employee/leaves (request)
```json
{ "from": "2025-12-24", "to": "2025-12-26", "reason": "Vacances" }
```

## Feedback

POST /api/v1/feedbacks (request)
```json
{ "reservationId": 123, "rating": 5, "comment": "Séjour parfait" }
```

GET /api/v1/client/feedbacks (response)
```json
{ "items": [ { "id": 501, "reservationId": 123, "rating": 5, "comment": "Séjour parfait", "reply": "Merci !" } ] }
```

POST /api/v1/admin/feedbacks/501/reply (request)
```json
{ "message": "Merci pour votre retour, au plaisir de vous revoir." }
```

## Événements & Installations

GET /api/v1/events?from=2025-10-02T00:00:00Z&to=2025-10-02T23:59:59Z&eventType=SPA (response)
```json
{ "items": [ { "id": 700, "title": "Spa matinée", "eventType": "SPA", "startAt": "2025-10-02T09:00:00Z", "endAt": "2025-10-02T12:00:00Z", "capacity": 10, "price": 30.0, "currency": "EUR", "available": 6 } ] }
```

POST /api/v1/events/700/bookings (request)
```json
{ "participants": 2 }
```

POST /api/v1/events/700/bookings (response)
```json
{ "bookingId": 9001, "eventId": 700, "participants": 2, "status": "CONFIRMED", "totalPrice": 60.0, "currency": "EUR" }
```

## Administration — Clients & Employés

POST /api/v1/admin/clients (request)
```json
{ "firstName": "Anna", "lastName": "Smith", "email": "anna@smith.com", "phone": "+33999888777" }
```

POST /api/v1/admin/clients (response)
```json
{ "id": 202, "firstName": "Anna", "lastName": "Smith", "email": "anna@smith.com" }
```

PUT /api/v1/admin/employees/310 (request)
```json
{ "firstName": "Paul", "lastName": "Martin", "status": "ACTIVE" }
```

## Analyses & Rapports (ADMIN)

GET /api/v1/admin/analytics/metrics (response)
```json
{
  "todayOccupancy": 0.78,
  "weeklyReservations": 56,
  "monthlyRevenue": 50230.5,
  "receivedComments": 12
}
```

GET /api/v1/admin/analytics/kpis (response)
```json
{ "occupancyRate": 0.75, "averageRating": 4.2, "monthlyRevenue": 50000.0 }
```

## RH (ADMIN)

POST /api/v1/admin/shifts (request)
```json
{ "employeeId": 310, "start": "2025-10-03T08:00:00Z", "end": "2025-10-03T16:00:00Z" }
```

POST /api/v1/admin/leaves/41/approve (response)
```json
{ "leaveId": 41, "status": "APPROVED" }
```

POST /api/v1/admin/events/700/notifications (request)
```json
{ "channel": "EMAIL", "subject": "Changement d'horaire", "message": "L'événement commence à 10h." }
```

## Fidélité (Client)

GET /api/v1/loyalty/balance (response)
```json
{ "points": 320 }
```

POST /api/v1/loyalty/redeem (request)
```json
{ "reservationId": 123, "points": 100 }
```

POST /api/v1/loyalty/redeem (response)
```json
{ "reservationId": 123, "appliedPoints": 100, "remainingPoints": 220 }
```

