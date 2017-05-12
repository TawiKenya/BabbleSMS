/**
 * Copyright 2017 Tawi Commercial Services Ltd
 *
 * Licensed under the Open Software License, Version 3.0 (the “License”); you may
 * not use this file except in compliance with the License. You may obtain a copy
 * of the License at:
 * http://opensource.org/licenses/OSL-3.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */

-- These tables describe the database of BabbleSMS.
-- Refer to "HOWTO.txt" for information on how to create the database user and
-- schema.

DROP TABLE IF EXISTS email;
DROP TABLE IF EXISTS phone;
DROP TABLE IF EXISTS contact;
DROP TABLE IF EXISTS network;
DROP TABLE IF EXISTS country;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS status;



-- =========================
-- 1. Account Management
-- =========================
-- -------------------
-- Table status
-- -------------------
CREATE TABLE status (
    uuid VARCHAR(40) UNIQUE NOT NULL,
    description VARCHAR(70) UNIQUE NOT NULL DEFAULT 'Status Description',
    PRIMARY KEY (uuid)
) ENGINE=InnoDB;

LOAD DATA LOCAL INFILE 'data/Status.csv' INTO TABLE status FIELDS TERMINATED BY '|' IGNORE 1 LINES;


-- -------------------
-- Table account
-- -------------------
CREATE TABLE account (
    uuid VARCHAR(40) UNIQUE NOT NULL,
    username VARCHAR(40) UNIQUE NOT NULL,
    logpassword VARCHAR(400),
    name VARCHAR(200),
    mobile VARCHAR(40),
    email VARCHAR(80),
    dailysmsLimit INTEGER,
    creationdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statusuuid VARCHAR(40) REFERENCES status(uuid),
    apiusername VARCHAR(80),
    apipassword VARCHAR(400),
    PRIMARY KEY (uuid)
) ENGINE=InnoDB;

LOAD DATA LOCAL INFILE 'data/Accounts.csv' INTO TABLE account FIELDS TERMINATED BY '|' IGNORE 1 LINES;


-- ================================
-- ================================
-- 2. Contact and Group Management
-- ================================
-- ================================

-- ----------------
-- Table country
-- ----------------
CREATE TABLE country (
    uuid VARCHAR(40) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    codeFIPS VARCHAR(5),
    PRIMARY KEY (uuid)
) ENGINE=InnoDB;

LOAD DATA LOCAL INFILE 'data/Countries.csv' INTO TABLE country FIELDS TERMINATED BY '|' IGNORE 1 LINES;


-- -------------------
-- Table network
-- -------------------
CREATE TABLE network (
    uuid VARCHAR(40) UNIQUE NOT NULL,
    name VARCHAR(40),
    countryUuid VARCHAR(40) REFERENCES country(uuid),
    PRIMARY KEY (uuid)
) ENGINE=InnoDB;

LOAD DATA LOCAL INFILE 'data/Networks.csv' INTO TABLE network FIELDS TERMINATED BY '|' IGNORE 1 LINES;


-- -------------------
-- Table contact
----------------------
CREATE TABLE contact (
    id SERIAL,
    uuid VARCHAR(40) UNIQUE NOT NULL,
    name VARCHAR(300) NOT NULL,
    description VARCHAR(400),
    accountUuid VARCHAR(40) REFERENCES account(uuid),
    statusUuid VARCHAR(40) REFERENCES status(uuid),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

LOAD DATA LOCAL INFILE 'data/Contacts.csv' INTO TABLE contact FIELDS TERMINATED BY '|'  IGNORE 1 LINES
(uuid, name, description, accountUuid, statusUuid);


-- -------------------
-- Table phone
----------------------
CREATE TABLE phone (
    Id SERIAL,
    uuid VARCHAR(40) UNIQUE NOT NULL,
    phonenumber VARCHAR(60),
    contactUuid VARCHAR(40) REFERENCES contact(uuid),
    statusUuid VARCHAR(40) REFERENCES status(uuid),
    networkUuid VARCHAR(40) REFERENCES network(uuid),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

LOAD DATA LOCAL INFILE 'data/Phones.csv' INTO TABLE phone FIELDS TERMINATED BY '|'  IGNORE 1 LINES
(uuid, phonenumber, contactUuid, statusUuid, networkUuid);


-- -------------------
-- Table email
----------------------
CREATE TABLE email (
    Id SERIAL,
    uuid VARCHAR(40) UNIQUE NOT NULL,
    address VARCHAR(60),
    contactUuid VARCHAR(40) REFERENCES contact(uuid),
    statusUuid VARCHAR(40) REFERENCES status(uuid),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

LOAD DATA LOCAL INFILE 'data/Emails.csv' INTO TABLE email FIELDS TERMINATED BY '|'  IGNORE 1 LINES
(uuid, address, contactUuid, statusUuid);


