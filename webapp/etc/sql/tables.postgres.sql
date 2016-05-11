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

\c postgres babblesms

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
ALTER TABLE status OWNER TO babblesms;


-- -------------------
-- Table Account
-- -------------------
CREATE TABLE account (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    username text UNIQUE NOT NULL,
    logpassword text,    
    name text,
    mobile text,
    email text,
    dailysmsLimit int,
    creationdate timestamp with time zone DEFAULT now(),
    statusuuid text REFERENCES status(uuid),
    apiusername text,
    apipassword text
);
ALTER TABLE account OWNER TO babblesms;



-- ================================
-- ================================
-- 2. Contact and Group Management
-- ================================
-- ================================

-- ----------------
-- Table country
-- ----------------
CREATE TABLE country (
    id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    name text NOT NULL,
    codeFIPS text
);
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
ALTER TABLE Email OWNER TO babblesms;


-- -------------------
-- Table groups
----------------------

-- Since group is a reserved word in postgres we call our table groups instead of group
CREATE TABLE groups (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    name text NOT NULL,
    description text,
    creationdate timestamp with time zone DEFAULT now(),
    accountuuid text references account(uuid),
    statusuuid text references status(uuid)
);
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
ALTER TABLE contactgroup OWNER TO babblesms;


-- ==================
-- ==================
-- 3. SMS Management
-- ==================
-- ==================

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
ALTER TABLE mask OWNER TO babblesms;


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
ALTER TABLE incominglog OWNER TO babblesms;



-- -------------------
-- Table messagestatus
-- -------------------
CREATE TABLE messagestatus (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    description text
);
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
    messagestatusuuid text references messagestatus(uuid),
    phoneuuid text references phone(uuid)
);
ALTER TABLE outgoinglog OWNER TO babblesms;


-- -----------------------
-- Table outgoingGrouplog
-- -----------------------
CREATE TABLE outgoingGrouplog (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    origin text,
    networkuuid text references network(uuid),
    destination text references groups(uuid),
    message text,
    logTime timestamp with time zone DEFAULT now(),
    sender text references account(uuid),
    messagestatusuuid text references messagestatus(uuid)
);
ALTER TABLE outgoingGrouplog OWNER TO babblesms;


------------------------------
--Table contactgroupsent
------------------------------
CREATE TABLE contactgroupsent(
    Id SERIAL PRIMARY KEY,
    sentcontactuuid text,
    sentgroupuuid text references outgoingGrouplog(uuid)
 );
ALTER TABLE contactgroupsent OWNER TO babblesms;


-- -------------------
-- Table messagetemplate
-- -------------------
CREATE TABLE messagetemplate (
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    title text,
    contents text,
    accountuuid text REFERENCES account(uuid)
);
ALTER TABLE messagetemplate OWNER TO babblesms;


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
ALTER TABLE NotificationStatus OWNER TO babblesms;


-- ----------------------
-- Table ShortcodePurchase
-- ----------------------
CREATE TABLE ShortcodePurchase(
    Id SERIAL PRIMARY KEY,
    Uuid text UNIQUE NOT NULL,
    accountuuid text references account(uuid),
    shortcodeuuid text references Shortcode(uuid),
    count integer NOT NULL CHECK (count>=0),
    purchasedate timestamp with time zone   
);  
ALTER TABLE ShortcodePurchase OWNER TO babblesms;


-- ---------------------
-- Table ShortcodeBalance
-- ---------------------
CREATE TABLE ShortcodeBalance(
  Id SERIAL PRIMARY KEY,
  Uuid text UNIQUE NOT NULL,
  accountuuid text references account(uuid),
  Shortcodeuuid text references Shortcode(uuid),
  count integer NOT NULL CHECK(count >= 0)
);
ALTER TABLE ShortcodeBalance OWNER TO babblesms;


-------------------------
-- Table MaskPurchase
-- ----------------------
CREATE TABLE MaskPurchase(
      Id SERIAL PRIMARY KEY,
      Uuid text UNIQUE NOT NULL,
      accountuuid text references account(uuid),
      maskuuid text references Mask(uuid),
      count integer NOT NULL CHECK (count>=0),
      purchasedate timestamp with time zone 
);
ALTER TABLE MaskPurchase OWNER TO babblesms;


-- --------------------
-- Table MaskBalance
-- ---------------------
CREATE TABLE MaskBalance(
    Id SERIAL PRIMARY KEY,
    Uuid text UNIQUE NOT NULL,
    accountuuid text references account(uuid),
    maskuuid text references Mask(uuid),
    count integer NOT NULL CHECK (count>=0)  
);
ALTER TABLE MaskBalance OWNER TO babblesms;



-- =========================
-- =========================
-- 4. SMS Gateway Management
-- =========================
-- =========================
-- --------------------
-- Table SMSGateway
-- ---------------------
CREATE TABLE SMSGateway(
    Id SERIAL PRIMARY KEY,
    accountuuid text references account(uuid),
    url text NOT NULL,
    username text NOT NULL,
    passwd text NOT NULL
);
ALTER TABLE SMSGateway OWNER TO babblesms;


-- --------------------
-- Table SentGatewayLog
-- ---------------------
CREATE TABLE SentGatewayLog(
    Id SERIAL PRIMARY KEY,
    uuid text UNIQUE NOT NULL,
    accountuuid text references account(uuid),
    response text,
    responsedate timestamp with time zone
);
ALTER TABLE SentGatewayLog OWNER TO babblesms;