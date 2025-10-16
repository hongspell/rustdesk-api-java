-- V2__init_data.sql
-- Initial data for RustDesk API

-- Insert default groups
-- Group type: 1 = Default Group, 2 = Shared Group
INSERT INTO groups (id, name, type, created_at, updated_at) VALUES
(1, '默认群组', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '共享群组', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert default admin user
-- Username: admin
-- Password: admin123 (BCrypt hash: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH)
-- Note: This is a default password and should be changed immediately after first login
-- is_admin: 1 = true (admin), 0 = false (regular user)
INSERT INTO users (username, email, password, nickname, is_admin, status, group_id, created_at, updated_at) VALUES
('admin', 'admin@rustdesk.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Administrator', 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
