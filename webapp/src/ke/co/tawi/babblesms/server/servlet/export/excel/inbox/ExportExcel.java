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
package ke.co.tawi.babblesms.server.servlet.export.excel.inbox;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.log.IncomingLog;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO;
import ke.co.tawi.babblesms.server.persistence.utils.CountUtils;
import ke.co.tawi.babblesms.server.session.SessionConstants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Allows the client to export a list of Topup activity to a Microsoft Excel
 * sheet.
 * <p>
 * For a list of HTTP header fields, see
 * <a href="http://en.wikipedia.org/wiki/List_of_HTTP_header_fields">
 * http://en.wikipedia.org/wiki/List_of_HTTP_header_fields}
 * </a>
 * <p>
 * For a list of Microsoft Office MIME types, see
 * <a href="http://bit.ly/aZQzzH">http://bit.ly/aZQzzH</a>
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ExportExcel extends HttpServlet {
    private final int PAGE_SIZE=15;
    private final String SPREADSHEET_NAME = "Inbox Export.xlsx";
    private static final long serialVersionUID = 3896751907947782599L;
    private static int pageno;

    private Cache accountsCache, networksCache;

    // This is a mapping between the UUIDs of networks and their names
    private HashMap<String, String> networkHash;
    private Account account;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timezoneFormatter;
    private PhoneDAO phnDAO;
    private ContactDAO ctDAO;
    private ServletOutputStream out;
    
    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);       
        
        dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        timezoneFormatter = new SimpleDateFormat("z");
        
         phnDAO = PhoneDAO.getInstance();
         ctDAO = ContactDAO.getInstance();
         

        CacheManager mgr = CacheManager.getInstance();
        accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
        networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
              
        networkHash = new HashMap<>();
       
        List<?> keys = networksCache.getKeys();
        Element element;
        Network network;

        for (Object key : keys) {
            element = networksCache.get(key);
            network = (Network) element.getObjectValue();
            networkHash.put(network.getUuid(), network.getName());
        }

    }

    /**    
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     * @throws java.io.IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {

        out = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Cache-Control", "cache, must-revalidate");
        response.setHeader("Pragma", "public");

        HttpSession session = request.getSession(false); 
        
        int CurrentPage;
        List<IncomingLog> clog = new ArrayList<>();
        
        String dd = request.getParameter("ExportPageExcel");         
        String AllPages = request.getParameter("ExportAllExcel"); 
        
                 if(dd==null){  CurrentPage=1;     	}            
                 else{  CurrentPage = Integer.parseInt(dd);    }
           
                pageno = (CurrentPage-1)*PAGE_SIZE;
               
        String sessionEmail = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
        Element element = accountsCache.get(sessionEmail);
        account = (Account) element.getObjectValue();
        
        String fileName = new StringBuffer(account.getName()).append(" ")
                .append(StringUtils.trimToEmpty(account.getUsername()))
                .append(" ")
                .append(SPREADSHEET_NAME)
                .toString();

        response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
        
        clog =getListDetails(CurrentPage,AllPages);
         createExcelSheets(clog);
       }
    
    
    
    /**
     * Returns MS Excel file of the data specified for exporting.
     * @param List<IncomingLog>
     * Method create excelSheets and sends them
     ****/    
    public void createExcelSheets(List<IncomingLog>InLog) throws IOException{    	
    	 List<Phone> phoneList;
    	 //String cont = null;
    	
        XSSFWorkbook xf = new XSSFWorkbook();
        XSSFCreationHelper ch =xf.getCreationHelper();
      
        XSSFSheet s =xf.createSheet();
        //create the first row
        XSSFRow r1 = s.createRow(0);
  	            XSSFCell c11 = r1.createCell(0);
  	                  c11.setCellValue(ch.createRichTextString("*")); 
	            XSSFCell c12 = r1.createCell(1);
	                  c12.setCellValue(ch.createRichTextString("Message"));
	            XSSFCell c13 = r1.createCell(2);
	                  c13.setCellValue(ch.createRichTextString("Source"));
	            XSSFCell c14 = r1.createCell(3);
	                  c14.setCellValue(ch.createRichTextString("Destination"));
	            XSSFCell c15 = r1.createCell(4);
	                  c15.setCellValue(ch.createRichTextString("Network"));
	            XSSFCell c16 = r1.createCell(5);
	                  c16.setCellValue(ch.createRichTextString("Time ("+timezoneFormatter.format(new Date())+") Time Zone"));
	            XSSFCell c17 = r1.createCell(6);
	                  c17.setCellValue(ch.createRichTextString("Message Id"));
        
        
        int i=1;
        //create other rows
          for(IncomingLog log:InLog){ 
        	  phoneList=phnDAO.getPhones(log.getOrigin() );
        	          	  
        	  XSSFRow r = s.createRow(i);
        	  //row number
        	  XSSFCell c1 = r.createCell(0);
        	      c1.setCellValue(i+pageno);
        	      
        	    //get message  
        	  XSSFCell c2 = r.createCell(1);        	
        	      c2.setCellValue(ch.createRichTextString(log.getMessage()));
        	      
        	 //get phone numbers
        	      XSSFCell c3 = r.createCell(2);
        	      if(phoneList.size()>0){
        	         for(Phone phone:phoneList){ 
        		 Contact contacts = ctDAO.getContact(phone.getContactUuid());      	                     
        			  c3.setCellValue(ch.createRichTextString(contacts.getName()));  }   
        			       }
        		  else{ 
        			  c3.setCellValue(ch.createRichTextString(log.getOrigin()));     }        		   	     
        	     
        	      
        	   //get destination   
        	  XSSFCell c4 = r.createCell(3);
        	      c4.setCellValue(ch.createRichTextString(log.getDestination()));
        	      
        	  //get network name    
        	   XSSFCell c5 = r.createCell(4);
        	      c5.setCellValue(ch.createRichTextString(networkHash.get(log.getNetworkUuid()))); 
        	   
        	      //get date 
        	  XSSFCell c6 = r.createCell(5);         	  
        	     c6.setCellValue(ch.createRichTextString(""+dateFormatter.format(log.getLogTime())));
        	              	      
        	      //get message id
        	  XSSFCell c7 = r.createCell(6); 
        	       c7.setCellValue(ch.createRichTextString(log.getUuid())); 
        	  i++;
        	         	  
          }
          xf.write(out);
          out.flush();          
          out.close();
    }
    
    
    
    
    /**
    *
    * @param CurrentPage to be converted to excel
    * @param All, String to show that all the pages should be converted to excel 
    * @return a list incommingLog details   
    */
    public List<IncomingLog> getListDetails(int CurrentPage, String All){
    	CountUtils countutils = CountUtils.getInstance();
    	int totalcount=countutils.getIncomingCount(account.getUuid());
    	
    	IncomingLogDAO DAO = IncomingLogDAO.getInstance();    	
    	List<IncomingLog> collection = new ArrayList<>();
    	
    	//assigned if all pages should be retrieved
    	if(All!=null){ 
    	    collection = DAO.getIncomingLog(account, (CurrentPage-1)*PAGE_SIZE, totalcount); 
    	         }
    	//assigned if a given specific page should retrieved (Only one retrieved)
    	else{
    		collection = DAO.getIncomingLog(account, (CurrentPage-1)*PAGE_SIZE, CurrentPage*PAGE_SIZE);
    	     	}
    	return collection;
    }

    
    
    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     * @throws java.io.IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
   
}


