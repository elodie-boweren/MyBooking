### com.mybooking.room — Chambres & statuts

- Rôle: gestion des chambres (listes/filtre, détail, admin CRUD) et mises à jour de statut par les employés (PROPRE/SALE/HORS_SERVICE).

- Sous-dossiers attendus:
  - `controller`: `RoomController` (public), `EmployeeRoomOpsController` (workspace), `AdminRoomController` (si séparation CRUD admin).
  - `service`: `RoomService` (recherche/filtre), `RoomStatusService` (updates, journalisation).
  - `domain`: `Room`, `Equipment`, `RoomPhoto`, `RoomStatusUpdate`, enums `RoomType`, `RoomStatus`, `RoomCleanStatus`.
  - `repository`: `RoomRepository`, `EquipmentRepository`, `RoomPhotoRepository`, `RoomStatusUpdateRepository`.
  - `dto`: `RoomDto`, `RoomSearchCriteria`, `CreateOrUpdateRoomRequest`, `UpdateRoomStatusRequest`.

- Endpoints couverts:
  - Public/Client: `GET /api/v1/rooms`, `GET /api/v1/rooms/{id}`, `GET /api/v1/rooms/{id}/availability`.
  - Admin: `POST/PUT/DELETE /api/v1/rooms`.
  - Employé: `PUT /api/v1/employee/rooms/{id}/status`.

- Liens MCD/MPD/UML:
  - Tables: `room`, `room_photo`, `equipment`, `room_equipment`, `room_status_update`.
  - Classes: `Room`, `RoomPhoto`, `Equipment`, `RoomStatusUpdate` + enums.

- Notes d’implémentation:
  - Index sur `status` et critères fréquents; pagination par défaut.
  - `availability` calculée côté service (chevauchements de réservations).
