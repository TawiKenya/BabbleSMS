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
package ke.co.tawi.babblesms.server.persistence.geolocation;

import ke.co.tawi.babblesms.server.beans.geolocation.Country;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 */
public class CountryDAO extends GenericDAO implements BabbleCountryDAO {

    private static CountryDAO networkDAO;

    private Logger logger;

    /**
     * @return a singleton instance of CountryDAO
     */
    public static CountryDAO getInstance() {
        if (networkDAO == null) {
            networkDAO = new CountryDAO();
        }
        return networkDAO;
    }

    
    /**
     *
     */
    protected CountryDAO() {
        super();
        logger = LogManager.getLogger(this.getClass());
    }

    
    /**
     * Used for testing purposes only.
     *
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
     */
    public CountryDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = LogManager.getLogger(this.getClass());
    }


    /**
     *
     * @param name
     * @return network
     * Returns 
     *
     */
    @Override
    public Country getCountryByName(String name) {
        Country network = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Country WHERE LOWER(name) like LOWER(?);");
            pstmt.setString(1, "%"+name +"%");
            rset = pstmt.executeQuery();

            if (rset.next()) {
                network = b.toBean(rset, Country.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting network with uuid: " + name);
            logger.error(ExceptionUtils.getStackTrace(e));
            
        } 
        
        return network;
    }

    
    /**
     *
     */
    @Override
    public Country getCountry(String uuid) {
        Country network = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Country WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                network = b.toBean(rset, Country.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting network with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            
        } 
        
        return network;
    }
    

    /**
     *
     */
    @Override
    public List<Country> getAllCountries() {
        List<Country> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Country;");
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Country.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all networks");
            logger.error(ExceptionUtils.getStackTrace(e));
            
        } 
        
        return list;
    }

}
