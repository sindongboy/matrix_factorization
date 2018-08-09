package com.skplanet.nlp.algorithm;

import org.jblas.DoubleMatrix;

/**
 * Factorizer Family Interface
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public interface Factorizer {

    /**
     * Factorize the matrix into two sub-matrices
     */
    void factorize();

    /**
     * Get Prediction Matrix
     * @return prediction matrix {@link DoubleMatrix}
     */
    DoubleMatrix getPrediction();

}
