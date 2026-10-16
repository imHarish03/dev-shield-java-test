CREATE TABLE IF NOT EXISTS accounts (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner   VARCHAR(100) NOT NULL,
    balance BIGINT NOT NULL DEFAULT 0
);

MERGE INTO accounts (id, owner, balance) KEY (id) VALUES (1, 'alice', 1200), (2, 'bob', 800);
