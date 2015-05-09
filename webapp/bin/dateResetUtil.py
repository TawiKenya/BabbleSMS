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


con = None

try:
    con = psycopg2.connect("dbname='babblesmsdb' user='babblesms' host='localhost' password='Hymfatsh8'")

    cur = con.cursor()    
    cur.execute("SELECT * FROM Account")

    rows = cur.fetchall()

    for row in rows:
        print row


except psycopg2.DatabaseError, e:
    print 'Error %s' % e    
    sys.exit(1)
    
    
finally:
    
    if con:
        con.close()
