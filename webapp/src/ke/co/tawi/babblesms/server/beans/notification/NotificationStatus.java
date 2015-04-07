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
package ke.co.tawi.babblesms.server.beans.notification;

import ke.co.tawi.babblesms.server.beans.StorableBean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * A notification status.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class NotificationStatus extends StorableBean {
	
	private String notificationUuid;
	private boolean readFlag;
	private Date readDate;
	
	/**
	 * 
	 */
	public NotificationStatus() {
		super();
		notificationUuid = "";
		readFlag = false;
		readDate = new Date();
	}

	/**
	 * @return the notificationUuid
	 */
	public String getNotificationUuid() {
		return notificationUuid;
	}

	/**
	 * @param notificationUuid - the notificationUuid to set
	 */
	public void setNotificationUuid(String notificationUuid) {
		this.notificationUuid = StringUtils.trimToEmpty(notificationUuid);
	}

	/**
	 * @return the readFlag
	 */
	public boolean isReadFlag() {
		return readFlag;
	}

	/**
	 * @param readFlag - the readFlag to set
	 */
	public void setReadFlag(boolean readFlag) {
		this.readFlag = readFlag;
	}

	/**
	 * @return the readDate
	 */
	public Date getReadDate() {
		return new Date(readDate.getTime());
	}

	/**
	 * @param readDate - the readDate to set
	 */
	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}

	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NotificationStatus [getUuid()=");
		builder.append(getUuid());
		builder.append(", notificationUuid=");
		builder.append(notificationUuid);
		builder.append(", readFlag=");
		builder.append(readFlag);
		builder.append(", readDate=");
		builder.append(readDate);
		builder.append("]");
		return builder.toString();
	}
}
