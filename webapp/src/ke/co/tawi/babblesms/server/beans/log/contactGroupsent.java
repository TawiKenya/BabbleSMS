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

/**
 * @author Migwi Ndung'u <code>mailto:<a>migwindungu0@gmail.com</a>
 *</code> 
 *
 * 
 */
public class contactGroupsent {
	private String SentGroupUuid;
	private String SentContactUuid;
	
	/**
	 * Set SentGroupUuid value
	 * @param SentGroupUuid
	 */
	
	public void setSentGroupUuid(String SentGroupUuid){
		this.SentGroupUuid=SentGroupUuid;
	}
	
	/**
	 * Returns SentGroupUuid Value
	 * @return SentContactUuid
	 */
    public String getSentGroupUuid(){
    	return SentGroupUuid;
    }
    
     /**
      * sets SentContactUuid
      * @param SentContactUuid
      */
    public void setSentContactUuid(String SentContactUuid){
    	this.SentContactUuid=SentContactUuid;
    }
    
    /**
     * Returns SentContactUuid
     * @return SentContactUuid
     */
    public String getSentContactUuid(){
    	return SentContactUuid;
    }

    	/**
    	 * @see java.lang.Object#toString()
    	 */
    	
    	@Override
    	public String toString() {
    	StringBuilder build = new StringBuilder();
    	build.append("contactGroupsent[");
    	build.append("getSentGroupUuid =");
    	build.append(getSentGroupUuid());
    	build.append(",getSentContactUuid =");
    	build.append(getSentContactUuid());
    	build.append("]");
    	return build.toString();
    	}
    
}
