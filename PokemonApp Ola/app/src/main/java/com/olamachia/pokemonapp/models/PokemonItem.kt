package com.olamachia.pokemonapp.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PokemonItem(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: ArrayList<Results?>

)