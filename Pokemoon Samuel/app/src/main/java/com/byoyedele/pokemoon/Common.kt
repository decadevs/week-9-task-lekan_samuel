package com.byoyedele.pokemoon

// The get function for the PokemonAPI is called here using the getClient method from the RetrofitClient

object Common {
    val retrofitService: PokemonApi
        get() = RetrofitClient.getClient("https://pokeapi.co/api/v2/").create(PokemonApi::class.java)
}



