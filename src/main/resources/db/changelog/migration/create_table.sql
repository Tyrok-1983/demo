--liquibase formatted sql

--changeset artem:1
--comment: 123
create table if not exists public.user_info (id bigserial primary key not null,
                                        bank_id varchar(50),
                                        first_name varchar(50),
                                        middle_name varchar(50),
                                        last_name varchar(50),
                                        birth_date varchar(50),
                                        pas_number varchar(50) not null,
                                        birth_place varchar(50),
                                        phone_number varchar(50) not null,
                                        email varchar(50),
                                        registration_address varchar(50),
                                        residential_address varchar(50));
