CREATE TABLE IF NOT EXISTS  tweets (
    id bigint not null primary key generated always as identity,
    author_id bigint not null references users(id),
    body character varying(280) not null,
    created_at timestamp not null default now(),
    updated_at timestamp default now(),
    deleted_at timestamp
)