-- Habilita a extensão pgcrypto para UUID
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Garante que o tipo ENUM 'role' existe
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'role') THEN
CREATE TYPE role AS ENUM ('ADMIN', 'USER');
END IF;
END
$$;

-- Garante que a coluna 'role' da tabela 'users' está com o tipo correto
DO $$
BEGIN
  IF EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_name = 'users'
      AND column_name = 'role'
      AND udt_name <> 'role'
  ) THEN
ALTER TABLE users
ALTER COLUMN role TYPE role USING role::role;
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
