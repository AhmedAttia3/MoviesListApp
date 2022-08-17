package com.smart.movieslist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.movieslist.repository.MoviesRepository
import com.smart.movieslist.utils.DataResource
import com.smart.movieslist.utils.UiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject
constructor(
    private val repository: MoviesRepository
) : ViewModel() {
    private val _state:MutableStateFlow<UiStates> = MutableStateFlow(UiStates.Loading)
    val state :StateFlow<UiStates> = _state


    fun getGenres(){
        viewModelScope.launch {
            _state.value = UiStates.Loading
            when(val response = repository.getGenres()){
                is DataResource.Error -> _state.value = UiStates.Error(response.exception)
                is DataResource.Success -> {
                    _state.value = UiStates.Success(response.value)
                }
            }
        }
    }
}
