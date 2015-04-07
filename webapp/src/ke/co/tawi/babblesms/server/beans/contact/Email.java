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
package ke.co.tawi.babblesms.server.beans.contact;

import ke.co.tawi.babblesms.server.beans.StorableBean;

import org.apache.commons.lang3.StringUtils;

/**
 * An email belonging to a {@link Contact}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Email extends StorableBean {

    private String address;
    private String contactUuid;
    private String statusUuid;

    /**
     * 
     */
    public Email() {
        super();
        address = "";
        contactUuid = "";
        statusUuid = "";
    }

    
    /**
     * @return an email address
     */
    public String getAddress() {
        return address;
    }

    
    
    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = StringUtils.trimToEmpty(address);
    }

        
    /**
     * @return a Contact UUID
     */
    public String getContactuuid() {
        return contactUuid;
    }

    
    /**
     * @param contactuuid
     */
    public void setContactuuid(String contactuuid) {
        this.contactUuid = StringUtils.trimToEmpty(contactuuid);
    }

    
    /**
     * @return a Status UUD
     */
    public String getStatusuuid() {
        return statusUuid;
    }

    
    /**
     * @param statusuuid
     */
    public void setStatusuuid(String statusuuid) {
        this.statusUuid = StringUtils.trimToEmpty(statusuuid);
    }

    
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Email [getUuid()=");
		builder.append(getUuid());
		builder.append(", address=");
		builder.append(address);
		builder.append(", contactuuid=");
		builder.append(contactUuid);
		builder.append(", statusuuid=");
		builder.append(statusUuid);
		builder.append("]");
		return builder.toString();
	}
}
