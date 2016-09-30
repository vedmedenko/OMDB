package com.dataroottrainee.rxomdb.core.rest.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviesResponse {

    @SerializedName("Search")
    List<Movie> movies;

    @SerializedName("totalResults")
    int count;

    @SerializedName("Response")
    boolean ok;

    public MoviesResponse() {
        movies = new ArrayList<Movie>();
    }

    public ArrayList<com.dataroottrainee.rxomdb.core.storage.entities.Movie> getMovies() {
        ArrayList<com.dataroottrainee.rxomdb.core.storage.entities.Movie> m = new ArrayList<>();
        for (Movie mov : movies) {
            m.add(mov.toDBType());
        }
        return m;
    }

    public boolean getOK() {
        return ok;
    }
}
