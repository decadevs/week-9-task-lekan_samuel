package com.olamachia.pokemonapp.models
import com.google.gson.annotations.SerializedName
import com.olamachia.pokemonapp.models.BlackWhite

data class GenerationV(
    @SerializedName("black-white")
    val blackWhite: BlackWhite
)