### com.mybooking.auth — Authentification & RBAC

- Rôle: gérer l’inscription client, la connexion, le profil courant, le reset mot de passe, et le contrôle d’accès par rôles (CLIENT, EMPLOYE, ADMIN).

- Sous-dossiers attendus:
  - `controller`: `AuthController` (register, login, logout, profile, forgot/reset), éventuellement `ProfileController` pour `/client/profile` si partagé.
  - `service`: `AuthService`, `TokenService`, `PasswordService`.
  - `domain`: entités `User`, `Role`, `UserRole`, `NotificationPreference` (si non centralisées ailleurs), enums `RoleCode`.
  - `repository`: `UserRepository`, `RoleRepository`, `NotificationPreferenceRepository`.
  - `dto`: `RegisterRequest`, `LoginRequest`, `ProfileDto`, `ForgotPasswordRequest`, `ResetPasswordRequest`, `NotificationPreferencesDto`.

- Endpoints couverts:
  - `POST /api/v1/auth/register`, `POST /api/v1/auth/login`, `POST /api/v1/auth/logout`.
  - `GET /api/v1/auth/profile`.
  - `POST /api/v1/auth/password/forgot`, `POST /api/v1/auth/password/reset`.
  - `GET/PUT /api/v1/client/notifications/preferences`.

- Liens MCD/MPD/UML:
  - Tables: `app_user`, `role`, `user_role`, `notification_preference`.
  - Classes: `User`, `Role`, `NotificationPreference`, `RoleCode`.

- Notes d’implémentation:
  - JWT stateless, refresh token optionnel.
  - Email unique, contraintes et index alignés sur MPD.
  - Prévoir semences de rôles (CLIENT, EMPLOYE, ADMIN) via Flyway.

## TODO checklist
- [ ] Entités: `User`, `Role`, `UserRole`, `NotificationPreference` (mapping JPA conforme MPD).
- [ ] Repositories: `UserRepository` (findByEmail), `RoleRepository`.
- [ ] Services: `AuthService` (register, login, profile), `PasswordService` (reset flow), `TokenService`.
- [ ] Controllers: `AuthController` (register/login/logout/profile/forgot/reset).
- [ ] DTOs: Register/Login/Profile/Forgot/Reset/NotificationPreferences + validations Bean Validation.
- [ ] Sécurité: intégration avec `SecurityConfig` (providers, filters).
- [ ] Flyway: V1 seeds rôles (CLIENT, EMPLOYE, ADMIN).
- [ ] Tests: unitaires services (hash, jwt), web layer (status codes), intégration (register/login/profile).
