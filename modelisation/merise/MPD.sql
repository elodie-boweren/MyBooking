-- MyBooking — MPD (PostgreSQL DDL)
-- Aligné sur MCD, fonctionnalités et API

-- ENUM simulés via CHECK (pragmatisme portable). En prod, on peut créer des types ENUM.

create table app_user (
  id bigserial primary key,
  first_name varchar(50) not null,
  last_name varchar(50) not null,
  email varchar(100) not null unique,
  password varchar(255) not null,
  phone varchar(20) not null,
  address varchar(255) not null,
  birth_date date not null,
  role varchar(20) not null check (role in ('CLIENT','EMPLOYEE','ADMIN')),
  created_at timestamptz default now() not null,
  updated_at timestamptz default now() not null
);

create table notification_preference (
  user_id bigint primary key references app_user(id) on delete cascade,
  email_enabled boolean not null default true,
  sms_enabled boolean not null default false
);

create table employee (
  user_id bigint primary key references app_user(id) on delete cascade,
  status varchar(16) not null check (status in ('ACTIVE','INACTIVE')),
  job_title varchar(100)
);

create table room (
  id bigserial primary key,
  number varchar(20) not null,
  room_type varchar(32) not null check (room_type in ('SINGLE','DOUBLE','DELUXE','FAMILY')),
  capacity int not null check (capacity > 0 and capacity <= 10),
  price numeric(12,2) not null check (price > 0),
  currency char(3) not null,
  status varchar(16) not null check (status in ('AVAILABLE','OCCUPIED','OUT_OF_SERVICE')),
  description text,
  equipment varchar(255),
  created_at timestamptz default now() not null,
  updated_at timestamptz default now() not null
);
create index idx_room_status on room(status);

create table equipment (
  id serial primary key,
  name varchar(100) not null unique
);

create table room_equipment (
  room_id bigint not null references room(id) on delete cascade,
  equipment_id int not null references equipment(id) on delete restrict,
  primary key (room_id, equipment_id)
);

create table room_photo (
  id bigserial primary key,
  room_id bigint not null references room(id) on delete cascade,
  url text not null
);

create table room_status_update (
  id bigserial primary key,
  room_id bigint not null references room(id) on delete cascade,
  user_id bigint not null references app_user(id) on delete restrict,
  status varchar(16) not null check (status in ('PROPRE','SALE','HORS_SERVICE')),
  note text,
  at timestamptz not null default now()
);
create index idx_room_status_update_room on room_status_update(room_id);

create table reservation (
  id bigserial primary key,
  client_id bigint not null references app_user(id) on delete restrict,
  room_id bigint not null references room(id) on delete restrict,
  check_in date not null,
  check_out date not null,
  number_of_guests int not null check (number_of_guests > 0 and number_of_guests <= 10),
  total_price numeric(12,2) not null check (total_price > 0),
  currency char(3) not null,
  status varchar(20) not null check (status in ('CONFIRMED','CANCELLED')),
  points_used int default 0,
  points_discount numeric(12,2) default 0.00,
  created_at timestamptz default now() not null,
  updated_at timestamptz default now() not null
);
create index idx_reservation_client on reservation(client_id);
create index idx_reservation_room on reservation(room_id);
create index idx_reservation_dates on reservation(check_in, check_out);

create table feedback (
  id bigserial primary key,
  reservation_id bigint not null references reservation(id) on delete cascade,
  user_id bigint not null references app_user(id) on delete restrict,
  rating int not null check (rating between 1 and 5),
  comment text,
  created_at timestamptz not null default now()
);

create table feedback_reply (
  id bigserial primary key,
  feedback_id bigint not null references feedback(id) on delete cascade,
  admin_user_id bigint not null references app_user(id) on delete restrict,
  message text not null,
  created_at timestamptz not null default now()
);

create table loyalty_account (
  id bigserial primary key,
  user_id bigint not null unique references app_user(id) on delete cascade,
  balance int not null default 0 check (balance >= 0)
);

create table loyalty_transaction (
  id bigserial primary key,
  account_id bigint not null references loyalty_account(id) on delete cascade,
  type varchar(10) not null check (type in ('EARN','REDEEM')),
  points int not null check (points > 0),
  reservation_id bigint references reservation(id) on delete set null,
  created_at timestamptz not null default now()
);
create index idx_loyalty_tx_account on loyalty_transaction(account_id);

create table installation (
  id bigserial primary key,
  name varchar(100) not null,
  description text,
  installation_type varchar(32) not null check (installation_type in ('SPA_ROOM','CONFERENCE_ROOM','GYM','POOL','TENNIS_COURT','WEDDING_ROOM')),
  capacity int not null check (capacity > 0 and capacity <= 200),
  hourly_rate numeric(12,2) not null check (hourly_rate > 0),
  currency char(3) not null,
  equipment varchar(255),
  created_at timestamptz default now() not null,
  updated_at timestamptz default now() not null
);
create index idx_installation_type on installation(installation_type);

create table event (
  id bigserial primary key,
  name varchar(100) not null,
  description text,
  event_type varchar(32) not null check (event_type in ('SPA','CONFERENCE','YOGA_CLASS','FITNESS','WEDDING')),
  start_at timestamptz not null,
  end_at timestamptz not null,
  capacity int not null check (capacity > 0 and capacity <= 100),
  price numeric(12,2) not null check (price > 0),
  currency char(3) not null,
  installation_id bigint not null references installation(id) on delete restrict,
  created_at timestamptz default now() not null,
  updated_at timestamptz default now() not null
);
create index idx_event_time on event(start_at, end_at);
create index idx_event_type on event(event_type);
create index idx_event_installation on event(installation_id);

create table event_booking (
  id bigserial primary key,
  event_id bigint not null references event(id) on delete restrict,
  user_id bigint not null references app_user(id) on delete restrict,
  participants int not null check (participants > 0),
  status varchar(16) not null check (status in ('PENDING','CONFIRMED','CANCELLED')),
  created_at timestamptz not null default now(),
  total_price numeric(12,2) not null check (total_price >= 0),
  currency char(3) not null
);
create index idx_event_booking_event on event_booking(event_id);
create index idx_event_booking_user on event_booking(user_id);

create table announcement (
  id bigserial primary key,
  title varchar(200) not null,
  body text not null,
  created_at timestamptz not null default now()
);

create table announcement_reply (
  id bigserial primary key,
  announcement_id bigint not null references announcement(id) on delete cascade,
  employee_user_id bigint not null references employee(user_id) on delete restrict,
  message text not null,
  created_at timestamptz not null default now()
);

create table employee_task (
  id bigserial primary key,
  employee_user_id bigint not null references employee(user_id) on delete restrict,
  title varchar(200) not null,
  description text,
  status varchar(16) not null check (status in ('TODO','IN_PROGRESS','DONE')),
  note text,
  photo_url text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);
create index idx_employee_task_user on employee_task(employee_user_id);
create index idx_employee_task_status on employee_task(status);

create table shift (
  id bigserial primary key,
  employee_user_id bigint not null references employee(user_id) on delete restrict,
  start_at timestamptz not null,
  end_at timestamptz not null
);
create index idx_shift_user on shift(employee_user_id);
create index idx_shift_time on shift(start_at, end_at);

create table leave_request (
  id bigserial primary key,
  employee_user_id bigint not null references employee(user_id) on delete restrict,
  from_date date not null,
  to_date date not null,
  status varchar(16) not null check (status in ('PENDING','APPROVED','REJECTED')),
  reason text
);
create index idx_leave_user on leave_request(employee_user_id);

create table training (
  id serial primary key,
  title varchar(200) not null,
  start_date date not null,
  end_date date not null
);

create table employee_training (
  employee_user_id bigint not null references employee(user_id) on delete restrict,
  training_id int not null references training(id) on delete restrict,
  status varchar(16) not null check (status in ('ASSIGNED','IN_PROGRESS','COMPLETED')),
  assigned_at timestamptz not null default now(),
  completed_at timestamptz,
  primary key (employee_user_id, training_id)
);

create table event_notification (
  id bigserial primary key,
  event_id bigint not null references event(id) on delete cascade,
  channel varchar(16) not null check (channel in ('EMAIL','SMS')),
  subject varchar(200) not null,
  body text not null,
  created_at timestamptz not null default now(),
  created_by bigint not null references app_user(id) on delete restrict
);

-- Seed suggestions (roles)
-- insert into role(code,label) values ('CLIENT','Client'),('EMPLOYE','Employé'),('ADMIN','Administrateur');

