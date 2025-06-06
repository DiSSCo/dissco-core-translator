create type translator_type as enum ('biocase', 'dwca');

create table source_system
(
    id text not null
        primary key,
    version integer default 1 not null,
    name text not null,
    endpoint text not null,
    created timestamp with time zone not null,
    modified timestamp with time zone not null,
    tombstoned timestamp with time zone,
    mapping_id text not null,
    creator text not null,
    translator_type translator_type not null,
    data jsonb not null,
    dwc_dp_link text,
    dwca_link text,
    eml bytea
);

create table data_mapping
(
    id                   text                     not null,
    version              integer                  not null,
    name                 text                     not null,
    created              timestamp with time zone not null,
    modified             timestamp with time zone not null,
    date_tombstoned      timestamp with time zone,
    creator              text                     not null,
    mapping_data_standard varchar                  not null,
    data                 jsonb                    not null,
    constraint data_mapping_pk
        primary key (id, version)
);

create type error_code as enum ('TIMEOUT', 'DISSCO_EXCEPTION');

create type job_state as enum ('SCHEDULED', 'RUNNING', 'FAILED', 'COMPLETED');

create table translator_job_record
(
    job_id            uuid                     not null,
    job_state         job_state                not null,
    source_system_id  text                     not null,
    time_started      timestamp with time zone not null,
    time_completed    timestamp with time zone,
    processed_records int,
    error             error_code,
    PRIMARY KEY (job_id, source_system_id),
    FOREIGN KEY (source_system_id) REFERENCES source_system (id)
);