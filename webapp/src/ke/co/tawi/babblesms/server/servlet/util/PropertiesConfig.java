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
package ke.co.tawi.babblesms.server.servlet.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.log4j.Logger;


/**
 * Used to read in the values within the configuration file.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class PropertiesConfig extends HttpServlet {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	private static String configFile = "";
	
	// Values in config file to be retained in a HashMap
	private static final HashMap<String,String> configHash = new HashMap<>(); 
	
	/**
	 * @param config
	 * @throws ServletException
	 * @see javax.servlet.Servlet#init(ServletConfig)
	 */
        @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		configFile = getServletContext().getRealPath("/") + getInitParameter("config-file");
		initConfigHash();
		
	}

	/**
      * @param request 
      * @param response 
      * @throws ServletException 
      * @throws IOException 
      * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
        @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("index.jsp");
	}

	
	/**  
	 * @param configAttribute
	 * @return 		the String value of the attribute we seek
	 */
	public static String getConfigValue(String configAttribute) {
		return configHash.get(configAttribute);
	}
	
	/**
	 * Populate the internal HashMap which will hold configuration keys and values
	 */
	private void initConfigHash() {
		PropertiesConfiguration config;		
		String key;
		
		try {
			config = new PropertiesConfiguration();
			config.setListDelimiter('|');	// our array delimiter
			config.setFileName(configFile); 
			config.load();
			
			Iterator<String> keys = config.getKeys();
			
			while(keys.hasNext()) {
				key = keys.next();
				configHash.put(key, (String)config.getProperty(key));
			}
			
		} catch (ConfigurationException e) {
			logger.error("ConfigurationException when trying to initialize configuration HashMap");
			logger.error(ExceptionUtils.getStackTrace(e));			
		} 
	}
}
