create type gender_type as enum('MALE', 'FEMALE');

create type member_occupation_type as enum('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

create table collectivity(
    id varchar(255) primary key,
    location varchar(255),
    president_id varchar(255) references member(id),
    vice_president_id varchar(255) references member(id),
    treasurer_id varchar(255) references member(id),
    secretary_id varchar(255) references member(id)
);