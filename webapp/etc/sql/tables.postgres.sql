/**
 * Copyright 2015 Tawi Commercial Services Ltd
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
-- Schema Name: babblesmsdb
-- Username: babblesms
-- Password: Hymfatsh8

-- These tables describe the database of BabbleSMS

-- Make sure you have created a Postgres user with the above username, password
-- and appropriate permissions. For development environments, you can make the 
-- database user to be a superuser to allow for copying of external files. 

-- Then run the "dbSetup.sh" script in the bin folder of this project.

\c postgres

-- Then execute the following:
DROP DATABASE IF EXISTS babblesmsdb; -- To drop a database you can't be logged into it. Drops if it exists.
CREATE DATABASE babblesmsdb;

-- Connect with the database on the username
\c babblesmsdb babblesms

-- =========================
-- 1. Account Management
-- =========================
-- -------------------
-- Table status
-- -------------------
CREATE TABLE status (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    description text UNIQUE NOT NULL
);
-- import data from the CSV file for the status table
\COPY status(uuid,description) FROM '/tmp/Status.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE status OWNER TO babblesms;


-- -------------------
-- Table Account
-- -------------------
CREATE TABLE Account (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    username text UNIQUE NOT NULL,
    logpassword text,
    apiusername text,
    apipassword text,
    usertype text,
    name text,
    mobile text,
    email text,
    dailysmsLimit int,
    creationdate timestamp with time zone DEFAULT now(),
    statusuuid text REFERENCES status(uuid)
);

-- import data from the CSV file for the Accounts table
\COPY Account(uuid,username,logpassword,apiusername,apipassword,usertype,name,mobile,email,dailysmsLimit,statusuuid) FROM '/tmp/Accounts.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE Account OWNER TO babblesms;


-- ----------------
-- Table country
-- ----------------
CREATE TABLE country (
    id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    name text NOT NULL,
    codeFIPS text
);

\COPY country(uuid, name, codeFIPS) FROM '/tmp/Countries.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE country OWNER TO babblesms;


-- -------------------
-- Table network
-- -------------------
CREATE TABLE network (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    name text,
    countryUuid text REFERENCES country(uuid)
);

\COPY network(uuid,name,countryUuid) FROM '/tmp/Networks.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE network OWNER TO babblesms;


-- -------------------
-- Table contact
----------------------
CREATE TABLE contact (
    id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    name text NOT NULL,
    description text,
    accountuuid text REFERENCES account(uuid),
    statusuuid text REFERENCES status(uuid)
);

\COPY contact(uuid,name,description,accountuuid,statusuuid) FROM '/tmp/Contacts.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE contact OWNER TO babblesms;


-- -------------------
-- Table phone
----------------------
CREATE TABLE phone (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    phonenumber text,
    contactuuid text references contact(uuid),
    statusuuid text references status(uuid),
    networkuuid text references network(uuid)
);

-- import data from the CSV file for the phone table
\COPY phone(uuid,phonenumber,contactuuid,statusuuid,networkuuid) FROM '/tmp/phone.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE phone OWNER TO babblesms;


-- -------------------
-- Table Email
----------------------
CREATE TABLE Email (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    address text,
    contactuuid text references contact(uuid),
    statusuuid text references status(uuid)
);

-- import data from the CSV file for the Email table
\COPY Email(uuid,address,contactuuid,statusuuid) FROM '/tmp/Email.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE Email OWNER TO babblesms;


-- -------------------
-- Table groups
----------------------

-- Since group is a reserved word in postgres we call our table groups instead of group
CREATE TABLE groups (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    name text,
    description text,
    creationdate timestamp with time zone DEFAULT now(),
    accountuuid text references account(uuid),
    statusuuid text references status(uuid)
);

-- import data from the CSV file for the group table
\COPY groups(uuid,name,description,accountuuid,statusuuid) FROM '/tmp/Groups.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE groups OWNER TO babblesms;



-- -------------------
-- Table contactgroup
----------------------
CREATE TABLE contactgroup (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    contactuuid text references contact(uuid),
    groupuuid text references groups(uuid),
    accountuuid text references account(uuid)
);

-- import data from the CSV file for the contactgroup table
\COPY contactgroup(uuid,contactuuid,groupuuid,accountuuid) FROM '/tmp/contactgroup.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE contactgroup OWNER TO babblesms;


-- -------------------
-- Table shortcode
-- -------------------
CREATE TABLE shortcode (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    codenumber text,
    accountuuid text references account(uuid),
    networkuuid text references network(uuid),
    creationdate timestamp with time zone DEFAULT now()
);

\COPY shortcode(uuid,codenumber,accountuuid,networkuuid) FROM '/tmp/Shortcodes.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE shortcode OWNER TO babblesms;


-- -------------------
-- Table mask
-- -------------------
CREATE TABLE mask (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    maskname text,
    accountuuid text references account(uuid),
    networkuuid text references network(uuid),
    creationdate timestamp with time zone DEFAULT now()
);

\COPY mask(uuid,maskname,accountuuid,networkuuid) FROM '/tmp/mask.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE mask OWNER TO babblesms;


 -- -------------------
-- Table purchaseHistory
-- -------------------
CREATE TABLE purchaseHistory (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    amount int,
    source text,
    purchasetime timestamp with time zone DEFAULT now(),
    accountuuid text references account(uuid),
    networkuuid text references network(uuid)
);

-- import data from the CSV file for the Accounts table
\COPY purchaseHistory(uuid,amount,source,accountuuid,networkuuid,purchasetime) FROM '/tmp/purchaseHistory.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE purchaseHistory OWNER TO babblesms;



-- -------------------
-- Table incominglog
-- -------------------
CREATE TABLE incominglog (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    origin text,
    destination text,
    recipientuuid text references account(uuid),
    message text,
    logTime timestamp with time zone DEFAULT now(),
    networkuuid text references network(uuid)
);

\COPY incominglog(uuid,origin,destination,recipientuuid,message,logTime,networkuuid) FROM '/tmp/IncomingLogs.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE incominglog OWNER TO babblesms;



-- -------------------
-- Table messagestatus
-- -------------------
CREATE TABLE messagestatus (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    description text
);

\COPY messagestatus(uuid,description) FROM '/tmp/messagestatus.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE messagestatus OWNER TO babblesms;


-- -------------------
-- Table outgoinglog
-- -------------------
CREATE TABLE outgoinglog (
    Id SERIAL PRIMARY KEY,
    uuid text NOT NULL,
    origin text,
    destination text,
    message text,
    logTime timestamp with time zone DEFAULT now(),
    networkuuid text references network(uuid),
    sender text references account(uuid),
    messagestatusuuid text references messagestatus(uuid)
);

-- import data from the CSV file for the outgoinglog table
\COPY outgoinglog(uuid,origin,destination,message,networkuuid,sender,messagestatusuuid,logTime) FROM '/tmp/outgoinglog.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE outgoinglog OWNER TO babblesms;


-- -------------------
-- Table outgoingGrouplog
-- -------------------
CREATE TABLE outgoingGrouplog (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    origin text,
    destination text references groups(uuid),
    message text,
    logTime timestamp with time zone DEFAULT now(),
    sender text references account(uuid),
    messagestatusuuid text references messagestatus(uuid)
);

-- import data from the CSV file for the outgoingGrouplog table
\COPY outgoingGrouplog(uuid,origin,destination,message,sender,messagestatusuuid,logTime) FROM '/tmp/outgoingGrouplog.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE outgoingGrouplog OWNER TO babblesms;


-- -------------------
-- Table messagetemplate
-- -------------------
CREATE TABLE messagetemplate (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    templatetitle text,
    contents text,
    accountuuid text references account(uuid)
);

-- import data from the CSV file for the Accounts table
\COPY messagetemplate(uuid,templatetitle,contents,accountuuid) FROM '/tmp/messagetemplate.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE messagetemplate OWNER TO babblesms;


-- -------------------
-- Table credit
-- -------------------
CREATE TABLE credit (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    source text,
    credit int,
    accountuuid text references account(uuid)
);

-- import data from the CSV file for the Accounts table
\COPY credit(uuid,source,credit,accountuuid) FROM '/tmp/credit.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE credit OWNER TO babblesms;

-- -------------------
-- Table Notification
-- -------------------
CREATE TABLE Notification (
	Id serial PRIMARY KEY,
	Uuid text UNIQUE NOT NULL,
	origin text NOT NULL,
	ShortDesc text NOT NULL,
	LongDesc text,
	published text DEFAULT 'yes',
	NotificationDate timestamp with time zone  DEFAULT now()
);

-- import data from the CSV file for the Notification table
\COPY Notification (uuid,ShortDesc,LongDesc,origin,NotificationDate) FROM '/tmp/Notification.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE Notification OWNER TO babblesms;


-- ---------------------------
-- Table NotificationStatus
-- ---------------------------
CREATE TABLE NotificationStatus (
	Id serial PRIMARY KEY,
	Uuid text UNIQUE NOT NULL ,
	NotificationUuid text NOT NULL REFERENCES Notification(Uuid),
	ReadFlag text DEFAULT 'N',
	ReadDate timestamp with time zone 
);

-- import data from the CSV file for the NotificationStatus table
\COPY NotificationStatus (uuid,NotificationUuid) FROM '/tmp/NotificationStatus.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE NotificationStatus OWNER TO babblesms;

-- --------------------
-- Table AccountBalance
-- --------------------
CREATE TABLE AccountBalance(
	Id SERIAL PRIMARY KEY,
	Uuid text UNIQUE NOT NULL,
	origin text NOT NULL,
	accountuuid text references account(uuid),
	networkuuid text references network(uuid),
	balance integer NOT NULL CHECK (balance >= 0)
);

-- import data from the CSV file for the AccountBalance table
\COPY AccountBalance (uuid,origin,accountuuid,networkuuid,balance) FROM '/tmp/AccountBalance.csv' WITH DELIMITER AS '|' CSV HEADER
ALTER TABLE AccountBalance OWNER TO babblesms;
