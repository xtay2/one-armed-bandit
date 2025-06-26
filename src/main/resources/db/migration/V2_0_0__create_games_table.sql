create type reel as enum ('APPLE', 'BANANA', 'CLEMENTINE');

create table games
(
    id           bigint generated always as identity,
    timestamp    timestamp not null,
    stake        integer   not null,
    reward       integer   not null,
    winning_reel reel,

    primary key (id)
);