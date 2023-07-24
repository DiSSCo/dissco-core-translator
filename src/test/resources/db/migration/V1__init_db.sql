create table mapping
(
    id text not null,
    version integer not null,
    name text not null,
    description text,
    mapping jsonb not null,
    created timestamp with time zone not null,
    creator text not null,
    deleted timestamp with time zone,
    sourcedatastandard varchar not null,
    constraint new_mapping_pk
        primary key (id, version)
);

create table source_system
(
    id text not null
        constraint new_source_system_pkey
            primary key,
    name text not null,
    endpoint text not null,
    description text,
    created timestamp with time zone not null,
    deleted timestamp with time zone,
    mapping_id text not null,
    version integer default 1 not null,
    creator text default '0000-0002-5669-2769'::text not null
);