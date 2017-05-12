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
package ke.co.tawi.babblesms.server.persistence;

import ke.co.tawi.babblesms.server.beans.StorableBean;
import ke.co.tawi.babblesms.server.beans.StorableBeanByUUID;
import ke.co.tawi.babblesms.server.beans.StorableBeanById;

import java.util.List;

/**
 * Our storage definition
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public abstract class Storage {

	/**
	 * This can both save new objects, as well as update objects if a bean with
	 * a matching primary key is present in the database.
	 * 
	 * @param bean
	 * @return
	 */
	public abstract StorableBean save(StorableBean bean);
	
	
	public abstract StorableBeanByUUID get(Class aClass, String uuid);
	
	
	public abstract StorableBeanById get(Class aClass, long id);
	
	
	/**
	 * It is safe to get all objects whose primary key is an uuid because they
	 * are not intended to be many.
	 * 
	 * @param aClass
	 * @return a list of all objects in persistence
	 */
	public abstract List<StorableBeanByUUID> getAll(Class aClass);
	
	
	/**
	 * Select a set of objects from persistence which has a certain fieldName (column) and whose
	 * value is fieldValue
	 * 
	 * @param aClass
	 * @param fieldName
	 * @param fieldValue
	 * @return a matching list of all objects in persistence
	 */
	public abstract List<StorableBean> get(Class aClass, Object fieldName, Object fieldValue);
}
