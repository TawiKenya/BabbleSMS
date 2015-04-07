package ke.co.tawi.babblesms.server.persistence.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DbFileUtils}
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., Oct 30, 2013  
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * @version %I%, %G%
 * 
 */
public class TestDbFileUtils {

	final String DB_NAME = "airtimegwdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "airtimegw";
    final String DB_PASSWD = "CrutNoc1";
    final int DB_PORT = 5432;

    final String SQL_QUERY = "SELECT * FROM masterAirtimeBalance;",
    	SQL_QUERY2 = "SELECT * FROM topup;",
        SQL_QUERY3 = "SELECT topup.uuid, topup.msisdn, topup.amount, network.name, topupStatus.status, topup.topupTime " +
        		"FROM topup " +
        		"INNER JOIN network ON topup.networkUuid=network.uuid " +
        		"INNER JOIN topupStatus ON topup.topupStatusUuid=topupStatus.uuid " +
        		"WHERE topup.accountUuid = '9756f889-811a-4a94-b13d-1c66c7655a7f'";
        
    final String CSV_FILE = "/tmp/airtimegw/Balance.csv",
    		CSV_FILE2 = "/tmp/airtimegw/Topups.csv";
    		
    private DbFileUtils dbFileUtils;
    
    
	/**
	 * Test method for {@link DbFileUtils#sqlResultToCSV(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSqlResultToCSV() {
		dbFileUtils = new DbFileUtils(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		FileUtils.deleteQuietly(new File(CSV_FILE));
		FileUtils.deleteQuietly(new File(CSV_FILE2));
		
		assertTrue(dbFileUtils.sqlResultToCSV(SQL_QUERY, CSV_FILE, '|'));
		assertTrue(dbFileUtils.sqlResultToCSV(SQL_QUERY3, CSV_FILE2, '|'));
	}

}
