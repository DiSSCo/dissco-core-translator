CREATE TABLE public.mapping
(
    id          text        NOT NULL,
    "version"   int4        NOT NULL,
    "name"      text        NOT NULL,
    description text        NULL,
    "mapping"   jsonb       NOT NULL,
    created     timestamptz NOT NULL,
    creator     text        NOT NULL,
    deleted     timestamptz NULL,
    CONSTRAINT new_mapping_pk PRIMARY KEY (id, version)
);

CREATE TABLE public.source_system
(
    id          text        NOT NULL,
    "name"      text        NOT NULL,
    endpoint    text        NOT NULL,
    description text        NULL,
    created     timestamptz NOT NULL,
    deleted     timestamptz NULL,
    mapping_id  text        NOT NULL,
    CONSTRAINT new_source_system_endpoint_key UNIQUE (endpoint),
    CONSTRAINT new_source_system_pkey PRIMARY KEY (id)
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