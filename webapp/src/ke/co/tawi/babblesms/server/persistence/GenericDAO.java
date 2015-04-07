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

import ke.co.tawi.babblesms.server.servlet.util.DbPoolUtil;


/**
 * What is common to all data access objects (DAOs).
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class GenericDAO {
	
	/**
     *
     */
    protected DBCredentials dbCredentials;
	
	/**
     *
     */
    public GenericDAO() {
    	dbCredentials = DbPoolUtil.getDBCredentials();
	}
	
    
	/**
	 * 
	 * @param dbName
	 * @param dbHost
	 * @param dbUsername
	 * @param dbPassword
	 * @param dbPort
	 */
	public GenericDAO(String dbName, String dbHost, String dbUsername, String dbPassword, int dbPort) {
		dbCredentials = new DBCredentials(dbName, dbHost, dbUsername, dbPassword, dbPort);
	}
	
	
	/**
	 * 
	 */
	public void closeConnections() {
		dbCredentials.closeConnections();
	}
}
