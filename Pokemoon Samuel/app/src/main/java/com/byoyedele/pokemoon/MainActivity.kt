package com.byoyedele.pokemoon

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var service: PokemonApi
    lateinit var mAdapter: RecyclerAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkConnectivity()
        val setLimit = findViewById<EditText>(R.id.limit)
        setLimit.setText("100")
        var limit: Int = setLimit.text.toString().toInt()
        service = Common.retrofitService
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        launchService(limit)

        set_limit.setOnClickListener {
            limit = setLimit.text.toString().toInt()
            launchService(limit)
        }

        refresh_button.setOnClickListener {
            recreate()
        }
    }

    private fun launchService(limit: Int) {
        service.get(limit).enqueue(object : Callback<PokeAll> {
            override fun onFailure(call: Call<PokeAll>, error: Throwable) {
                Toast.makeText(this@MainActivity, "$error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<PokeAll>, response: Response<PokeAll>) {
                mAdapter = RecyclerAdapter(baseContext, response.body()?.results!!)
                mAdapter.notifyDataSetChanged()
                recyclerView.adapter = mAdapter
            }
        })
    }

    private fun checkConnectivity(){
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (!isConnected){
            Toast.makeText(this, "Network not connected, Kindly check connection.", Toast.LENGTH_LONG).show()
            refresh_button.visibility = View.VISIBLE
        } else refresh_button.visibility = View.GONE
    }
}