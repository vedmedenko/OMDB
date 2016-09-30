package com.dataroottrainee.rxomdb.core.rest.models;

public class Movie {

    public String Title;
    public String Year;
    public String Rated;
    public String Released;
    public String Runtime;
    public String Genre;
    public String Director;
    public String Writer;
    public String Actors;
    public String Plot;
    public String Language;
    public String Country;
    public String Awards;
    public String Poster;
    public String Metascore;
    public String imdbRating;
    public String imdbVotes;
    public String imdbID;
    public String Type;

    public com.dataroottrainee.rxomdb.core.storage.entities.Movie toDBType() {
        return com.dataroottrainee.rxomdb.core.storage.entities.Movie.FACTORY.creator.create(Title, Year, Rated, Released, Runtime, Genre,
                Director, Writer, Actors, Plot, Language, Country, Awards, Poster, Metascore, imdbRating, imdbVotes, imdbID, Type);
    }

}
