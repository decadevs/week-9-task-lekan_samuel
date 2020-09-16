package com.byoyedele.pokemoon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pokemon_template.view.*
import java.security.AccessController.getContext
import java.util.*

class RecyclerAdapter(val context: Context, private val pokemon: List<Result>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_template, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pokey = pokemon[position]
        holder.setData(pokey, position)
    }

    override fun getItemCount(): Int {
        return pokemon.size
    }




    inner class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var currentPokey: Result? = null
        var currentPos = 0
        var currentUrl = ""
        var currentId = 0
//
        // The onClickListener is set on the init here in the MyViewHolder inner class
        // This is used to launch a new activity which goes along with the necessary information.

        init {
            itemView.setOnClickListener {
//               Toast.makeText(context, "${currentPokey?.name} and ${currentPokey?.url}", Toast.LENGTH_LONG).show()
                val intent = Intent(itemView.context, PokeyDetail::class.java).apply {
                    putExtra("NAME", currentPokey?.name)
                    putExtra("URL", currentUrl)
                    putExtra("ID", currentId)
                }
                itemView.context.startActivity(intent)
            }
        }

        
        fun setData(pokey: Result?, position: Int) {
            itemView.pokey_name.text = pokey!!.name.toUpperCase(Locale.ROOT)
            val pokeyUrl = pokey.url
            val imgUrl = getPokeyId(pokeyUrl)
            this.currentUrl = imgUrl
            this.currentPokey = pokey
            this.currentPos = position
            this.currentId = pokeyUrl.substring(34, pokeyUrl.length-1).toInt()

            Glide.with(context).load(imgUrl).into(itemView.pokey_img)
            Glide.with(context).load(imgUrl).into(itemView.bg_img)
        }
    }

    private fun getPokeyId(item: String): String {
        val id = item.substring(34, item.length-1).toInt()
        return "https://pokeres.bastionbot.org/images/pokemon/$id.png"
    }
}