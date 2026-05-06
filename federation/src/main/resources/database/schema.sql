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
DROP TABLE IF EXISTS activity CASCADE ;

DROP SEQUENCE IF EXISTS member_id_seq CASCADE;
DROP SEQUENCE IF EXISTS collectivity_id_seq CASCADE;
DROP SEQUENCE IF EXISTS membership_fee_id_seq CASCADE;
DROP SEQUENCE IF EXISTS transaction_id_seq CASCADE;
DROP SEQUENCE IF EXISTS member_payment_id_seq CASCADE;
DROP SEQUENCE IF EXISTS financial_account_id_seq CASCADE;
DROP SEQUENCE IF EXISTS activity_id_seq cascade ;

DROP TYPE gender_type;
DROP TYPE member_occupation_type;
DROP TYPE frequency_type;
DROP TYPE activity_status_type;
DROP TYPE payment_mode_type;
DROP TYPE mobile_banking_service_type;
DROP TYPE bank_type;


CREATE TYPE gender_type AS ENUM ('MALE', 'FEMALE');
CREATE TYPE member_occupation_type AS ENUM ('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');
CREATE TYPE frequency_type AS ENUM ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');
CREATE TYPE activity_status_type AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE payment_mode_type AS ENUM ('CASH', 'MOBILE_BANKING', 'BANK_TRANSFER');
CREATE TYPE mobile_banking_service_type AS ENUM ('AIRTEL_MONEY', 'MVOLA', 'ORANGE_MONEY');
CREATE TYPE bank_type AS ENUM ('BRED', 'MCB', 'BMOI', 'BOA', 'BGFI', 'AFG', 'ACCES_BAQUE', 'BAOBAB', 'SIPEM');


CREATE SEQUENCE member_id_seq START 1000;
CREATE SEQUENCE collectivity_id_seq START 1000;
CREATE SEQUENCE membership_fee_id_seq START 3000;
CREATE SEQUENCE transaction_id_seq START 1000;
CREATE SEQUENCE member_payment_id_seq START 1000;
CREATE SEQUENCE financial_account_id_seq START 1000;
CREATE SEQUENCE IF NOT EXISTS activity_id_seq START 1000;



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
    email                 VARCHAR(255),
    registration_date     date                default current_date,
    occupation            member_occupation_type,
    registration_fee_paid BOOLEAN             DEFAULT FALSE,
    membership_dues_paid  BOOLEAN             DEFAULT FALSE,
    collectivity_id       VARCHAR
);

ALTER TABLE member
    ALTER COLUMN registration_date SET DEFAULT CURRENT_DATE;

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
    speciality        VARCHAR(255),
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



CREATE TABLE activity
(
    id                          VARCHAR PRIMARY KEY DEFAULT 'act_' || nextval('activity_id_seq'),
    collectivity_id             VARCHAR REFERENCES collectivity (id) ON DELETE CASCADE,
    label                       VARCHAR(255) NOT NULL,
    activity_type               VARCHAR(50)  NOT NULL CHECK (activity_type IN ('MEETING', 'TRAINING', 'OTHER')),
    member_occupation_concerned VARCHAR(255),
    recurrence_week_ordinal     INT CHECK (recurrence_week_ordinal BETWEEN 1 AND 5),
    recurrence_day_of_week      VARCHAR(2) CHECK (recurrence_day_of_week IN ('MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU')),
    executive_date              DATE,
    created_at                  TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_recurrence_or_date CHECK (
        (recurrence_week_ordinal IS NOT NULL AND recurrence_day_of_week IS NOT NULL AND executive_date IS NULL) OR
        (recurrence_week_ordinal IS NULL AND recurrence_day_of_week IS NULL AND executive_date IS NOT NULL)
        )
);

-- Pour la table présence
DROP TABLE IF EXISTS member_attendance CASCADE;
DROP SEQUENCE IF EXISTS attendance_id_seq CASCADE;

CREATE SEQUENCE attendance_id_seq START 1000;

CREATE TABLE member_attendance (
                                   id                VARCHAR PRIMARY KEY DEFAULT 'att_' || nextval('attendance_id_seq'),
                                   activity_id       VARCHAR NOT NULL REFERENCES activity(id) ON DELETE CASCADE,
                                   member_id         VARCHAR NOT NULL REFERENCES member(id) ON DELETE CASCADE,
                                   attendance_status VARCHAR(10) NOT NULL CHECK (attendance_status IN ('ATTENDED', 'MISSING')),
                                   created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT uk_activity_member UNIQUE (activity_id, member_id)
);



-- Index
CREATE INDEX idx_activity_collectivity ON activity (collectivity_id);
CREATE INDEX idx_member_email ON member (email);
CREATE INDEX idx_member_collectivity_id ON member (collectivity_id);
CREATE INDEX idx_collectivity_members_collectivity ON collectivity_members (collectivity_id);
CREATE INDEX idx_collectivity_members_member ON collectivity_members (member_id);
CREATE INDEX idx_member_payment_member ON member_payment (member_id);
CREATE INDEX idx_member_payment_date ON member_payment (creation_date);
CREATE INDEX idx_transaction_collectivity ON collectivity_transaction (collectivity_id);
CREATE INDEX idx_transaction_date ON collectivity_transaction (creation_date);
CREATE INDEX idx_activity_collectivity ON activity (collectivity_id);
CREATE INDEX idx_attendance_activity ON member_attendance(activity_id);
CREATE INDEX idx_attendance_member ON member_attendance(member_id);


UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-M1';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-M2';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-M3';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-M4';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-M5';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-M6';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-M7';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-M8';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-NEW-1';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-NEW-2';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-NEW-3';
UPDATE member SET collectivity_id = 'col-1' WHERE id = 'C1-NEW-4';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C2-NEW-1';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C2-NEW-2';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C2-NEW-3';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C1-M1';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C1-M2';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C1-M3';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C1-M4';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C1-M5';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C1-M6';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C1-M7';
UPDATE member SET collectivity_id = 'col-2' WHERE id = 'C1-M8';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-M1';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-M2';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-M3';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-M4';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-M5';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-M6';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-M7';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-M8';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-NEW-1';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-NEW-2';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-NEW-3';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-NEW-4';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-NEW-5';
UPDATE member SET collectivity_id = 'col-3' WHERE id = 'C3-NEW-6';
