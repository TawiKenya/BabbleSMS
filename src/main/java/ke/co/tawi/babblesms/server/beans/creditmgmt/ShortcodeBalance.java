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
package ke.co.tawi.babblesms.server.beans.creditmgmt;

/**
 * A Shortcode balance (credit).
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ShortcodeBalance extends SMSBalance {

	private String shortcodeUuid;
	
	/**
	 * 
	 */
	public ShortcodeBalance() {
		super();
		
		shortcodeUuid = "";
	}

	
	/**
	 * @return the shortcodeUuid
	 */
	public String getShortcodeUuid() {
		return shortcodeUuid;
	}

	
	/**
	 * @param shortcodeUuid the shortcodeUuid to set
	 */
	public void setShortcodeUuid(String shortcodeUuid) {
		this.shortcodeUuid = shortcodeUuid;
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShortcodeBalance [shortcodeUuid=");
		builder.append(shortcodeUuid);
		builder.append(", getUuid()=");
		builder.append(getUuid());
		builder.append(", getAccountUuid()=");
		builder.append(getAccountUuid());
		builder.append(", getCount()=");
		builder.append(getCount());
		builder.append("]");
		return builder.toString();
	}
}
