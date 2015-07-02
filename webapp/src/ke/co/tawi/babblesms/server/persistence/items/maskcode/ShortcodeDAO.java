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
package ke.co.tawi.babblesms.server.persistence.items.maskcode;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

/**
 * Persistence implementation for {@link Shortcode}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ShortcodeDAO extends GenericDAO implements BabbleShortcodeDAO {

    private static ShortcodeDAO shortcodeDAO;

    private final Logger logger;

    public static ShortcodeDAO getInstance() {
        if (shortcodeDAO == null) {
            shortcodeDAO = new ShortcodeDAO();
        }
        return shortcodeDAO;
    }

    /**
     *
     */
    protected ShortcodeDAO() {
        super();
        logger = Logger.getLogger(this.getClass());
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
    public ShortcodeDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }

    /**
     *
     */
    @Override
    public boolean putShortcode(Shortcode shortcode) {
        boolean success = true;
        try(Connection conn = dbCredentials.getConnection();
        		 PreparedStatement  pstmt = conn.prepareStatement("INSERT INTO Shortcode (Uuid, codenumber,accountuuid,networkuuid) VALUES (?,?,?,?);");) {
           
            pstmt.setString(1, shortcode.getUuid());
            pstmt.setString(2, shortcode.getCodenumber());
            pstmt.setString(3, shortcode.getAccountuuid());
            pstmt.setString(4, shortcode.getNetworkuuid());

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put shortcode: " + shortcode);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
          
        return success;
    }

    

    /**
     *
     */
    @Override
    public Shortcode getShortcode(String uuid) {
        Shortcode shortcode = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try(Connection conn = dbCredentials.getConnection();
        		PreparedStatement  pstmt = conn.prepareStatement("SELECT * FROM Shortcode WHERE Uuid = ?;");) {
            
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                shortcode = b.toBean(rset, Shortcode.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting shortcode with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        return shortcode;
    }
    
    /**
     *
     */
    @Override
    public Shortcode getShortcodeBycodeNumber(String codenumber,String networkuuid) {
        Shortcode shortcode = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try (Connection  conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Shortcode WHERE codenumber = ? AND networkuuid=?;");){
          
            pstmt.setString(1, codenumber);
            pstmt.setString(2, networkuuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                shortcode = b.toBean(rset, Shortcode.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting shortcode with uuid: " + codenumber);
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        return shortcode;
    }
    
    
    /**
     *
     */
    @Override
    public List<Shortcode> getShortcodebyaccountuuid(String accuuid) {
        List<Shortcode> list = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try(  Connection conn = dbCredentials.getConnection();
        		 PreparedStatement  pstmt = conn.prepareStatement("SELECT * FROM Shortcode WHERE accountuuid=?;");) {
          
            pstmt.setString(1, accuuid);
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Shortcode.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all shortcodes");
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        return list;
    }

    /**
     *
     */
    @Override
    public List<Shortcode> getAllShortcode() {
        List<Shortcode> list = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try(Connection conn = dbCredentials.getConnection();
        		PreparedStatement  pstmt = conn.prepareStatement("SELECT * FROM Shortcode;");) {
           
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Shortcode.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all shortcodes");
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        return list;
    }

    /**
     * @param uuid
     * @param shortcode
     * @return success
     */
    @Override
    public boolean updateShortcode(Shortcode shortcode) {
        boolean success = true; 
        try( Connection conn = dbCredentials.getConnection();
        		PreparedStatement  pstmt = conn.prepareStatement("UPDATE Shortcode SET codenumber=?,accountuuid=?,networkuuid=? WHERE Uuid = ?;");) {
           
            pstmt.setString(1,shortcode.getCodenumber());
            pstmt.setString(2,shortcode.getAccountuuid());
            pstmt.setString(3,shortcode.getNetworkuuid());
            pstmt.setString(4,shortcode.getUuid());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting shortcode with uuid " + shortcode);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        }
          
        return success;
    }

    /**
     *
     */
    @Override
    public boolean deleteShortcode(String uuid) {
        boolean success = true;
        try(Connection conn = dbCredentials.getConnection();
        		 PreparedStatement  pstmt = conn.prepareStatement("DELETE FROM Shortcode WHERE Uuid = ?;");) {
           
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting shortcode with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        return success;
    }

    
}
