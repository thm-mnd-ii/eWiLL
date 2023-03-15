INSERT INTO roles(id,name) VALUES(0,'ROLE_ADMIN');
INSERT INTO roles(id,name) VALUES(1,'ROLE_USER');

INSERT INTO users(id,email,password,username) VALUES (1,'admin@admin','admin','admin');
INSERT INTO users(id,email,password,username) VALUES (2,'user@user','user','user');

INSERT INTO user_roles(user_id,role_id) VALUES (1,0);
INSERT INTO user_roles(user_id,role_id) VALUES (1,1);
INSERT INTO user_roles(user_id,role_id) VALUES (2,1);

Insert into semester(id, name, start_date, end_date) VALUES (0, 'SS23', '2023-04-01', '2023-09-30');

INSERT INTO course (id, active, creation_date, description,  name,  key_password, location, owner_id,semester_id) VALUES (0, true, '2023-04-01', 'Test Kurs 1', null,  '1234', 'Gießen', 1,0);
INSERT INTO course_user_role(id, course_id, role,user_id) VALUES (0,0,'STUDENT',1);
INSERT INTO course_user_role(id, course_id, role,user_id) VALUES (1,0,'STUDENT',2);




INSERT INTO public.diagram_config (id, diagram_type) VALUES (0, 'serm');
INSERT INTO ruleset(id,name,course_id,rules) VALUES (0,'SERM Ruleset?!',0,'[0,1,2]');
INSERT INTO diagram(id,category_id,config_id,connections,entities,name,owner_id) VALUES (0,0,0,'{}','{}','StandardLösungsmodell 1',1);
INSERT INTO task(id, course_id, description, due_date, name, ruleset_id, solution_model_id,  mediatype) VALUES (0, 0, 'Test Aufgabe 1 Beschreibung Lorem Impsum undso blablablabla blup :)', '2023-09-30', 'Test Aufgabe 1', 0, 0, 'serm');

