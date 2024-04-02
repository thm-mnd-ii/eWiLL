CREATE TABLE connection_type
(
    id    BIGINT       NOT NULL,
    name  VARCHAR(255) NOT NULL,
    icon  VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL,
    CONSTRAINT pk_connection_type PRIMARY KEY (id)
);

CREATE TABLE diagram_config_connection_type
(
    connection_type_id BIGINT NOT NULL,
    diagram_config_id  BIGINT NOT NULL,
    CONSTRAINT pk_diagram_config_connection_type PRIMARY KEY (connection_type_id, diagram_config_id)
);

CREATE TABLE diagram_config_element_type
(
    diagram_config_id BIGINT NOT NULL,
    element_type_id   BIGINT NOT NULL,
    CONSTRAINT pk_diagram_config_element_type PRIMARY KEY (diagram_config_id, element_type_id)
);

CREATE TABLE element_type
(
    id    BIGINT       NOT NULL,
    name  VARCHAR(255) NOT NULL,
    icon  VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL,
    CONSTRAINT pk_element_type PRIMARY KEY (id)
);

ALTER TABLE diagram_config_connection_type
    ADD CONSTRAINT fk_diaconcontyp_on_connection_type FOREIGN KEY (connection_type_id) REFERENCES connection_type (id);

ALTER TABLE diagram_config_connection_type
    ADD CONSTRAINT fk_diaconcontyp_on_diagram_config FOREIGN KEY (diagram_config_id) REFERENCES diagram_config (id);

ALTER TABLE diagram_config_element_type
    ADD CONSTRAINT fk_diaconeletyp_on_diagram_config FOREIGN KEY (diagram_config_id) REFERENCES diagram_config (id);

ALTER TABLE diagram_config_element_type
    ADD CONSTRAINT fk_diaconeletyp_on_element_type FOREIGN KEY (element_type_id) REFERENCES element_type (id);