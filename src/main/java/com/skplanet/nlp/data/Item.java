package com.skplanet.nlp.data;

/**
 * Item Family Interface
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public interface Item {

    /**
     * Get Item ID
     * @return item id
     */
    String getId();

    /**
     * Set Item ID
     * @param id item id
     */
    void setId(String id);

    /**
     * Get Item Index
     * @return item index
     */
    int getIndex();

    /**
     * Set Item Index
     * @param index item index
     */
    void setIndex(int index);
}
