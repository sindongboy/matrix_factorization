package com.skplanet.nlp.algorithm;

import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

/**
 * Baseline Factorizer
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/26/15
 */
public class BaseFactorizer extends AbstractFactorizer {
    // logger
    private static final Logger LOGGER = Logger.getLogger(BaseFactorizer.class.getName());

    /** number of iteration for gradient descent */
    protected int iteration;

    /** learning rate */
    protected double learningRate;

    /** regularization constant */
    protected double regularization;

    /**
     * Super Constructor, it initialize the feature matrices
     *
     * @param ratingMatrix given rating table
     * @param numFeature number of latent feature
     * @param iteration number of iteration
     * @param learningRate learning rate
     * @param regularization regularization
     *
     */
    public BaseFactorizer(
            DoubleMatrix ratingMatrix,
            int numFeature,
            int iteration,
            double learningRate,
            double regularization
    ) {
        super(ratingMatrix, numFeature);

        // set env.
        this.iteration = iteration;
        this.learningRate = learningRate;
        this.regularization = regularization;
    }

    /**
     * Factorize the matrix into two sub-matrices
     */
    public void factorize() {
        // iteration
        for (int iter = 0; iter < this.iteration; iter++) {
            // for each training example.
            for (int row = 0; row < this.ratingMatrix.getRows(); row++) {
                for (int col = 0; col < this.ratingMatrix.getColumns(); col++) {
                    double localError;
                    if (this.ratingMatrix.get(row, col) > 0) {
                        // get local error
                        localError = error(row, col);
                        // update matrices
                        update(row, col, localError);
                    }
                }
            }

            /** recompute the error */
            double error = 0.0;
            int count = 0;
            for (int row = 0; row < this.ratingMatrix.getRows(); row++) {
                for (int col = 0; col < this.ratingMatrix.getColumns(); col++) {
                    if (this.ratingMatrix.get(row, col) > 0) {
                        error += squaredError(row, col);
                        count++;
                    }
                }
            }

            LOGGER.info("iteration: " + iter + ", error: " + error / (2 * count));
        }

        createPrediction();
    }

    /**
     * Create Prediction Matrix
     */
    @Override
    protected void createPrediction() {
        double[][] resultData = new double[this.ratingMatrix.getRows()][this.ratingMatrix.getColumns()];

        // create prediction matrix
        for (int row = 0; row < this.ratingMatrix.getRows(); row++) {
            for (int col = 0; col < this.ratingMatrix.getColumns(); col++) {
                resultData[row][col] = userFeatureMatrix.getRow(row).dot(itemFeatureMatrix.getColumn(col));
            }
        }

        // assign result matrix
        this.predictions = new DoubleMatrix(resultData);
    }

    /**
     * Update Feature Matrices
     * @param row row index
     * @param col column index
     * @param error error
     */
    protected void update(int row, int col, double error) {
        // update P, Q
        for (int k = 0; k < this.numFeature; k++) {
            // batch update
            double prevP = this.userFeatureMatrix.get(row, k);
            double prevQ = this.itemFeatureMatrix.get(k, col);

            // base update
            //double updateP = prevP + (2 * this.learningRate * this.itemFeatureMatrix.get(k, col));
            //double updateQ = prevQ + (2 * this.learningRate * this.userFeatureMatrix.get(row, k));

            // regularized update
            double updateP = prevP + this.learningRate * (2 * error * itemFeatureMatrix.get(k, col) - this.regularization * userFeatureMatrix.get(row, k));
            double updateQ = prevQ + this.learningRate * (2 * error * userFeatureMatrix.get(row, k) - this.regularization * itemFeatureMatrix.get(k, col));

            userFeatureMatrix.put(row, k, updateP);
            itemFeatureMatrix.put(k, col, updateQ);
        }
    }

    /**
     * Error
     *
     * @param row row index
     * @param col column index
     * @return error
     */
    @Override
    protected double error(int row, int col) {
        // Rij - ^Rij
        return this.ratingMatrix.get(row, col) - this.predict(row, col);
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
        return this.userFeatureMatrix.getRow(row).dot(this.itemFeatureMatrix.getColumn(col));
    }

    /**
     * Squared Error
     * @param row row index
     * @param col column index
     * @return squared error
     */
    protected double squaredError(int row, int col) {
        double error = Math.pow(this.ratingMatrix.get(row, col) - this.userFeatureMatrix.getRow(row).dot(this.itemFeatureMatrix.getColumn(col)), 2);
        for (int k = 0; k < this.numFeature; k++) {
            error += (this.regularization / 2) * (Math.pow(this.userFeatureMatrix.get(row, k), 2) + Math.pow(this.itemFeatureMatrix.get(k, col), 2));
        }
        return Math.sqrt(error);

        /*
        double known = this.ratingMatrix.get(row, col);
        double pred = this.userFeatureMatrix.getRow(row).dot(this.itemFeatureMatrix.getColumn(col));
        return Math.pow(known - pred, 2);
        */
    }
}
