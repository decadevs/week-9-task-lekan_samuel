package com.olamachia.pokemonapp.models
import com.google.gson.annotations.SerializedName
import com.olamachia.pokemonapp.models.DreamWorld
import com.olamachia.pokemonapp.models.OfficialArtwork

data class Other(
    @SerializedName("dream_world")
    val dreamWorld: DreamWorld,
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)