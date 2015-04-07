package ke.co.tawi.babblesms.server.persistence.status;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public interface BabbleMessageStatusDAO {

    /**
     *
     * @param messageStatus
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putMessageStatus(MsgStatus messageStatus);

    /**
     *
     * @param uuid
     * @param network
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updateMessageStatus(String uuid, String description);

    /**
     *
     * @param uuid
     * @return	a network
     */
    public MsgStatus getMessageStatus(String uuid);

    /**
     *
     * @return	a list of networks
     */
    public List<MsgStatus> getAllMessageStatus();

    /**
     *
     * @param uuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean deleteMessageStatus(String uuid);

   
}
