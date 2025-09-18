-- Fix room_status_update table to match the entity
-- Drop the existing table and recreate it with the correct schema

DROP TABLE IF EXISTS room_status_update CASCADE;

CREATE TABLE room_status_update (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL REFERENCES room(id) ON DELETE CASCADE,
    previous_status VARCHAR(16) NOT NULL CHECK (previous_status IN ('AVAILABLE','OCCUPIED','OUT_OF_SERVICE')),
    new_status VARCHAR(16) NOT NULL CHECK (new_status IN ('AVAILABLE','OCCUPIED','OUT_OF_SERVICE')),
    updated_by_user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE RESTRICT,
    notes VARCHAR(500),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    update_reason VARCHAR(100),
    is_automatic BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_room_status_update_room ON room_status_update(room_id);
CREATE INDEX idx_room_status_update_user ON room_status_update(updated_by_user_id);
CREATE INDEX idx_room_status_update_date ON room_status_update(updated_at);
