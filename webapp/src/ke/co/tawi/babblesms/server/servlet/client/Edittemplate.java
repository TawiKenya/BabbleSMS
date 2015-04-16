package ke.co.tawi.babblesms.server.servlet.client;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ke.co.tawi.babblesms.server.beans.messagetemplate.MessageTemplate;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.items.messageTemplate.MessageTemplateDAO;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

/**
 * Receives form values from messagetemplate.jsp section and adds a new
 * {@link Addmessagetemplate} to the database.
 *
 * <p>
 * Copyright (c) Tawi Ltd., Sept 26, 2014
 *
 * @author <a href="mailto:josephk@tawi.mobi">Joseph Kimani</a>
 * @version %I%, %G%
 */
@WebServlet(name = "Edittemplate",
        urlPatterns = {"/Edittemplate", "/deletetemplate", "/addmsgtemplate"})
public class Edittemplate extends HttpServlet {

    private final CacheManager mgr = CacheManager.getInstance();
    private final MessageTemplateDAO msgtempDAO = MessageTemplateDAO.getInstance();
    private final String UPDATE_SUCCESS = "Template updated successfully.";
    private final String DELETE_SUCCESS = "Template deleted successfully.";
    private final String UPDATE_ERROR = "Template update FAILED.";
    private final String DELETE_ERROR = "Template deletion failed.";
    private final String ADD_SUCCESS = "SMS template created successfully.";
    private final String ADD_ERROR = "Addition failed.";
    private final String ERROR_NO_TITLE = "Title was not supplied";
    private final String ERROR_NO_CONTENT = "Template content was not supplied.";
    private final String ERROR_TITLE_EXISTS = "Template title already exists.";

    MessageTemplate msgtemp;

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        //if add message template is accessed.
        if (userPath.equals("/addmsgtemplate")) {

            String msgtitle = request.getParameter("title");
            String msgcontents = request.getParameter("content");
            String accuuid = request.getParameter("accountuuid");

            //No message title
            if (StringUtils.isBlank(msgtitle)) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_NO_TITLE);
             //No message contents provided

            } else if (StringUtils.isBlank(msgcontents)) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_NO_CONTENT);

                //Message title already exists in the system
            } else if (existstitle(msgtitle)) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_TITLE_EXISTS);

            } else {
                // If we get this far then all parameter checks are ok. 
                msgtemp = new MessageTemplate();
                msgtemp.setTitle(msgtitle);
                msgtemp.setContents(msgcontents);
                msgtemp.setAccountuuid(accuuid);

                if (msgtempDAO.putMessageTemplate(msgtemp)) {
                    session.setAttribute(SessionConstants.ADD_SUCCESS, ADD_SUCCESS);
                    //get correct id to upadate cache
                    msgtemp = msgtempDAO.titleexists(msgtitle);
                    mgr.getCache(CacheVariables.CACHE_MESSAGE_TEMPLATE_BY_UUID).put(new Element(msgtemp.getUuid(), msgtemp));
                } else {
                    session.setAttribute(SessionConstants.ADD_ERROR, ADD_ERROR);
                }
            }
            response.sendRedirect("account/messagetemplate.jsp");
        } // if delete account is called
        else if (userPath.equals("/Edittemplate")) {

            String msgtitle = request.getParameter("title");
            String msgcontents = request.getParameter("content");
            String templateuuid = request.getParameter("templateuuid");
            System.out.println(templateuuid);
            if (msgtempDAO.updateMessageTemplate(templateuuid, msgtitle, msgcontents)) {
                //mgr.getCache(CacheVariables.CACHE_MESSAGE_TEMPLATE_BY_UUID).put(new Element(templateuuid, msgtemp)); 
                session.setAttribute(SessionConstants.ADD_SUCCESS, UPDATE_SUCCESS);

            } else {
                session.setAttribute(SessionConstants.ADD_ERROR, UPDATE_ERROR);
            }
            //mgr.getCache(CacheVariables.CACHE_MESSAGE_TEMPLATE_BY_UUID).put(new Element(msgtemp.getUuid(), msgtemp)); 
            response.sendRedirect("account/messagetemplate.jsp");

        } // if delete template is called
        else if (userPath.equals("/deletetemplate")) {
            String templateuuid = request.getParameter("templateuuid");
            if (msgtempDAO.deleteMessageTemplate(templateuuid)) {

                session.setAttribute(SessionConstants.ADD_SUCCESS, DELETE_SUCCESS);
            } else {
                session.setAttribute(SessionConstants.ADD_ERROR, DELETE_ERROR);
            }

            //mgr.getCache(CacheVariables.CACHE_MESSAGE_TEMPLATE_BY_UUID).dispose();
            response.sendRedirect("account/messagetemplate.jsp");

        }
    }

    /**
     * Checks if the email supplied already exists in email database
     *
     * @param email
     * @return
     */
    private boolean existstitle(final String title) {
        boolean exists = false;

        if (msgtempDAO.titleexists(title) != null) {
            exists = true;
        }

        return exists;
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
