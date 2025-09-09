### com.mybooking.employee — Employés (profil & gestion Admin)

- Rôle: gérer les profils employés (workspace) côté employé, et CRUD employé côté Admin. Applique le statut `ACTIVE/INACTIVE`.

- Sous-dossiers attendus:
  - `controller`: `EmployeeProfileController` (`/api/v1/employee/profile`), `AdminEmployeeController` (`/api/v1/admin/employees`).
  - `service`: `EmployeeService`.
  - `domain`: entité `Employee`, enum `EmployeeStatus`.
  - `repository`: `EmployeeRepository`.
  - `dto`: `EmployeeDto`, `UpdateEmployeeRequest`.

- Endpoints couverts:
  - Admin: `GET/POST/PUT/DELETE /api/v1/admin/employees`.
  - Employé: `GET/PUT /api/v1/employee/profile`.

- Liens MCD/MPD/UML:
  - Tables: `employee` (PK = user_id), `app_user`.
  - Classes: `Employee`, `EmployeeStatus`, `User`.

- Notes d’implémentation:
  - Les actions du workspace (tâches, shifts, etc.) doivent vérifier `EmployeeStatus=ACTIVE`.
  - L’entité `Employee` référence le `User` (1–1); la suppression doit préserver l’historique.
