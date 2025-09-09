### com.mybooking.hr — RH simplifié (shifts, congés, formations, tâches)

- Rôle: outillage RH côté Admin (planification, congés, formations) et fonctionnalités workspace côté Employé (shifts, pointer, demandes, tâches), en coordination avec le package `employee`.

- Sous-dossiers attendus:
  - `controller`: `AdminShiftController`, `AdminLeaveController`, `AdminTrainingController`, `EmployeeShiftController`, `EmployeeLeaveController`, `EmployeeTaskController`.
  - `service`: `ShiftService`, `LeaveService`, `TrainingService`, `EmployeeTaskService`.
  - `domain`: `Shift`, `LeaveRequest`, `Training`, `EmployeeTraining`, `EmployeeTask`.
  - `repository`: Repositories pour ces entités.
  - `dto`: `ShiftDto`, `CreateShiftRequest`, `LeaveRequestDto`, `ApproveRejectRequest`, `TrainingDto`, `EmployeeTaskDto`.

- Endpoints couverts:
  - Admin: `GET/POST/PUT/DELETE /api/v1/admin/shifts`, `GET /api/v1/admin/leaves` + `POST /{id}/approve|reject`, `GET/POST/PUT/DELETE /api/v1/admin/trainings`.
  - Employé: `GET /api/v1/employee/shifts`, `POST /api/v1/employee/shifts/clock-in|clock-out`, `POST /api/v1/employee/leaves`, `GET/POST/PUT /api/v1/employee/tasks`.

- Liens MCD/MPD/UML:
  - Tables: `shift`, `leave_request`, `training`, `employee_training`, `employee_task`.
  - Classes: `Shift`, `LeaveRequest`, `Training`, `EmployeeTraining`, `EmployeeTask`.

- Notes d’implémentation:
  - Guards: seules les actions employé sont autorisées pour `EmployeeStatus=ACTIVE`.
  - Horodatage en UTC (timestamptz), index sur `(start_at, end_at)`.

## TODO checklist
- [ ] Entités: `Shift`, `LeaveRequest`, `Training`, `EmployeeTraining`, `EmployeeTask`.
- [ ] Repositories: par employé, par période; status tasks.
- [ ] Services: shifts (création/maj), clock-in/out, congés (approve/reject), formations, tâches (todo/in_progress/done).
- [ ] Controllers: admin & employé; DTOs; validations & guards ACTIVE.
- [ ] Tests: transitions (tasks), horodatage, autorisations, clock.
