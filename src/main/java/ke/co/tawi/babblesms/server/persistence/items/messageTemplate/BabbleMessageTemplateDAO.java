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
package ke.co.tawi.babblesms.server.persistence.items.messageTemplate;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.messagetemplate.MessageTemplate;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public interface BabbleMessageTemplateDAO {

    /**
     *
     * @param messageTemplate
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putMessageTemplate(MessageTemplate messageTemplate);
    
    
    /**
     *
     * @param uuid
     * @param accuuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    
    public List<MessageTemplate> getAllMessageTemplatesbyuuid(String accuuid);

    /**
     *
     * @param uuid
     * @param network
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updateMessageTemplate(MessageTemplate template);

    /**
     *
     * @param uuid
     * @return	a network
     */
    public MessageTemplate getMessageTemplate(String uuid);

    /**
     *
     * @return	a list of networks
     */
    public List<MessageTemplate> getAllMessageTemplates();

    /**
     *
     * @param uuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean deleteMessageTemplate(String uuid);

    
    /**
     *
     * @param uuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public MessageTemplate titleexists(String title);
   
}
