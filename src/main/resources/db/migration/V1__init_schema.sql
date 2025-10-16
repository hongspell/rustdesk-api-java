-- V1__init_schema.sql
-- Initial database schema for RustDesk API
-- Compatible with SQLite
-- All entities that extend BaseEntity have: id (PRIMARY KEY), created_at, updated_at
-- Index names are prefixed with table abbreviations to avoid conflicts

-- Create groups table
CREATE TABLE groups (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    type INTEGER NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_groups_name ON groups(name);
CREATE INDEX idx_groups_type ON groups(type);

-- Create users table
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    avatar VARCHAR(500),
    group_id INTEGER,
    is_admin INTEGER NOT NULL DEFAULT 0,
    status INTEGER NOT NULL DEFAULT 1,
    remark VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_group_id ON users(group_id);

-- Create peers table
CREATE TABLE peers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    row_id INTEGER,
    device_id VARCHAR(100) NOT NULL UNIQUE,
    cpu VARCHAR(200),
    hostname VARCHAR(200),
    memory VARCHAR(100),
    os VARCHAR(100),
    username VARCHAR(100),
    uuid VARCHAR(100),
    version VARCHAR(50),
    user_id INTEGER,
    last_online_time DATETIME,
    last_online_ip VARCHAR(50),
    group_id INTEGER,
    alias VARCHAR(200),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_peers_device_id ON peers(device_id);
CREATE INDEX idx_peers_user_id ON peers(user_id);
CREATE INDEX idx_peers_group_id ON peers(group_id);
CREATE INDEX idx_peers_uuid ON peers(uuid);

-- Create address_book_collection table
CREATE TABLE address_book_collection (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_abc_user_id ON address_book_collection(user_id);
CREATE INDEX idx_abc_name ON address_book_collection(name);
CREATE INDEX idx_abc_created_at ON address_book_collection(created_at);

-- Create address_books table
CREATE TABLE address_books (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    row_id INTEGER,
    device_id VARCHAR(100) NOT NULL,
    user_id INTEGER,
    username VARCHAR(100),
    password VARCHAR(255),
    hostname VARCHAR(200),
    alias VARCHAR(200),
    platform VARCHAR(50),
    tags TEXT,
    hash VARCHAR(255),
    collection_id INTEGER,
    force_always_relay INTEGER DEFAULT 0,
    rdp_port INTEGER,
    rdp_username VARCHAR(100),
    online INTEGER DEFAULT 0,
    login_name VARCHAR(100),
    same_server INTEGER DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ab_device_id ON address_books(device_id);
CREATE INDEX idx_ab_user_id ON address_books(user_id);
CREATE INDEX idx_ab_collection_id ON address_books(collection_id);
CREATE INDEX idx_ab_hash ON address_books(hash);

-- Create tags table
CREATE TABLE tags (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    user_id INTEGER,
    color VARCHAR(20),
    collection_id INTEGER,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tags_user_id ON tags(user_id);
CREATE INDEX idx_tags_collection_id ON tags(collection_id);
CREATE INDEX idx_tags_user_collection ON tags(user_id, collection_id);

-- Create user_tokens table
CREATE TABLE user_tokens (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    device_uuid VARCHAR(100),
    device_id VARCHAR(100),
    token VARCHAR(500) NOT NULL UNIQUE,
    expired_at DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ut_user_id ON user_tokens(user_id);
CREATE UNIQUE INDEX idx_ut_token ON user_tokens(token);
CREATE INDEX idx_ut_device_uuid ON user_tokens(device_uuid);
CREATE INDEX idx_ut_expired_at ON user_tokens(expired_at);

-- Create oauth table
CREATE TABLE oauth (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    op VARCHAR(100) NOT NULL,
    oauth_type VARCHAR(50) NOT NULL,
    client_id VARCHAR(500) NOT NULL,
    client_secret VARCHAR(500),
    auto_register INTEGER NOT NULL DEFAULT 0,
    scopes TEXT,
    issuer VARCHAR(500),
    pkce_enable INTEGER NOT NULL DEFAULT 0,
    pkce_method VARCHAR(10),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_oauth_op ON oauth(op);
CREATE INDEX idx_oauth_oauth_type ON oauth(oauth_type);

-- Create user_third table
CREATE TABLE user_third (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    open_id VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    username VARCHAR(255),
    email VARCHAR(255),
    verified_email INTEGER DEFAULT 0,
    picture VARCHAR(1000),
    union_id VARCHAR(255),
    oauth_type VARCHAR(50) NOT NULL,
    op VARCHAR(100) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_uth_user_id ON user_third(user_id);
CREATE INDEX idx_uth_open_id ON user_third(open_id);
CREATE INDEX idx_uth_op ON user_third(op);
CREATE INDEX idx_uth_oauth_type ON user_third(oauth_type);

-- Create login_log table
CREATE TABLE login_log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    client VARCHAR(50) NOT NULL,
    device_id VARCHAR(255),
    uuid VARCHAR(255),
    ip VARCHAR(100),
    type VARCHAR(50) NOT NULL,
    platform VARCHAR(255),
    user_token_id INTEGER,
    is_deleted INTEGER NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ll_user_id ON login_log(user_id);
CREATE INDEX idx_ll_client ON login_log(client);
CREATE INDEX idx_ll_device_id ON login_log(device_id);
CREATE INDEX idx_ll_uuid ON login_log(uuid);
CREATE INDEX idx_ll_type ON login_log(type);
CREATE INDEX idx_ll_is_deleted ON login_log(is_deleted);
CREATE INDEX idx_ll_created_at ON login_log(created_at);

-- Create audit_conn table
CREATE TABLE audit_conn (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    action VARCHAR(50) NOT NULL,
    conn_id VARCHAR(255) NOT NULL,
    peer_id VARCHAR(255) NOT NULL,
    from_peer VARCHAR(255),
    from_name VARCHAR(255),
    ip VARCHAR(100),
    session_id VARCHAR(255),
    type VARCHAR(100),
    uuid VARCHAR(255),
    close_time DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ac_conn_id ON audit_conn(conn_id);
CREATE INDEX idx_ac_peer_id ON audit_conn(peer_id);
CREATE INDEX idx_ac_from_peer ON audit_conn(from_peer);
CREATE INDEX idx_ac_action ON audit_conn(action);
CREATE INDEX idx_ac_session_id ON audit_conn(session_id);
CREATE INDEX idx_ac_created_at ON audit_conn(created_at);

-- Create audit_file table
CREATE TABLE audit_file (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    from_peer VARCHAR(255) NOT NULL,
    info TEXT,
    is_file INTEGER NOT NULL DEFAULT 1,
    path TEXT,
    peer_id VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    uuid VARCHAR(255),
    ip VARCHAR(100),
    num INTEGER,
    from_name VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_af_from_peer ON audit_file(from_peer);
CREATE INDEX idx_af_peer_id ON audit_file(peer_id);
CREATE INDEX idx_af_uuid ON audit_file(uuid);
CREATE INDEX idx_af_is_file ON audit_file(is_file);
CREATE INDEX idx_af_created_at ON audit_file(created_at);

-- Create share_record table
CREATE TABLE share_record (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    peer_id VARCHAR(255) NOT NULL,
    share_token VARCHAR(255) NOT NULL UNIQUE,
    password_type INTEGER NOT NULL DEFAULT 0,
    password VARCHAR(500),
    expire DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_sr_user_id ON share_record(user_id);
CREATE INDEX idx_sr_peer_id ON share_record(peer_id);
CREATE UNIQUE INDEX idx_sr_share_token ON share_record(share_token);
CREATE INDEX idx_sr_expire ON share_record(expire);
CREATE INDEX idx_sr_created_at ON share_record(created_at);

-- Create address_book_collection_rule table
CREATE TABLE address_book_collection_rule (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    collection_id INTEGER NOT NULL,
    rule INTEGER NOT NULL,
    type INTEGER NOT NULL,
    to_id INTEGER NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_abcr_user_id ON address_book_collection_rule(user_id);
CREATE INDEX idx_abcr_collection_id ON address_book_collection_rule(collection_id);
CREATE INDEX idx_abcr_to_id ON address_book_collection_rule(to_id);
CREATE INDEX idx_abcr_type ON address_book_collection_rule(type);
CREATE INDEX idx_abcr_rule ON address_book_collection_rule(rule);

-- Create server_cmd table
CREATE TABLE server_cmd (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cmd VARCHAR(255) NOT NULL,
    alias VARCHAR(255),
    option TEXT,
    explain TEXT,
    target INTEGER NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_sc_cmd ON server_cmd(cmd);
CREATE INDEX idx_sc_target ON server_cmd(target);
CREATE INDEX idx_sc_alias ON server_cmd(alias);
