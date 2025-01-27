CREATE TABLE packs (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    sender VARCHAR(255) NOT NULL,
    recipient VARCHAR(255)
    isHolliday BOOLEAN,
    fun_fact VARCHAR(255),
    estimated_delivery_date VARCHAR(255),
    status VARCHAR(50) DEFAULT 'CREATED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);