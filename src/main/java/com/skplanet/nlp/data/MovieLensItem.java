package com.skplanet.nlp.data;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Item Object for Movielens Dataset
 *
 * @author Donghun Shin / donghun.shin@sk.com
 * @date 12/24/15
 */
public final class MovieLensItem extends AbstractItem {
    // logger
    private static final Logger LOGGER = Logger.getLogger(MovieLensItem.class.getName());

    /** item id */
    // declared in superclass

    /** index */
    // declared in superclass

    /** title */
    private String title;

    /** release date */
    private String releaseDate;

    /** video release date */
    private String videoReleaseDate;

    /** IMDB url */
    private String imdbUrl;

    /**
     * MovieLens has total of 19 genres, and it allows multiple genres per movie
     */
    private boolean [] genre = new boolean[19];

    /**
     * Constructor
     *
     * @param itemId item id
     * @param index index
     * @param title title
     * @param releaseDate release date
     * @param videoReleaseDate video release date
     * @param imdbUrl IMDB url
     * @param genreIndex genre index
     */
    public MovieLensItem(
            String itemId,
            int index,
            String title,
            String releaseDate,
            String videoReleaseDate,
            String imdbUrl,
            List<Integer> genreIndex
    ) {

        this.id = itemId;
        this.index = index;
        this.title = title;
        this.releaseDate = releaseDate;
        this.videoReleaseDate = videoReleaseDate;
        this.imdbUrl = imdbUrl;

        // initialize genre flag to all false
        for (int i = 0; i < 19; i++) {
            if (genreIndex.contains(i)) {
                genre[i] = true;
            } else {
                genre[i] = false;
            }
        }
    }

    /**
     * Get Movie Title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get Release Date
     * @return release date
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Get Video Release Date
     * @return video releate date
     */
    public String getVideoReleaseDate() {
        return videoReleaseDate;
    }

    /**
     * Get IMDB URL
     * @return imdb url
     */
    public String getImdbUrl() {
        return imdbUrl;
    }

    /**
     * Get Genre Flag
     * @return genre flag
     */
    public boolean[] getGenre() {
        return genre;
    }
}
