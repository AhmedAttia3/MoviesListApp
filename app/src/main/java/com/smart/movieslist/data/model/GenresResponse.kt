package com.smart.movieslist.data.model

import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres")
    var genresList: List<GenreModel>,
)