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

package ke.co.tawi.babblesms.server.servlet.accountmngmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ke.co.tawi.babblesms.server.persistence.utils.networkcount;


/**
 * servlet implementation .
 * <p>
 *  
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 */

@SuppressWarnings("serial")
public class Netcount  extends HttpServlet {
	
	
     static HashMap<String, String> Hashnet;
	
     @Override
 	protected void doPost(HttpServletRequest request, HttpServletResponse response)
 			throws ServletException, IOException {
    		
		
		String uuid = request.getParameter("uuid");
    		PrintWriter out=response.getWriter();
    		out.println("<h1> "+uuid+" </h1>");
		System.out.println(" On the test by Migwi "+uuid);
		out.println("<h1> "+uuid+" </h1>");
		networkcount countnet = new networkcount();
		
		Hashnet= countnet.network(uuid);
		
		for(@SuppressWarnings("rawtypes")
		Map.Entry nets:Hashnet.entrySet()){
			System.out.println("Migwi Testing  "+nets.getKey()+"   "+nets.getValue());
		}
  }
  public HashMap<String,String> Networklist(){
	  return Hashnet;
  }

}