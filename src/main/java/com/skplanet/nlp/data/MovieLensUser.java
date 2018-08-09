package com.skplanet.nlp.data;

import org.apache.log4j.Logger;

/**
 * User Object for Movielens Dataset
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public final class MovieLensUser extends AbstractUser {
    // logger
    private static final Logger LOGGER = Logger.getLogger(MovieLensUser.class.getName());

    /** user id */
    // declared in superclass

    /** index */
    // declared in superclass

    /** user age */
    private int age;

    /** user gender, either men for M or women for F */
    private String gender;

    /** user occupation */
    private String occupation;

    /** user zipcode */
    private String zip;

    /**
     * Constructor
     *
     * @param userID user id
     * @param index index
     * @param age age
     * @param gender gender
     * @param occupation occupation
     * @param zip zipcode
     */
    public MovieLensUser(
            String userID,
            int index,
            int age,
            String gender,
            String occupation,
            String zip
    ) {
        this.id = userID;
        this.index = index;
        this.age = age;
        this.gender = gender;
        this.occupation = occupation;
        this.zip = zip;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getZip() {
        return zip;
    }
}
