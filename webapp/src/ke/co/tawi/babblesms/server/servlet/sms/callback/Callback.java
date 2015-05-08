package ke.co.tawi.babblesms.server.servlet.sms.callback;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ke.co.tawi.babblesms.server.beans.log.IncomingLog;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.items.maskcode.ShortcodeDAO;
import ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO;
import ke.co.tawi.babblesms.server.persistence.network.NetworkDAO;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * a Servlet that recieves incoming SMS and insert into db
 *
 * @author Email:<josephk@tawi.mobi>Joseph Kimani
 */
public class Callback extends HttpServlet {
    
    private CacheManager cacheManager;

    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    
    /**
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        doPost(request, response);
    }

    /**
     
     * @param request servlet request
     * @param response servlet response
     
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        //get parameter values
        if (request.getParameter("callbackType") != null) {
            //if callbacktype is incomingSMS proceed
            if (request.getParameter("callbackType").equals("incomingSms")) {
                
                if (request.getParameter("destination") != null && request.getParameter("source") != null && request.getParameter("message") != null && request.getParameter("messageId") != null && request.getParameter("network") != null && request.getParameter("datetime") != null) {
                    IncomingLogDAO incomingLogDAO = IncomingLogDAO.getInstance();
                    IncomingLog incomingLog = new IncomingLog();
                    
                    //get network uuid
                    NetworkDAO networkDAO=NetworkDAO.getInstance();
                    Network network=new Network();
                    String [] networkname=request.getParameter("network").split(" ");
                    
                    network=networkDAO.getNetworkByName(networkname[0]);
                    //get source uuid
                    ShortcodeDAO shortcodeDAO = ShortcodeDAO.getInstance();
                    Shortcode shortcode = new Shortcode();                    
                    shortcode=shortcodeDAO.getShortcodeBycodeNumber(request.getParameter("destination"),network.getUuid());
                   
                    incomingLog.setMessage(request.getParameter("message"));
                    incomingLog.setOrigin(request.getParameter("source"));
                    incomingLog.setDestination(shortcode.getUuid());
                    
                    
                    //add incoming SMS.
                    incomingLogDAO.putIncomingLog(incomingLog);
                    
                   }
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
