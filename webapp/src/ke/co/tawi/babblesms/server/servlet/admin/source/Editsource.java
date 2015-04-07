package ke.co.tawi.babblesms.server.servlet.admin.source;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountsDAO;
import ke.co.tawi.babblesms.server.persistence.items.maskcode.MaskDAO;
import ke.co.tawi.babblesms.server.persistence.items.maskcode.ShortcodeDAO;
import net.sf.ehcache.CacheManager;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Servlet used to edit source.
 * <p>
 * Copyright (c) Tawi Ltd., July 7,2014
 *
 * @author <a href="mailto:josephk@tawi.mobi">Joseph Kimani</a>
 */
@WebServlet(name = "editsource",
        urlPatterns = {"/editsource", "/deletesource"})
public class Editsource extends HttpServlet {

    private CacheManager cacheManager;
    private HttpSession session;
    private ShortcodeDAO shortcodeDAO;
    private MaskDAO maskDAO;

    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        maskDAO = MaskDAO.getInstance();
        shortcodeDAO = ShortcodeDAO.getInstance();
        cacheManager = CacheManager.getInstance();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
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

        // if edit source is called
        if (userPath.equals("/editsource")) {

            String accountuuid = request.getParameter("accuuid");
            String source = request.getParameter("source");
            String networkuuid = request.getParameter("networkuuid");
            String sourceuuid = request.getParameter("sourceuuid");

            if (maskDAO.getMask(sourceuuid) != null) {

                Mask mask = new Mask();
                mask.setAccountuuid(accountuuid);
                mask.setMaskname(source);
                mask.setNetworkuuid(networkuuid);
                mask.setUuid(sourceuuid);

                if (maskDAO.updateMask(mask)) {
                    session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Mask updated successfully.");
                } else {
                    session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Mask update failed.");

                }
              } else if (shortcodeDAO.getShortcode(sourceuuid) != null) {

                Shortcode shortcode = new Shortcode();
                shortcode.setAccountuuid(accountuuid);
                shortcode.setCodenumber(source);
                shortcode.setNetworkuuid(networkuuid);
                shortcode.setUuid(sourceuuid);

                if (shortcodeDAO.updateShortcode(shortcode)) {
                    session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Shortcode updated successfully.");
                } else {
                    session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Shortcode update failed.");

                }
            }

            response.sendRedirect("admin/source.jsp");

        } // if delete account is called
        else if (userPath.equals("/deletesource")) {

            String sourceuuid = request.getParameter("sourceuuid");

            if (maskDAO.getMask(sourceuuid) != null) {
                if (maskDAO.deleteMask(sourceuuid)) {

                    session.setAttribute(SessionConstants.ADMIN_DELETE_SUCCESS, "Mask deleted successfully.");
                } else {
                    session.setAttribute(SessionConstants.ADMIN_DELETE_ERROR, "Mask deletion failed.");

                }
            } else if (shortcodeDAO.getShortcode(sourceuuid) != null) {
                if (shortcodeDAO.deleteShortcode(sourceuuid)) {

                    session.setAttribute(SessionConstants.ADMIN_DELETE_SUCCESS, "Shortcode deleted successfully.");
                } else {
                    session.setAttribute(SessionConstants.ADMIN_DELETE_ERROR, "Shortcode deletion failed.");

                }
            }
            response.sendRedirect("admin/source.jsp");
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
