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
package ke.co.tawi.babblesms.server.persistence.logs;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.log.contactGroupsent;

/**
 * @author Migwi Ndung'u <code>mailto:<a>migwindungu0@gmail.com</a>
 *</code> 
 *
 * 
 */
public interface BabbleContactGroupSentDAO {
	
	/**
	 * Method that saves a new record of {@link contactGroupsent}
	 * 
	 * @param {@link contactGroupsent}
	 * @return <code> true if saved Successfully; or false</code>
	 * if otherwise
	 */
	
     public boolean put(contactGroupsent contactGroupsent);	
     
     /**
      * Method gets a List of {@link contactGroupsent} objects with the given SentGroupUuid
      * 
      * @param SentGroupUuid
      * @return {@link contactGroupsent}
      */

     public List<contactGroupsent> getUsingSentGroup(String SentGroupUuid);
     
     /**
      * Method gets a List of {@link contactGroupsent} objects with the given SentContactUuid
      * 
      * @param SentContactUuid
      * @return {@link contactGroupsent}
      */

     public List<contactGroupsent> getUsingSentContact(String SentContactUuid);

}
