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

import java.io.Serializable;
import java.util.Random;

/**
 * Represents an object in the Babblesms architecture that can be stored in the
 * RDBMS.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class StorableBean implements Serializable   {

	/**
	 * 
	 */
	public static final long serialVersionUID = new Random().nextLong();
	
		
	
}
