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

    private String messagestatusuuid;
    private String sender;
    

    public OutgoingGrouplog() {
        super();
                
        messagestatusuuid = "";
        sender = "";
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OutgoingGrouplog [getUuid()=");
		builder.append(getUuid());
		builder.append(", getOrigin()=");
		builder.append(getOrigin());
		builder.append(", getMessage()=");
		builder.append(getMessage());
		builder.append(", getLogTime()=");
		builder.append(getLogTime());
		builder.append(", getDestination()=");
		builder.append(getDestination());
		builder.append(", messagestatusuuid=");
		builder.append(messagestatusuuid);
		builder.append(", sender=");
		builder.append(sender);
		builder.append("]");
		return builder.toString();
	}    
}
