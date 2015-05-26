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
package ke.co.tawi.babblesms.server.beans.maskcode;

import ke.co.tawi.babblesms.server.beans.StorableBean;

/**
 * A superclass for a short code or a mask
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SMSSource extends StorableBean {

	private String source;
	private String accountuuid;
    private String networkuuid;
    
    
	/**
	 * 
	 */
	public SMSSource() {
		super();
		
		source = "";
        accountuuid = "";
        networkuuid = "";
	}


	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}


	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}


	/**
	 * @return the accountuuid
	 */
	public String getAccountuuid() {
		return accountuuid;
	}


	/**
	 * @param accountuuid the accountuuid to set
	 */
	public void setAccountuuid(String accountuuid) {
		this.accountuuid = accountuuid;
	}


	/**
	 * @return the networkuuid
	 */
	public String getNetworkuuid() {
		return networkuuid;
	}


	/**
	 * @param networkuuid the networkuuid to set
	 */
	public void setNetworkuuid(String networkuuid) {
		this.networkuuid = networkuuid;
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SMSSource [getUuid()=");
		builder.append(getUuid());
		builder.append(", source=");
		builder.append(source);
		builder.append(", accountuuid=");
		builder.append(accountuuid);
		builder.append(", networkuuid=");
		builder.append(networkuuid);
		builder.append("]");
		return builder.toString();
	}

}
