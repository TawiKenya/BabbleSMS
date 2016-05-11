#!/usr/bin/env bash
#
################################################################################
# Copyright 2015 Tawi Commercial Services Ltd
#
# Licensed under the Open Software License, Version 3.0 (the “License”); you may
# not use this file except in compliance with the License. You may obtain a copy
# of the License at:
# http://opensource.org/licenses/OSL-3.0
#
# Unless required by applicable law or agreed to in writing, software 
# distributed under the License is distributed on an “AS IS” BASIS, WITHOUT 
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#
# See the License for the specific language governing permissions and 
# limitations under the License.
################################################################################
# This script initializes the database for the project.
#
# Created by Michael Wakahe, <michael@tawi.mobi> on Oct 12 2014

# HOWTO use this script:
# * Be sure to back up the databases that are to be initialized if you wish to
#   preserve the data in them.
# * Ensure that the databases to be initialized are not open as the script 
#   runs.
# * Set the appropriate variables at the beginning of the script according to
#   your environment.

# Some notes on PostgreSQL:
# By default, PostgreSQL has a database called template1 containing privileges 
# and other housekeeping data and an adminstrative user named postgres. Unless 
# your system uses socket credentials, the postgres database user initially has 
# no password. To assign it a password (or to override the password assigned by 
# socket credentials), do as follows:
#
# * Become the user "postgres" by becoming superuser then "su - postgres"
# * Execute the following command:
# $> psql -c "ALTER USER postgres WITH PASSWORD 'newpassword'" -d template1
# The command above assigns the postgres user the password 'newpassword'.

#If you are creating the role and the database for the first time, then 
#you need to enable the lines below.
#Use these lines to automatically create a role in production environment 

#Begin automatic creation of role
DB_USERNAME="postgres"
DB_PASSWORD="root"
DB_HOST="localhost"

export PGUSER=$DB_USERNAME
export PGHOST=$DB_HOST
export PGPASSWORD=$DB_PASSWORD

echo "Creating new role..."

#If a user has not been created, uncomment the line below
#psql -c "CREATE USER babblesms WITH PASSWORD 'Hymfatsh8' CREATEDB;"

echo "Finished creating new role."
#End automatic creation of role

# Initialize the following variables as appropriate:
DB_USERNAME="babblesms"
DB_PASSWORD="Hymfatsh8"
DB_HOST="localhost"
DB_NAME="babblesmsdb"

# There should be no need to change anything below this line.
echo "Starting database initialization script..."

export PGUSER=$DB_USERNAME
export PGHOST=$DB_HOST
export PGPASSWORD=$DB_PASSWORD

# Copy data to be imported into the Linux temporary folder
cp -fr ../etc/sql/data/* /tmp

rm -f /tmp/IncomingLogs.csv
unzip -a /tmp/IncomingLogs.zip -d /tmp

psql -f ../etc/sql/tables.postgres.sql -d postgres

echo "\COPY status(uuid, description) FROM '/tmp/Status.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY Account(uuid, username, logpassword, name, mobile, email, dailysmsLimit, statusuuid, apiusername, apipassword) FROM '/tmp/Accounts.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY country(uuid, name, codeFIPS) FROM '/tmp/Countries.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY network(uuid, name, countryUuid) FROM '/tmp/Networks.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY contact(uuid,name,description,accountuuid,statusuuid) FROM '/tmp/Contacts.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY phone(uuid, phonenumber, contactuuid, statusuuid, networkuuid) FROM '/tmp/phone.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY email(uuid, address, contactuuid, statusuuid) FROM '/tmp/Email.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY groups(uuid,name,description,accountuuid,statusuuid,creationdate) FROM '/tmp/Groups.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY contactgroup(uuid,contactuuid,groupuuid,accountuuid) FROM '/tmp/contactgroup.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY shortcode(uuid,codenumber,accountuuid,networkuuid) FROM '/tmp/Shortcodes.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY mask(uuid,maskname,accountuuid,networkuuid) FROM '/tmp/Masks.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY incominglog(uuid,origin,destination,recipientuuid,message,logTime,networkuuid) FROM '/tmp/IncomingLogs.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY messagestatus(uuid,description) FROM '/tmp/messagestatus.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY outgoinglog(uuid,origin,destination,message,networkuuid,sender,messagestatusuuid,logTime,phoneuuid) FROM '/tmp/outgoinglog.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY outgoingGrouplog(uuid,origin,networkuuid,destination,message,sender,messagestatusuuid,logTime) FROM '/tmp/outgoingGrouplog.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY contactgroupsent(sentcontactuuid, sentgroupuuid) FROM '/tmp/contactgroupsent.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY messagetemplate(uuid,title,contents,accountuuid) FROM '/tmp/MessageTemplate.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY Notification (uuid,ShortDesc,LongDesc,origin,NotificationDate) FROM '/tmp/Notification.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY NotificationStatus (uuid,NotificationUuid) FROM '/tmp/NotificationStatus.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY ShortcodePurchase (Uuid,accountuuid,Shortcodeuuid,count,purchasedate) FROM '/tmp/ShortcodePurchase.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY ShortcodeBalance (Uuid,accountuuid,Shortcodeuuid,count) FROM '/tmp/ShortcodeBalance.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY MaskPurchase (Uuid,accountuuid,maskuuid,count,purchasedate) FROM '/tmp/MaskPurchase.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY MaskBalance (Uuid,accountuuid,Maskuuid,count) FROM '/tmp/MaskBalance.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY SMSGateway (accountuuid,url,username,passwd) FROM '/tmp/SMSGateway.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME
echo "\COPY SMSGateway (accountuuid,url,username,passwd) FROM '/tmp/SMSGateway.csv' WITH DELIMITER AS '|' CSV HEADER" | psql $DB_NAME

#psql -f ../etc/sql/tables.quartz.sql -d postgres

echo "Have finished initializing database."

# vim:tabstop=2:expandtab:shiftwidth=2
