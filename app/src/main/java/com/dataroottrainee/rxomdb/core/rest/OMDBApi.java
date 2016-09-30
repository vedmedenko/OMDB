package com.dataroottrainee.rxomdb.core.rest;

import com.dataroottrainee.rxomdb.core.rest.models.Movie;
import com.dataroottrainee.rxomdb.core.rest.models.MoviesResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface OMDBApi {

    @GET("/")
    Observable<MoviesResponse> getMovies(@Query("s") String query, @Query("page") int page);

    @GET("/")
    Observable<Movie> getMovie(@Query("i") String imdbID);

}
