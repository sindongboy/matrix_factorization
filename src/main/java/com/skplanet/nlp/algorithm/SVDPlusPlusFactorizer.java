package com.skplanet.nlp.algorithm;

import com.skplanet.nlp.util.Util;
import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

import java.util.Map;
import java.util.Set;

/**
 * SVD++ Implementation
 * (Yehuda Koren, http://www.recsyswiki.com/wiki/SVD++)
 * <p>
 * biased matrix factorization model + implicit feedback information integrated
 * <p>
 * implicit feedback : number of user X number of item - it may contain click count or browsing or so.
 * implicit features : number of items X number of features - learnt by training.
 * <p>
 * Implementation summary!
 * <p>
 * 1. item feature 는 BiasedFactorized 와 동일하다
 * 2. user feature 는 변경 사항이 있다 기존의 err * Pu 부분의 Pu 를 Pu + Implicit feedback 으로 확장만 하면 된다
 * 3. 추가적으로 implicit feedback model parameter, Y[j,] 를 업데이트 시켜줘야 한다
 * 4. 나머지는 모두 동일하다
 * <p>
 * TODO: 구현은 완료됨 Performance Tuning needed
 *
 * @author Donghn Shin / donghun.shin@sk.com
 * @date 1/18/16
 */
public final class SVDPlusPlusFactorizer extends BiasedFactorizer {
    // log
    private static final Logger LOGGER = Logger.getLogger(SVDPlusPlusFactorizer.class.getName());

    /** implicit feedback information : this is to be learnt */
    private DoubleMatrix implicitFeatureMatrix;

    /** user index to list of implicit feedback index mapping */
    private Map<Integer, Set<Integer>> user2ImplicitMap;

    /**
     * Super Constructor, it initialize the feature matrices
     *
     * @param ratingMatrix given rating table
     * @param numFeature   number of latent feature
     */
    public SVDPlusPlusFactorizer(
            /** rating matrix */
            DoubleMatrix ratingMatrix,
            /** implicit feedback by user */
            Map<Integer, Set<Integer>> implicitFeedbackMap,
            /** number of feature */
            int numFeature,
            /** number of iteration */
            int numIter,
            /** learning rate */
            double learnRate,
            /** regularization constant */
            double regConst
    ) {
        super(ratingMatrix, numFeature, numIter, learnRate, regConst);

        /** copy implicit feedback mapping */
        this.user2ImplicitMap = implicitFeedbackMap;

        // initialize implicit features : i x k
        LOGGER.info("initialize implicit feature vector ....");
        double[][] implicitFeatureArray = new double[numItem][numFeature];
        for (int row = 0; row < numUser; row++) {
            for (int k = 0; k < numFeature; k++) {
                implicitFeatureArray[row][k] = Util.nextGaussian();
            }
        }
        this.implicitFeatureMatrix = new DoubleMatrix(implicitFeatureArray);
        LOGGER.info("initialize implicit feature vector done");

    }

    /**
     * Prediction
     * <p>
     * Prediction model is following.
     * <p>
     * global mean + user biases + item biases + item features * ( user features + user-item iteration ) +
     *
     * @param row row index
     * @param col column index
     * @return prediction for Rij
     */
    @Override
    protected double predict(int row, int col) {

        double featureScore = 0.0;
        for (int k = 0; k < this.numFeature; k++) {
            Set<Integer> ratedItemSet = this.user2ImplicitMap.get(row);
            double sum = 0;
            for (int ratedId : ratedItemSet) {
                sum += implicitFeatureMatrix.get(ratedId, k);
            }
            featureScore += (this.userFeatureMatrix.get(row, k) + sum / Math.sqrt((double) ratedItemSet.size())) * this.itemFeatureMatrix.get(k, col);
        }

        return  this.GLOBAL_MEAN + this.itemBias.get(col) + this.userBias.get(row) + featureScore;
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
     * Update Feature Matrices
     *
     * @param row   row index
     * @param col   column index
     * @param error error
     */
    @Override
    protected void update(int row, int col, double error) {

        // update user bias : b_u + b_i
        double prevUserBias = this.userBias.get(row);
        double prevItemBias = this.itemBias.get(col);
        double updateUserBias = prevUserBias + this.learningRate * (error - this.regularization * prevUserBias);
        double updateItemBias = prevItemBias + this.learningRate * (error - this.regularization * prevItemBias);
        this.userBias.put(row, updateUserBias);
        this.itemBias.put(col, updateItemBias);

        // update user / item features : q_i, q_u
        Set<Integer> ratedSet = this.user2ImplicitMap.get(row);
        for (int k = 0; k < this.numFeature; k++) {
            // pre-calculation
            double prevUserFeature = this.userFeatureMatrix.get(row, k);
            double prevItemFeature = this.itemFeatureMatrix.get(k, col);

            double sum = 0;
            for (int ratedId : ratedSet) {
                sum += this.itemFeatureMatrix.get(ratedId, k);
            }

            // p_u
            double updateUserFeature = prevUserFeature + this.learningRate * (error
                    * (prevItemFeature + (sum / Math.sqrt((double) ratedSet.size())))
                    - this.regularization * prevUserFeature);
            this.userFeatureMatrix.put(row, k, updateUserFeature);

            // q_i
            double updateItemFeature = prevItemFeature + this.learningRate * (error * prevUserFeature - this.regularization * prevItemFeature);
            this.itemFeatureMatrix.put(k, col, updateItemFeature);

            // update implicit feature : y_j
            for (int ratedId : ratedSet) {
                    double prevYj = this.implicitFeatureMatrix.get(ratedId, k);
                    double updateYj = prevYj + this.learningRate * (error * prevItemFeature / Math.sqrt((double)ratedSet.size()) - this.regularization * prevYj);
                    this.implicitFeatureMatrix.put(ratedId, k, updateYj);
            }
        }


    }

    /**
     * Factorize the matrix into two sub-matrices
     */
    @Override
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
                        LOGGER.debug("local error: " + localError);
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
     * Squared Error
     *
     * @param row row index
     * @param col column index
     * @return squared error
     */
    @Override
    protected double squaredError(int row, int col) {
        return super.squaredError(row, col);
    }
}
