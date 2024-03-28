create sequence hibernate_sequence start 1 increment 1;

    create table category (
       id int8 not null,
        name varchar(255) not null,
        user_id int8 not null,
        primary key (id)
    );

    create table connection_type (
       id int8 not null,
        color varchar(255) not null,
        icon varchar(255) not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table course (
       id int8 not null,
        active boolean not null,
        creation_date varchar(255) not null,
        description varchar(1000000) not null,
        key_password varchar(255) not null,
        location varchar(255) not null,
        name varchar(255) not null,
        owner_id int8 not null,
        semester_id int8 not null,
        primary key (id)
    );

    create table course_user_role (
       id int8 not null,
        course_id int8 not null,
        role varchar(20),
        user_id int8 not null,
        primary key (id)
    );

    create table diagram (
       id int8 not null,
        category_id int8 not null,
        config_id int8,
        connections varchar(100000) not null,
        entities varchar(100000) not null,
        name varchar(255) not null,
        owner_id int8 not null,
        primary key (id)
    );

    create table diagram_config (
       id int8 not null,
        diagram_type varchar(255) not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table diagram_config_connection_type (
       diagram_config_id int8 not null,
        connection_type_id int8 not null,
        primary key (diagram_config_id, connection_type_id)
    );

    create table diagram_config_element_type (
       diagram_config_id int8 not null,
        element_type_id int8 not null,
        primary key (diagram_config_id, element_type_id)
    );

    create table diagram_history (
       id int8 not null,
        connections varchar(100000) not null,
        diagram_id int8 not null,
        entities varchar(100000) not null,
        save_type varchar(255),
        time_stamp varchar(255) not null,
        primary key (id)
    );

    create table element_type (
       id int8 not null,
        color varchar(255) not null,
        icon varchar(255) not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table feedback (
       id int8 not null,
        first_name varchar(255),
        last_name varchar(255),
        status varchar(255),
        text varchar(100000),
        times varchar(255),
        primary key (id)
    );

    create table result (
       id int8 not null,
        comment jsonb,
        correct boolean not null,
        score float4 not null,
        submission_id int8 not null,
        primary key (id)
    );

    create table roles (
       id int8 not null,
        name varchar(20),
        primary key (id)
    );

    create table ruleset (
       id int8 not null,
        name varchar(255) not null,
        course_id varchar(100000) not null,
        primary key (id)
    );

    create table semester (
       id int8 not null,
        end_date varchar(255) not null,
        name varchar(255) not null,
        start_date varchar(255) not null,
        primary key (id)
    );

    create table submission (
       id int8 not null,
        attempt int4 not null,
        date varchar(255) not null,
        diagram varchar(100000) not null,
        task_id int8 not null,
        user_id int8 not null,
        primary key (id)
    );

    create table task (
       id int8 not null,
        course_id int8 not null,
        description varchar(100000) not null,
        due_date varchar(255) not null,
        liability varchar(255),
        max_submissions int4,
        mediatype varchar(255) not null,
        name varchar(255) not null,
        ruleset_id int8 not null,
        show_level varchar(255),
        solution_model_id int8,
        level varchar(255),
        primary key (id)
    );

    create table user_roles (
       user_id int8 not null,
        role_id int8 not null,
        primary key (user_id, role_id)
    );

    create table users (
       id int8 not null,
        email varchar(255) not null,
        first_name varchar(255),
        last_name varchar(255),
        username varchar(255) not null,
        primary key (id)
    );

    alter table users 
       add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table course 
       add constraint FKlmyb73uymsfhqh374ndr3n4c0 
       foreign key (semester_id) 
       references semester;

    alter table diagram_config_connection_type 
       add constraint FKl4ff63sed5s1iohi86plsiba7 
       foreign key (connection_type_id) 
       references connection_type;

    alter table diagram_config_connection_type 
       add constraint FKmjlfepw8avs7d4t8ttnkn9g81 
       foreign key (diagram_config_id) 
       references diagram_config;

    alter table diagram_config_element_type 
       add constraint FK4v9vlv4nd3p7mte9k6gkv77id 
       foreign key (element_type_id) 
       references element_type;

    alter table diagram_config_element_type 
       add constraint FK52w7v4vv8jc8voo71cswsefkk 
       foreign key (diagram_config_id) 
       references diagram_config;

    alter table user_roles 
       add constraint FKh8ciramu9cc9q3qcqiv4ue8a6 
       foreign key (role_id) 
       references roles;

    alter table user_roles 
       add constraint FKhfh9dx7w3ubf1co1vdev94g3f 
       foreign key (user_id) 
       references users;
