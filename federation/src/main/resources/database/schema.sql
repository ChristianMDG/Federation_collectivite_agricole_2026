-- =====================================================
-- 1. Supprimer les tables dans le bon ordre (si besoin)
-- =====================================================
DROP TABLE IF EXISTS collectivity_financial_account CASCADE;
DROP TABLE IF EXISTS member_payment CASCADE;
DROP TABLE IF EXISTS collectivity_transaction CASCADE;
DROP TABLE IF EXISTS bank_account CASCADE;
DROP TABLE IF EXISTS mobile_banking_account CASCADE;
DROP TABLE IF EXISTS cash_account CASCADE;
DROP TABLE IF EXISTS financial_account CASCADE;
DROP TABLE IF EXISTS membership_fee CASCADE;
DROP TABLE IF EXISTS collectivity_members CASCADE;
DROP TABLE IF EXISTS member_referees CASCADE;
DROP TABLE IF EXISTS collectivity CASCADE;
DROP TABLE IF EXISTS member CASCADE;

DROP SEQUENCE IF EXISTS member_id_seq CASCADE;
DROP SEQUENCE IF EXISTS collectivity_id_seq CASCADE;
DROP SEQUENCE IF EXISTS membership_fee_id_seq CASCADE;
DROP SEQUENCE IF EXISTS transaction_id_seq CASCADE;
DROP SEQUENCE IF EXISTS member_payment_id_seq CASCADE;
DROP SEQUENCE IF EXISTS financial_account_id_seq CASCADE;


CREATE TYPE gender_type AS ENUM ('MALE', 'FEMALE');
CREATE TYPE member_occupation_type AS ENUM ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');
CREATE TYPE frequency_type AS ENUM ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');
CREATE TYPE activity_status_type AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE payment_mode_type AS ENUM ('CASH', 'MOBILE_BANKING', 'BANK_TRANSFER');
CREATE TYPE mobile_banking_service_type AS ENUM ('AIRTEL_MONEY', 'MVOLA', 'ORANGE_MONEY');
CREATE TYPE bank_type AS ENUM ('BRED', 'MCB', 'BMOI', 'BOA', 'BGFI', 'AFG', 'ACCES_BAQUE', 'BAOBAB', 'SIPEM');


CREATE SEQUENCE member_id_seq START 1000;
CREATE SEQUENCE collectivity_id_seq START 2000;
CREATE SEQUENCE membership_fee_id_seq START 3000;
CREATE SEQUENCE transaction_id_seq START 1000;
CREATE SEQUENCE member_payment_id_seq START 1000;
CREATE SEQUENCE financial_account_id_seq START 1000;


CREATE TABLE member
(
    id                    VARCHAR PRIMARY KEY DEFAULT 'mem_' || nextval('member_id_seq'),
    firstname             VARCHAR(255) NOT NULL,
    lastname              VARCHAR(255) NOT NULL,
    birthday              DATE,
    gender                gender_type,
    address               VARCHAR(255),
    profession            VARCHAR(255),
    phone_number          INT,
    email                 VARCHAR(255) UNIQUE,
    occupation            member_occupation_type,
    registration_fee_paid BOOLEAN             DEFAULT FALSE,
    membership_dues_paid  BOOLEAN             DEFAULT FALSE,
    collectivity_id       VARCHAR
);


CREATE TABLE member_referees
(
    member_id  VARCHAR REFERENCES member (id) ON DELETE CASCADE,
    referee_id VARCHAR REFERENCES member (id) ON DELETE CASCADE,
    PRIMARY KEY (member_id, referee_id)
);


CREATE TABLE collectivity
(
    id                VARCHAR PRIMARY KEY DEFAULT 'col_' || nextval('collectivity_id_seq'),
    number            VARCHAR(50) UNIQUE,
    name              VARCHAR(255) UNIQUE,
    location          VARCHAR(255),
    president_id      VARCHAR REFERENCES member (id),
    vice_president_id VARCHAR REFERENCES member (id),
    treasurer_id      VARCHAR REFERENCES member (id),
    secretary_id      VARCHAR REFERENCES member (id)
);


CREATE TABLE collectivity_members
(
    collectivity_id VARCHAR REFERENCES collectivity (id) ON DELETE CASCADE,
    member_id       VARCHAR REFERENCES member (id) ON DELETE CASCADE,
    PRIMARY KEY (collectivity_id, member_id)
);


CREATE TABLE membership_fee
(
    id              VARCHAR PRIMARY KEY  DEFAULT 'mf_' || nextval('membership_fee_id_seq'),
    collectivity_id VARCHAR NOT NULL REFERENCES collectivity (id) ON DELETE CASCADE,
    eligible_from   DATE,
    frequency       frequency_type,
    amount          DECIMAL(15, 2),
    label           VARCHAR(255),
    status          activity_status_type DEFAULT 'ACTIVE'
);


CREATE TABLE financial_account
(
    id         VARCHAR PRIMARY KEY DEFAULT 'acc_' || nextval('financial_account_id_seq'),
    amount     DECIMAL(15, 2)      DEFAULT 0,
    created_at TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP           DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE cash_account
(
    id VARCHAR PRIMARY KEY REFERENCES financial_account (id) ON DELETE CASCADE
);


CREATE TABLE mobile_banking_account
(
    id                     VARCHAR PRIMARY KEY REFERENCES financial_account (id) ON DELETE CASCADE,
    holder_name            VARCHAR(255)                NOT NULL,
    mobile_banking_service mobile_banking_service_type NOT NULL,
    mobile_number          VARCHAR(20)                 NOT NULL
);


CREATE TABLE bank_account
(
    id                  VARCHAR PRIMARY KEY REFERENCES financial_account (id) ON DELETE CASCADE,
    holder_name         VARCHAR(255) NOT NULL,
    bank_name           bank_type    NOT NULL,
    bank_code           VARCHAR(10)  NOT NULL,
    bank_branch_code    VARCHAR(10)  NOT NULL,
    bank_account_number VARCHAR(20)  NOT NULL,
    bank_account_key    VARCHAR(10)  NOT NULL
);


CREATE TABLE collectivity_financial_account
(
    collectivity_id      VARCHAR REFERENCES collectivity (id) ON DELETE CASCADE,
    financial_account_id VARCHAR REFERENCES financial_account (id) ON DELETE CASCADE,
    PRIMARY KEY (collectivity_id, financial_account_id)
);


CREATE TABLE collectivity_transaction
(
    id                  VARCHAR PRIMARY KEY        DEFAULT 'tr_' || nextval('transaction_id_seq'),
    collectivity_id     VARCHAR REFERENCES collectivity (id),
    member_id           VARCHAR REFERENCES member (id),
    membership_fee_id   VARCHAR REFERENCES membership_fee (id),
    account_credited_id VARCHAR REFERENCES financial_account (id),
    amount              DECIMAL(15, 2)    NOT NULL,
    payment_mode        payment_mode_type NOT NULL,
    creation_date       DATE              NOT NULL DEFAULT CURRENT_DATE,
    created_at          TIMESTAMP                  DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE member_payment
(
    id                  VARCHAR PRIMARY KEY        DEFAULT 'mp_' || nextval('member_payment_id_seq'),
    member_id           VARCHAR REFERENCES member (id),
    membership_fee_id   VARCHAR REFERENCES membership_fee (id),
    account_credited_id VARCHAR REFERENCES financial_account (id),
    amount              DECIMAL(15, 2)    NOT NULL,
    payment_mode        payment_mode_type NOT NULL,
    creation_date       DATE              NOT NULL DEFAULT CURRENT_DATE,
    created_at          TIMESTAMP                  DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX idx_member_email ON member (email);
CREATE INDEX idx_member_collectivity_id ON member (collectivity_id);
CREATE INDEX idx_collectivity_members_collectivity ON collectivity_members (collectivity_id);
CREATE INDEX idx_collectivity_members_member ON collectivity_members (member_id);
CREATE INDEX idx_member_payment_member ON member_payment (member_id);
CREATE INDEX idx_member_payment_date ON member_payment (creation_date);
CREATE INDEX idx_transaction_collectivity ON collectivity_transaction (collectivity_id);
CREATE INDEX idx_transaction_date ON collectivity_transaction (creation_date);