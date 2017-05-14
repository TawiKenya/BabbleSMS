


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
