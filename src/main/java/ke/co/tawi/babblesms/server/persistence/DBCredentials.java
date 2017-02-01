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
package ke.co.tawi.babblesms.server.persistence;

import ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Information on logging into the database. Also has does pooling of JDBC. This
 * class connects to the database.
 * <p>
 * Connection credentials like database name, password and IP are in an external
 * configuration file.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class DBCredentials {

    private Logger logger = Logger.getLogger(this.getClass());
    private HikariDataSource datasource;
    
    private Connection con;

    private String dbName="", dbHost="", dbUsername="", dbPassword="";
    private int dbPort=5432, dbPoolSize=5;
    
    
    /**
     * By default we assume that we are in an application server and that there
     * is a servlet that is fired on startup that reads configurations from a
     * configuration file.
     */
    public DBCredentials() {

        dbName = PropertiesConfig.getConfigValue("DATABASE_NAME");
        dbHost = PropertiesConfig.getConfigValue("DATABASE_HOST");
        dbUsername = PropertiesConfig.getConfigValue("DATABASE_USERNAME");
        dbPassword = PropertiesConfig.getConfigValue("DATABASE_PASSWORD");
        dbPoolSize = Integer.parseInt(PropertiesConfig.getConfigValue("DATABASE_POOL_SIZE"));
        
        initConnection();
    }

    
    /**
     * This method is mainly for test cases. We specify connection parameters
     * manually because we are not in an application server.
     *
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
     */
    public DBCredentials(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        this.dbName = dbName;
        this.dbHost = dbHost;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbPort = dbPort;
        
        initConnection();
    }
    

    /**
     * This method returns a pooled connection.
     *
     * @return Connection
     */
    public Connection getConnection() {
        Connection conn = null;
        
        try {
            conn = datasource.getConnection();

        } catch (SQLException e) {
            logger.error("SQLException when trying to get an SQL Connection.");
            logger.error(ExceptionUtils.getStackTrace(e));

            initConnection();
        }
        
        return conn;        
    }
    

    /**
     * This method returns an unpooled connection.
     *
     * @return Connection
     */
    public Connection getJdbcConnection() {
       String dbURL;
              
       dbURL = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
      
        // Loading underlying JDBC driver
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(dbURL, dbUsername, dbPassword); //set up jdbc connection that doesn't use HikariCP

        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException when trying to get unpooled JDBC connection");
            logger.error(ExceptionUtils.getStackTrace(e));
            
        } catch (SQLException ex) {
                logger.error("SQLException when trying to get unpooled JDBC connection");
            logger.error(ExceptionUtils.getStackTrace(ex));
        }

        return con;
    }
    

    /**
     *
     */
    public void closeConnections() {
        if (datasource != null) {
                datasource.shutdown();
        }
    }
    

    /**
     *
     */
    private void initConnection() {
                
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(dbPoolSize);
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("serverName", dbHost);
        config.addDataSourceProperty("databaseName", dbName);
        config.addDataSourceProperty("user", dbUsername);
        config.addDataSourceProperty("password", dbPassword);
        
        datasource = new HikariDataSource(config);       
    }
}