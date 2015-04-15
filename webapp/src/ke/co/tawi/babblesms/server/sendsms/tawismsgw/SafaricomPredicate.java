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
package ke.co.tawi.babblesms.server.sendsms.tawismsgw;

import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * Used to filter only Safaricom Outgoing SMS.
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class SafaricomPredicate implements Predicate {

	// The following matches what is in the database table 'Network'.
	final String NETWORKUUID_SAFARICOM_KE = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78";
			
	/**
	 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
	 */
	@Override
	public boolean evaluate(Object obj) {
		
		if(obj instanceof OutgoingLog) {
			OutgoingLog log = (OutgoingLog) obj;
			
			return StringUtils.equalsIgnoreCase(NETWORKUUID_SAFARICOM_KE, log.getNetworkuuid());			
		}
		
		return false;
	}

}
