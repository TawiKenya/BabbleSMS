package ke.co.tawi.babblesms.server.threads.SendSMS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static java.lang.System.out;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountBalanceDAO;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountsDAO;
import ke.co.tawi.babblesms.server.persistence.items.credit.CreditDAO;
import ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO;
import ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 *
 * @author Email:<mecmurimi@gmail.com>Erick Murimi
 */
public class SendSMS extends Thread {

    private final OutgoingLogDAO outgoingLogDAO;
    private final OutgoingLog outgoingLog;
    private final AccountsDAO accountDAO;
    private final Account account;
    private CacheManager cacheManager;

    /**
     * @param outgoingLog
     */
    public SendSMS(OutgoingLog outgoingLog, Account account) {
        super();
        this.outgoingLog = outgoingLog;
        this.account = account;
        outgoingLogDAO = OutgoingLogDAO.getInstance();
        accountDAO = AccountsDAO.getInstance();
        cacheManager = CacheManager.getInstance();

    }

    @Override
    public void run() {
        CacheManager mgr = CacheManager.getInstance();
        Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
        HashMap<String, String> networkHash = new HashMap<String, String>();
        Element element;
        List keys;
        Network network;
        keys = networksCache.getKeys();
        for (Object key : keys) {
            element = networksCache.get(key);
            network = (Network) element.getObjectValue();
            networkHash.put(network.getUuid(), network.getName());
        }

        String Apiusername = account.getApiusername();
        String Apipassword = account.getApipassword();

        
        Boolean success = true;

        success = outgoingLogDAO.putOutgoingLog(outgoingLog);
        //update cache with correct values.
        //OutgoingLog outgoinglog=new OutgoingLog();
        //outgoinglog=outgoingLogDAO.getOutgoingLoglast();
        //mgr.getCache(CacheVariables.).put(new Element(outgoingLog.getUuid(), outgoingLog));

        //HttpSession session=session.getSession(false);
        /**
         * *********************************************
         */
        // Code to send an SMS through SMSGW start
        /**
         * *********************************************
         */
        String apiUrl = PropertiesConfig.getConfigValue("API_URL");
        String apiaccount = PropertiesConfig.getConfigValue("API_ACCOUNT");
        String status = networkHash.get(outgoingLog.getNetworkuuid());
        String[] arraynetwork = networkHash.get(outgoingLog.getNetworkuuid()).split(" ");
        String networkfinal = arraynetwork[0];
        
        try {
            String response = getResponse(apiUrl + "?"
                    + "username=" + URLEncoder.encode(Apiusername, "UTF-8")
                    + "&password=" + URLEncoder.encode(Apipassword, "UTF-8")
                    + "&account=" + apiaccount
                    + "&source=" + outgoingLog.getOrigin()
                    + "&destination=" + outgoingLog.getDestination()
                    + "&message=" + URLEncoder.encode(outgoingLog.getMessage(), "UTF-8")
                    + "&network=" + networkfinal
            );
            System.out.println(
                    "Response for a sendsms request: "
                    + response);
            String[] explode = response.split(":");
            String GWresponse = explode[1].substring(0, explode[1].length() - 1);

            int res = Integer.parseInt(GWresponse);

            if (res == 0) {
                AccountBalanceDAO AccBalDAO = AccountBalanceDAO.getInstance();
                AccBalDAO.deductCredit(outgoingLog.getOrigin(),"B936DA83-8A45-E9F0-2EAE-D75F5C232E78",1);

            }

        } catch (UnsupportedEncodingException e) {
            System.err.println("UnsupportedEncodingException while trying to send SMS.");
        }

        //cacheManager.getCache(CacheVariables.CACHE_NETWORK_BY_UUID).put(new Element(outgoingLog.getUuid(), outgoingLog));
    }

    /**
     *
     * @param urlStr
     * @return String
     */
    private static String getResponse(String urlStr) {
        URLConnection conn;
        URL url;
        BufferedReader reader;
        String response = "";

        try {
            url = new URL(urlStr);
            conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();

            reader.close();

        } catch (MalformedURLException e) {
            System.err.println("MalformedURLException exception");
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("IOException exception");
            e.printStackTrace();
        }

        return response;
    }

}
