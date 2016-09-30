package com.dataroottrainee.rxomdb.util;

public final class ConstantsManager {

    // Main Activity constants.

    public static final String MA_BOOL_TV_HELP_VISIBILITY = "com.dataroottrainee.rxomdb.ui.Main.MainActivity.TV_HELP_VISIBILITY";
    public static final String MA_STRING_ET_TEXT = "com.dataroottrainee.rxomdb.ui.Main.MainActivity.ET_TEXT";
    public static final String MA_ARRAYLIST_MOVIES = "com.dataroottrainee.rxomdb.ui.Main.MainActivity.MOVIES";
    public static final String MA_HELP_TEXT = "com.dataroottrainee.rxomdb.ui.Main.MainActivity.HELP_TEXT";

    // Detail Activity constants.

    public static final String DA_EXTRA_MOVIE = "com.dataroottrainee.rxomdb.ui.Detail.DetailActivity.EXTRA_MOVIE";
    public static final String DA_EXTRA_CACHED = "com.dataroottrainee.rxomdb.ui.Detail.DetailActivity.EXTRA_CACHED";

    // Detail Fragment constants.

    public static final String DF_ARG_MAP = "com.dataroottrainee.rxomdb.ui.Detail.DetailFragment.ARG_MAP";
    public static final String DF_ARG_POSTER = "com.dataroottrainee.rxomdb.ui.Detail.DetailFragment.ARG_POSTER";

    // Service constants.

    public static final String SE_QUERY = "com.dataroottrainee.rxomdb.core.services.LoadMoviesService.QUERY";
    public static final String SE_PAGE = "com.dataroottrainee.rxomdb.core.services.LoadMoviesService.PAGE";
    public static final String SE_IMDB_ID = "com.dataroottrainee.rxomdb.core.services.LoadMoviesService.IMDB_ID";
    public static final String SE_ADD = "com.dataroottrainee.rxomdb.core.services.LoadMoviesService.ADD";

    public static final String SE_ERROR = "com.dataroottrainee.rxomdb.core.services.LoadMoviesService.ERROR";

    public static final String SE_BROADCAST_ACTION_MOVIES = "com.dataroottrainee.rxomdb.core.services.LoadMoviesService.BROADCAST_ACTION_MOVIES";
    public static final String SE_ARRAYLIST_MOVIES = "com.dataroottrainee.rxomdb.core.services.LoadMoviesService.ARRAYLIST_MOVIES";
    public static final String SE_OK = "com.dataroottrainee.rxomdb.core.services.LoadMoviesService.OK";

    public static final String SE_BROADCAST_ACTION_MOVIE = "com.dataroottrainee.rxomdb.core.services.LoadMoviesService.BROADCAST_ACTION_MOVIE";
    public static final String SE_MOVIE = "com.dataroottrainee.rxomdb.core.services.LoadMoviesService.MOVIE";

    // DB constants.

    public static final String DATABASE_NAME = "MoviesDB";
    public static final int DATABASE_VERSION = 1;

    // Retrofit constants.

    public static final String BASE_URL = "http://www.omdbapi.com/";

    // RestModule constants.

    public static final int CONNECTION_TIME_OUT = 50;
    public static final int READ_TIME_OUT = 50;

    private ConstantsManager() {
        throw new AssertionError("No instances");
    }
}
