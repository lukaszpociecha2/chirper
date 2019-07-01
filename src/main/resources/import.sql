INSERT INTO user_role (name, description) VALUES ("ROLE_USER", "basic user")
INSERT INTO user_role (name, description) VALUES ("ROLE_ADMIN", "privilidged")


INSERT INTO user (first_name, last_name, email, password) VALUES ('John', 'Doe', 'john@doe.com', '$2a$10$3ieDluhWuy30gLF5G8Nm2uagrNIa38gvfjTQT4yGTz2BPmjqWTSQG')
INSERT INTO user_roles (user_id, roles_id) VALUES (1, 1)


INSERT INTO user (first_name, last_name, email, password) VALUES ('admin', 'admin', 'admin@admin.com', '$2a$10$iVtJnY.r38CXF0P9Tty3zulhQTQ68c5mfS.FWq1MPHQCO0M.fmNPu')
INSERT INTO user_roles (user_id, roles_id) VALUES (2, 1)
INSERT INTO user_roles (user_id, roles_id) VALUES (2, 2)