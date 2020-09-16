package com.olamachia.pokemonapp.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.olamachia.pokemonapp.R


class PokemonListActivity : AppCompatActivity() {

    private lateinit var viewModel: PokemonViewModel    //object of the PokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_list)
        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java) //initializing the viewModel object


        //Moving to the next fragment
        supportFragmentManager.beginTransaction().apply {


            replace(R.id.flFragment, PokemonListFragment())
//            addToBackStack(null)
            commit()
        }

    }

}

