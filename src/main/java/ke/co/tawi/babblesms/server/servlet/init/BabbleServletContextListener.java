/**
 * Copyright 2015 Tawi Commercial Services Ltd
 * 
 * Licensed under the Open Software License, Version 3.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a copy
 * of the License at:
 * http://opensource.org/licenses/OSL-3.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package ke.co.tawi.babblesms.server.servlet.init;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ke.co.tawi.babblesms.server.persistence.HibernateUtil;

import org.hibernate.SessionFactory;

/**
 * Initialize certain activities as the webapp starts, and shut them down on 
 * exit.
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
@WebListener
public class BabbleServletContextListener implements ServletContextListener {

		
	/**
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Have started listening to context.");
		
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		
		Map properties = sessionFactory.getProperties();
		Iterator iter = properties.keySet().iterator();
		
		String key;
		
		while(iter.hasNext()) {
			key = (String)iter.next();
			System.out.println("Key is '" + key + ", value is '" + properties.get(key) + "'");
		}
	}

	
	
	/**
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Have stopped listening to context.");

	}
}

