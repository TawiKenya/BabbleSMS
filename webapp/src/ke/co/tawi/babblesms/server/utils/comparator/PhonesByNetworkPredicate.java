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
package ke.co.tawi.babblesms.server.utils.comparator;

import ke.co.tawi.babblesms.server.beans.contact.Phone;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * Used to filter {@link Phone}s by a Network.
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class PhonesByNetworkPredicate implements Predicate {

	// The following should match what is in the database table 'Network'.	
	private String networkUuid;
	
	
	/**
	 * Disable the default constructor.
	 */
	private PhonesByNetworkPredicate() {}
	
	
	/**
	 * @param networkUuid
	 */
	public PhonesByNetworkPredicate(String networkUuid) {
		this.networkUuid = networkUuid;
	}
	
	
	/**
	 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
	 */
	@Override
	public boolean evaluate(Object obj) {
		
		if(obj instanceof Phone) {
			Phone p = (Phone) obj;
			
			return StringUtils.equalsIgnoreCase(networkUuid, p.getNetworkuuid());			
		}
		
		return false;
	}

}
