/**
 * 
 */
package ke.co.tawi.babblesms.test.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.creditmgmt.SMSBalance;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO;
import ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO;
import ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig;
import ke.co.tawi.babblesms.server.utils.net.EmailUtil;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author peter
 *
 */
public class QuartzJob implements Job {
	private SmsBalanceDAO balanceDAO;
	private AccountDAO accountDAO;
	
	List<SMSBalance> balList = new ArrayList<>();
	List<Account> accountList = new ArrayList<>();
	HashMap<String,Integer> balanceHash = new HashMap<String,Integer>(); 
	
	int balance =0; 
	Account account; 
	String email ="" ;  
	String usernane ="";
	/**
	 * 
	 */
	public QuartzJob() {
		balanceDAO = SmsBalanceDAO.getInstance();
	    accountDAO = AccountDAO.getInstance();
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("###BabbleSMS Quartz Running @TestByPeter###");
		 sendMail();
    }

	private void sendMail() {
		balList = balanceDAO.getAllBalances();
		accountList = accountDAO.getAllAccounts();
		 
		if(balList !=null && accountList !=null ){
			
		
		
		for(SMSBalance bb : balList){
			balanceHash.put(bb.getAccountUuid(), bb.getCount());
		}
			for(Account a : accountList) {
				 try{	 
					    usernane = a.getUsername();
			        	balance = balanceHash.get(a.getUuid()); 
			            String body = "Hello "+usernane+ " your Balance is:"+balance;
			            email = a.getEmail();
			          

					EmailUtil util = new EmailUtil(
							PropertiesConfig.getConfigValue("EMAIL_DEFAULT_EMAIL_FROM"), // from
							email, // to 
							"Credit Report", // subject, 
							body, 
							PropertiesConfig.getConfigValue("EMAIL_OUTGOING_SMTP"), // outgoing SMTP host
						 	Integer.parseInt(PropertiesConfig.getConfigValue("EMAIL_OUTGOING_SMTP_PORT")), // outgoing SMTP port
						 	PropertiesConfig.getConfigValue("EMAIL_OUTGOING_SMTP_USERNAME"), // outgoing SMTP username 
						 	PropertiesConfig.getConfigValue("EMAIL_OUTGOING_SMTP_PASSWORD") // outgoing SMTP password
							);
					util.start();
					 
					 
				 }catch(Exception e){
					 e.printStackTrace();
					 
				 }
			 }
		}
		
	}//end private void sendMail() {
	
}
