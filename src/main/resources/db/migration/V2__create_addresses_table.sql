-- Criação da tabela addresses

CREATE TABLE addresses (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   street VARCHAR(255) NOT NULL,
   number VARCHAR(255) NOT NULL,
   complement VARCHAR(255),
   neighborhood VARCHAR(255) NOT NULL,
   city VARCHAR(255) NOT NULL,
   state CHAR(2) NOT NULL,
   postal_code VARCHAR(10) NOT NULL,
   user_id UUID NOT NULL,

   CONSTRAINT fk_user_address FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
