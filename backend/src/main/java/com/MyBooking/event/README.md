### com.mybooking.event — Événements & réservations d’événement

- Rôle: gérer le catalogue d’événements (type fixe, créneaux startAt/endAt, prix/devise), les réservations d’événement côté client, et les notifications liées aux événements côté admin.

- Sous-dossiers attendus:
  - `controller`: `EventController` (public), `AdminEventController` (CRUD), `AdminEventNotificationController` (notifications), `EventBookingController` (si séparé).
  - `service`: `EventService`, `EventBookingService`, `EventNotificationService` (capacité, calcul des totaux, envoi notif).
  - `domain`: `Event` (enum `EventType` incluant WEDDING), `EventBooking` (statut PENDING/CONFIRMED/CANCELLED), `EventNotification`.
  - `repository`: `EventRepository`, `EventBookingRepository`, `EventNotificationRepository`.
  - `dto`: `EventDto`, `CreateOrUpdateEventRequest`, `CreateBookingRequest`, `BookingDto`, `NotificationRequest`.

- Endpoints couverts:
  - Client: `GET /api/v1/events?from&to&eventType`, `GET /api/v1/events/{id}`, `POST /api/v1/events/{id}/bookings`.
  - Admin: `GET/POST/PUT/DELETE /api/v1/admin/events`, `PUT /api/v1/admin/events/{id}/open|close`, `POST /api/v1/admin/events/{id}/notifications`.

- Liens MCD/MPD/UML:
  - Tables: `event`, `event_booking`, `event_notification`.
  - Classes: `Event`, `EventBooking`, `EventNotification`, enums `EventType`, `EventBookingStatus`.

- Notes d’implémentation:
  - Filtre par fenêtre temporelle (index sur start_at, end_at) et `event_type`.
  - Calcul `totalPrice = participants * price` (en devise de l’événement).

## TODO checklist
- [ ] Entités: `Event`, `EventBooking`, `EventNotification` + enums.
- [ ] Repositories: filtres par fenêtre et type; bookings par user/event.
- [ ] Services: création/maj événement, booking (capacité, total), notifications.
- [ ] Controllers: public + admin + bookings; DTOs; validations.
- [ ] Tests: fenêtre temporelle, capacité, totalPrice, envoi notification (mock).
