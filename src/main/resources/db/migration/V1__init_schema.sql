-- Habilita a extensão pgcrypto (para gen_random_uuid), se não existir
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Criação segura do tipo ENUM 'role'
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'role') THEN
    CREATE TYPE role AS ENUM ('ADMIN', 'USER');
  END IF;
END
$$;

-- Criação segura da tabela 'users'
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT FROM information_schema.tables
    WHERE table_name = 'users'
  ) THEN
    CREATE TABLE users (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      name VARCHAR(255) NOT NULL,
      email VARCHAR(255) NOT NULL UNIQUE,
      password VARCHAR(255) NOT NULL,
      role role NOT NULL DEFAULT 'USER',
      active BOOLEAN DEFAULT true
    );
  END IF;
END
$$;

-- Inserção segura do usuário admin (evita duplicação)
INSERT INTO users (id, name, email, password, role, active)
SELECT
  gen_random_uuid(),
  'Admin User',
  'admin@example.com',
  '$2a$10$wHd7Qt4St6fd1s2A7Snb3uhnSMUvxMP5KVi3saJejWPjLhJ68q4YW', -- senha: admin123
  'ADMIN',
  true
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE email = 'admin@example.com'
);