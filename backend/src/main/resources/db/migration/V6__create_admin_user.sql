-- Create admin user with specified credentials
-- Password: Pass123@ (BCrypt hashed)

INSERT INTO app_user (first_name, last_name, email, password, phone, address, birth_date, role, created_at, updated_at)
VALUES (
    'Admin',
    'User',
    'admin@example.com',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', -- Pass123@
    '+1 (555) 000-0000',
    '123 Admin Street, Admin City, AC 12345',
    '1990-01-01',
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Verify the user was created
SELECT id, first_name, last_name, email, role, created_at 
FROM app_user 
WHERE email = 'admin@example.com';
