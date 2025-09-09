### com.mybooking.client — Administration des clients (côté Admin)

- Rôle: fournir au rôle ADMIN la gestion CRUD des comptes client (lecture, création, modification, suppression), distincte des endpoints d’auth et profil.

- Sous-dossiers attendus:
  - `controller`: `AdminClientController` (prefix `/api/v1/admin/clients`).
  - `service`: `ClientAdminService` (opérations CRUD, règles d’intégrité).
  - `dto`: `ClientDto`, `CreateClientRequest`, `UpdateClientRequest`.

- Endpoints couverts:
  - `GET /api/v1/admin/clients`, `GET /api/v1/admin/clients/{id}`.
  - `POST /api/v1/admin/clients`, `PUT /api/v1/admin/clients/{id}`, `DELETE /api/v1/admin/clients/{id}`.

- Liens MCD/MPD/UML:
  - Table: `app_user` (rôle CLIENT associé dans `user_role`).
  - Classe: `User` (avec `Role CLIENT`).

- Notes d’implémentation:
  - Vérifier l’unicité email; gérer soft delete via désactivation si nécessaire (sinon suppression réelle).
  - MapStruct pour projection `User` -> `ClientDto`.
