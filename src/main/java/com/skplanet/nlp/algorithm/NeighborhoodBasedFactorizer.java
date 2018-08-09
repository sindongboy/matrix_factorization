package com.skplanet.nlp.algorithm;

import org.jblas.DoubleMatrix;

/**
 * Biased Matrix Factorization with Neighborhood Information
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 1/12/16
 */
public class NeighborhoodBasedFactorizer extends BiasedFactorizer {

    /** similarity between items */
    private DoubleMatrix itemSimilarity;

    /**
     * Super Constructor, it initialize the feature matrices
     *
     * @param ratingMatrix   given rating table
     * @param numFeature     number of latent feature
     * @param iteration      number of iteration
     * @param learningRate   learning rate
     * @param regularization regularization
     */
    public NeighborhoodBasedFactorizer(DoubleMatrix ratingMatrix, int numFeature, int iteration, double learningRate, double regularization) {
        super(ratingMatrix, numFeature, iteration, learningRate, regularization);
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
        int ratingCount = getRatingCount(row);
        double rateNorm = Math.pow(ratingCount, -0.5);

        DoubleMatrix userRatingRecords = this.ratingMatrix.getRow(row);
        for (int itemIndex = 0; itemIndex < this.numItem; itemIndex++) {
            if (userRatingRecords.get(itemIndex) > 0) {

            }
        }
        return super.predict(row, col);
    }

    /**
     * Get the number of items user (userIndex) rate
     * @param userIndex user index
     * @return number of items user rated
     */
    private int getRatingCount(int userIndex) {
        DoubleMatrix userRatingRecords = this.ratingMatrix.getRow(userIndex);
        int count = 0;
        for (int itemIndex = 0; itemIndex < this.numItem; itemIndex++) {
            if (userRatingRecords.get(itemIndex) > 0) {
                count++;
            }
        }
        return count;
    }


    /**
     * Get User's Average Rating
     * @param userIndex user index
     * @return average rating for a user
     */
    private double getUserAverageRating(int userIndex) {
        DoubleMatrix userRatingRecords = this.ratingMatrix.getRow(userIndex);
        int count = 0;
        double sum = 0;
        for (int itemIndex = 0; itemIndex < this.numItem; itemIndex++) {
            if (userRatingRecords.get(itemIndex) > 0) {
                count++;
                sum += userRatingRecords.get(itemIndex);
            }
        }
        return sum / count;
    }


}
