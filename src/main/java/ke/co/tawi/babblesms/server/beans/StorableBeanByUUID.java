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
package ke.co.tawi.babblesms.server.beans;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * An object that can be persisted with its primary key as an uuid.
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
@MappedSuperclass
public class StorableBeanByUUID extends StorableBean {

	@Id
	@Column(name = "uuid", unique = true)
    @NotEmpty
	private String uuid;
        
	
	/**
	 * 
	 */
	public StorableBeanByUUID() {
		uuid = UUID.randomUUID().toString();
	}
		
	
	/**
	 * @return the uuid
	 */
	
	public String getUuid() {
		return uuid;
	}
	
	
	/**
	 * @param uuid - the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
