-- Habilita a extensão pgcrypto para gerar UUIDs
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Garante que o tipo ENUM 'role' exista
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'role') THEN
CREATE TYPE role AS ENUM ('ADMIN', 'USER');
END IF;
END$$;

-- Verifica e recria a tabela 'users' se necessário
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT FROM information_schema.tables
    WHERE table_schema = 'public' AND table_name = 'users'
  ) THEN
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role role NOT NULL DEFAULT 'USER',
                       active BOOLEAN DEFAULT true
);
ELSE
    -- Verifica se a coluna 'role' está com tipo incorreto
    IF EXISTS (
      SELECT 1 FROM information_schema.columns
      WHERE table_name = 'users' AND column_name = 'role' AND data_type != 'USER-DEFINED'
    ) THEN
ALTER TABLE users
ALTER COLUMN role TYPE role USING role::role;
END IF;
END IF;
END$$;

-- Inserção segura do usuário admin
INSERT INTO users (id, name, email, password, role, active)
SELECT
    gen_random_uuid(),
    'Admin User',
    'admin@example.com',
    '$2a$10$wHd7Qt4St6fd1s2A7Snb3uhnSMUvxMP5KVi3saJejWPjLhJ68q4YW', -- senha: admin123
    'ADMIN'::role,
    true
    WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE email = 'admin@example.com'
);
