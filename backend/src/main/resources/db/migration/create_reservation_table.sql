CREATE TABLE reservation (
                             id BIGSERIAL PRIMARY KEY,
                             check_in DATE NOT NULL,
                             check_out DATE NOT NULL,
                             total NUMERIC(10,2) NOT NULL,
                             currency VARCHAR(3) NOT NULL,
                             used_points INT,
                             status VARCHAR(20) NOT NULL,
                             user_id BIGINT NOT NULL,
                             room_id BIGINT NOT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT now(),
                             updated_at TIMESTAMP NOT NULL DEFAULT now(),

                             CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES "user"(id),
                             CONSTRAINT fk_reservation_room FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE INDEX idx_reservation_user ON reservation(user_id);
CREATE INDEX idx_reservation_room ON reservation(room_id);
CREATE INDEX idx_reservation_dates ON reservation(check_in, check_out);