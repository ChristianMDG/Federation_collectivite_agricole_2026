create type gender_type as enum ('MALE', 'FEMALE');
create type member_occupation_type as enum ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');
CREATE TYPE frequency_type AS ENUM ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');
CREATE TYPE activity_status_type AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE payment_mode_type AS ENUM ('CASH', 'MOBILE_BANKING', 'BANK_TRANSFER');
create table IF NOT EXISTS member
(
    id                    varchar primary key,
    firstname             varchar(255) not null,
    lastname              varchar(255) not null,
    birthday              date,
    gender                gender_type,
    address               varchar(255),
    profession            varchar(255),
    phone_number          int,
    email                 varchar(255) unique,
    occupation            member_occupation_type,
    registration_fee_paid BOOLEAN,
    membership_dues_paid  BOOLEAN,
    collectivity_id       VARCHAR
);
CREATE TABLE IF NOT EXISTS member_referees
(
    member_id  VARCHAR REFERENCES member (id),
    referee_id VARCHAR REFERENCES member (id),
    PRIMARY KEY (member_id, referee_id)
);

create table IF NOT EXISTS collectivity
(
    id                varchar(255) primary key,
    location          varchar(255),
    president_id      varchar(255) references member (id),
    vice_president_id varchar(255) references member (id),
    treasurer_id      varchar(255) references member (id),
    secretary_id      varchar(255) references member (id)
);

ALTER TABLE collectivity
    ADD COLUMN number VARCHAR(50) UNIQUE,
    ADD COLUMN name   VARCHAR(255) UNIQUE;

CREATE TABLE IF NOT EXISTS collectivity_members
(
    collectivity_id VARCHAR(255) REFERENCES collectivity (id) ON DELETE CASCADE,
    member_id       VARCHAR(255) REFERENCES member (id) ON DELETE CASCADE,
    PRIMARY KEY (collectivity_id, member_id)
);

CREATE TABLE IF NOT EXISTS membership_fee
(
    id              VARCHAR(255) PRIMARY KEY,
    collectivity_id VARCHAR(255) REFERENCES collectivity (id),
    eligible_from   DATE           NOT NULL,
    frequency       frequency_type NOT NULL,
    amount          DECIMAL(15, 2) NOT NULL,
    label           VARCHAR(255),
    status          activity_status_type DEFAULT 'ACTIVE'
);
CREATE TABLE IF NOT EXISTS membership_fee
(
    id              VARCHAR PRIMARY KEY,
    collectivity_id VARCHAR NOT NULL REFERENCES collectivity (id) ON DELETE CASCADE,
    eligible_from   DATE,
    frequency       frequency_type,
    amount          NUMERIC(10, 2),
    label           VARCHAR(255),
    status          activity_status_type
);

CREATE TABLE IF NOT EXISTS collectivity_transaction
(
    id                VARCHAR(255) PRIMARY KEY,
    collectivity_id   VARCHAR(255) REFERENCES collectivity (id),
    member_id         VARCHAR(255) REFERENCES member (id),
    membership_fee_id VARCHAR(255) REFERENCES membership_fee (id),
    amount            DECIMAL(15, 2) NOT NULL,
    payment_mode      VARCHAR(50)    NOT NULL,
    transaction_date  DATE           NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS collectivity_transaction
(
    id                VARCHAR(255) PRIMARY KEY,
    collectivity_id   VARCHAR(255) REFERENCES collectivity (id),
    member_id         VARCHAR(255) REFERENCES member (id),
    membership_fee_id VARCHAR(255) REFERENCES membership_fee (id),
    amount            DECIMAL(15, 2) NOT NULL,
    payment_mode      payment_mode_type NOT NULL,
    transaction_date  DATE           NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS member_payment
(
    id                VARCHAR(255) PRIMARY KEY   DEFAULT 'mp_' || nextval('member_payment_id_seq'),
    member_id         VARCHAR(255)      NOT NULL,
    membership_fee_id VARCHAR(255),
    amount            DECIMAL(15, 2)    NOT NULL CHECK (amount > 0),
    payment_mode      payment_mode_type NOT NULL,
    payment_date      DATE              NOT NULL DEFAULT CURRENT_DATE,
    created_at        TIMESTAMP                  DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_member_payment_member FOREIGN KEY (member_id) REFERENCES member (id) ON DELETE CASCADE,
    CONSTRAINT fk_member_payment_fee FOREIGN KEY (membership_fee_id) REFERENCES membership_fee (id) ON DELETE SET NULL
);

CREATE SEQUENCE IF NOT EXISTS member_id_seq START 1000;
CREATE SEQUENCE IF NOT EXISTS collectivity_id_seq start 2000;
CREATE SEQUENCE IF NOT EXISTS membership_fee_id_seq START 3000;
CREATE SEQUENCE IF NOT EXISTS transaction_id_seq START 1000;
CREATE SEQUENCE IF NOT EXISTS member_payment_id_seq START 1000;

