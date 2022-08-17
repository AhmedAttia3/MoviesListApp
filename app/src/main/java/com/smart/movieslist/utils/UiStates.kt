package com.smart.movieslist.utils

sealed class UiStates{
    object Loading : UiStates()
    data class Success<out T : Any>(val data: T?) : UiStates()
    data class Error(val exception: Throwable) : UiStates()
}