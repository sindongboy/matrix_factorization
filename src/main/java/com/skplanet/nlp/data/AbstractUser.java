package com.skplanet.nlp.data;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * Abstract User Class
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public abstract class AbstractUser implements User {
    // logger
    private static final Logger LOGGER = Logger.getLogger(AbstractUser.class.getName());

    /** user id */
    protected String id;

    /** user index */
    protected int index;

    /**
     * Super Constructor
     */
    protected AbstractUser() {

    }

    /**
     * Super Constructor
     * @param userID user id
     */
    protected AbstractUser(String userID) {
        this.id = userID;
    }

    /**
     * Get User ID
     * @return user id
     */
    public String getId() {
        return id;
    }

    /**
     * Set User ID
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
     * Set index
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

        final AbstractUser other = (AbstractUser) obj;
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
