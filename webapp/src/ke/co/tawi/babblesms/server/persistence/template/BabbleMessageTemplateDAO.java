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
package ke.co.tawi.babblesms.server.persistence.template;

import ke.co.tawi.babblesms.server.beans.messagetemplate.MessageTemplate;
import ke.co.tawi.babblesms.server.beans.account.Account;

import java.util.List;

/**
 * Persistence description for an SMS message template.
 * <p>
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleMessageTemplateDAO {

    /**
     *
     * @param template
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean put(MessageTemplate template);
    
    
    
    /**
     *
     * @param uuid
     * @return	a {@link MessageTemplate} with the corresponding UUID, or null 
     * if none matches
     */    
    public MessageTemplate getTemplate(String uuid);

    
    
    /**
     * @param account
     * @return a list of {@link MessageTemplate}s belonging to this 
     * {@link Account}.
     */
    public List<MessageTemplate> getTemplates(Account account);
    
    
    
  
    /**
     * Allows one to update the {@link MessageTemplate} that has the 
     * corresponding UUID.
     * 
     * @param template
     * @param uuid
     * @return whether the update was successful or not
     */
    public boolean update(MessageTemplate template, String uuid);


    
    /**
     * Deletes a {@link MessageTemplate} from the database that has the same 
     * UUID as the one in the parameter.
     * 
     * @param template
     */
    public void delete(MessageTemplate template);
    
  
    
    /**
     * @param title
     * @param account
     * @return whether or not a {@link MessageTemplate} with that title exists
     * in the particular {@link Account}
     */
    public boolean titleExists(String title, Account account);
   
}
