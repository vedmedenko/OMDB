package com.dataroottrainee.rxomdb.core.storage.entities;

import android.os.Parcelable;

import com.dataroottrainee.rxomdb.core.storage.MovieModel;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class Movie implements MovieModel, Parcelable {
    public static final Factory<Movie> FACTORY = new Factory<>(AutoValue_Movie::new);

    public static final RowMapper<Movie> SELECT_ALL_MAPPER = FACTORY.select_allMapper();

    public static final RowMapper<Movie> SELECT_BY_ID_MAPPER = FACTORY.select_by_idMapper();
}