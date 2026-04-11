create table users (
    email text not null,
    hashedPassword text not null,
    firstName text,
    lastName text,
    company text,
    role text not null
);

alter table users
add constraint pk_users primary key (email);

insert into users (
    email,
    hashedPassword,
    firstName,
    lastName,
    company,
    role
) values (
    'mailer@mailinator.com',
    '$2a$10$7XxTL6cI5UDRXhjNnrLuY.3fZQggtaa6wv2K/TyG2fE6dINoKjMNa',
    'Mailer',
    'Man',
    'Rock the JVM',
    'ADMIN'
);

insert into users (
    email,
    hashedPassword,
    firstName,
    lastName,
    company,
    role
) values (
    'riccardo@rockthejvm.com',
    '$2a$10$cFOIo3E1e8Le7OfJ5WwLNOr/XKtMjUyuI.dAGtJMA2AEA9Q1ws6zu',
    'Riccardo',
    'Cordin',
    'Rock the JVM',
    'RECRUITER'
);