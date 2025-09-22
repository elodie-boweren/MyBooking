-- Fix event_booking table to match EventBooking entity
-- Add missing columns and rename existing ones

-- First, drop the existing table and recreate it with the correct schema
DROP TABLE IF EXISTS event_booking CASCADE;

CREATE TABLE event_booking (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL REFERENCES event(id) ON DELETE RESTRICT,
    client_user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE RESTRICT,
    reservation_id BIGINT REFERENCES reservation(id) ON DELETE SET NULL,
    event_date_time TIMESTAMPTZ NOT NULL,
    duration_hours INTEGER NOT NULL CHECK (duration_hours > 0),
    booking_date TIMESTAMPTZ NOT NULL,
    number_of_participants INTEGER NOT NULL CHECK (number_of_participants > 0),
    total_price NUMERIC(12,2) NOT NULL CHECK (total_price >= 0),
    status VARCHAR(16) NOT NULL CHECK (status IN ('PENDING','CONFIRMED','CANCELLED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create indexes
CREATE INDEX idx_event_booking_event ON event_booking(event_id);
CREATE INDEX idx_event_booking_client ON event_booking(client_user_id);
CREATE INDEX idx_event_booking_reservation ON event_booking(reservation_id);
CREATE INDEX idx_event_booking_status ON event_booking(status);
