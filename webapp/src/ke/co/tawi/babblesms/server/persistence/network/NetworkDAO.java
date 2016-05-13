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
package ke.co.tawi.babblesms.server.persistence.network;

import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.log4j.Logger;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:mwenda@tawi.mobi">Peter Mwenda</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class NetworkDAO extends GenericDAO implements BabbleNetworkDAO {

    private static NetworkDAO networkDAO;

    private BeanProcessor beanProcessor = new BeanProcessor();
    
    private Logger logger;
  
       
    
    /**
     * @return the singleton instance of {@link NetworkDAO}
     */
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
     * @see ke.co.tawi.babblesms.server.persistence.network.BabbleNetworkDAO#putNetwork(ke.co.tawi.babblesms.server.beans.network.Network)
     */
    @Override
    public boolean putNetwork(Network network) {
        boolean success = true;
        try (Connection  conn = dbCredentials.getConnection();
        		 PreparedStatement  pstmt = conn.prepareStatement("INSERT INTO Network "
        		 		+ "(Uuid, Name, countryuuid) VALUES (?,?,?);");) {
            
            pstmt.setString(1, network.getUuid());
            pstmt.setString(2, network.getName());
            pstmt.setString(3, network.getCountryuuid());            

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put: " + network);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        
        return success;
    }
    
    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.network.BabbleNetworkDAO#getNetworkByName(java.lang.String)
     */
    @Override
    public Network getNetworkByName(String name) {
        Network network = null;

        ResultSet rset = null;

        try( Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Network "
        				+ "WHERE LOWER(name) like LOWER(?);");        		
        		) {
           
            pstmt.setString(1, "%" + name + "%");
            rset = pstmt.executeQuery();

            if (rset.next()) {
                network = beanProcessor.toBean(rset, Network.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting network with name: " + name);
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        
        return network;
    }
    
    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.network.BabbleNetworkDAO#getNetwork(java.lang.String)
     */
    @Override
    public Network getNetwork(String uuid) {
        Network network = null;
        ResultSet rset = null;
        
        try(Connection conn = dbCredentials.getConnection();
        		 PreparedStatement   pstmt = conn.prepareStatement("SELECT * FROM Network WHERE Uuid = ?;");) {
           
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                network = beanProcessor.toBean(rset, Network.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting network with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        
        return network;
    }
    
    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.network.BabbleNetworkDAO#getAllNetworks()
     */
    @Override
    public List<Network> getAllNetworks() {
        List<Network> list = null;
        ResultSet rset = null;

        try( 
        		Connection conn = dbCredentials.getConnection();
        		 PreparedStatement   pstmt = conn.prepareStatement("SELECT * FROM Network;");) {
            
            rset = pstmt.executeQuery();

            list = beanProcessor.toBeanList(rset, Network.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all networks");
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        
        return list;
    }    

    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.network.BabbleNetworkDAO#updateNetwork(java.lang.String, java.lang.String)
     */
    @Override
    public boolean updateNetwork(String uuid, Network network) {
        boolean success = true;

        try( Connection conn = dbCredentials.getConnection();
        		 PreparedStatement  pstmt = conn.prepareStatement("UPDATE Network SET Name=?, countryUuid=? WHERE Uuid = ?;");) {
            
            pstmt.setString(1, network.getName());
            pstmt.setString(2, network.getCountryuuid());
            pstmt.setString(3, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting network with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        return success;
    }

}
