package com.skplanet.nlp.data;

/**
 * User Family Interface
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public interface User {

    /**
     * Get User ID
     * @return user id
     */
    String getId();

    /**
     * Set User ID
     * @param id user id
     */
    void setId(String id);

    /**
     * Get User Index
     * @return user index
     */
    int getIndex();

    /**
     * Set User Index
     * @param index user index
     */
    void setIndex(int index);
}
