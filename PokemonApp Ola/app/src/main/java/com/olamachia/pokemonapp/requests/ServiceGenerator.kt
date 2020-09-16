package com.olamachia.pokemonapp.requests

import com.olamachia.pokemonapp.util.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


/**
 * ServiceGenerator class with static field and members*/

class ServiceGenerator{

    companion object {

        fun getPokemonApi(): PokemonApi {

            val client = OkHttpClient.Builder().build() //creating the client

            /**
             * Using Retrofit builder to make network requests and specifying the base url
             * Using GsonConvertFactory to convert json representations to kotlin object*/

            //private val retrofitApi = retrofit.create(PokemonApi::class.java)

            return Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PokemonApi::class.java)
        }
    }

}