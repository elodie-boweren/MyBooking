### API Endpoints — MyBooking

Convention:
- Toutes les routes sont préfixées par `/api`.
- Versionnage recommandé: `/api/v1/...` (indicatif ici).
- Auth standard JWT (Authorization: Bearer ...). Rôles: `CLIENT`, `ADMIN`, `EMPLOYE`.

## Auth & Comptes
- POST `/api/v1/auth/register` — Inscription client
- POST `/api/v1/auth/login` — Connexion (client/admin/employé)
- POST `/api/v1/auth/logout` — Déconnexion
- POST `/api/v1/auth/password/forgot` — Demander réinitialisation mot de passe
- POST `/api/v1/auth/password/reset` — Réinitialiser mot de passe
- GET `/api/v1/auth/profile` — Récupérer le profil courant

## Notifications (préférences du client)
- GET `/api/v1/client/notifications/preferences` — Lire préférences de notification (CLIENT)
- PUT `/api/v1/client/notifications/preferences` — Mettre à jour préférences (CLIENT)

## Profil (client & employé)
- GET `/api/v1/client/profile` — Consulter mon profil (CLIENT)
- PUT `/api/v1/client/profile` — Mettre à jour mon profil (CLIENT)
- PUT `/api/v1/client/password` — Changer mon mot de passe (CLIENT)

## Chambres
- GET `/api/v1/rooms` — Lister/filtrer les chambres (dates, capacité, roomType, prix, currency)
- GET `/api/v1/rooms/{roomId}` — Détails d'une chambre (photos, équipements, description, roomType, prix, currency)
- GET `/api/v1/rooms/{roomId}/availability` — Disponibilités en temps réel

- POST `/api/v1/rooms` — Créer une chambre (roomType, capacité, prix, currency, équipements, statut)
- PUT `/api/v1/rooms/{roomId}` — Mettre à jour une chambre (roomType, capacité, prix, currency, équipements, statut)
- DELETE `/api/v1/rooms/{roomId}` — Supprimer une chambre

## Réservations
Client:
- POST `/api/v1/reservations` — Créer une réservation (total en devise)
- GET `/api/v1/client/reservations` — Lister mes réservations (filtres: `scope=upcoming|past|all`, `from`, `to`, `page`, `size`, `sort`)
- GET `/api/v1/client/reservations/upcoming` — Mes réservations à venir (raccourci)
- GET `/api/v1/client/reservations/past` — Mes réservations passées (raccourci)
- GET `/api/v1/reservations/{reservationId}` — Détail d'une réservation (si propriétaire)
- PUT `/api/v1/reservations/{reservationId}` — Modifier (si politique le permet)
- DELETE `/api/v1/reservations/{reservationId}` — Annuler (si politique le permet)
  - Statut de réservation: `CONFIRMED` ou `CANCELLED`

Employé (consultation simple):
- GET `/api/v1/reservations/search` — Rechercher une réservation (nom/ID/date) (lecture seule)

Administration réservations (ADMIN):
- GET `/api/v1/admin/reservations` — Lister toutes les réservations
- GET `/api/v1/admin/reservations/{reservationId}` — Détail d'une réservation
- POST `/api/v1/admin/reservations` — Créer au nom d'un client
- PUT `/api/v1/admin/reservations/{reservationId}` — Modifier
- DELETE `/api/v1/admin/reservations/{reservationId}` — Annuler
- POST `/api/v1/admin/reservations/{reservationId}/reassign` — Réassigner à une autre chambre (implémenté en annulation+recréation)
  - Statut de réservation: `CONFIRMED` ou `CANCELLED`

## Clients (ADMIN)
- GET `/api/v1/admin/clients` — Lister les clients
- GET `/api/v1/admin/clients/{clientId}` — Détail client
- POST `/api/v1/admin/clients` — Créer client
- PUT `/api/v1/admin/clients/{clientId}` — Mettre à jour client
- DELETE `/api/v1/admin/clients/{clientId}` — Supprimer client

## Employés (ADMIN)
- GET `/api/v1/admin/employees` — Lister employés
- GET `/api/v1/admin/employees/{employeeId}` — Détail employé
- POST `/api/v1/admin/employees` — Créer employé
- PUT `/api/v1/admin/employees/{employeeId}` — Mettre à jour employé (profil, statut)
- DELETE `/api/v1/admin/employees/{employeeId}` — Supprimer employé

## Feedback
Client:
- POST `/api/v1/feedbacks` — Créer un feedback (après séjour, rating 1–5)
- GET `/api/v1/client/feedbacks` — Lister mes feedbacks

Administration:
- GET `/api/v1/admin/feedbacks` — Lister tous les feedbacks
- GET `/api/v1/admin/feedbacks/{feedbackId}` — Détail feedback
- POST `/api/v1/admin/feedbacks/{feedbackId}/reply` — Répondre à un feedback

## Événements & Installations
Client:
- GET `/api/v1/events` — Lister/filtrer événements (fenêtre: `from`, `to`; filtres: `eventType`)
- GET `/api/v1/events/{eventId}` — Détail événement (title, eventType, startAt, endAt, capacity, price, currency)
- POST `/api/v1/events/{eventId}/bookings` — Réserver un événement (créneau horaire, selon capacité, total en devise)

Administration:
- GET `/api/v1/admin/events` — Lister événements
- GET `/api/v1/admin/events/{eventId}` — Détail événement
- POST `/api/v1/admin/events` — Créer événement/installation
- PUT `/api/v1/admin/events/{eventId}` — Mettre à jour
- DELETE `/api/v1/admin/events/{eventId}` — Supprimer
- PUT `/api/v1/admin/events/{eventId}/open` — Ouvrir les réservations
- PUT `/api/v1/admin/events/{eventId}/close` — Fermer les réservations
- POST `/api/v1/admin/events/{eventId}/notifications` — Envoyer notification liée à l'événement

## Programme de fidélité (client)
- GET `/api/v1/loyalty/balance` — Solde de points du client
- GET `/api/v1/loyalty/history` — Historique des points
- POST `/api/v1/loyalty/redeem` — Utiliser des points sur une réservation

## Analyses & Rapports (ADMIN)
- GET `/api/v1/admin/analytics/metrics` — Métriques (occupation jour, réservations semaine, revenu mois, commentaires reçus)
- GET `/api/v1/admin/analytics/kpis` — KPIs (taux d'occupation, note moyenne (1–5), CA mensuel)
- GET `/api/v1/admin/reports/export` — Export CSV/PDF (params: type, période)

## RH  (ADMIN)
- GET `/api/v1/admin/shifts` — Planning/horaires des employés
- POST `/api/v1/admin/shifts` — Créer un shift
- PUT `/api/v1/admin/shifts/{shiftId}` — Mettre à jour un shift
- DELETE `/api/v1/admin/shifts/{shiftId}` — Supprimer un shift
- GET `/api/v1/admin/leaves` — Lister demandes de congé
- POST `/api/v1/admin/leaves/{leaveId}/approve` — Approuver
- POST `/api/v1/admin/leaves/{leaveId}/reject` — Refuser
- GET `/api/v1/admin/trainings` — Lister formations/certifications
- POST `/api/v1/admin/trainings` — Créer formation
- PUT `/api/v1/admin/trainings/{trainingId}` — Mettre à jour formation
- DELETE `/api/v1/admin/trainings/{trainingId}` — Supprimer formation

## Employé — Espace (Employee Workspace) avec les fonctionnalités qu'on a définies
Profil & Auth:
- GET `/api/v1/employee/profile` — Mon profil
- PUT `/api/v1/employee/profile` — Mettre à jour mon profil
- POST `/api/v1/employee/logout` — Déconnexion

Shifts:
- GET `/api/v1/employee/shifts` — Mes prochains shifts
- POST `/api/v1/employee/shifts/clock-in` — Pointer entrée
- POST `/api/v1/employee/shifts/clock-out` — Pointer sortie
- POST `/api/v1/employee/leaves` — Demander un congé

Tâches:
- GET `/api/v1/employee/tasks` — Mes tâches
- POST `/api/v1/employee/tasks` — Créer une tâche personnelle
- PUT `/api/v1/employee/tasks/{taskId}` — Mettre à jour état/note

Chambres (opérations de statut):
- GET `/api/v1/employee/rooms/search` — Rechercher chambre par numéro/statut
- PUT `/api/v1/employee/rooms/{roomId}/status` — Mettre à jour statut + note

Réservations (consultation):
- GET `/api/v1/employee/reservations/search` — Rechercher réservation (lecture seule)

Messages & annonces:
- GET `/api/v1/employee/announcements` — Lire annonces
- POST `/api/v1/employee/announcements/{announcementId}/reply` — Répondre (message simple)

