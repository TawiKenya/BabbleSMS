/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ke.co.tawi.babblesms.server.servlet.group;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import ke.co.tawi.babblesms.server.beans.contact.ContactGroup;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
/**
 *
 * @author josephk
 */
@WebServlet(name = "Group", urlPatterns = {"/Group"})
public class Group extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        
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
        processRequest(request, response);
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
        processRequest(request, response);
        
        String gname=request.getParameter("gname");
        String gdesc=request.getParameter("gdesc");
        int accesslevel=Integer.parseInt(request.getParameter("access"));
        
            
                
       /* ContactGroup contbean=new ContactGroup();
        
        contbean.setGroupname(gname);
        contbean.setGroupcode(gdesc);
        contbean.setAccesslevel(accesslevel);
        
        ContactGroupDAO contDA0=ContactGroupDAO.getInstance();
        
        Boolean success=false;
        success=contDA0.putContactGroup(contbean);
        
        if(success==true){
            response.sendRedirect("account/groups.jsp");
        }
       */
        
        
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
