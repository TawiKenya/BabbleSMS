
package ke.co.tawi.babblesms.server.servlet.admin.notification;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.notification.Notification;
import ke.co.tawi.babblesms.server.beans.notification.NotificationStatus;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.notification.NotificationDAO;
import ke.co.tawi.babblesms.server.persistence.notification.NotificationStatusDAO;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

/**
 * Servlet used to add/edit and delete notifications.
 * <p>
 * Copyright (c) Tawi Ltd., July 7,2014
 *
 * @author <a href="mailto:josephk@tawi.mobi">Joseph Kimani</a>
 */
@WebServlet(name = "addnotification",
        urlPatterns = {"/addnotification", "/editnotification", "/deletenotification"})
public class Addnotification extends HttpServlet {

    final String ERROR_NO_ORIGIN = "Please provide a Origin.";
    final String ERROR_NO_ACCOUNT = "Please provide account.";
    final String ERROR_NO_SHORTDESC = "Please provide short description.";
    final String ERROR_NO_LONGDESC = "Please provide long description.";

    private String origin, account, shortdesc, longdesc;

    // This is used to store parameter names and values from the form.
    private HashMap<String, String> paramHash;

    private NotificationDAO notificationDAO;

    private NotificationStatusDAO notificationStatusDAO;

    private CacheManager cacheManager;
    private HttpSession session;

    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        notificationDAO = NotificationDAO.getInstance();
        notificationStatusDAO = NotificationStatusDAO.getInstance();

        cacheManager = CacheManager.getInstance();
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();
        HttpSession session = request.getSession(false);

        // if add account is called
        if (userPath.equals("/addnotification")) {
            setClassParameters(request);

            initParamHash();
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_PARAMETERS, paramHash);

            // No Origin provided
            if (StringUtils.isBlank(origin)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_NO_ORIGIN);

                // No Shortdesc provided      
            } else if (StringUtils.isBlank(shortdesc)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_NO_SHORTDESC);

                // No Longdesc provided
            } else if (StringUtils.isBlank(longdesc)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_NO_LONGDESC);

            } else {
                // If we get this far then all parameter checks are ok.         
                session.setAttribute(SessionConstants.ADMIN_ADD_NOTIFICATION_SUCCESS_KEY, "s");

                // Reduce our session data
                session.setAttribute(SessionConstants.ADMIN_ADD_NOTIFICATION_PARAMETERS, null);
                session.setAttribute(SessionConstants.ADMIN_ADD_NOTIFICATION_ERROR_KEY, null);

                addNotification();
                session.setAttribute(SessionConstants.ADMIN_ADD_SUCCESS, "Notification created successfully.");
            }
            //redirect according to Origin
            response.sendRedirect("admin/" + origin.toLowerCase() + "notices.jsp");
        } // if edit account is called
        else if (userPath.equals("/editnotification")) {

            Notification notification = new Notification();
            
            //notification.setAccountuuid(StringUtils.trimToEmpty(request.getParameter("accuuid")));
            notification.setLongDesc(StringUtils.trimToEmpty(request.getParameter("longdesc")));
            notification.setShortDesc(StringUtils.trimToEmpty(request.getParameter("shortdesc")));
            notification.setOrigin(StringUtils.trimToEmpty(request.getParameter("origin")));
            notification.setPublished(StringUtils.trimToEmpty(request.getParameter("publish")));
            notification.setUuid(StringUtils.trimToEmpty(request.getParameter("notifuuid")));
            if (notificationDAO.updateNotification(notification)) {
                session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "notification updated successfully.");
            } else {
                session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "notification update failed.");
            }

            response.sendRedirect("admin/" + request.getParameter("origin").toLowerCase() + "notices.jsp");

        } // if delete account is called
        else if (userPath.equals("/deletenotification")) {

            String notifuuid = StringUtils.trimToEmpty(request.getParameter("notifuuid"));
            String origin = StringUtils.trimToEmpty(request.getParameter("origin"));
             //delete notification status first
             notificationStatusDAO.deleteNotificationStatus(notifuuid);
             
            if (notificationDAO.deleteNotification(notifuuid)) {
                session.setAttribute(SessionConstants.ADMIN_DELETE_SUCCESS, "notification deleted successfully.");
            } else {
                session.setAttribute(SessionConstants.ADMIN_DELETE_ERROR, "notification deletion failed.");
            }

            response.sendRedirect("admin/" + origin.toLowerCase() + "notices.jsp");
        }

    }

    /**
     *
     */
    private void addNotification() {
        Notification notif = new Notification();

        //notif.setAccountuuid(account);
        notif.setLongDesc(longdesc);
        notif.setOrigin(origin);
        notif.setShortDesc(shortdesc);
        String uuid=notif.getUuid();
        notif.setUuid(uuid);

        notificationDAO.putNotification(notif);
        
        NotificationStatus notifstatus=new NotificationStatus();
        
        notifstatus.setNotificationUuid(uuid);
        notificationStatusDAO.putNotificationStatus(notifstatus);
        
       }

    
    /**
     * Set the class variables that represent form parameters.
     *
     * @param request
     */
    private void setClassParameters(HttpServletRequest request) {
        origin = StringUtils.trimToEmpty(request.getParameter("origin"));
        shortdesc = StringUtils.trimToEmpty(request.getParameter("shortdesc"));
        longdesc = StringUtils.trimToEmpty(request.getParameter("longdesc"));
        account = StringUtils.trimToEmpty(request.getParameter("user"));

    }

    /**
     * Place all the received parameters in our class HashMap.
     *
     */
    private void initParamHash() {
        paramHash = new HashMap<>();

        paramHash.put("origin", origin);
        paramHash.put("shortdesc", shortdesc);
        paramHash.put("longdesc", longdesc);
        paramHash.put("account", account);

    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
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
