package ke.co.tawi.babblesms.server.persistence.network;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.network.Network;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public interface BabbleNetworkDAO {

    /**
     *
     * @param network
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putNetwork(Network network);

    /**
     *
     * @param uuid
     * @param network
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updateNetwork(String uuid, String network);

    /**
     *
     * @param uuid
     * @return	a network
     */
    public Network getNetwork(String uuid);

    /**
     *
     * @param name
     * @return	a network
     */
    public Network getNetworkByName(String name);

    /**
     *
     * @return	a list of networks
     */
    public List<Network> getAllNetworks();

    /**
     *
     * @param uuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean deleteNetwork(String uuid);

    
}
