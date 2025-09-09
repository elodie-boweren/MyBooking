### com.mybooking.common — fondations transverses

- Rôle: regroupe les préoccupations techniques communes: configuration Spring Boot, sécurité, gestion d’erreurs, mapping, outils, configuration OpenAPI. Aucun code métier ici.

- Sous-dossiers attendus:
  - `config`: configuration Spring (Jackson, Locale, OpenAPI, CORS, Pageable, Flyway, MapStruct), `ApplicationProperties`.
  - `security`: `SecurityConfig` (stateless JWT), filtres JWT, utilitaires de token, `CurrentUser` resolver.
  - `exception`: `GlobalExceptionHandler` (`@ControllerAdvice`), erreurs normalisées (Problem+JSON), exceptions métier (ex. `BusinessRuleException`, `NotFoundException`).
  - `mapper`: mappers MapStruct communs et config de mappage (ex. `OffsetDateTimeMapper`, `MoneyMapper`).
  - `util`: helpers techniques (date/heure, pagination, validation utilitaire).

- Principales classes/fichiers:
  - `SecurityConfig`: protège les routes, rôles `ROLE_CLIENT`, `ROLE_EMPLOYE`, `ROLE_ADMIN`. Active JWT Bearer.
  - `JwtAuthenticationFilter`, `TokenService` (si placé ici), `PasswordEncoder` bean (BCrypt).
  - `OpenApiConfig`: groupage par package, doc des schémas communs.
  - `GlobalExceptionHandler`: mappe `MethodArgumentNotValidException`, `ConstraintViolationException`, `DataIntegrityViolationException`, etc.

- Acteurs & endpoints couverts:
  - Tous (infrastructure). Définit les autorisations par motif d’URL (voir modules applicatifs).

- Notes d’implémentation:
  - `spring.jpa.hibernate.ddl-auto=validate` (Flyway gère le schéma).
  - `@Validated` sur controllers/services, Bean Validation sur DTOs.
  - Journalisation structurée; Actuator activé via `management.endpoints.web.exposure.include=health,info`.

- Tests à prévoir:
  - Tests de sécurité (WebMvc slice) pour vérifier l’accès/403.
  - Tests de contrôleur d’erreurs (validation -> 400, introuvable -> 404).

## TODO checklist (à faire dans ce package)
- [ ] Créer `SecurityConfig` (stateless JWT), configurer antMatchers par ressource et rôles.
- [ ] Implémenter `JwtAuthenticationFilter` et `TokenService` (sign/verify, expiry).
- [ ] Exposer `PasswordEncoder` (BCrypt, strength >= 10).
- [ ] Ajouter `OpenApiConfig` (groupes par package, `servers`), activer Swagger UI.
- [ ] Mettre en place `GlobalExceptionHandler` + format Problem JSON.
- [ ] Configurer Jackson (module JavaTime), CORS (Angular origin), Pageable par défaut.
- [ ] Ajout properties `application.yml` (profiles dev/test/prod).
- [ ] Tests: sécurité (403/401), mapping erreurs, CORS.
