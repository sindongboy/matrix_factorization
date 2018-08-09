package com.skplanet.nlp.io;

import com.skplanet.nlp.data.Item;
import com.skplanet.nlp.data.User;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Abstract Matrix Reader
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public abstract class AbstractMatrixReader implements MatrixReader {
    // logger
    private static final Logger LOGGER = Logger.getLogger(AbstractMatrixReader.class.getName());

    /** user list */
    protected List<User> userList;

    /** item list */
    protected List<Item> itemList;

}
