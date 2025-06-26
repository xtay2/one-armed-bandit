create type transaction_type as enum ('DEPOSIT', 'WITHDRAWAL', 'PLAY');

create table transactions
(
    id               bigint generated always as identity,
    timestamp        timestamp        not null,
    balance          integer          not null,
    diff             integer          not null,
    transaction_type transaction_type not null,

    primary key (id)
);