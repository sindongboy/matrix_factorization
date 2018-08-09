package com.skplanet.nlp.driver;

import com.skplanet.config.Configuration;
import com.skplanet.nlp.Prop;
import com.skplanet.nlp.algorithm.BiasedFactorizer;
import com.skplanet.nlp.algorithm.Factorizer;
import com.skplanet.nlp.cli.CommandLineInterface;
import com.skplanet.nlp.data.Item;
import com.skplanet.nlp.data.MovieLensItem;
import com.skplanet.nlp.data.User;
import com.skplanet.nlp.io.MovieLensMatrixReader;
import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * MovieLens Example
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public class MovieLensExample {
    // logger
    private static final Logger LOGGER = Logger.getLogger(MovieLensExample.class.getName());

    public static void main(String[] args) throws IOException {
        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("o", "output", true, "output file", false);
        cli.parseOptions(args);

        // get the data path and hyperparams
        Configuration movielensConfig = Configuration.getInstance();
        movielensConfig.loadProperties(Prop.MOVIELENS_CONFIG);

        // get rating table path
        String ratingTableName = movielensConfig.readProperty(Prop.MOVIELENS_CONFIG, Prop.RATE_TBL);
        URL ratingTableURL = movielensConfig.getResource(ratingTableName);
        if (ratingTableURL == null) {
            LOGGER.error("rating table doesn't exist: " + ratingTableName + ", check your classpath or the file name");
            System.exit(1);
        }

        // get user info. path
        String userInfoName = movielensConfig.readProperty(Prop.MOVIELENS_CONFIG, Prop.USER_INFO);
        URL userInfoURL = movielensConfig.getResource(userInfoName);
        if (userInfoURL == null) {
            LOGGER.error("user information file doesn't exist: " + userInfoName + ", check your classpath or the file name");
            System.exit(1);
        }

        // get item info. path
        String itemInfoName = movielensConfig.readProperty(Prop.MOVIELENS_CONFIG, Prop.ITEM_INFO);
        URL itemInfoURL = movielensConfig.getResource(itemInfoName);
        if (itemInfoURL == null) {
            LOGGER.error("item information file doesn't exist: " + itemInfoName + ", check your classpath or the file name");
            System.exit(1);
        }

        // get leraning rate
        double learningRate = Double.parseDouble(movielensConfig.readProperty(Prop.MOVIELENS_CONFIG, Prop.LEARN_RATE));

        // get reg. const.
        double regConst = Double.parseDouble(movielensConfig.readProperty(Prop.MOVIELENS_CONFIG, Prop.REG_CONST));

        // get number of feature
        int numFeat = Integer.parseInt(movielensConfig.readProperty(Prop.MOVIELENS_CONFIG, Prop.FEAT_NUM));

        // get number of iteration
        int iteration = Integer.parseInt(movielensConfig.readProperty(Prop.MOVIELENS_CONFIG, Prop.ITER));

        MovieLensMatrixReader reader = new MovieLensMatrixReader(
                userInfoURL.getFile(),
                itemInfoURL.getFile(),
                ratingTableURL.getFile()
        );

        LOGGER.info("load input matrices ....");
        // load user info
        reader.loadUserInfo();
        // load item info
        reader.loadItemInfo();
        // load rating table
        reader.loadRatingTable();
        LOGGER.info("load input matrices done");

        LOGGER.info("factorize the rating matrix ....");
        /*
        Factorizer factorizer = new BaseFactorizer(
        */
        Factorizer factorizer = new BiasedFactorizer(
                reader.getRatingMatrix(),
                numFeat, // number of feature
                iteration, // iteration
                learningRate, // learning rate
                regConst // regularization constant
        );
        factorizer.factorize();

        LOGGER.info("factorize the rating matrix done");


        DoubleMatrix predictions = factorizer.getPrediction();
        DoubleMatrix knowns = reader.getRatingMatrix();

        // index to user mapping
        Map<Integer, User> indexToUserMapping = reader.getIndexToUserMapping();

        // index to item mapping
        Map<Integer, Item> indexToItemMapping = reader.getIndexToItemMapping();



        if (cli.hasOption("o")) {
            File predictFile = new File(cli.getOption("o") + ".predict");
            File knownFile = new File(cli.getOption("o") + ".known");
            BufferedWriter predictWriter;
            BufferedWriter knownWriter;
            predictWriter = new BufferedWriter(new FileWriter(predictFile));
            knownWriter = new BufferedWriter(new FileWriter(knownFile));
            LOGGER.info("writing result ....");
            for (int row = 0; row < predictions.getRows(); row++) {
                User user = indexToUserMapping.get(row);
                for (int col = 0; col < predictions.getColumns(); col++) {
                    MovieLensItem movie = (MovieLensItem) indexToItemMapping.get(col);
                    if (knowns.get(row, col) > 0) {
                        knownWriter.write(user.getId() + "\t" + movie.getId() + "\t" + knowns.get(row, col));
                        knownWriter.newLine();
                    } else {
                        predictWriter.write(user.getId() + "\t" + movie.getId() + "\t" + predictions.get(row, col));
                        predictWriter.newLine();
                    }
                }
            }
            predictWriter.close();
            knownWriter.close();
            LOGGER.info("writing result done");
        }
    }
}
