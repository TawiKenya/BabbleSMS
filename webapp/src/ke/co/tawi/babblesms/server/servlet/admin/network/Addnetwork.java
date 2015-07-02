package ke.co.tawi.babblesms.server.servlet.admin.network;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.network.NetworkDAO;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;

/**
 * Servlet used to add/edit and delete networks.
 * <p>
 * Copyright (c) Tawi Ltd., July 7,2014
 *
 * @author <a href="mailto:josephk@tawi.mobi">Joseph Kimani</a>
 */
@WebServlet(name = "addnetwork",
        urlPatterns = {"/addnetwork", "/editnetwork", "/deletenetwork"})
public class Addnetwork extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3689148443770189970L;
	final String ERROR_NO_NETWORKNAME = "Please provide a Network Name.";
    final String ERROR_NETWORKNAME_EXISTS = "The Network Name provided already exists in the system.";
    final String ERROR_NO_COUNTRYNAME = "Please select Country.";

    private String networkname;
    private String countryuuid;
    private Cache networkCache;
    // This is used to store parameter names and values from the form.
    private HashMap<String, String> paramHash;
    //private EmailValidator emailValidator;

    private NetworkDAO networkDAO;

    private CacheManager cacheManager;
    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        CacheManager mgr = CacheManager.getInstance();
        networkCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID); 
        networkDAO = NetworkDAO.getInstance();

        cacheManager = CacheManager.getInstance();
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userPath = request.getServletPath();
        HttpSession session = request.getSession(false);

        // if add network is called
        if (userPath.equals("/addnetwork")) {
            setClassParameters(request);

            initParamHash();
            session.setAttribute(SessionConstants.ADMIN_ADD_NETWORK_PARAMETERS, paramHash);

            // No Network Name provided
            if (StringUtils.isBlank(networkname)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_NETWORK_ERROR_KEY, ERROR_NO_NETWORKNAME);

                // No country Name provided
            } else if (StringUtils.isBlank(countryuuid)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_NETWORK_ERROR_KEY, ERROR_NO_COUNTRYNAME);

                // The Country Name already exists in the system 
            } else if (existsNetworkName(networkname)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_NETWORK_ERROR_KEY, ERROR_NETWORKNAME_EXISTS);

            } else {
                // If we get this far then all parameter checks are ok.         
                session.setAttribute(SessionConstants.ADMIN_ADD_NETWORK_SUCCESS_KEY, "s");

                // Reduce our session data
                session.setAttribute(SessionConstants.ADMIN_ADD_NETWORK_PARAMETERS, null);
                session.setAttribute(SessionConstants.ADMIN_ADD_NETWORK_ERROR_KEY, null);

                addNetwork();
                session.setAttribute(SessionConstants.ADMIN_ADD_SUCCESS, "Network created successfully.");
            }

            response.sendRedirect("admin/network.jsp");

        } // if edit network is called
        else if (userPath.equals("/editnetwork")) {             
            String networkuuid = request.getParameter("networkuuid");
            String networkname = request.getParameter("networkname");
            Network net = new Network();
            net.setUuid(networkuuid);
            net.setName(networkname); 
            if (networkDAO.updateNetwork(networkuuid, networkname)) {
                session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Network updated successfully.");
                networkCache.put(new Element(net.getUuid(), net));   
            } else {
                session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Network update failed.");
            }

            response.sendRedirect("admin/network.jsp");

        } // if delete network is called
        else if (userPath.equals("/deletenetwork")) {

            String networkuuid = request.getParameter("networkuuid");

            if (networkDAO.deleteNetwork(networkuuid)) {
                session.setAttribute(SessionConstants.ADMIN_DELETE_SUCCESS, "Network deleted successfully.");
            } else {
                session.setAttribute(SessionConstants.ADMIN_DELETE_ERROR, "Network deletion failed.");
            }
        
        response.sendRedirect("admin/network.jsp");
     
    }
}




/**
 *
 */ 
private void addNetwork() {
        Network n = new Network();

        n.setName(networkname);
        n.setCountryuuid(countryuuid);

        networkDAO.putNetwork(n);

        n = networkDAO.getNetworkByName(networkname);   // Ensures the network is populated with the correct ID
        updateNetworkCache(n);
    }

    /**
     *
     * @param acc
     */
    private void updateNetworkCache(Network net) {
        //cacheManager.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME).put(new Element(acc.getEmail(), acc));
        cacheManager.getCache(CacheVariables.CACHE_NETWORK_BY_UUID).put(new Element(net.getUuid(), net));
    }

    
    
    /**
     * Set the class variables that represent form parameters.
     *
     * @param request
     */
    private void setClassParameters(HttpServletRequest request) {
        networkname = StringUtils.trimToEmpty(request.getParameter("network"));
        countryuuid = StringUtils.trimToEmpty(request.getParameter("countryuuid"));
    
    }

    /**
     * Place all the received parameters in our class HashMap.
     *
     */
    private void initParamHash() {
        paramHash = new HashMap<>();

        paramHash.put("networkname", networkname);
        paramHash.put("countryuuid", countryuuid);

    }

    /**
     *
     * @param name
     * @return whether or network name the unique name exists in the system
     */
    private boolean existsNetworkName(final String name) {
        boolean exists = false;
       System.out.println(name); 
        if (networkDAO.getNetworkByName(name) != null) {
            exists = true;
        }

        return exists;
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
}

/*
 ** Local Variables:
 **   mode: java
 **   c-basic-offset: 2
 **   tab-width: 2
 **   indent-tabs-mode: nil
 ** End:
 **
 ** ex: set softtabstop=2 tabstop=2 expandtab:
 **
 */
