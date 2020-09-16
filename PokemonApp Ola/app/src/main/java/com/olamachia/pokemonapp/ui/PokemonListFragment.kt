package com.olamachia.pokemonapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.olamachia.pokemonapp.R
import com.olamachia.pokemonapp.requests.NetWorkConnection
import kotlinx.android.synthetic.main.fragment_pokemon_list.*

class PokemonListFragment: Fragment() {

    var isFragmentVisible: Boolean = true

    private lateinit var viewModel: PokemonViewModel //object of the PokemonViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java) // initializing the viewModel object

        return inflater.inflate(R.layout.fragment_pokemon_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var adapter: PokemonAdapter = PokemonAdapter(mutableListOf()) // instance/object of the PokemonAdapter class


        //viewModel.getPokemonItem()  // calling the getPokemonItem from the PokemonViewModel class

        viewModel.pokemonList.observe(viewLifecycleOwner, Observer {

            adapter.setMyList(it)   //observing the pokemonList from the PokeViewModel class and setting it to the PokemonAdapter
            // with the help of the setMyList method
        })
        rvPokemonItem.adapter = adapter
        rvPokemonItem.layoutManager = LinearLayoutManager(this.context)





        val networkConnection = NetWorkConnection(activity?.applicationContext!!)
        networkConnection.observe(requireActivity(), Observer {
            if (isFragmentVisible) {
                if (it){
                    viewModel.getPokemonItem()
                    networkAvailableResponse()
                } else {
                    noNetworkResponse()
                }
            }
        })



        /**
         * Adding click listeners to each item on the recyclerview
         * Setting and sending PokemonID and PokemonName to the displayPokemonfragment*/

        rvPokemonItem.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {
                val bundle = Bundle()
                bundle.putString("POKEMON_ID", "${position + 1}")

                val currentPokemonName = adapter.pokemonList[position]?.name

                bundle.putString("POKEMON_NAME", currentPokemonName)

                val displayPokemonFragment = DisplayPokemonFragment()
                displayPokemonFragment.arguments = bundle

                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, displayPokemonFragment)
                    addToBackStack(null)
                    commit()
                }
                //Toast.makeText(context, "$position", Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()

        isFragmentVisible = false
    }

    /**
     * High-order addOnItemClickListener */

    private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClick(holder.adapterPosition, view)
                }
            }

        })
    }

    /**
     * OnItemClickListener with the onItemClick method */

    interface OnItemClickListener{
        fun onItemClick(position: Int, view: View)
    }


    /**
     * noNetworkResponse method to set the visibilities of the views onfailure of the getPokemon
     * API Call*/

    fun noNetworkResponse() {
        tvErrorMessage.visibility = View.VISIBLE
        ivPokemonError.visibility = View.VISIBLE
        rvPokemonItem.visibility = View.GONE
    }

    fun networkAvailableResponse() {
        tvErrorMessage.visibility = View.GONE
        ivPokemonError.visibility = View.GONE
        rvPokemonItem.visibility = View.VISIBLE
    }




}