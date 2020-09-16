package com.olamachia.pokemonapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.olamachia.pokemonapp.R
import com.olamachia.pokemonapp.models.PokemonDetails
import com.olamachia.pokemonapp.requests.NetWorkConnection
import com.olamachia.pokemonapp.requests.ServiceGenerator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import kotlinx.android.synthetic.main.pokemon_display.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class DisplayPokemonFragment: Fragment() {


    private lateinit var viewModel: PokemonViewModel
    val baseUrl = "https://pokeres.bastionbot.org/images/pokemon"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
        return inflater.inflate(R.layout.pokemon_display, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /** getting the set arguments from the pokemonListFragment screen*/

        val pokemonID = this.arguments?.getString("POKEMON_ID")
        val pokemonName = this.arguments?.getString("POKEMON_NAME")



        /** Getting and setting the display image for the pokemon display page*/

        Picasso.get()
            .load("$baseUrl/$pokemonID.png")
            .into(ivPokemonDisplay)


        tvDisplayName.text = pokemonName



        val networkConnection = NetWorkConnection(activity?.applicationContext!!)
        networkConnection.observe(requireActivity(), Observer {

                if (it) {

                    if (pokemonID != null) {
                        viewModel.getPokemonItemProperties(pokemonID)
                    }
                    networkAvailableForPokemonFetch()

                } else {
                    noNetworkForPokemonFetch()
                }

        })

        /**Observing onResponse of the getPokemonItemDetails*/

        viewModel.pokemonDetails.observe(viewLifecycleOwner, Observer {
            tvDisplayWeight.text = it.weight.toString()
            tvDisplayOrder.text = it.order.toString()
            tvDisplayOrder.text = it.height.toString()
            tvDisplayExp.text = it.baseExperience.toString()


            var sb = StringBuilder()

            for (i in it.abilities.indices){
                sb.append(it.abilities[i].ability.name).append("\n").append("\n")
            }

            tvAbilities.text = sb.toString()
            sb.clear()

            for (i in it.stats.indices){
                sb.append(it.stats[i].stat.name).append("\n").append("\n")
            }


            tvStat.text = sb.toString()
            sb.clear()

            for (i in 0..4){
                sb.append(it.moves[i].move.name).append("\n").append("\n")
            }
            tvMoves.text = sb.toString()

        })


        /**Observing onFailure of the getPokemonItemDetails*/

//        viewModel.failureTwo.observe(viewLifecycleOwner, Observer {
//            if (it == true) noNetworkForPokemonFetch()
//        })


    }



    private fun noNetworkForPokemonFetch() {
        tvNameTitle.visibility = View.GONE
        tvWeightTitle.visibility = View.GONE
        tvOrderTitle.visibility = View.GONE
        tvBaseExpTitle.visibility = View.GONE
        tvStat.visibility = View.GONE
        tvAbilities.visibility = View.GONE
        tvMoves.visibility = View.GONE
        abilitiesTV.visibility = View.GONE
        moves.visibility = View.GONE
        statsTV.visibility = View.GONE
        tvDisplayName.visibility = View.GONE
        ivPokemonDisplay.visibility = View.GONE
        tvDisplayWeight.visibility = View.GONE
        tvDisplayExp.visibility = View.GONE
        tvDisplayOrder.visibility = View.GONE

        tvPokemonError.visibility = View.VISIBLE
        ivPokemonErrorTwo.visibility = View.VISIBLE
    }

    private fun networkAvailableForPokemonFetch() {
        tvNameTitle.visibility = View.VISIBLE
        tvWeightTitle.visibility = View.VISIBLE
        tvOrderTitle.visibility = View.VISIBLE
        tvBaseExpTitle.visibility = View.VISIBLE
        tvStat.visibility = View.VISIBLE
        tvAbilities.visibility = View.VISIBLE
        tvMoves.visibility = View.VISIBLE
        abilitiesTV.visibility = View.VISIBLE
        moves.visibility = View.VISIBLE
        statsTV.visibility = View.VISIBLE
        tvDisplayName.visibility = View.VISIBLE
        ivPokemonDisplay.visibility = View.VISIBLE

        tvPokemonError.visibility = View.GONE
        ivPokemonErrorTwo.visibility = View.GONE
    }

}