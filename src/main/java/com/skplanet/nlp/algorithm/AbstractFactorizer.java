package com.skplanet.nlp.algorithm;

import com.skplanet.nlp.util.Util;
import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

/**
 * Abstract Factorizer class
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/26/15
 */
public abstract class AbstractFactorizer implements Factorizer {
    // logger
    private static final Logger LOGGER = Logger.getLogger(AbstractFactorizer.class.getName());

    /** User Feature Matrix */
    protected DoubleMatrix userFeatureMatrix = null;

    /** Item Feature Matrix */
    protected DoubleMatrix itemFeatureMatrix = null;

    /** rating matrix */
    protected DoubleMatrix ratingMatrix = null;

    /** prediction matrix */
    protected DoubleMatrix predictions = null;

    /** number of user */
    protected int numUser;

    /** number of item */
    protected int numItem;

    /** number of feature */
    protected int numFeature;

    /**
     * Super Constructor, it initialize the feature matrices
     * @param ratingMatrix given rating table
     * @param numFeature number of latent feature
     */
    public AbstractFactorizer(DoubleMatrix ratingMatrix, int numFeature) {
        // assign counts
        this.numUser = ratingMatrix.getRows();
        this.numItem = ratingMatrix.getColumns();
        this.numFeature = numFeature;

        // assign rating matrix
        this.ratingMatrix = ratingMatrix;

        // ----------------------- //
        // init feature matrices
        // ----------------------- //

        // user feature
        double[][] userFeatureArray = new double[numUser][numFeature];
        for (int row = 0; row < numUser; row++) {
            for (int col = 0; col < numFeature; col++) {
                userFeatureArray[row][col] = Util.nextGaussian();
            }
        }
        this.userFeatureMatrix = new DoubleMatrix(userFeatureArray);

        // item feature
        double[][] itemFeatureArray = new double[numFeature][numItem];
        for (int row = 0; row < numFeature; row++) {
            for (int col = 0; col < numItem; col++) {
                itemFeatureArray[row][col] = Util.nextGaussian();
            }
        }
        this.itemFeatureMatrix = new DoubleMatrix(itemFeatureArray);
    }

    /**
     * Error
     *
     * @param row row index
     * @param col column index
     * @return error
     */
    protected double error(int row, int col) {
        return this.ratingMatrix.get(row, col) - this.userFeatureMatrix.getRow(row).dot(this.itemFeatureMatrix.getColumn(col));
    }

    /**
     * Prediction
     * @param row row index
     * @param col column index
     * @return prediction for Rij
     */
    protected abstract double predict(int row, int col);

    /**
     * Create {@link DoubleMatrix} for final predictions
     */
    protected abstract void createPrediction();

     /**
     * Update Feature Matrices
     *
     * @param row   row index
     * @param col   column index
     * @param error error
     */
    protected abstract void update(int row, int col, double error);

    /**
     * Get Prediction Matrix
     *
     * @return prediction matrix {@link DoubleMatrix}
     */
    public DoubleMatrix getPrediction() {
        if (this.predictions == null) {
            LOGGER.error("Prediction matrix isn't generated yet!");
            throw new NullPointerException();
        }
        return this.predictions;
    }
}
