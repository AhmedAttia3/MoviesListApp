package com.smart.movieslist.data.storage.remote

import com.smart.movieslist.data.model.GenresResponse
import com.smart.movieslist.data.model.MovieDetailsResponse
import com.smart.movieslist.data.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesApiService {
    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page")page: Int,
        @Query("with_genres")genreId: Int?
    ): MoviesResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") searchQuery: String,
        @Query("page") page: Int
    ): MoviesResponse

    @GET("movie/{movieId}")
    suspend fun movieDetails(@Path("movieId")movieId: Int): MovieDetailsResponse


}