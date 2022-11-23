INSERT INTO roles(id,name) VALUES(0,'ROLE_ADMIN');
INSERT INTO roles(id,name) VALUES(1,'ROLE_USER');

INSERT INTO users(id,email,password,username) VALUES (1,'admin@admin','admin','admin');
INSERT INTO users(id,email,password,username) VALUES (2,'user@user','user','user');

INSERT INTO user_roles(user_id,role_id) VALUES (1,0);
INSERT INTO user_roles(user_id,role_id) VALUES (1,1);
INSERT INTO user_roles(user_id,role_id) VALUES (2,1);
