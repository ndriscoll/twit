CREATE TABLE IF NOT EXISTS  subscriptions (
    user_id bigint not null references users(id),
    author_id bigint not null references users(id)
)