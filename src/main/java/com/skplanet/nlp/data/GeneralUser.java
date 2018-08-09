package com.skplanet.nlp.data;

/**
 * General Purpose User Object
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public class GeneralUser extends AbstractUser {

    /**
     * Super Constructor
     *
     * @param userID user id
     */
    public GeneralUser(String userID) {
        super(userID);
    }


    /**
     * Set Index
     *
     * @param index index to be set
     */
    public void setIndex(int index) {
        super.setIndex(index);
    }
}
