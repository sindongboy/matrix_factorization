package com.skplanet.nlp.data;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Abstract Item Class
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public abstract class AbstractItem implements Item {
    // logger
    private static final Logger LOGGER = Logger.getLogger(AbstractItem.class.getName());

    /** item id */
    protected String id;

    /** item index */
    protected int index;

    /**
     * Super Constructor
     */
    protected AbstractItem() {

    }

    /**
     * Super Constructor
     * @param itemID user id
     */
    protected AbstractItem(String itemID) {
        this.id = itemID;
    }

    /**
     * Get Item ID
     * @return item id
     */
    public String getId() {
        return id;
    }

    /**
     * Set Item ID
     * @param id id to be set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get Index
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set Index
     * @param index index to be set
     */
    public void setIndex(int index) {
        this.index = index;
    }


    /**
     * Override Equals method
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final AbstractItem other = (AbstractItem) obj;
        if (!other.getId().equals(getId())) {
            return false;
        }
        return true;
    }

    /**
     * Override hashCode()
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(id).append(index).toHashCode();
    }
}
