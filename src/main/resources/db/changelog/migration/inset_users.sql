--liquibase formatted sql

--changeset artem:2
--comment: 321

INSERT INTO public.user_info VALUES(1, '123', 'Иванов', 'Иван', 'Иванович', '12.12.2000', '1234 567891', 'Рязань', '71234567895', null, null, null);
INSERT INTO public.user_info VALUES(2, '234', 'Петров', 'Иван', 'Иванович', '11.12.2000', '3254 567891', 'Казань', '71534567145', null, null, null);
INSERT INTO public.user_info VALUES(3, '345', 'Сидоров', 'Иван', 'Иванович', '10.12.2000', '9856 567891', 'Омск', '71834567523', null, null, null);
INSERT INTO public.user_info VALUES(4, '345', 'Иванов', 'Петр', 'Иванович', '10.12.2000', '9856 567891', 'Омск', '71834567523', null, null, null);