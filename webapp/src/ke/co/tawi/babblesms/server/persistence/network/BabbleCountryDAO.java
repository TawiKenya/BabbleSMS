package ke.co.tawi.babblesms.server.persistence.network;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.network.Country;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public interface BabbleCountryDAO {

    /**
     *
     * @param Country
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putCountry(Country country);

    /**
     *
     * @param uuid
     * @param country
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updateCountry(String uuid, String country, String code);

    /**
     *
     * @param uuid
     * @return	a Country
     */
    public Country getCountry(String uuid);

    /**
     *
     * @param name
     * @return	a Country
     */
    public Country getCountryByName(String name);

    /**
     *
     * @return	a list of Countries
     */
    public List<Country> getAllCountries();

    /**
     *
     * @param uuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean deleteCountry(String uuid);

    
}
