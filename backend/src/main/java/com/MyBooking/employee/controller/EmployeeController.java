package com.MyBooking.employee.controller;

import com.MyBooking.employee.dto.*;
import com.MyBooking.employee.service.EmployeeService;
import com.MyBooking.employee.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employee")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ==================== EMPLOYEE PROFILE ====================

    @GetMapping("/profile")
    public ResponseEntity<EmployeeResponseDto> getMyProfile(@RequestHeader("X-User-Id") Long userId) {
        Employee employee = employeeService.getEmployeeByUserId(userId);
        EmployeeResponseDto response = convertToEmployeeResponseDto(employee);
        return ResponseEntity.ok(response);
    }

    // ==================== TASK MANAGEMENT ====================

    @GetMapping("/tasks")
    public ResponseEntity<Page<TaskResponseDto>> getMyTasks(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) TaskStatus status,
            Pageable pageable) {
        Page<EmployeeTask> tasks = employeeService.getEmployeeTasks(userId, status, pageable);
        Page<TaskResponseDto> response = tasks.map(this::convertToTaskResponseDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> updateMyTask(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long taskId,
            @Valid @RequestBody TaskUpdateRequestDto request) {
        // Verify the task belongs to this employee
        EmployeeTask task = employeeService.getEmployeeTasks(userId, null, Pageable.unpaged())
                .getContent()
                .stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found or not assigned to you"));
        
        EmployeeTask updatedTask = employeeService.updateTaskStatus(taskId, request.getStatus(), request.getNote());
        TaskResponseDto response = convertToTaskResponseDto(updatedTask);
        return ResponseEntity.ok(response);
    }

    // ==================== LEAVE REQUEST MANAGEMENT ====================

    @PostMapping("/leave-requests")
    public ResponseEntity<LeaveRequestResponseDto> createLeaveRequest(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody LeaveRequestCreateRequestDto request) {
        LeaveRequest leaveRequest = employeeService.createLeaveRequest(userId, request.getFromDate(), request.getToDate(), request.getReason());
        LeaveRequestResponseDto response = convertToLeaveRequestResponseDto(leaveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/leave-requests")
    public ResponseEntity<Page<LeaveRequestResponseDto>> getMyLeaveRequests(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) LeaveRequestStatus status,
            Pageable pageable) {
        Page<LeaveRequest> requests = employeeService.getEmployeeLeaveRequests(userId, status, pageable);
        Page<LeaveRequestResponseDto> response = requests.map(this::convertToLeaveRequestResponseDto);
        return ResponseEntity.ok(response);
    }

    // ==================== TRAINING MANAGEMENT ====================

    @GetMapping("/trainings")
    public ResponseEntity<Page<EmployeeTrainingResponseDto>> getMyTrainings(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) TrainingStatus status,
            Pageable pageable) {
        Page<EmployeeTraining> trainings = employeeService.getEmployeeTrainings(userId, status, pageable);
        Page<EmployeeTrainingResponseDto> response = trainings.map(this::convertToEmployeeTrainingResponseDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/trainings/{trainingId}/status")
    public ResponseEntity<EmployeeTrainingResponseDto> updateMyTrainingStatus(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long trainingId,
            @Valid @RequestBody EmployeeTrainingUpdateRequestDto request) {
        EmployeeTraining employeeTraining = employeeService.updateEmployeeTrainingStatus(userId, trainingId, request.getStatus());
        EmployeeTrainingResponseDto response = convertToEmployeeTrainingResponseDto(employeeTraining);
        return ResponseEntity.ok(response);
    }

    // ==================== SHIFT MANAGEMENT ====================

    @GetMapping("/shifts")
    public ResponseEntity<Page<ShiftResponseDto>> getMyShifts(
            @RequestHeader("X-User-Id") Long userId,
            Pageable pageable) {
        Page<Shift> shifts = employeeService.getEmployeeShifts(userId, pageable);
        Page<ShiftResponseDto> response = shifts.map(this::convertToShiftResponseDto);
        return ResponseEntity.ok(response);
    }

    // ==================== AVAILABILITY CHECKS ====================

    @GetMapping("/availability")
    public ResponseEntity<EmployeeAvailabilityDto> checkMyAvailability(@RequestHeader("X-User-Id") Long userId) {
        boolean available = employeeService.isEmployeeAvailableForTask(userId);
        Employee employee = employeeService.getEmployeeByUserId(userId);
        
        String reason = "ACTIVE";
        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            reason = "INACTIVE";
        } else if (employeeService.hasInProgressTraining(userId)) {
            reason = "IN_TRAINING";
        } else {
            // Check for approved leave
            LocalDate today = LocalDate.now();
            if (!employeeService.isEmployeeAvailableForShift(userId, today)) {
                reason = "ON_LEAVE";
            }
        }
        
        EmployeeAvailabilityDto response = new EmployeeAvailabilityDto(
                userId,
                employee.getUser().getFirstName() + " " + employee.getUser().getLastName(),
                available,
                reason
        );
        return ResponseEntity.ok(response);
    }

    // ==================== DTO CONVERSION METHODS ====================

    private EmployeeResponseDto convertToEmployeeResponseDto(Employee employee) {
        return new EmployeeResponseDto(
                employee.getUserId(),
                employee.getUser().getEmail(),
                employee.getUser().getFirstName(),
                employee.getUser().getLastName(),
                employee.getStatus(),
                employee.getJobTitle(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }

    private TaskResponseDto convertToTaskResponseDto(EmployeeTask task) {
        return new TaskResponseDto(
                task.getId(),
                task.getEmployee().getId(),
                task.getEmployee().getFirstName() + " " + task.getEmployee().getLastName(),
                task.getEmployee().getEmail(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getNote(),
                task.getPhotoUrl(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    private LeaveRequestResponseDto convertToLeaveRequestResponseDto(LeaveRequest request) {
        return new LeaveRequestResponseDto(
                request.getId(),
                request.getEmployee().getId(),
                request.getEmployee().getFirstName() + " " + request.getEmployee().getLastName(),
                request.getEmployee().getEmail(),
                request.getFromDate(),
                request.getToDate(),
                request.getStatus(),
                request.getReason()
        );
    }

    private EmployeeTrainingResponseDto convertToEmployeeTrainingResponseDto(EmployeeTraining employeeTraining) {
        return new EmployeeTrainingResponseDto(
                employeeTraining.getEmployee().getId(),
                employeeTraining.getEmployee().getFirstName() + " " + employeeTraining.getEmployee().getLastName(),
                employeeTraining.getEmployee().getEmail(),
                employeeTraining.getTraining().getId(),
                employeeTraining.getTraining().getTitle(),
                employeeTraining.getTraining().getStartDate(),
                employeeTraining.getTraining().getEndDate(),
                employeeTraining.getStatus(),
                employeeTraining.getAssignedAt(),
                employeeTraining.getCompletedAt()
        );
    }

    private ShiftResponseDto convertToShiftResponseDto(Shift shift) {
        return new ShiftResponseDto(
                shift.getId(),
                shift.getEmployee().getId(),
                shift.getEmployee().getFirstName() + " " + shift.getEmployee().getLastName(),
                shift.getEmployee().getLastName(),
                shift.getStartAt(),
                shift.getEndAt()
        );
    }
}
