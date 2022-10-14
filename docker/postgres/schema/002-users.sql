CREATE TABLE IF NOT EXISTS users (
    "id" bigint NOT NULL PRIMARY KEY generated always as identity,
    "name" VARCHAR(255)
);