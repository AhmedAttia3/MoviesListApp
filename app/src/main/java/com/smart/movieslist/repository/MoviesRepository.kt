package com.smart.movieslist.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.smart.movieslist.data.model.GenreModel
import com.smart.movieslist.data.model.GenresResponse
import com.smart.movieslist.data.model.MovieDetailsResponse
import com.smart.movieslist.data.model.MovieModel
import com.smart.movieslist.data.storage.local.db.AppDao
import com.smart.movieslist.data.storage.remote.MoviesApiService
import com.smart.movieslist.utils.DataResource
import com.smart.movieslist.utils.safeApiCall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository class that works with local and remote data sources.
 */

class MoviesRepository
@Inject
constructor(
    private val service: MoviesApiService,
    private val db: AppDao
) {


    suspend fun getGenres(): DataResource<GenresResponse> {
        /** this delay to can see loading state this server is super fast 😂😂😂 */
        delay(1000)
        return safeApiCall {
            val genres = service.getGenres()
            val list = genres.genresList.toMutableList()
            list.add(0, GenreModel(0, "All"))
            genres.genresList = list
            genres
        }
    }

    fun getSearchResultStream(query: String, genreId: Int?): Flow<PagingData<MovieModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = { MoviesPagingSource(service, db, query, genreId) }
        ).flow
    }

    fun addMoveToFavorite(move: MovieModel) {
        db.addMoveToFavorite(move)
    }

    fun removeItemFromFavorite(move: MovieModel) {
        db.removeMoveFromFavorite(move.id)
    }

    suspend fun getMovieDetails(movieId: Int): DataResource<MovieDetailsResponse> {
        return safeApiCall { service.movieDetails(movieId) }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
        const val NETWORK_PAGE_SIZE = 20
    }
}
