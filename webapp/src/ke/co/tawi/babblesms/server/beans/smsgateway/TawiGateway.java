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
package ke.co.tawi.babblesms.server.beans.smsgateway;

import ke.co.tawi.babblesms.server.beans.StorableBean;

/**
 * Connectivity details for the Tawi SMS Gateway.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TawiGateway extends StorableBean {

	private String url;
	private String username, passwd;
	private String accountUuid;
	
	
	/**
	 * 
	 */
	public TawiGateway() {
		super();
		
		url = "";
		username = "";
		passwd = "";
		accountUuid = "";
	}



	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}



	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}



	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}



	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}



	/**
	 * @return the passwd
	 */
	public String getPasswd() {
		return passwd;
	}



	/**
	 * @param passwd the passwd to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}



	/**
	 * @return the accountUuid
	 */
	public String getAccountUuid() {
		return accountUuid;
	}



	/**
	 * @param accountUuid the accountUuid to set
	 */
	public void setAccountUuid(String accountUuid) {
		this.accountUuid = accountUuid;
	}



	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TawiGateway [accountUuid=");
		builder.append(accountUuid);
		builder.append(", url=");
		builder.append(url);
		builder.append(", username=");
		builder.append(username);
		builder.append(", passwd=");
		builder.append(passwd);
		builder.append("]");
		return builder.toString();
	}

}
