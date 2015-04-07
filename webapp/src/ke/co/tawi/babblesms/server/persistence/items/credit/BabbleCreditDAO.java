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
package ke.co.tawi.babblesms.server.persistence.items.credit;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Credit;

/**
 * Persistence description for {@link Credit}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleCreditDAO {

    /**
     *
     * @param credit
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putCredit(Credit credit);

    /**
     *
     * @param uuid
     * @param credit
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updateCredit(String uuid, int amount);

    /**
     *
     * @param uuid
     * @param credit
     * @return		<code>true</code> if successfully deducted; <code>false</code> for
     * otherwise
     */
    public boolean deductCredit(String source, int amount);
    /**
     *
     * @param uuid
     * @return	a credit
     */
    public Credit getCredit(String uuid);
    
    
    /**
     *
     * @param uuid
     * @return	a credit
     */
    public Credit getCreditbysource(String source, String accuuid);
    
    /**
     *
     * @param accuuid
     * @return	a credit
     */
    public List<Credit> getAllCreditsByaccountuuid(String accuuid);

    /**
     *
     * @return	a list of credit
     */
    public List<Credit> getAllCredits();
    
}
