create type gender_type as enum('MALE', 'FEMALE');

create type member_occupation_type as enum('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

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
    email                 varchar(255),
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
