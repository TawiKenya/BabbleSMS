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
package ke.co.tawi.babblesms.server.persistence.utils;

import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

/**
 * A utility class to allow for import and export of data from the RDBMS to file
 * (CSV)
 * <p>
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 *
 */
public class DbFileUtils extends GenericDAO {

    private static DbFileUtils dbFileUtils;

    private Logger logger = LogManager.getLogger(this.getClass());
    

    /**
     * Get the singleton instance
     *
     * @return {@link DbFileUtils}
     */
    public static DbFileUtils getInstance() {
        if (dbFileUtils == null) {
            dbFileUtils = new DbFileUtils();
        }

        return dbFileUtils;
    }

    
    /**
     *
     */
    protected DbFileUtils() {
        super();
    }

    /**
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
     */
    public DbFileUtils(String dbName, String dbHost, String dbUsername, String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
    }
    

    /**
     * This is used to export the results of an SQL query into a CSV text file.
     *
     * @param sqlQuery
     * @param fileName this should include the full path of the file e.g.
     * /tmp/myFile.csv
     * @param delimiter
     *
     * @return whether the action was successful or not
     */
    public boolean sqlResultToCSV(String sqlQuery, String fileName, char delimiter) {
        boolean success = true;
        
        String sanitizedQuery = StringUtils.remove(sqlQuery, ';');

        BufferedWriter writer;

        try(
        		// Return a database connection that is not pooled
                // to enable the connection to be cast to BaseConnection
        		Connection conn = dbCredentials.getJdbcConnection();
        		) {
        	
            FileUtils.deleteQuietly(new File(fileName));
            FileUtils.touch(new File(fileName));
            writer = new BufferedWriter(new FileWriter(fileName));

            CopyManager copyManager = new CopyManager((BaseConnection) conn);

            StringBuffer query = new StringBuffer("COPY (")
                    .append(sanitizedQuery)
                    .append(") to STDOUT WITH DELIMITER '")
                    .append(delimiter)
                    .append("'");

            copyManager.copyOut(query.toString(), writer);
            writer.close();

        } catch (SQLException e) {
            logger.error("SQLException while exporting results of query '" + sqlQuery
                    + "' to file '" + fileName + "'.");
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;

        } catch (IOException e) {
            logger.error("IOException while exporting results of query '" + sqlQuery
                    + "' to file '" + fileName + "'.");
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 

        return success;
    }

    
    /**
     * This is used to import the results of CSV text file to the database.
     *
     * @param sqlQuery
     * @param fileLocation this should include the full path of the file e.g.
     * /tmp/myFile.csv
     *
     * @return whether the action was successful or not
     */
    public boolean importCSVToDatabase(String sqlQuery, File fileLocation) {
        boolean success = false;

        FileReader fileReader;

        try(
        		// Return a database connection that is not pooled
                // to enable the connection to be cast to BaseConnection
        		Connection conn = dbCredentials.getJdbcConnection();
        		) {
        	
            String fileName = fileLocation.getName();
            if (!StringUtils.contains(fileName, "null")) {

                fileReader = new FileReader(fileLocation);

                CopyManager copyManager = new CopyManager((BaseConnection) conn);

                copyManager.copyIn(sqlQuery, fileReader);

                fileReader.close();

                success = true;
            }

        } catch (SQLException e) {
        	logger.error("SQLException while importing results of  '"
                    + fileLocation + "' and SQL query: " + sqlQuery);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;

        } catch (IOException e) {
        	logger.error("SQLException while importing results of  '"
                    + fileLocation + "' and SQL query: " + sqlQuery);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 

        return success;
    }

}
