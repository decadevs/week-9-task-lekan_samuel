package com.byoyedele.pokemoon

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// The RetrofitClient Object itself that handles the getting of the data and the conversion is
// also done here using GSON Converter Factory.
object RetrofitClient {
    var retrofit : Retrofit? = null
    fun getClient(baseUrl: String) : Retrofit {
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}
