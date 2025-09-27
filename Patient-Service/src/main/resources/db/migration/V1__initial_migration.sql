create table patients
(
    id              uuid      primary key default gen_random_uuid(),
    name            varchar(100) not null,
    email           varchar(255) not null unique,
    address         varchar(255) not null,
    date_of_birth   date not null,
    registered_date date default current_date not null
);
