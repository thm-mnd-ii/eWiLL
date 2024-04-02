ALTER TABLE diagram_config
    ADD created_by BIGINT;

ALTER TABLE diagram_config
    ADD creation_date date;

ALTER TABLE diagram_config
    ADD last_modified date;

ALTER TABLE diagram_config
    ADD CONSTRAINT FK_DIAGRAM_CONFIG_ON_CREATEDBY FOREIGN KEY (created_by) REFERENCES users (id);