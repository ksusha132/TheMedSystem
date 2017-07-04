INSERT INTO role (name) VALUES ('admin');
INSERT INTO role (name) VALUES ('user');

INSERT INTO contact_type (id_contact_type, type) VALUES (1, 'skype');
INSERT INTO contact_type (id_contact_type, type) VALUES (2, 'vk');

INSERT INTO speciality (id_spec, name) VALUES (1, 'dentist');
INSERT INTO speciality (id_spec, name) VALUES (2, 'surgeon');
INSERT INTO speciality (id_spec, name) VALUES (3, 'urologist');
INSERT INTO speciality (id_spec, name) VALUES (4, 'therapist');

INSERT INTO doctor (id_doc, name, patronimic, second_name, time_from, time_to, university, id_spec) VALUES (1, 'Sergey', 'Ivanovich', 'Petrov', '09:00:00', '15:00:00', 'VMedA', 1);
INSERT INTO doctor (id_doc, name, patronimic, second_name, time_from, time_to, university, id_spec) VALUES (2, 'Dmitri', 'Srgeevich', 'Ivanov', '15:00:00', '21:00:00', 'Mechnikova', 2);
INSERT INTO doctor (id_doc, name, patronimic, second_name, time_from, time_to, university, id_spec) VALUES (3, 'Ivan', 'Alexeevich', 'Sidorov', '15:00:00', '21:00:00', 'Pavlova', 2);
INSERT INTO doctor (id_doc, name, patronimic, second_name, time_from, time_to, university, id_spec) VALUES (4, 'Roman', 'Olegovich', 'Petrov', '15:00:00', '21:00:00', 'Pedeotricheskaya', 3);




INSERT INTO user (id_user, email, gender, last_name, login, patronymic, name, address, police_num, phone_num, age, password, id_role, enabled) VALUES (1,'test@mail.ru', 2, 'Orlova', 'ksu132', 'Dmitrievna', 'Ksusha', 'Burtceva street', '139234732482348', '9117714269' ,23 , '$2a$10$UotdQ1uKsAK7gs3T5kNaYubeH7ExSnG3TA2f/0QnCpnFGPNRnyGpq', 1, 1);
INSERT INTO user (id_user, email, gender, last_name, login, patronymic, name, address, police_num, phone_num, age, password, id_role, enabled) VALUES (2,'test2@mail.ru', 1, 'Stepanov', 'pasha132', 'Dmitrievich', 'Pasha', 'Baseinaya street', '139234732482348', '9117774269', 38 ,'$2a$10$UotdQ1uKsAK7gs3T5kNaYubeH7ExSnG3TA2f/0QnCpnFGPNRnyGpq', 2 , 1);
INSERT INTO user (id_user, email, gender, last_name, login, patronymic, name, address, police_num, phone_num, age, password, id_role, enabled) VALUES (7,'test7@mail.ru', 2, 'Shestakova','shest', 'Valintinovna','Ann', 'Gorohovaya street', '139234732482348', '9118884269' ,23 ,'$2a$10$MgGvoPBfYbV.IXNY6IHYrekEmTvzfNQaEaS3EBDfdsqaujSbB1X5u', 1, 1);


INSERT INTO contact_info (id_contact_info, id_user, value, id_contact_type) VALUES (1,1,'user132',1);
INSERT INTO contact_info (id_contact_info, id_user, value, id_contact_type) VALUES (2,1,'user1321',2);

