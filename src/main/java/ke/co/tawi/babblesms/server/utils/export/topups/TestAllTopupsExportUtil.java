package ke.co.tawi.babblesms.server.utils.export.topups;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the {@link AllTopupsExportUtil}
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., Oct 31, 2013  
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class TestAllTopupsExportUtil {

	final String CSV_FILE = "/tmp/airtimegw/Topups.csv";
	final String EXCEL_FILE = "/tmp/airtimegw2/Topups.xlsx";
	
	
	/**
	 * Test method for {@link AllTopupsExportUtil#createExcelExport(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateExcelExport() {
		assertTrue(AllTopupsExportUtil.createExcelExport(CSV_FILE, "|", EXCEL_FILE));
	}

}
