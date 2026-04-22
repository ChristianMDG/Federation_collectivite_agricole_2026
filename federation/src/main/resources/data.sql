create type gender_type as enum ('MALE', 'FEMALE');

create type member_occupation_type as enum ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

create table member
(
    id                    varchar primary key,
    firstname             varchar(255) not null,
    lastname              varchar(255) not null,
    birthday              date,
    gender                gender_type,
    address               varchar(255),
    profession            varchar(255),
    phone_number          int,
    email                 varchar(255) unique ,
    occupation            member_occupation_type,
    registration_fee_paid BOOLEAN,
    membership_dues_paid  BOOLEAN,
    collectivity_id       VARCHAR
);
CREATE TABLE member_referees
(
    member_id  VARCHAR REFERENCES member (id),
    referee_id VARCHAR REFERENCES member (id),
    PRIMARY KEY (member_id, referee_id)
);

create table collectivity
(
    id                varchar(255) primary key,
    location          varchar(255),
    president_id      varchar(255) references member (id),
    vice_president_id varchar(255) references member (id),
    treasurer_id      varchar(255) references member (id),
    secretary_id      varchar(255) references member (id)
);

CREATE TABLE collectivity_members
(
    collectivity_id VARCHAR(255) REFERENCES collectivity (id) ON DELETE CASCADE,
    member_id       VARCHAR(255) REFERENCES member (id) ON DELETE CASCADE,
    PRIMARY KEY (collectivity_id, member_id)
);

ALTER TABLE collectivity
    ADD COLUMN number VARCHAR(50) UNIQUE,
    ADD COLUMN name   VARCHAR(255) UNIQUE;

CREATE SEQUENCE member_id_seq START 1000;
create sequence collectivity_id_seq start 2000;

CREATE TYPE frequency_type AS ENUM (
    'MONTHLY',
    'YEARLY'
    );

CREATE TYPE activity_status_type AS ENUM (
    'ACTIVE',
    'INACTIVE'
    );

CREATE TABLE membership_fee (
                                id               VARCHAR PRIMARY KEY,
                                collectivity_id  VARCHAR NOT NULL REFERENCES collectivity(id) ON DELETE CASCADE,
                                eligible_from    DATE,
                                frequency        frequency_type,
                                amount           NUMERIC(10,2),
                                label            VARCHAR(255),
                                status           activity_status_type
);

CREATE TYPE payment_mode_type AS ENUM (
    'CASH',
    'MOBILE_BANKING',
    'BANK_TRANSFER'
);

CREATE TABLE member_payment (
                                id VARCHAR PRIMARY KEY,
                                member_id VARCHAR NOT NULL,
                                amount NUMERIC NOT NULL,
                                payment_mode payment_mode_type NOT NULL,
                                membership_fee_id VARCHAR NOT NULL,
                                account_credited_id VARCHAR NOT NULL,
                                creation_date DATE DEFAULT CURRENT_DATE
);

ALTER TABLE member_payment
    ADD CONSTRAINT fk_member_payment_member
        FOREIGN KEY (member_id) REFERENCES member(id);

CREATE TABLE collectivity_transaction (
                                          id VARCHAR PRIMARY KEY,
                                          creation_date DATE DEFAULT CURRENT_DATE,
                                          amount NUMERIC NOT NULL,
                                          payment_mode payment_mode_type NOT NULL,
                                          account_credited_id VARCHAR NOT NULL,
                                          member_id VARCHAR NOT NULL
);


CREATE SEQUENCE membership_fee_id_seq START 3000;

