-- Fix notification_preference table to match entity
-- Drop existing table and recreate with proper structure

DROP TABLE IF EXISTS notification_preference CASCADE;

CREATE TABLE notification_preference (
    id bigserial primary key,
    user_id bigint not null references app_user(id) on delete cascade,
    notification_type varchar(50) not null,
    email_enabled boolean not null default true,
    sms_enabled boolean not null default false,
    push_enabled boolean not null default true,
    is_active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-- Create index for better performance
CREATE INDEX idx_notification_preference_user_id ON notification_preference(user_id);
CREATE INDEX idx_notification_preference_type ON notification_preference(notification_type);
