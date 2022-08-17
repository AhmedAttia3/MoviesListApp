

package com.smart.movieslist.ui.moviesFragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.smart.movieslist.data.model.MovieModel
import com.smart.movieslist.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject
constructor(
    private val repository: MoviesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Stream of immutable states representative of the UI.
     */
    val uiState: StateFlow<PagingUiState>

    val pagingDataFlow: Flow<PagingData<MovieModel>>

    /**
     * Processor of side effects from the UI which in turn feedback into [uiState]
     */
    val uiActions: (PagingUiAction) -> Unit

    init {
        val genreId: Int? = savedStateHandle[GENRE_ID]
        val lastQueryScrolled: String = savedStateHandle[LAST_QUERY_SCROLLED] ?: DEFAULT_QUERY

        val actionStateFlow = MutableSharedFlow<PagingUiAction>()

        val searches = actionStateFlow
            .filterIsInstance<PagingUiAction.Start>()
            .distinctUntilChanged()

        val queriesScrolled = actionStateFlow
            .filterIsInstance<PagingUiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(
                PagingUiAction.Scroll(
                    currentQuery = lastQueryScrolled,
                    genreId = genreId
                )
            ) }

        pagingDataFlow = searches
            .flatMapLatest { searchMovie(queryString = it.query, genreId =it.genreId) }
            .cachedIn(viewModelScope)

        uiState = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) ->
            PagingUiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                genreId = search.genreId,
                // If the search query matches the scroll query, the user has scrolled
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = PagingUiState()
            )

        uiActions = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_QUERY_SCROLLED] = uiState.value.lastQueryScrolled
        savedStateHandle[GENRE_ID] = uiState.value.genreId
        super.onCleared()
    }


    private fun searchMovie(queryString: String, genreId: Int?): Flow<PagingData<MovieModel>> =
        repository.getSearchResultStream(queryString, genreId)



    fun addMovieToFavorite(MovieModel: MovieModel) {
        repository.addMoveToFavorite(MovieModel)
    }

    fun removeMovieFromFavorite(MovieModel: MovieModel) {
        repository.removeItemFromFavorite(MovieModel)
    }


}

sealed class PagingUiAction {
    data class Start(val query: String = "", val genreId:Int? = null) : PagingUiAction()
    data class Scroll(val currentQuery: String, val genreId:Int?) : PagingUiAction()
}

data class PagingUiState(
    val genreId: Int? = null,
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)

private const val GENRE_ID: String = "genre_id"
private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
private const val DEFAULT_QUERY = ""