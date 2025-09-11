Backend plan (Spring Boot) — high-level

Goals
Clean, modular Spring Boot backend aligned to our MERISE, MPD, UML, and APIs.
Clear separation of concerns, testability, and production readiness (security, validation, docs, migrations).

Project structure (packages by feature, shared kernel for cross-cutting)
Root modules
pom.xml or build.gradle (Maven recommended)
src/main/java/com/mybooking/...
src/main/resources/
src/test/java/...

Recommended package layout
com.mybooking.common
config (Jackson, OpenAPI, CORS, i18n, Pageable config)
exception (GlobalExceptionHandler, ProblemDetails)
security (JWT filters, SecurityConfig, Role authorizations)
mapper (MapStruct mappers)
util (DateTime, Money, Pagination utils)

com.mybooking.auth
controller (AuthController)
service (AuthService, TokenService, PasswordService)
domain (User, Role, UserRole, NotificationPreference)
repository (UserRepository, RoleRepository)
dto (LoginRequest, RegisterRequest, ProfileDto)

com.mybooking.customer (Admin: clients CRUD)
controller (AdminClientController)
service (ClientAdminService)
repository (reuse UserRepository)
dto (ClientDto, CreateClientRequest)

com.mybooking.employee (Admin: employees CRUD; Employee workspace profile)
controller (AdminEmployeeController, EmployeeProfileController)
service (EmployeeService)
domain (Employee)
repository (EmployeeRepository)
dto (EmployeeDto, UpdateEmployeeRequest)

com.mybooking.room
controller (RoomController, EmployeeRoomOpsController)
service (RoomService, RoomStatusService)
domain (Room, Equipment, RoomPhoto, RoomStatusUpdate)
repository (RoomRepository, EquipmentRepository, RoomStatusUpdateRepository)
dto (RoomDto, RoomSearchCriteria, UpdateRoomStatusRequest)

com.mybooking.reservation
controller (ReservationController, AdminReservationController, EmployeeReservationReadController)
service (ReservationService, availability rules)
domain (Reservation)
repository (ReservationRepository)
dto (CreateReservationRequest, ReservationDto)

com.mybooking.feedback
controller (FeedbackController, AdminFeedbackController)
service (FeedbackService)
domain (Feedback, FeedbackReply)
repository (FeedbackRepository, FeedbackReplyRepository)
dto (CreateFeedbackRequest, ReplyRequest)

com.mybooking.loyalty
controller (LoyaltyController)
service (LoyaltyService)
domain (LoyaltyAccount, LoyaltyTransaction)
repository (LoyaltyAccountRepository, LoyaltyTransactionRepository)
dto (BalanceDto, HistoryItemDto, RedeemRequest)

com.mybooking.event
controller (EventController, AdminEventController, AdminEventNotificationController)
service (EventService, EventBookingService, EventNotificationService)
domain (Event, EventBooking, EventNotification)
repository (EventRepository, EventBookingRepository, EventNotificationRepository)
dto (EventDto, CreateBookingRequest, BookingDto)

com.mybooking.hr
controller (AdminShiftController, AdminLeaveController, AdminTrainingController, EmployeeShiftController, EmployeeLeaveController)
service (ShiftService, LeaveService, TrainingService, EmployeeTaskService)
domain (Shift, LeaveRequest, Training, EmployeeTraining, EmployeeTask)
repository (repositories for all)
dto (ShiftDto, LeaveRequestDto, TrainingDto, TaskDto)

com.mybooking.announcement
controller (EmployeeAnnouncementController)
service (AnnouncementService)
domain (Announcement, AnnouncementReply)
repository (AnnouncementRepository, AnnouncementReplyRepository)
dto (AnnouncementDto, ReplyDto)

com.mybooking.analytics
controller (AnalyticsController)
service (AnalyticsService)
dto (MetricsDto, KpisDto)

Resources
src/main/resources/application.yml (profiles: dev, test, prod)
src/main/resources/db/migration (Flyway SQL from MPD.sql broken into versioned migrations)
src/main/resources/static/ (optional), templates/ (optional)
Build, dependencies
Spring Boot starters: Web, Security, Validation, Data JPA, Actuator
Database: PostgreSQL driver, Flyway
Mapping: MapStruct
JWT: spring-security-oauth2-resource-server or jjwt
Docs: springdoc-openapi-starter-webmvc-ui
Test: JUnit 5, Mockito, Spring Test, Testcontainers (Postgres), WireMock (if needed)
Cross-cutting architecture
Security
JWT auth (Bearer), ROLE_CLIENT, ROLE_EMPLOYE, ROLE_ADMIN
Method-level security (@PreAuthorize) by module
Password hashing (BCrypt), account activation flags for employees (ACTIVE/INACTIVE guards)
Validation & errors
Bean Validation on DTOs; ConstraintViolation -> Problem+JSON via @ControllerAdvice
Mapping
Entity <-> DTO via MapStruct mappers per feature
Persistence
JPA entities matching MPD; explicit indexes via migrations
Pagination/sorting for list endpoints
Observability
Actuator (health, metrics), structured logging (JSON-ready)

API
Versioned /api/v1, springdoc UI at /swagger-ui.html
Consistent response envelopes for pages (items, page, size, total)
Implementation plan (backend-only, no UI yet)
Milestone 0 — Project bootstrap
Initialize Spring Boot project (Maven), add dependencies
Configure application.yml profiles; set Postgres connection
Add Flyway; split MPD.sql into V1__init.sql (+ seed roles)
Add OpenAPI, CORS, Jackson modules


Milestone 1 — Auth & RBAC
Entities: User, Role, UserRole, NotificationPreference
JWT security, password hashing, login/register/profile/reset
Role-based guards; data fixtures (roles)
Tests: unit (services), web-slice (controllers), integration (Testcontainers)


Milestone 2 — Rooms
Entities: Room, Equipment, RoomPhoto, RoomStatusUpdate
Endpoints: list/filter, detail, availability stub, admin CRUD, employee status updates
Validation (roomType, currency), pagination
Tests (repository specs for filters, controller slices)


Milestone 3 — Reservations
Entity: Reservation (status: CONFIRMED/CANCELLED)
Client CRUD (according to policy), admin management (reassign = cancel+create), employee read-only search
Availability rules (basic overlap check)
Tests (domain rules, overlap queries, controllers)


Milestone 4 — Feedback
Entities: Feedback, FeedbackReply
Endpoints: create/list (client), reply (admin)
Tests (rating validation 1–5, reply ownership)


Milestone 5 — Loyalty
Entities: LoyaltyAccount (1–1), LoyaltyTransaction (EARN/REDEEM)
Endpoints: balance, history, redeem (apply to reservation total, update usedPoints)
Consistency rules (cannot redeem > balance)
Tests (account lifecycle, transactions)


Milestone 6 — Events
Entities: Event (eventType, startAt/endAt, price, currency), EventBooking (totalPrice, currency), EventNotification
Endpoints: list/filter by window+type, detail, booking; admin CRUD, open/close, notifications
Capacity checks, total calculations
Tests (time window queries, capacity constraints)


Milestone 7 — HR & Employee workspace
Entities: Employee, Shift, LeaveRequest, Training, EmployeeTraining, EmployeeTask, Announcement, AnnouncementReply
Admin: shifts/leaves/trainings; Employee: profile/shifts/clock/leaves/tasks/announcements
Guards: only ACTIVE employees perform workspace actions
Tests (clocking, leave transitions, task state machine)


Milestone 8 — Analytics
Endpoints: metrics and KPIs (query-based), no new tables
Tests (service-level aggregation with seeded data)