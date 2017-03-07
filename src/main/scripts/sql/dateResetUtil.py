#!/usr/bin/env python
# -*- coding: utf-8 -*-
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
# Python script to reset the dates on the Incoming and Outgoing SMS database 
# tables.
#
# Tested using Python 2.7.6 on a PostgreSQL 9.3.5 database
#
# Michael Wakahe, michael@tawi.mobi
#
################################################################################
#
# In order to run the script, ensure that the Python psycopg2 module is 
# installed. To install in Ubuntu, run the following command:
#
# $ sudo apt-get install python-psycopg2
# 
################################################################################

import psycopg2
import sys
import datetime
from datetime import timedelta
from random import randint


#-------------------------------------------------------------------------------
# This class allows us to fetch an index in a database row of a resultset by 
# name instead of by index number.
#-------------------------------------------------------------------------------
class reg(object):
    def __init__(self, cursor, row):
	for (attr, val) in zip((d[0] for d in cursor.description), row) :
	    setattr(self, attr, val)



#-------------------------------------------------------------------------------
# Gives us a random time between a start and an end date
# Note that both 'start' and 'end' arguments should be datetime objects.
#-------------------------------------------------------------------------------
def random_date(start, end):
    return start + timedelta(
        seconds = randint(0, abs(int((end - start).total_seconds()))) )



#-------------------------------------------------------------------------------
# This method subtracts 'delta' number of months from a datetime object
#-------------------------------------------------------------------------------
def monthdelta(date, delta):
    m, y = (date.month-delta) % 12, date.year + ((date.month)+delta-1) // 12
    if not m: m = 12
    d = min(date.day, [31,
        29 if y%4==0 and not y%400==0 else 28,31,30,31,30,31,31,30,31,30,31][m-1])
    return date.replace(day=d,month=m, year=y)



#-------------------------------------------------------------------------------
# Database functionality below
#-------------------------------------------------------------------------------
def resetDates(dbTable, dbColumn):
    con = None

    try:
        monthDelta = 3 # The number of months in the past from now for which the dates should fall in between
    
        dateNow = datetime.datetime.now()    
        dateStart = monthdelta(dateNow, monthDelta)
            
        con = psycopg2.connect("dbname='babblesmsdb' user='babblesms' host='localhost' password='Hymfatsh8'")

        cur = con.cursor() 
        cur2 = con.cursor()
        
        cur.execute("SELECT * FROM " + str(dbTable) + ";")

        while True:
        
            row = cur.fetchone()
            
            if row == None:
                break
            
            r = reg(cur, row)
            uuid = r.uuid
            
            cur2.execute("UPDATE " + str(dbTable) + " SET " + str(dbColumn) + " =%s WHERE uuid=%s;", (random_date(dateStart, dateNow), uuid))            
            con.commit()


    except psycopg2.DatabaseError, e:
        print 'Error %s' % e    
        sys.exit(1)


    finally:        
        if con:
            con.close()



#-------------------------------------------------------------------------------
# Main method
#-------------------------------------------------------------------------------
print "Have started the date reset utility."

resetDates("incominglog", "logTime")
resetDates("outgoinglog", "logTime")
resetDates("outgoingGrouplog", "logTime")

print "Have finished running utility."
