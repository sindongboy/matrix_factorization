package com.skplanet.nlp.algorithm;

import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

/**
 * Regularized Matrix Factorization with biases
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 1/4/16
 */
public class BiasedFactorizer extends BaseFactorizer {
    // logger
    private static final Logger LOGGER = Logger.getLogger(BiasedFactorizer.class.getName());

    /** Global Mean of rating */
    protected double GLOBAL_MEAN;

    /** user bias */
    protected DoubleMatrix userBias;

    /** item bias */
    protected DoubleMatrix itemBias;

    /** learning rate for bias */
    protected double biasLearningRate = 0.5;

    /** bias regularized constant */
    protected double biasReg = 0.1;
    /**
     * Super Constructor, it initialize the feature matrices
     *
     * @param ratingMatrix   given rating table
     * @param numFeature     number of latent feature
     * @param iteration      number of iteration
     * @param learningRate   learning rate
     * @param regularization regularization
     */
    public BiasedFactorizer(
            DoubleMatrix ratingMatrix, 
            int numFeature, 
            int iteration, 
            double learningRate, 
            double regularization
            ) 
    {
        super(ratingMatrix, numFeature, iteration, learningRate, regularization);

        // get global mean
        double sum = 0.0;
        int count = 0;
        for (int row = 0; row < this.numUser; row++) {
            for (int col = 0; col < this.numItem; col++) {
                if (this.ratingMatrix.get(row, col) > 0) {
                    sum += this.ratingMatrix.get(row, col);
                    count++;
                }
            }
        }
        this.GLOBAL_MEAN = sum / count;

        // init user bias
        double[] tmpArray = new double[this.numUser];
        for (int index = 0; index < this.numUser; index++) {
            tmpArray[index] = 0.0;
        }
        this.userBias = new DoubleMatrix(tmpArray);

        // init item bias
        tmpArray = new double[this.numItem];
        for (int index = 0; index < this.numItem; index++) {
            tmpArray[index] = 0.0;
        }
        this.itemBias = new DoubleMatrix(tmpArray);
    }

    /**
     * Factorize the matrix into two sub-matrices
     */
    @Override
    public void factorize() {
        super.factorize();
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
        // update user bias
        double prevUserBias = this.userBias.get(row);
        double prevItemBias = this.itemBias.get(col);

        /* Mahout Version : introduces biasLearningRate, biasRegularizationConstant
        double updateUserBias = prevUserBias + this.biasLearningRate * this.learningRate * (error - this.biasReg * this.regularization * prevUserBias);
        double updateItemBias = prevItemBias + this.biasLearningRate * this.learningRate * (error - this.biasReg * this.regularization * prevItemBias);
        */
        double updateUserBias = prevUserBias + this.learningRate * (error - this.regularization * prevUserBias);
        double updateItemBias = prevItemBias + this.learningRate * (error - this.regularization * prevItemBias);

        this.userBias.put(row, updateUserBias);
        this.itemBias.put(col, updateItemBias);

        // update features
        for (int k = 0; k < this.numFeature; k++) {
            double prevUserFeature = this.userFeatureMatrix.get(row, k);
            double prevItemFeature = this.itemFeatureMatrix.get(k, col);

            double updateUserFeature = prevUserFeature + this.learningRate * (error * prevItemFeature - this.regularization * prevUserFeature);
            double updateItemFeature = prevItemFeature + this.                                                                                                                                                                                                                                                                                                                                                                                                                                      learningRate * (error * prevUserFeature - this.regularization * prevItemFeature);

            this.userFeatureMatrix.put(row, k, updateUserFeature);
            this.itemFeatureMatrix.put(k, col, updateItemFeature);
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
        return super.error(row, col);
    }

    /**
     * Squared Error
     *
     * @param row row index
     * @param col column index
     * @return squared error
     */
    @Override
    protected double squaredError(int row, int col) {
        double error = 0.0;
        double rui = this.ratingMatrix.get(row, col);
        error = this.ratingMatrix.get(row, col)
                - this.GLOBAL_MEAN
                - this.itemBias.get(col)
                - this.userBias.get(row)
                - this.userFeatureMatrix.getRow(row).dot(this.itemFeatureMatrix.getColumn(col));

        error = Math.pow(error, 2);


        // regularized term
        double userSquaredBias = Math.pow(this.userBias.get(row), 2);
        double itemSquaredBias = Math.pow(this.itemBias.get(col), 2);
        double userSquaredNorm = Math.pow(this.userFeatureMatrix.getRow(row).norm1(), 2);
        double itemSquaredNorm = Math.pow(this.itemFeatureMatrix.getColumn(col).norm1(), 2);

        error += this.regularization * (userSquaredBias + itemSquaredBias + userSquaredNorm + itemSquaredNorm);

        return error;
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
                resultData[row][col] = predict(row, col);
            }
        }

        // assign result matrix
        this.predictions = new DoubleMatrix(resultData);
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
        double featureScore = 0.0;
        for (int k = 0; k < this.numFeature; k++) {
            featureScore += this.userFeatureMatrix.get(row, k) * this.itemFeatureMatrix.get(k, col);
        }
        return this.GLOBAL_MEAN +
                this.itemBias.get(col) +
                this.userBias.get(row) +
                featureScore;
    }
}
