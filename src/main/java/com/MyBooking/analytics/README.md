### com.mybooking.analytics — Analyses & KPIs

- Rôle: exposer des métriques opérationnelles et des KPIs (tableaux de bord) via agrégations sur les données; pas de nouvelles tables.

- Sous-dossiers attendus:
  - `controller`: `AnalyticsController` (metrics, kpis).
  - `service`: `AnalyticsService` (requêtes et agrégations SQL/JPA/specs).
  - `dto`: `MetricsDto` (occupation jour, réservations semaine, revenu mois, commentaires reçus), `KpisDto` (taux d’occupation, note moyenne 1–5, CA mensuel).

- Endpoints couverts:
  - `GET /api/v1/admin/analytics/metrics`, `GET /api/v1/admin/analytics/kpis`.

- Liens MCD/MPD/UML:
  - Source: `reservation`, `room`, `feedback`, `event_booking`.

- Notes d’implémentation:
  - Optimiser par index; éventuellement vues matérialisées si nécessaire (via Flyway repeatables).

## TODO checklist
- [ ] DTOs: `MetricsDto`, `KpisDto`.
- [ ] Service: requêtes d’agrégation (occupation jour, réservations semaine, revenu mois, commentaires reçus, avg rating 1–5, CA mensuel).
- [ ] Controller: `AnalyticsController` (GET `/metrics`, `/kpis`).
- [ ] Tests: données seed en @Sql / Testcontainers, vérifier agrégations.
