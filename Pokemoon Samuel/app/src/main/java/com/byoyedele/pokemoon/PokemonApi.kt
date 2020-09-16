package com.byoyedele.pokemoon

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// The main GET methods are declared here within this interface for the two kinds of getting we'll
// be using in this app following the two different routes.

interface PokemonApi {

    @GET("pokemon")
    fun get(@Query("limit") limit: Int):Call<PokeAll>

    @GET("pokemon/{id}")
    fun getPokey(@Path("id")id: Int):Call<Pokemon>
}