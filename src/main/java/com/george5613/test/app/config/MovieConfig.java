package com.george5613.test.app.config;

import java.util.Properties;

public class MovieConfig {

    private static final String KEY_SCREENSHOT_PATH = "SCREENSHOT_PATH";
    private static final String KEY_MOVIE_NAME = "MOVIE_NAME";
    private static final String KEY_PHONE_NUMBER = "PHONE_NUMBER";
    private static final String KEY_CINEMA_NAME = "CINEMA_NAME";
    private static final String KEY_CINEMA_FILTER = "CINEMA_FILTER";
    private static final String KEY_DATE = "DATE";
    private static final String KEY_TIME = "TIME";

    private String mScreenShotPath;
    private String mMovieName;
    private String mPhoneNumber;
    private String mCinemaName;
    private String mCinemaFilter;
    private String mDate;
    private String mTime;

    public MovieConfig(Properties properties) {
        if (properties == null) return;
        mScreenShotPath = properties.getProperty(KEY_SCREENSHOT_PATH);
        mMovieName = properties.getProperty(KEY_MOVIE_NAME);
        mPhoneNumber = properties.getProperty(KEY_PHONE_NUMBER);
        mCinemaName = properties.getProperty(KEY_CINEMA_NAME);
        mCinemaFilter = properties.getProperty(KEY_CINEMA_FILTER);
        mDate = properties.getProperty(KEY_DATE);
        mTime = properties.getProperty(KEY_TIME);
    }

    public String getScreenShotPath() {
        return mScreenShotPath;
    }

    public String getMovieName() {
        return mMovieName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getCinemaFilter() {
        return mCinemaFilter;
    }

    public String getCinemaName() {
        return mCinemaName;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }
}
