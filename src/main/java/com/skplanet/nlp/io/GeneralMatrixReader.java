package com.skplanet.nlp.io;

import com.skplanet.nlp.data.*;
import org.apache.log4j.Logger;
import org.jblas.DoubleMatrix;

import java.io.*;
import java.util.*;

/**
 * Matrices Reader for general purpose
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 01/29/16
 */
public class GeneralMatrixReader extends AbstractMatrixReader {
    // logger
    private static final Logger LOGGER = Logger.getLogger(GeneralMatrixReader.class.getName());

    private DoubleMatrix ratingMatrix;

    /** user information path */
    private String userInfoPath;

    /** item information path */
    private String itemInfoPath;

    /** rating table path */
    private String ratingPath;

    /** index to {@link User} mapping */
    Map<Integer, User> indexToUserMapping = null;

    /** {@link User} to index mapping */
    Map<User, Integer> userToIndexMapping = null;

    /** user id to index mapping */
    Map<String, Integer> userIdToIndexMapping = null;

    /** index to {@link Item} mapping */
    Map<Integer, Item> indexToItemMapping = null;

    /** {@link Item} to index mapping */
    Map<Item, Integer> itemToIndexMapping = null;

    /** user id to index mapping */
    Map<String, Integer> itemIdToIndexMapping = null;

    /**
     * Constructor
     *
     * @param userInfoPath user information file
     * @param itemInfoPath item information file
     * @param ratingPath rating table file
     */
    public GeneralMatrixReader(String userInfoPath, String itemInfoPath, String ratingPath) {
        this.userInfoPath = userInfoPath;
        this.itemInfoPath = itemInfoPath;
        this.ratingPath = ratingPath;
    }


    /**
     * Load User Information
     *
     * <only contains user id>
     *
     * @return {@link Map <Integer,  User >} index to {@link User} mapping
     */
    public void loadUserInfo() {
        this.indexToUserMapping = new HashMap<Integer, User>();
        String line;
        File file = new File(this.userInfoPath);

        List<User> userList = null;
        List<Integer> indexList = null;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            int index = 0;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }

                /** to ignore all field but 0 */
                String[] fields = line.trim().split("\\t");

                String id = fields[0];

                // create user
                User user = new GeneralUser(id);
                if (userList == null) {
                    userList = new ArrayList<User>();
                }
                if (indexList == null) {
                    indexList = new ArrayList<Integer>();
                }
                userList.add(user);
                indexList.add(index++);
            }
            reader.close();

            // shuffle index
            Collections.shuffle(indexList);
            // and set index to each user object
            index = 0;
            for (User user : userList) {
                user.setIndex(indexList.get(index++));
            }

            // index mapping
            for (int i = 0; i < userList.size(); i++) {
                if (this.indexToUserMapping == null) {
                    this.indexToUserMapping = new HashMap<Integer, User>();
                }
                if (this.userToIndexMapping == null) {
                    this.userToIndexMapping = new HashMap<User, Integer>();
                }
                if (this.userIdToIndexMapping == null) {
                    this.userIdToIndexMapping = new HashMap<String, Integer>();
                }

                this.indexToUserMapping.put(indexList.get(i), userList.get(i));
                this.userToIndexMapping.put(userList.get(i), indexList.get(i));
                this.userIdToIndexMapping.put(userList.get(i).getId(), indexList.get(i));
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("can't find the file: " + file.getName(), e);
        } catch (IOException e) {
            LOGGER.error("can't read the file: " + file.getName(), e);
        }
    }

    /**
     * Load Item Information
     *
     * @return {@link Map <Integer, Item>} index to {@link Item} mapping
     */
    public void loadItemInfo() {
        this.indexToItemMapping = new HashMap<Integer, Item>();
        String line;
        File file = new File(this.itemInfoPath);

        List<Item> itemList = null;
        List<Integer> indexList = null;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            int index = 0;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }

                /** to ignore all field but 0 */
                String[] fields = line.trim().split("\\t");


                String id = fields[0];

                // create item
                Item item = new GeneralItem(id);

                if (itemList == null) {
                    itemList = new ArrayList<Item>();
                }
                if (indexList == null) {
                    indexList = new ArrayList<Integer>();
                }

                itemList.add(item);
                indexList.add(index++);
            }
            reader.close();

            // shuffle index
            Collections.shuffle(indexList);
            // and set index to each item object

            index = 0;
            for (Item item : itemList) {
                item.setIndex(index++);
            }

            // index mapping
            for (int i = 0; i < itemList.size(); i++) {
                if (this.indexToItemMapping == null) {
                    this.indexToItemMapping = new HashMap<Integer, Item>();
                }
                if (this.itemToIndexMapping == null) {
                    this.itemToIndexMapping = new HashMap<Item, Integer>();
                }
                if (this.itemIdToIndexMapping == null) {
                    this.itemIdToIndexMapping = new HashMap<String, Integer>();
                }

                this.indexToItemMapping.put(indexList.get(i), itemList.get(i));
                this.itemToIndexMapping.put(itemList.get(i), indexList.get(i));
                this.itemIdToIndexMapping.put(itemList.get(i).getId(), indexList.get(i));
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("can't find the file: " + file.getName(), e);
        } catch (IOException e) {
            LOGGER.error("can't read the file: " + file.getName(), e);
        }
    }

    /**
     * Load Matrix to {@link DoubleMatrix}
     *
     * @return {@link DoubleMatrix}
     */
    public void loadRatingTable() {

        // is user info loaded?
        if (this.indexToUserMapping == null) {
            LOGGER.error("User Information not loaded!: " + this.userInfoPath);
            throw new NullPointerException();
        }

        // is item info loaded?
        if (this.indexToItemMapping == null) {
            LOGGER.error("Item Information not loaded!: " + this.itemInfoPath, new NullPointerException());
            throw new NullPointerException();
        }

        int numUser = this.indexToUserMapping.keySet().size();
        int numItem = this.indexToItemMapping.keySet().size();

        double[][] data = new double[numUser][numItem];

        // initialize the raw data array.
        for (int row = 0; row < numUser; row++) {
            for (int col = 0; col < numItem; col++) {
                data[row][col] = 0;
            }
        }

        File file = new File(this.ratingPath);
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(new FileReader(file));
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }

                if (count % 1000 == 0) {
                    LOGGER.info("rating table loading: " + count);
                }

                String[] fields = line.trim().split("\\t");

                if (fields.length != 3) {
                    LOGGER.warn("Rating Table must have 3 fields per line: " + line);
                    continue;
                }

                String userId = fields[0].trim();
                String itemId = fields[1].trim();
                double rate = Double.parseDouble(fields[2].trim());

                int userIndex = this.userIdToIndexMapping.get(userId);
                int itemIndex = this.itemIdToIndexMapping.get(itemId);

                data[userIndex][itemIndex] = rate;

                count++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("can't find the file: " + file.getName(), e);
        } catch (IOException e) {
            LOGGER.error("can't read the file: " + file.getName(), e);
        }

        this.ratingMatrix = new DoubleMatrix(data);
    }

    /**
     * Get Rating Matrix
     * @return rating matrix {@link DoubleMatrix}
     */
    public DoubleMatrix getRatingMatrix() {
        if (ratingMatrix == null) {
            LOGGER.error("Rating Matrix isn't loaded yet!");
            throw new NullPointerException();
        }
        return ratingMatrix;
    }

    /**
     * Get index to {@link User} mapping
     * @return index to {@link User} mapping
     */
    public Map<Integer, User> getIndexToUserMapping() {
        return indexToUserMapping;
    }

    /**
     * Get {@link User} to index mapping
     * @return {@link User} to index mapping
     */
    public Map<User, Integer> getUserToIndexMapping() {
        return userToIndexMapping;
    }

    /**
     * Get user id to index mapping
     * @return user id to index mapping
     */
    public Map<String, Integer> getUserIdToIndexMapping() {
        return userIdToIndexMapping;
    }

    /**
     * Get {@link Item} to index mapping
     * @return {@link Item} to index mapping
     */
    public Map<Integer, Item> getIndexToItemMapping() {
        return indexToItemMapping;
    }

    /**
     * Get {@link Item} to index mapping
     * @return {@link Item} to index mapping
     */
    public Map<Item, Integer> getItemToIndexMapping() {
        return itemToIndexMapping;
    }

    /**
     * Get item id to index mapping
     * @return item id to index mapping
     */
    public Map<String, Integer> getItemIdToIndexMapping() {
        return itemIdToIndexMapping;
    }
}
