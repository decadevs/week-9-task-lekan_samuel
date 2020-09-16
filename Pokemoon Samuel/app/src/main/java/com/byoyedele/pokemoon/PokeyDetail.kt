package com.byoyedele.pokemoon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.byoyedele.pokemoon.jsonclasses.Ability
import com.byoyedele.pokemoon.jsonclasses.AbilityX
import kotlinx.android.synthetic.main.activity_pokey_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PokeyDetail : AppCompatActivity() {
    private lateinit var pokeyName: String
    private lateinit var pokeyUrl: String
    private var pokeyId: Int = 0
    private lateinit var service: PokemonApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokey_detail)

        // Get the values of the keys that were sent on click from Recycler Adapter.
        val bundle = intent
        pokeyName = ""
        pokeyUrl = ""
        pokeyId = 0

        if (bundle != null) {
            pokeyName = bundle.extras?.getString("NAME").toString().toUpperCase(Locale.ROOT)    // Get the value of the name and convert it to Uppercase.
            pokeyUrl = bundle.extras?.getString("URL").toString()                               // Get the value of the URL and set as PokeyURL
            pokeyId = bundle.extras?.getInt("ID") ?: 0                                          // Get the value of ID
        }

        val pokeyImage = findViewById<ImageView>(R.id.pokey_image)
        var pokeyNameSite = findViewById<TextView>(R.id.pokey_new_name)
        pokeyNameSite.text = pokeyName
        Glide.with(this).load(pokeyUrl).into(pokeyImage)


        service = Common.retrofitService

        service.getPokey(pokeyId).enqueue(object : Callback<Pokemon> {
            override fun onFailure(call: Call<Pokemon>, error: Throwable) {
                Toast.makeText(this@PokeyDetail, "$error", Toast.LENGTH_LONG).show()
            }

            // OnResponse of the Data, set them to the appropriate Textviews already set on the activity_pokey_detail XML file

            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.body() != null) {
                    height.text = "H: ${response.body()?.height.toString()}m"
                    weight.text = "W: ${response.body()?.weight.toString()}kg"
                    abilities_list.text = "Abilities: ${response.body()?.abilities?.joinToString { it.ability.name }}"
                    form_list.text = "Forms: ${response.body()?.forms?.joinToString { it.name }}"
                    baseExperience_int.text = "Base Experience: ${response.body()?.baseExperience.toString()}"
                    game_indices_list.text = "Game Indices: ${response.body()?.gameIndices?.joinToString { it.gameIndex.toString() }}"
                    held_items_list.text = "Held Items: ${response.body()?.heldItems?.joinToString { it.item.name }}"
                    moves_list.text = "Moves: ${response.body()?.moves?.joinToString { it.move.name }}"
                    order.text = "Order: ${response.body()?.order.toString()}"
                    species.text = "Species: ${response.body()?.species?.name.toString()}"
                    stats.text = "Stats: ${response.body()?.stats?.joinToString { it.stat.name }}"
                    types.text = "Types: ${response.body()?.types?.joinToString { it.type.name }}"
                } else {
                    Toast.makeText(this@PokeyDetail, "Wahala wao : ${response.body()}", Toast.LENGTH_LONG).show()
                    //On failure, display failure message
                }
            }
        })
    }
}