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
 * An outgoing SMS to a {@link ke.co.tawi.babblesms.server.beans.contact.Group}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class OutgoingGrouplog extends Log {

    private String messagestatusUuid;
    private String sender;
    

    /**
     * 
     */
    public OutgoingGrouplog() {
        super();
                
        messagestatusUuid = "";
        sender = "";
    }
    
    
    /**
     * @return the messagestatusUuid
     */
    public String getMessagestatusuuid() {
        return messagestatusUuid;
    }

    
    /**
     * @param messagestatusUuid
     */
    public void setMessagestatusUuid(String messagestatusUuid) {
        this.messagestatusUuid = StringUtils.trimToEmpty(messagestatusUuid);
    }

    
    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    
    /**
     * @param sender
     */
    public void setSender(String sender) {
        this.sender = StringUtils.trimToEmpty(sender);
    }
    
       
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OutgoingGrouplog [getUuid()=");
		builder.append(getUuid());
		builder.append(", getOrigin()=");
		builder.append(getOrigin());
		builder.append(", networkuuid=");
		builder.append(getNetworkUuid());
		builder.append(", getMessage()=");
		builder.append(getMessage());
		builder.append(", getLogTime()=");
		builder.append(getLogTime());
		builder.append(", getDestination()=");
		builder.append(getDestination());
		builder.append(", messagestatusUuid=");
		builder.append(messagestatusUuid);
		builder.append(", sender=");
		builder.append(sender);
		builder.append("]");
		return builder.toString();
	}    
}
