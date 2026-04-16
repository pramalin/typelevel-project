create table recoverytokens (
    email text not null,
    token text not null,
    expiration bigint not null
);

alter table recoverytokens
add constraint pk_recoverytokens primary key (email);