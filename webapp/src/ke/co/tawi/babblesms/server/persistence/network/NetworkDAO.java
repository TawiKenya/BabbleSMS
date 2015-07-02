package ke.co.tawi.babblesms.server.persistence.network;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import net.sf.ehcache.CacheManager;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @author <a href="mailto:mwenda@tawi.mobi">peter mwenda</a>
 * @author <a href="mailto:michael@tawi.mobi">michael wakahe</a>
 * @version %I%, %G%
 */
public class NetworkDAO extends GenericDAO implements BabbleNetworkDAO {

    private static NetworkDAO networkDAO;

    private final Logger logger;
  
    
    
    public static NetworkDAO getInstance() {
        if (networkDAO == null) {
            networkDAO = new NetworkDAO();
        }
        return networkDAO;
    }

    /**
     *
     */
    protected NetworkDAO() {
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
    public NetworkDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
   
    }

    /**
     *
     */
    @Override
    public boolean putNetwork(Network network) {
        boolean success = true;
        try (Connection  conn = dbCredentials.getConnection();
        		 PreparedStatement  pstmt = conn.prepareStatement("INSERT INTO Network (Uuid, Name,countryuuid) VALUES (?,?,?);");) {
            
            pstmt.setString(1, network.getUuid());
            pstmt.setString(2, network.getName());
            pstmt.setString(3, network.getCountryuuid());
            

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put network: " + network);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        return success;
    }

    /**
     *
     * @param name
     * @return network
     *
     */
    @Override
    public Network getNetworkByName(String name) {
        Network network = null;

        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try( Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Network WHERE LOWER(name) like LOWER(?);");
        		
        		) {
           
            pstmt.setString(1, "%"+name +"%");
            rset = pstmt.executeQuery();

            if (rset.next()) {
                network = b.toBean(rset, Network.class);
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
    public Network getNetwork(String uuid) {
        Network network = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try(Connection conn = dbCredentials.getConnection();
        		 PreparedStatement   pstmt = conn.prepareStatement("SELECT * FROM Network WHERE Uuid = ?;");) {
           
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                network = b.toBean(rset, Network.class);
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
    public List<Network> getAllNetworks() {
        List<Network> list = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try( Connection conn = dbCredentials.getConnection();
        		 PreparedStatement   pstmt = conn.prepareStatement("SELECT * FROM Network;");) {
            
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Network.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all networks");
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        return list;
    }

    /**
     * @param uuid
     * @param network
     * @return success
     */
    @Override
    public boolean updateNetwork(String uuid, String network) {
        boolean success = true;

        try( Connection conn = dbCredentials.getConnection();
        		 PreparedStatement  pstmt = conn.prepareStatement("UPDATE Network SET Name=? WHERE Uuid = ?;");) {
            
            pstmt.setString(1, network);
            pstmt.setString(2, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting network with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        return success;
    }

    /**
     *
     */
    @Override
    public boolean deleteNetwork(String uuid) {
        boolean success = true;
        try (Connection conn = dbCredentials.getConnection();
        		PreparedStatement  pstmt = conn.prepareStatement("DELETE FROM Network WHERE Uuid = ?;");){
            
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting network with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        return success;
    }

}
