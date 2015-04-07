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
    public boolean updateMessageTemplate(String uuid,String templatetitle,String contents);

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
