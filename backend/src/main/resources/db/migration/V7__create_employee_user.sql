-- Create employee user with specified credentials
-- Password: Pass123@ (BCrypt hashed)

INSERT INTO app_user (first_name, last_name, email, password, phone, address, birth_date, role, created_at, updated_at)
VALUES (
    'Employee',
    'User',
    'employee@example.com',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', -- Pass123@
    '+1 (555) 000-0001',
    '123 Employee Street, Employee City, EC 12346',
    '1990-01-01',
    'EMPLOYEE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Create employee record
INSERT INTO employee (user_id, status, job_title, created_at, updated_at)
SELECT id, 'ACTIVE', 'General Employee', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM app_user 
WHERE email = 'employee@example.com';

-- Verify the user was created
SELECT u.id, u.first_name, u.last_name, u.email, u.role, e.status, e.job_title
FROM app_user u
LEFT JOIN employee e ON u.id = e.user_id
WHERE u.email = 'employee@example.com';
