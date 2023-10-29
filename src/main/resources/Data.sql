DROP TABLE funkos CASCADE;
CREATE TABLE IF NOT EXISTS funkos (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     cod UUID DEFAULT RANDOM_UUID() NOT NULL,
                                     nombre VARCHAR(255),
                                     modelo VARCHAR(6) CHECK (modelo IN ('MARVEL', 'DISNEY', 'ANIME', 'OTROS')),
                                     precio DECIMAL(10, 2),
                                     fecha_lanzamiento DATE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);