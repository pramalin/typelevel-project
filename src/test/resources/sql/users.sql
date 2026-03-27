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
    'rockthejvm',
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
    'riccardorulez',
    'Riccardo',
    'Cordin',
    'Rock the JVM',
    'RECRUITER'
);