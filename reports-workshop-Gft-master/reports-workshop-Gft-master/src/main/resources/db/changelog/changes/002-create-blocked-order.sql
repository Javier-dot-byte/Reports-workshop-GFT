--liquibase formatted sql

--changeset pedro:002-create-blocked-order
CREATE TABLE IF NOT EXISTS blocked_order (
                               order_id         VARCHAR(100) NOT NULL,
                               factory_id       VARCHAR(100) NOT NULL,
                               reason           TEXT         NOT NULL,
                               blocked_since_day INTEGER     NOT NULL,
                               CONSTRAINT pk_blocked_order PRIMARY KEY (order_id)
);