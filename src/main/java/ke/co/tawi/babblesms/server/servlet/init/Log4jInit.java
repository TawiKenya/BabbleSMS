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
package ke.co.tawi.babblesms.server.servlet.init;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Initializes the log4j configuration file.
 * <p>
 * Ensure that corresponding entry is set in the web.xml file. 
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */

public class Log4jInit extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
    private final Logger logger = Logger.getLogger(this.getClass());
    
    
    /**
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        initConfig();
        logger.info("Have initialized log4j");
    }

    
    /**
	 * 
	 * @param request
      * @param response
      * @throws ServletException
      * @throws IOException  
	 */	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	initConfig();
    	
    	PrintWriter out = response.getWriter();
    	out.println("Have reloaded log4j settings.");
    	out.close();
    }
    
    
    /**
	 * 
	 * @param request
	 * @param response
      * @throws ServletException
      * @throws IOException  
	 */	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	doPost(request, response);
    }
    
    
    /**
     * 
     */
    private void initConfig() {
    	String prefix = getServletContext().getRealPath("/");
        String file = getServletConfig().getInitParameter("log4j-init-file");

        // if the log4j-init-file is not set, then no point in trying
        if (file != null) {
            PropertyConfigurator.configure(prefix + file);
        }
    }

}
