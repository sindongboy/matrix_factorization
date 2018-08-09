package com.skplanet.nlp;

/**
 * Project Static Properties
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 11/24/15
 */
public class Prop {
    // configuration file name
    public static final String MAIN_CONFIG = "main.properties";
    public static final String MOVIELENS_CONFIG = "movielens.properties";

    // COMMON
    // rating matrix path
    public static final String RATE_TBL = "RATE_TBL";
    // user info path
    public static final String USER_INFO = "USER_INFO";
    // item info path
    public static final String ITEM_INFO = "ITEM_INFO";
    // number of iteration
    public static final String ITER = "ITER";
    // learning rate
    public static final String LEARN_RATE = "LEARN_RATE";
    // regularization constant
    public static final String REG_CONST = "REG_CONST";
    // number of features
    public static final String FEAT_NUM = "FEAT_NUM";

    // Misc.
    public static final int SORT_ASCENDING = 1;
    public static final int SORT_DESCENDING = 1;


    // model output base
    public static final String MODEL_OUTPUT_PREFIX = "OUTPUT_BASE";
    // user matrix file extension
    public static final String USER_MAT_EXT = "USER_MAT_EXT";
    // item matrix file extension
    public static final String ITEM_MAT_EXT= "ITEM_MAT_EXT";
    // user bias file extension
    public static final String USER_BIAS_EXT = "USER_BIAS_EXT";
    // item bias file extension
    public static final String ITEM_BIAS_EXT = "ITEM_BIAS_EXT";

}
