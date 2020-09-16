package com.olamachia.pokemonapp.requests

import com.olamachia.pokemonapp.models.PokemonDetails
import com.olamachia.pokemonapp.models.PokemonItem
import retrofit2.Call
import retrofit2.http.*

/**
 * API Queries to get pokemon details from the Webservice*/

interface PokemonApi {
    @GET("pokemon?limit=40&offset=0/")
    fun getPokemonItem(): Call<PokemonItem> // Query to get all Pokemon Items

    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") key: String): Call<PokemonDetails> // Query to get each pokemon's details

    @GET
    fun getMorePokemon(@Url url: String): Call<PokemonItem>
}