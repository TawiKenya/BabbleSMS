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
import org.apache.log4j.Logger;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

/**
 * A utility class to allow for import and export of data from the RDBMS to file
 * (CSV)
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., Oct 30, 2013
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 *
 */
public class DbFileUtils extends GenericDAO {

    private static DbFileUtils dbFileUtils;

    private Logger logger = Logger.getLogger(this.getClass());

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

        Connection conn = null;

        String sanitizedQuery = StringUtils.remove(sqlQuery, ';');

        BufferedWriter writer;

        try {
            FileUtils.deleteQuietly(new File(fileName));
            FileUtils.touch(new File(fileName));
            writer = new BufferedWriter(new FileWriter(fileName));

            //Return a database connection that does not
            //use BoneCp to enable the connection to be cast
            //to BaseConnection
            conn = dbCredentials.getJdbcConnection();

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

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
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

        Connection conn = null;

        FileReader fileReader;

        try {
            String fileName = fileLocation.getName();
            if (!StringUtils.contains(fileName, "null")) {

                fileReader = new FileReader(fileLocation);

				// Return a database connection that does not
                // use BoneCp to enable the connection to be cast
                // to BaseConnection
                conn = dbCredentials.getJdbcConnection();

                CopyManager copyManager = new CopyManager((BaseConnection) conn);

                copyManager.copyIn(sqlQuery, fileReader);

                fileReader.close();

                success = true;
            }

        } catch (SQLException | IOException e) {
            logger.error("SQLException while importing results of  '"
                    + fileLocation + "' to the database.");
            logger.error(ExceptionUtils.getStackTrace(e));
            System.out.println(e);

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        return success;
    }

}

/*
** Local Variables:
**   mode: java
**   c-basic-offset: 2
**   tab-width: 2
**   indent-tabs-mode: nil
** End:
**
** ex: set softtabstop=2 tabstop=2 expandtab:
**
*/
