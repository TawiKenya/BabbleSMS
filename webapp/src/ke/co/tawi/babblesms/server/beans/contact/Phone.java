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
 * A phone number belonging to a {@link Contact}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Phone extends StorableBean {

	public static final String ACTIVE_STATUSUUID = "396F2C7F-961C-5C12-3ABF-867E7FD029E6";
	
    private String phoneNumber;
    private String contactUuid;
    private String statusUuid;
    private String networkUuid;
        
    /**
     * 
     */
    public Phone() {
        super();
        
        phoneNumber = "";
        contactUuid = "";
        statusUuid = "";
        networkUuid = "";
    }
    

    public String getPhonenumber() {
        return phoneNumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phoneNumber = StringUtils.trimToEmpty(phonenumber);
    }

    public String getContactUuid() {
        return contactUuid;
    }

    public void setContactUuid(String contactsuuid) {
        this.contactUuid = StringUtils.trimToEmpty(contactsuuid);
    }

    public String getStatusuuid() {
        return statusUuid;
    }

    public void setStatusuuid(String statusuuid) {
        this.statusUuid = StringUtils.trimToEmpty(statusuuid);
    }

    public String getNetworkuuid() {
        return networkUuid;
    }

    public void setNetworkuuid(String networkuuid) {
        this.networkUuid = StringUtils.trimToEmpty(networkuuid);
    }

    
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Phone [getUuid()=");
		builder.append(getUuid());
		builder.append(", phonenumber=");
		builder.append(phoneNumber);
		builder.append(", contactuuid=");
		builder.append(contactUuid);
		builder.append(", statusuuid=");
		builder.append(statusUuid);
		builder.append(", networkuuid=");
		builder.append(networkUuid);
		builder.append("]");
		return builder.toString();
	}


	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		
		Phone phone;
		
		if(obj instanceof Phone) {
			phone = (Phone) obj;
			
			return getUuid().equals(phone.getUuid());
		}
		
		return false;
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getUuid().hashCode();
	}
}
