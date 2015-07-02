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

import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

/**
 * Persistence implementation for {@link Mask}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class MaskDAO extends GenericDAO implements BabbleMaskDAO {

    private static MaskDAO maskDAO;

    private final Logger logger;

    public static MaskDAO getInstance() {
        if (maskDAO == null) {
            maskDAO = new MaskDAO();
        }
        return maskDAO;
    }

    /**
     *
     */
    protected MaskDAO() {
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
    public MaskDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }

    /**
     *
     */
    @Override
    public boolean putMask(Mask mask) {
        boolean success = true;
        try (Connection conn = dbCredentials.getConnection();
        		PreparedStatement  pstmt = conn.prepareStatement("INSERT INTO Mask (Uuid, maskname,accountuuid,networkuuid) VALUES (?,?,?,?);");){
            
            pstmt.setString(1, mask.getUuid());
            pstmt.setString(2, mask.getMaskname());
            pstmt.setString(3, mask.getAccountuuid());
            pstmt.setString(4, mask.getNetworkuuid());

            pstmt.execute();
            
            

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put mask: " + mask);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        return success;
    }

    /**
     *
     * @param name
     * @return mask
     *
     */
    @Override
    public Mask getMaskByName(String name) {
        Mask mask = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try( Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Mask WHERE maskName = ?;");) {
           
            pstmt.setString(1, name);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                mask = b.toBean(rset, Mask.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting mask with uuid: " + name);
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        return mask;
    }

    /**
     *
     */
    @Override
    public Mask getMask(String uuid) {
        Mask mask = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try(Connection conn = dbCredentials.getConnection();
        		PreparedStatement  pstmt = conn.prepareStatement("SELECT * FROM Mask WHERE Uuid = ?;");) {
            
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                mask = b.toBean(rset, Mask.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting mask with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        return mask;
    }

    /**
     *
     */
    @Override
    public List<Mask> getAllMasks() {
        List<Mask> list = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try(Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Mask;");) {
           
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Mask.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all masks");
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        return list;
    }
    
    /**
     *
     */
    @Override
    public List<Mask> getmaskbyaccount(String accuuid) {
        List<Mask> masklist = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try(Connection conn = dbCredentials.getConnection();
        		 PreparedStatement   pstmt = conn.prepareStatement("SELECT * FROM Mask WHERE accountuuid=?;");) {
           
            pstmt.setString(1, accuuid);
            rset = pstmt.executeQuery();

            masklist = b.toBeanList(rset, Mask.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all masks");
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        return masklist;
    }


    /**
     * @param uuid
     * @param mask
     * @return success
     */
    @Override
    public boolean updateMask(Mask mask) {
        boolean success = true;
        try(Connection conn = dbCredentials.getConnection();
        		PreparedStatement  pstmt = conn.prepareStatement("UPDATE Mask SET MASKName=?,accountuuid=?,networkuuid=? WHERE Uuid = ?;");) {
           
            pstmt.setString(1, mask.getMaskname());
            pstmt.setString(2, mask.getAccountuuid());
            pstmt.setString(3, mask.getNetworkuuid());
             pstmt.setString(4, mask.getUuid());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting mask with uuid " + mask);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        return success;
    }

    /**
     *
     */
    @Override
    public boolean deleteMask(String uuid) {
        boolean success = true;
        try( Connection conn = dbCredentials.getConnection();
        		PreparedStatement  pstmt = conn.prepareStatement("DELETE FROM Mask WHERE Uuid = ?;");) {
           
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting mask with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        return success;
    }

}
