-- Manual script to create admin user
-- Run this directly in your PostgreSQL database

-- First, check if user already exists
SELECT id, email, role FROM app_user WHERE email = 'admin@example.com';

-- If no user exists, insert the admin user
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
)
ON CONFLICT (email) DO UPDATE SET
    first_name = EXCLUDED.first_name,
    last_name = EXCLUDED.last_name,
    password = EXCLUDED.password,
    phone = EXCLUDED.phone,
    address = EXCLUDED.address,
    birth_date = EXCLUDED.birth_date,
    role = EXCLUDED.role,
    updated_at = CURRENT_TIMESTAMP;

-- Verify the user was created/updated
SELECT id, first_name, last_name, email, role, created_at, updated_at 
FROM app_user 
WHERE email = 'admin@example.com';
