create table if not exists posts
(
    id         bigserial
        primary key,
    title      varchar not null,
    content    varchar,
    created_at date    not null,
    file_path varchar
);
