package com.olamachia.pokemonapp.models

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("name")
    var name: String,
    @SerializedName("url")
    var url: String
)

