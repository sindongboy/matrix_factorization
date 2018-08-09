package com.skplanet.nlp.io;

import org.jblas.DoubleMatrix;

/**
 * Matrix Reader Family Interface
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public interface MatrixReader {

    /**
     * Load User Information
     */
    void loadUserInfo();

    /**
     * Load Item Information
     */
    void loadItemInfo();

    /**
     * Load Matrix to {@link DoubleMatrix}
     */
    void loadRatingTable();

}
