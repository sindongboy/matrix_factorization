package com.skplanet.nlp.algorithm;

import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

/**
 * Feature based Matrix Factorization
 * (http://www.recsyswiki.com/wiki/Feature-based_matrix_factorization)
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 1/5/16
 */
public final class FeatureBasedFactorizer extends AbstractFactorizer {
    // logger
    private static final Logger LOGGER = Logger.getLogger(FeatureBasedFactorizer.class.getName());

    /** global mean */
    private double globalMean;

    /** global features & biases */
    DoubleMatrix globalFeatures;
    DoubleMatrix globalBiases;

    /** user features & biases */
    DoubleMatrix userFeatures;
    DoubleMatrix userBiases;

    /** item features & biases */
    DoubleMatrix itemFeatures;
    DoubleMatrix itemBiases;

    /**
     * it initialize the feature matrices
     *
     * @param ratingMatrix given rating table
     * @param numFeature   number of latent feature
     */
    public FeatureBasedFactorizer(
            // TODO: add more arguments needed such as iteration, model parameters etc.
            DoubleMatrix ratingMatrix,
            int numFeature) {
        super(ratingMatrix, numFeature);
    }

    /**
     * Create {@link org.jblas.DoubleMatrix} contains final predictions
     */
    @Override
    protected void createPrediction() {
        double[][] result = new double[numUser][numItem];
        for (int row = 0; row < numUser; row++) {
            for (int col = 0; col < numItem; col++) {
                result[row][col] = this.predict(row, col);
            }
        }
        this.predictions = new DoubleMatrix(result);
    }

    /**
     * Factorize the matrix into two sub-matrices
     */
    @Override
    public void factorize() {
        // TODO: implement SGD
        /*
        for (int iter = 0; iter < this.iteration; iter++) {
        .....
        }
        */
    }

    /**
     * Update Feature Matrices
     *
     * @param row   row index
     * @param col   column index
     * @param error error
     */
    @Override
    protected void update(int row, int col, double error) {
        // TODO: implement update rule
    }

    /**
     * Prediction
     *
     * @param row row index
     * @param col column index
     * @return prediction for Rij
     */
    @Override
    protected double predict(int row, int col) {
        // TODO: write the formula for prediction
        return 0;
    }

    /**
     * Error
     *
     * 이것도 그냥 L2 loss 사용 : (r - ^r)2
     *
     * @param row row index
     * @param col column index
     * @return error
     */
    @Override
    protected double error(int row, int col) {
        return super.error(row, col);
    }

    /**
     * Get Prediction Matrix
     *
     * @return prediction matrix {@link DoubleMatrix}
     */
    @Override
    public DoubleMatrix getPrediction() {
        return super.getPrediction();
    }
}
