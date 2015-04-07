package ke.co.tawi.babblesms.server.persistence.items.purchaseHistory;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.PurchaseHistory;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public interface BabblePurchaseHistoryDAO {

    /**
     *
     * @param purchaseHistory
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putPurchaseHistory(PurchaseHistory purchaseHistory);

    /**
     *
     * @param uuid
     * @param network
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updatePurchaseHistory(String uuid, String source);

    /**
     *
     * @param uuid
     * @return	a network
     */
    public PurchaseHistory getPurchaseHistory(String uuid);

    /**
     *
     * @return	a list of networks
     */
    public List<PurchaseHistory> getAllPurchaseHistory();

    /**
     *
     * @param account
     * @return	a list of networks
     */
    public List<PurchaseHistory> getPurchaseHistoryByAccount(String account);

    /**
     *
     * @param uuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean deletePurchaseHistory(String uuid);

    
}
