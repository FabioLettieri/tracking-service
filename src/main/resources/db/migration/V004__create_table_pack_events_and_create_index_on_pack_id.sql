CREATE TABLE pack_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pack_id BIGINT NOT NULL,
    location VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    event_date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pack_id) REFERENCES packs(id) ON DELETE CASCADE
);

CREATE INDEX idx_pack_events_pack_id ON pack_events(pack_id);
