CREATE TABLE users (
  id UUID PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  email VARCHAR(200) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  created TIMESTAMP NOT NULL,
  modified TIMESTAMP NOT NULL,
  last_login TIMESTAMP NOT NULL,
  token VARCHAR(1024) NOT NULL,
  is_active BOOLEAN NOT NULL
);

CREATE TABLE phones (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  number VARCHAR(50) NOT NULL,
  citycode VARCHAR(10) NOT NULL,
  countrycode VARCHAR(10) NOT NULL,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_phones_user_id ON phones(user_id);
