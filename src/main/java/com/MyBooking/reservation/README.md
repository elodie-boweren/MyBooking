### com.mybooking.reservation — Réservations

- Rôle: gérer le cycle de vie des réservations côté client et côté admin (CRUD), fournir recherche lecture seule aux employés. Status limités à `CONFIRMED` ou `CANCELLED`.

- Sous-dossiers attendus:
  - `controller`: `ReservationController` (client), `AdminReservationController` (admin), `EmployeeReservationReadController` (search RO).
  - `service`: `ReservationService` (règles: chevauchement, annulation, réassignation = annuler+créer).
  - `domain`: `Reservation`, enum `ReservationStatus`.
  - `repository`: `ReservationRepository` (requêtes par date/plage), projections.
  - `dto`: `CreateReservationRequest`, `UpdateReservationRequest`, `ReservationDto`, `ReservationSearchCriteria`.

- Endpoints couverts:
  - Client: `POST /api/v1/reservations`, `GET /api/v1/client/reservations`, `GET/PUT/DELETE /api/v1/reservations/{id}`.
  - Employé: `GET /api/v1/employee/reservations/search` (lecture seule).
  - Admin: `GET/POST/PUT/DELETE /api/v1/admin/reservations`, `POST /api/v1/admin/reservations/{id}/reassign`.

- Liens MCD/MPD/UML:
  - Table: `reservation` (check_in, check_out, total, currency, used_points, status).
  - Classes: `Reservation`, `ReservationStatus`.

- Notes d’implémentation:
  - Validation dates (checkIn < checkOut), capacité chambre, overlap.
  - Annulation: passer à CANCELLED, conserver historique; réassignation = annuler puis créer une nouvelle.

## TODO checklist
- [ ] Entité `Reservation` + enum `ReservationStatus` (CONFIRMED/CANCELLED).
- [ ] Repository: requêtes par utilisateur, plage de dates, pagination.
- [ ] Service: création (règles d’overlap), annulation, réassignation.
- [ ] Controllers: client/admin/employé (RO) selon endpoints.
- [ ] DTOs: create/update/search/response + validations.
- [ ] Tests: règles d’overlap, transitions de statut, reassign.
