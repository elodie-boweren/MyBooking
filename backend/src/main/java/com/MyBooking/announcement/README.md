### com.mybooking.announcement — Annonces internes

- Rôle: publier des annonces côté Admin et permettre aux employés de lire et répondre simplement.

- Sous-dossiers attendus:
  - `controller`: `EmployeeAnnouncementController` (liste + post reply), `AdminAnnouncementController` (si besoin de CRUD Admin).
  - `service`: `AnnouncementService`.
  - `domain`: `Announcement`, `AnnouncementReply`.
  - `repository`: `AnnouncementRepository`, `AnnouncementReplyRepository`.
  - `dto`: `AnnouncementDto`, `ReplyDto`, `CreateAnnouncementRequest` (optionnel).

- Endpoints couverts:
  - Employé: `GET /api/v1/employee/announcements`, `POST /api/v1/employee/announcements/{id}/reply`.
  - Admin (optionnel): `GET/POST/PUT/DELETE /api/v1/admin/announcements`.

- Liens MCD/MPD/UML:
  - Tables: `announcement`, `announcement_reply`.
  - Classes: `Announcement`, `AnnouncementReply`.

- Notes d’implémentation:
  - Les réponses référencent un employé (FK sur `employee(user_id)` dans MPD).

## TODO checklist
- [ ] Entités: `Announcement`, `AnnouncementReply`.
- [ ] Repositories: annonces, réponses par annonce et employé.
- [ ] Services: publier annonces (admin), répondre (employé).
- [ ] Controllers: GET employé, POST reply; (optionnel CRUD admin).
- [ ] Tests: autorisations et création de reply liée à un employé actif.
