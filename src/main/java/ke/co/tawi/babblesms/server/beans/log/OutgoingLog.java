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
package ke.co.tawi.babblesms.server.beans.log;

import org.apache.commons.lang3.StringUtils;

/**
 * An outgoing SMS to a {@link ke.co.tawi.babblesms.server.beans.contact.Contact}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class OutgoingLog extends Log {

    private String messagestatusuuid;
    private String sender;
    private String phoneUuid;
    

    /**
     * 
     */
    public OutgoingLog() {
        super();
        messagestatusuuid = "";
        sender = "";
        phoneUuid = "";
    }
    
    
    public String getMessagestatusuuid() {
        return messagestatusuuid;
    }

    public void setMessagestatusuuid(String messagestatusuuid) {
        this.messagestatusuuid = StringUtils.trimToEmpty(messagestatusuuid);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = StringUtils.trimToEmpty(sender);
    }

    

    /**
	 * @return the phoneUuid
	 */
	public String getPhoneUuid() {
		return phoneUuid;
	}


	/**
	 * @param phoneUuid the phoneUuid to set
	 */
	public void setPhoneUuid(String phoneUuid) {
		this.phoneUuid = phoneUuid;
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OutgoingLog [getUuid()=");
		builder.append(getUuid());
		builder.append(", getOrigin()=");
		builder.append(getOrigin());
		builder.append(", getMessage()=");
		builder.append(getMessage());
		builder.append(", getDestination()=");
		builder.append(getDestination());
		builder.append(", getLogTime()=");
		builder.append(getLogTime());
		builder.append(", getNetworkUuid()=");
		builder.append(getNetworkUuid());
		builder.append(", messagestatusuuid=");
		builder.append(messagestatusuuid);
		builder.append(", sender=");
		builder.append(sender);
		builder.append(", networkuuid=");
		builder.append(getNetworkUuid());
		builder.append(", phoneUuid=");
		builder.append(phoneUuid);
		builder.append("]");
		return builder.toString();
	}
	
}
