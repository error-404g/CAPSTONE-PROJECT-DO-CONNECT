CREATE DATABASE IF NOT EXISTS email_notification_db;
USE email_notification_db;

CREATE TABLE email_notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    to_email VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message TEXT,
    type VARCHAR(50),
    sent BOOLEAN DEFAULT FALSE,
    status VARCHAR(255) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
