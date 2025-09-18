//Employee Management - CRUD operations for employees
//Task Management - Assign, update, and track employee tasks
//Leave Request Management - Handle leave requests with overlap validation
//Training Management - Manage employee training assignments
//Shift Management - Handle employee shifts and scheduling
//HR Analytics - Statistics and reporting/analytics

package com.MyBooking.employee.service;

import com.MyBooking.auth.domain.User;
import com.MyBooking.auth.domain.Role;
import com.MyBooking.auth.repository.UserRepository;
import com.MyBooking.common.exception.BusinessRuleException;
import com.MyBooking.common.exception.NotFoundException;
import com.MyBooking.employee.domain.*;
import com.MyBooking.employee.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeTaskRepository employeeTaskRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private EmployeeTrainingRepository employeeTrainingRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private UserRepository userRepository;

    // ==================== EMPLOYEE MANAGEMENT ====================

    /**
     * Create a new employee
     */
    public Employee createEmployee(Long userId, String jobTitle) {
        // Validate user exists and is not already an employee
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        if (employeeRepository.existsByUserId(userId)) {
            throw new BusinessRuleException("User is already an employee");
        }

        // Create employee
        Employee employee = new Employee(user, EmployeeStatus.ACTIVE, jobTitle);
        return employeeRepository.save(employee);
    }

    /**
     * Update employee information
     */
    public Employee updateEmployee(Long userId, String jobTitle, EmployeeStatus status) {
        Employee employee = getEmployeeByUserId(userId);
        
        if (jobTitle != null) {
            employee.setJobTitle(jobTitle);
        }
        if (status != null) {
            employee.setStatus(status);
        }
        
        return employeeRepository.save(employee);
    }

    /**
     * Deactivate an employee
     */
    public void deactivateEmployee(Long userId, String reason) {
        Employee employee = getEmployeeByUserId(userId);
        employee.setStatus(EmployeeStatus.INACTIVE);
        employeeRepository.save(employee);
    }

    /**
     * Get employee by user ID
     */
    @Transactional(readOnly = true)
    public Employee getEmployeeByUserId(Long userId) {
        return employeeRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Employee not found with user ID: " + userId));
    }

    /**
     * Search employees with criteria
     */
    @Transactional(readOnly = true)
    public Page<Employee> searchEmployees(EmployeeStatus status, String jobTitle, Pageable pageable) {
        return employeeRepository.findByCriteria(status, jobTitle, pageable);
    }

    /**
     * Get all active employees
     */
    @Transactional(readOnly = true)
    public List<Employee> getActiveEmployees() {
        return employeeRepository.findActiveEmployees();
    }

    /**
     * Get employees by job title
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByJobTitle(String jobTitle) {
        return employeeRepository.findByJobTitle(jobTitle);
    }

    /**
     * Get active employee count
     */
    @Transactional(readOnly = true)
    public long getActiveEmployeeCount() {
        return employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
    }

    // ==================== TASK MANAGEMENT ====================

    /**
     * Create a new task for an employee
     */
    public EmployeeTask createTask(Long employeeId, String title, String description) {
        // Validate employee exists and is active
        Employee employee = getEmployeeByUserId(employeeId);
        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            throw new BusinessRuleException("Cannot assign task to inactive employee");
        }

        // Check if employee is in training
        if (hasInProgressTraining(employeeId)) {
            throw new BusinessRuleException("Cannot assign task to employee currently in training");
        }

        EmployeeTask task = new EmployeeTask(employee.getUser(), title, description, TaskStatus.TODO);
        return employeeTaskRepository.save(task);
    }

    /**
     * Update task status
     */
    public EmployeeTask updateTaskStatus(Long taskId, TaskStatus status, String note) {
        EmployeeTask task = employeeTaskRepository.findById(taskId)
            .orElseThrow(() -> new NotFoundException("Task not found with ID: " + taskId));

        task.setStatus(status);
        if (note != null) {
            task.setNote(note);
        }

        return employeeTaskRepository.save(task);
    }

    /**
     * Get tasks for an employee
     */
    @Transactional(readOnly = true)
    public Page<EmployeeTask> getEmployeeTasks(Long employeeId, TaskStatus status, Pageable pageable) {
        User employee = getEmployeeByUserId(employeeId).getUser();
        
        if (status != null) {
            return employeeTaskRepository.findByEmployeeAndStatus(employee, status, pageable);
        } else {
            return employeeTaskRepository.findByEmployee(employee, pageable);
        }
    }

    /**
     * Get all tasks with status
     */
    @Transactional(readOnly = true)
    public Page<EmployeeTask> getAllTasks(TaskStatus status, Pageable pageable) {
        if (status != null) {
            return employeeTaskRepository.findByStatus(status, pageable);
        } else {
            return employeeTaskRepository.findAll(pageable);
        }
    }

    // ==================== LEAVE REQUEST MANAGEMENT ====================

    /**
     * Create a leave request
     */
    public LeaveRequest createLeaveRequest(Long employeeId, LocalDate fromDate, LocalDate toDate, String reason) {
        // Validate employee exists and is active
        Employee employee = getEmployeeByUserId(employeeId);
        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            throw new BusinessRuleException("Cannot create leave request for inactive employee");
        }

        // Validate date range
        if (fromDate.isAfter(toDate)) {
            throw new BusinessRuleException("Leave start date cannot be after end date");
        }

        if (fromDate.isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Leave start date cannot be in the past");
        }

        // Check for overlapping leave requests
        List<LeaveRequest> overlappingRequests = leaveRequestRepository
            .findOverlappingLeaveRequestsForEmployeeId(employeeId, fromDate, toDate);
        
        if (!overlappingRequests.isEmpty()) {
            throw new BusinessRuleException("Employee already has overlapping leave request");
        }

        // Check for overlapping training
        if (hasOverlappingTraining(employeeId, fromDate, toDate)) {
            throw new BusinessRuleException("Employee has overlapping training during requested leave period");
        }

        LeaveRequest leaveRequest = new LeaveRequest(employee.getUser(), fromDate, toDate, 
            LeaveRequestStatus.PENDING, reason);
        return leaveRequestRepository.save(leaveRequest);
    }

    /**
     * Approve a leave request
     */
    public LeaveRequest approveLeaveRequest(Long requestId, String approverNote) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
            .orElseThrow(() -> new NotFoundException("Leave request not found with ID: " + requestId));

        if (request.getStatus() != LeaveRequestStatus.PENDING) {
            throw new BusinessRuleException("Only pending leave requests can be approved");
        }

        request.setStatus(LeaveRequestStatus.APPROVED);
        return leaveRequestRepository.save(request);
    }

    /**
     * Reject a leave request
     */
    public LeaveRequest rejectLeaveRequest(Long requestId, String rejectionReason) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
            .orElseThrow(() -> new NotFoundException("Leave request not found with ID: " + requestId));

        if (request.getStatus() != LeaveRequestStatus.PENDING) {
            throw new BusinessRuleException("Only pending leave requests can be rejected");
        }

        request.setStatus(LeaveRequestStatus.REJECTED);
        return leaveRequestRepository.save(request);
    }

    /**
     * Get leave requests for an employee
     */
    @Transactional(readOnly = true)
    public Page<LeaveRequest> getEmployeeLeaveRequests(Long employeeId, LeaveRequestStatus status, Pageable pageable) {
        User employee = getEmployeeByUserId(employeeId).getUser();
        
        if (status != null) {
            return leaveRequestRepository.findByEmployeeAndStatus(employee, status, pageable);
        } else {
            return leaveRequestRepository.findByEmployee(employee, pageable);
        }
    }

    /**
     * Get all pending leave requests
     */
    @Transactional(readOnly = true)
    public Page<LeaveRequest> getPendingLeaveRequests(Pageable pageable) {
        return leaveRequestRepository.findByStatus(LeaveRequestStatus.PENDING, pageable);
    }

    // ==================== TRAINING MANAGEMENT ====================

    /**
     * Assign training to an employee
     */
    public EmployeeTraining assignTraining(Long employeeId, Long trainingId) {
        // Validate employee exists and is active
        Employee employee = getEmployeeByUserId(employeeId);
        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            throw new BusinessRuleException("Cannot assign training to inactive employee");
        }

        // Validate training exists
        Training training = trainingRepository.findById(trainingId)
            .orElseThrow(() -> new NotFoundException("Training not found with ID: " + trainingId));

        // Check for overlapping training
        if (hasOverlappingTraining(employeeId, training.getStartDate(), training.getEndDate())) {
            throw new BusinessRuleException("Employee already has overlapping training");
        }

        // Check if employee is already assigned to this training
        if (employeeTrainingRepository.existsByEmployeeAndTraining(employee.getUser(), training)) {
            throw new BusinessRuleException("Employee is already assigned to this training");
        }

        EmployeeTraining employeeTraining = new EmployeeTraining(employee.getUser(), training, TrainingStatus.ASSIGNED);
        return employeeTrainingRepository.save(employeeTraining);
    }

    /**
     * Get active trainings for an employee
     */
    @Transactional(readOnly = true)
    public List<EmployeeTraining> getActiveTrainings(Long employeeId) {
        User employee = getEmployeeByUserId(employeeId).getUser();
        return employeeTrainingRepository.findByEmployeeAndStatus(employee, TrainingStatus.IN_PROGRESS);
    }

    /**
     * Check if employee has any in-progress training
     */
    @Transactional(readOnly = true)
    public boolean hasInProgressTraining(Long employeeId) {
        User employee = getEmployeeByUserId(employeeId).getUser();
        return employeeTrainingRepository.existsByEmployeeAndStatus(employee, TrainingStatus.IN_PROGRESS);
    }

    /**
     * Check if employee has overlapping training in date range
     */
    @Transactional(readOnly = true)
    public boolean hasOverlappingTraining(Long employeeId, LocalDate fromDate, LocalDate toDate) {
        User employee = getEmployeeByUserId(employeeId).getUser();
        List<EmployeeTraining> trainings = employeeTrainingRepository.findByEmployee(employee);
        
        for (EmployeeTraining training : trainings) {
            if (training.getStatus() == TrainingStatus.IN_PROGRESS) {
                Training t = training.getTraining();
                if (!(t.getEndDate().isBefore(fromDate) || t.getStartDate().isAfter(toDate))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get employees in training on a specific date
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesInTraining(LocalDate date) {
        List<EmployeeTraining> inProgressTrainings = employeeTrainingRepository.findByStatus(TrainingStatus.IN_PROGRESS);
        
        return inProgressTrainings.stream()
            .filter(training -> {
                Training t = training.getTraining();
                return !date.isBefore(t.getStartDate()) && !date.isAfter(t.getEndDate());
            })
            .map(training -> employeeRepository.findByUser(training.getEmployee()).orElse(null))
            .filter(employee -> employee != null)
            .toList();
    }

    /**
     * Get training assignments for an employee
     */
    @Transactional(readOnly = true)
    public Page<EmployeeTraining> getEmployeeTrainings(Long employeeId, TrainingStatus status, Pageable pageable) {
        User employee = getEmployeeByUserId(employeeId).getUser();
        
        if (status != null) {
            return employeeTrainingRepository.findByEmployeeAndStatus(employee, status, pageable);
        } else {
            return employeeTrainingRepository.findByEmployee(employee, pageable);
        }
    }

    // ==================== AVAILABILITY CHECKS ====================

    /**
     * Check if employee is available for shift on a specific date
     */
    @Transactional(readOnly = true)
    public boolean isEmployeeAvailableForShift(Long employeeId, LocalDate date) {
        Employee employee = getEmployeeByUserId(employeeId);
        
        // Check if employee is active
        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            return false;
        }

        // Check if employee is in training
        if (hasInProgressTraining(employeeId)) {
            return false;
        }

        // Check if employee has approved leave on this date
        User employeeUser = employee.getUser();
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmployeeAndStatus(employeeUser, LeaveRequestStatus.APPROVED);
        
        for (LeaveRequest leave : leaveRequests) {
            if (!date.isBefore(leave.getFromDate()) && !date.isAfter(leave.getToDate())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if employee is available for task assignment
     */
    @Transactional(readOnly = true)
    public boolean isEmployeeAvailableForTask(Long employeeId) {
        Employee employee = getEmployeeByUserId(employeeId);
        
        // Check if employee is active
        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            return false;
        }

        // Check if employee is in training
        return !hasInProgressTraining(employeeId);
    }

    // ==================== STATISTICS AND ANALYTICS ====================

    /**
     * Get employee statistics
     */
    @Transactional(readOnly = true)
    public EmployeeStatistics getEmployeeStatistics() {
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
        long inactiveEmployees = employeeRepository.countByStatus(EmployeeStatus.INACTIVE);
        
        long pendingLeaveRequests = leaveRequestRepository.countByStatus(LeaveRequestStatus.PENDING);
        long approvedLeaveRequests = leaveRequestRepository.countByStatus(LeaveRequestStatus.APPROVED);
        
        long inProgressTrainings = employeeTrainingRepository.countByStatus(TrainingStatus.IN_PROGRESS);
        long completedTrainings = employeeTrainingRepository.countByStatus(TrainingStatus.COMPLETED);
        
        return new EmployeeStatistics(
            totalEmployees, activeEmployees, inactiveEmployees,
            pendingLeaveRequests, approvedLeaveRequests,
            inProgressTrainings, completedTrainings
        );
    }

    /**
     * Get distinct job titles
     */
    @Transactional(readOnly = true)
    public List<String> getDistinctJobTitles() {
        return employeeRepository.findDistinctJobTitles();
    }

    /**
     * Get employees created in last N days
     */
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesCreatedInLastDays(int days) {
        LocalDateTime date = LocalDateTime.now().minusDays(days);
        return employeeRepository.getEmployeesCreatedInLastDays(date);
    }
}

// Statistics DTO
class EmployeeStatistics {
    private final long totalEmployees;
    private final long activeEmployees;
    private final long inactiveEmployees;
    private final long pendingLeaveRequests;
    private final long approvedLeaveRequests;
    private final long inProgressTrainings;
    private final long completedTrainings;

    public EmployeeStatistics(long totalEmployees, long activeEmployees, long inactiveEmployees,
                            long pendingLeaveRequests, long approvedLeaveRequests,
                            long inProgressTrainings, long completedTrainings) {
        this.totalEmployees = totalEmployees;
        this.activeEmployees = activeEmployees;
        this.inactiveEmployees = inactiveEmployees;
        this.pendingLeaveRequests = pendingLeaveRequests;
        this.approvedLeaveRequests = approvedLeaveRequests;
        this.inProgressTrainings = inProgressTrainings;
        this.completedTrainings = completedTrainings;
    }

    // Getters
    public long getTotalEmployees() { return totalEmployees; }
    public long getActiveEmployees() { return activeEmployees; }
    public long getInactiveEmployees() { return inactiveEmployees; }
    public long getPendingLeaveRequests() { return pendingLeaveRequests; }
    public long getApprovedLeaveRequests() { return approvedLeaveRequests; }
    public long getInProgressTrainings() { return inProgressTrainings; }
    public long getCompletedTrainings() { return completedTrainings; }
}