ALTER TABLE diagram_config
    DROP COLUMN creation_date;

ALTER TABLE diagram_config
    DROP COLUMN last_modified;

ALTER TABLE diagram_config
    ADD creation_date TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE diagram_config
    ADD last_modified TIMESTAMP WITHOUT TIME ZONE;