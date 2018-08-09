package com.skplanet.nlp.driver;

import com.skplanet.nlp.cli.CommandLineInterface;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MovieLens Data Evaluation Driver
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 1/26/16
 */
public class MovieLensEval {
    private static final Logger LOGGER = Logger.getLogger(MovieLensEval.class.getName());

    public static double error = 0.0;
    public static Map<String, Double> predictions = new HashMap<String, Double>();
    public static Map<String, Double> knowns = new HashMap<String, Double>();
    public static List<String> idList = new ArrayList<String>();
    public static void main(String[] args) throws IOException {
        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("p", "pred", true, "prediction file path", true);
        cli.addOption("k", "know", true, "known file path", true);
        cli.parseOptions(args);

        String line;
        // load prediction
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(new File(cli.getOption("p"))));
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }

            String[] fields = line.split("\\t");
            predictions.put(fields[0] + "_" + fields[1], Double.parseDouble(fields[2]));
        }
        reader.close();

        // load knowns
        reader = new BufferedReader(new FileReader(new File(cli.getOption("k"))));
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }

            String[] fields = line.split("\\t");
            idList.add(fields[0] + "_" + fields[1]);
            knowns.put(fields[0] + "_" + fields[1], Double.parseDouble(fields[2]));
        }
        reader.close();

        report();
    }

    static double RMSE() {
        double errorSum = 0.0;
        int count = 0;
        for (String id : idList) {
            if (count % 100 == 0) {
                LOGGER.info("evaluating: " + count);
            }
            count++;
            double pErr = predictions.get(id);
            double kErr = knowns.get(id);

            errorSum += Math.pow(kErr - pErr, 2);
        }
        return Math.sqrt(errorSum / idList.size());
    }

    static void report() {
        double rmse = RMSE();

        System.out.println("RMSE: " + rmse);
    }
}
