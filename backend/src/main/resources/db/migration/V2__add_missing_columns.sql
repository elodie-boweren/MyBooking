-- Add missing columns to existing tables
-- This migration adds audit columns and fixes schema mismatches

-- Add missing columns to employee table
ALTER TABLE employee ADD COLUMN IF NOT EXISTS created_at timestamptz NOT NULL DEFAULT now();
ALTER TABLE employee ADD COLUMN IF NOT EXISTS updated_at timestamptz NOT NULL DEFAULT now();

-- Update existing records to have proper timestamps
UPDATE employee SET created_at = now(), updated_at = now() WHERE created_at IS NULL OR updated_at IS NULL;
