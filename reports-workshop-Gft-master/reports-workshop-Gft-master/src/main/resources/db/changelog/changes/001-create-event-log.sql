--liquibase formatted sql

--changeset pedro:001-create-event-log
CREATE TABLE IF NOT EXISTS event_log (
                           id          VARCHAR(255) NOT NULL,
                           event_type  VARCHAR(100) NOT NULL,
                           source_service VARCHAR(100) NOT NULL,
                           payload     TEXT         NOT NULL,
                           simulation_day INTEGER   NOT NULL,
                           occurred_at VARCHAR(50)  NOT NULL,
                           CONSTRAINT pk_event_log PRIMARY KEY (id)
);