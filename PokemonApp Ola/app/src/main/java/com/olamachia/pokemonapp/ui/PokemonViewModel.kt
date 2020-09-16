package com.olamachia.pokemonapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.olamachia.pokemonapp.models.PokemonDetails
import com.olamachia.pokemonapp.models.PokemonItem
import com.olamachia.pokemonapp.models.Results
import retrofit2.Call
import retrofit2.Callback
import com.olamachia.pokemonapp.requests.ServiceGenerator
import retrofit2.Response


/**
 * ViewModel Class */

class PokemonViewModel(

): ViewModel() {

    private val _result = MutableLiveData<MutableList<Results?>>()
    val pokemonList: LiveData<MutableList<Results?>>
        get() = _result

    private val _pokemonDetails = MutableLiveData<PokemonDetails>()

    val pokemonDetails: LiveData<PokemonDetails>
        get() = _pokemonDetails

    var next: String? = null

    var failure = MutableLiveData<Boolean>()

    var failureTwo = MutableLiveData<Boolean>()


    /** API call to getPokemonItems from the API webservice */


    fun getPokemonItem() {
        ServiceGenerator.getPokemonApi().getPokemonItem().enqueue(
            object: Callback<PokemonItem> {
                override fun onResponse(
                    call: Call<PokemonItem>,
                    response: Response<PokemonItem>
                ) {
                    _result.value = response.body()?.results
                    failure.value = false

                }

                override fun onFailure(call: Call<PokemonItem>, t: Throwable) {
                    failure.value = true
                }
            }
        )
    }



    fun getPokemonItemProperties(id: String){
        ServiceGenerator.getPokemonApi().getPokemon(id).enqueue(
            object : Callback<PokemonDetails> {
                override fun onResponse(call: Call<PokemonDetails>, response: Response<PokemonDetails>) {

                    _pokemonDetails.value = response.body()
                }

                override fun onFailure(call: Call<PokemonDetails>, t: Throwable) {

                    t.message?.let { Log.d("FAILURE", it) }

                    failureTwo.value = true
                }

            }
        )

    }




    /** API call to getMorePokemonItems from the API webservice */

    fun getMorePokemon(next: String?) {

        ServiceGenerator.getPokemonApi().getMorePokemon(next!!).enqueue(
            object : Callback<PokemonItem> {
                override fun onResponse(
                    call: Call<PokemonItem>,
                    response: Response<PokemonItem>
                ) {
                    _result.value = response.body()?.results

                }

                override fun onFailure(call: Call<PokemonItem>, t: Throwable) {
                    failure.value = true
                }

            }
        )

    }



}