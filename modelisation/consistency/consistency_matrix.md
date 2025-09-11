### Matrice de cohérence — MyBooking

#### Auth & Profils
| ID | Feature | Acteurs | Réf. fonctionnalités | APIs | MCD | MPD | UML | Statut | Notes |
|---|---|---|---|---|---|---|---|---|---|
| FR-AUTH-01 | Inscription client | Client | Compte & Auth | POST /auth/register | USER | app_user | User | OK | birthdate |
| FR-AUTH-02 | Connexion | Client/Admin/Employé | Compte & Auth | POST /auth/login | USER, ROLE | app_user, role, user_role | User, Role | OK | JWT |
| FR-AUTH-03 | Profil courant | Tous | Compte & Auth | GET /auth/profile | USER | app_user | User | OK |  |
| FR-AUTH-04 | Reset mot de passe | Tous | Compte & Auth | POST /auth/password/forgot, /auth/password/reset | USER | app_user | User | OK |  |
| FR-CLT-01 | Profil client | Client | Profil | GET/PUT /client/profile | USER | app_user | User | OK |  |
| FR-CLT-02 | Préférences notif | Client | Compte & Auth | GET/PUT /client/notifications/preferences | NOTIFICATION_PREFERENCE | notification_preference | NotificationPreference | OK | 1–1 |

#### Chambres
| ID | Feature | Acteurs | Réf. fonctionnalités | APIs | MCD | MPD | UML | Statut | Notes |
|---|---|---|---|---|---|---|---|---|---|
| FR-ROOM-01 | Lister/filtrer | Client | Chambres | GET /rooms | ROOM | room | Room | OK | roomType, currency |
| FR-ROOM-02 | Détail | Client | Chambres | GET /rooms/{id} | ROOM, ROOM_PHOTO, EQUIPMENT | room, room_photo, equipment, room_equipment | Room, RoomPhoto, Equipment | OK |  |
| FR-ROOM-03 | Disponibilités | Client | Chambres | GET /rooms/{id}/availability | ROOM | room | Room | OK | service |
| FR-ROOM-ADM-01 | CRUD chambre | Admin | Chambres | POST/PUT/DELETE /rooms | ROOM | room | Room | OK |  |

#### Réservations
| ID | Feature | Acteurs | Réf. fonctionnalités | APIs | MCD | MPD | UML | Statut | Notes |
|---|---|---|---|---|---|---|---|---|---|
| FR-RES-01 | Créer réservation | Client | Réservations | POST /reservations | USER–RESERVATION–ROOM | reservation | Reservation | OK | status=CONFIRMED |
| FR-RES-02 | Mes réservations | Client | Réservations | GET /client/reservations (+scope) | USER–RESERVATION | reservation | Reservation | OK | currency |
| FR-RES-03 | Détail/Modif/Annul | Client | Réservations | GET/PUT/DELETE /reservations/{id} | USER–RESERVATION | reservation | Reservation | OK | CANCELLED |
| FR-RES-ADM-01 | Gestion réservations | Admin | Réservations | GET/POST/PUT/DELETE /admin/reservations, /reassign | RESERVATION | reservation | Reservation | OK | reassign=annuler+créer |

#### Clients & Employés (Admin)
| ID | Feature | Acteurs | Réf. fonctionnalités | APIs | MCD | MPD | UML | Statut | Notes |
|---|---|---|---|---|---|---|---|---|---|
| FR-CLTS-ADM-01 | CRUD clients | Admin | Clients & Employés | /admin/clients | USER | app_user | User | OK |  |
| FR-EMPS-ADM-01 | CRUD employés | Admin | Clients & Employés | /admin/employees | EMPLOYEE | employee | Employee | OK | ACTIVE/INACTIVE |

#### Feedback & Fidélité
| ID | Feature | Acteurs | Réf. fonctionnalités | APIs | MCD | MPD | UML | Statut | Notes |
|---|---|---|---|---|---|---|---|---|---|
| FR-FB-01 | Créer feedback | Client | Feedback | POST /feedbacks | FEEDBACK, RESERVATION | feedback | Feedback | OK | rating 1–5 |
| FR-FB-02 | Mes feedbacks | Client | Feedback | GET /client/feedbacks | FEEDBACK | feedback | Feedback | OK |  |
| FR-FB-ADM-01 | Répondre feedback | Admin | Feedback | POST /admin/feedbacks/{id}/reply | FEEDBACK_REPLY | feedback_reply | FeedbackReply | OK | auteur=User |
| FR-LYT-01 | Solde points | Client | Fidélité | GET /loyalty/balance | LOYALTY_ACCOUNT | loyalty_account | LoyaltyAccount | OK | 1–1 |
| FR-LYT-02 | Historique points | Client | Fidélité | GET /loyalty/history | LOYALTY_TRANSACTION | loyalty_transaction | LoyaltyTransaction | OK | EARN/REDEEM |
| FR-LYT-03 | Utiliser points | Client | Fidélité | POST /loyalty/redeem | LOYALTY_TRANSACTION, RESERVATION | loyalty_transaction, reservation | LoyaltyTransaction | OK |  |

#### Événements
| ID | Feature | Acteurs | Réf. fonctionnalités | APIs | MCD | MPD | UML | Statut | Notes |
|---|---|---|---|---|---|---|---|---|---|
| FR-EVT-01 | Lister/filtrer | Client | Événements | GET /events?from&to&eventType | EVENT | event | Event | OK | incl. WEDDING |
| FR-EVT-02 | Détail | Client | Événements | GET /events/{id} | EVENT | event | Event | OK | price/currency |
| FR-EVT-03 | Réserver | Client | Événements | POST /events/{id}/bookings | EVENT_BOOKING | event_booking | EventBooking | OK | totalPrice/currency |
| FR-EVT-ADM-01 | CRUD événements | Admin | Événements | /admin/events (GET/POST/PUT/DELETE/open/close) | EVENT | event | Event | OK |  |
| FR-EVT-ADM-02 | Notifications | Admin | Événements | POST /admin/events/{id}/notifications | EVENT_NOTIFICATION | event_notification | EventNotification | OK | createdBy user |

#### Analyses & Rapports
| ID | Feature | Acteurs | Réf. fonctionnalités | APIs | MCD | MPD | UML | Statut | Notes |
|---|---|---|---|---|---|---|---|---|---|
| FR-ANL-01 | Métriques | Admin | Analyses & Rapports | GET /admin/analytics/metrics | dérivé | requêtes | services | OK |  |
| FR-ANL-02 | KPIs | Admin | Analyses & Rapports | GET /admin/analytics/kpis | FEEDBACK, RESERVATION | feedback, reservation | services | OK | moyenne 1–5 |

#### RH & Espace Employé
| ID | Feature | Acteurs | Réf. fonctionnalités | APIs | MCD | MPD | UML | Statut | Notes |
|---|---|---|---|---|---|---|---|---|---|
| FR-RH-01 | Shifts (admin) | Admin | RH | POST/PUT/DELETE /admin/shifts | SHIFT | shift | Shift | OK |  |
| FR-RH-02 | Congés (admin) | Admin | RH | GET /admin/leaves + approve/reject | LEAVE_REQUEST | leave_request | LeaveRequest | OK |  |
| FR-RH-03 | Formations | Admin | RH | /admin/trainings (CRUD) | TRAINING, EMPLOYEE_TRAINING | training, employee_training | Training, EmployeeTraining | OK |  |
| FR-EMP-01 | Profil employé | Employé | Espace Employé | GET/PUT /employee/profile | EMPLOYEE–USER | employee, app_user | Employee | OK |  |
| FR-EMP-02 | Mes shifts | Employé | Espace Employé | GET /employee/shifts | SHIFT | shift | Shift | OK |  |
| FR-EMP-03 | Pointer | Employé | Espace Employé | POST /employee/shifts/clock-in|out | SHIFT | shift | Shift | OK | guard ACTIVE |
| FR-EMP-04 | Demande de congé | Employé | Espace Employé | POST /employee/leaves | LEAVE_REQUEST | leave_request | LeaveRequest | OK |  |
| FR-EMP-05 | Tâches | Employé | Espace Employé | GET/POST/PUT /employee/tasks | EMPLOYEE_TASK | employee_task | EmployeeTask | OK |  |
| FR-EMP-06 | Statut chambre | Employé | Espace Employé | PUT /employee/rooms/{id}/status | ROOM_STATUS_UPDATE | room_status_update | RoomStatusUpdate | OK |  |
| FR-EMP-07 | Annonces | Employé | Espace Employé | GET /employee/announcements; POST reply | ANNOUNCEMENT, ANNOUNCEMENT_REPLY | announcement, announcement_reply | Announcement, AnnouncementReply | OK |  |
| FR-EMP-08 | Recherche réservations | Employé | Espace Employé | GET /employee/reservations/search | RESERVATION | reservation | Reservation | OK | read-only |

