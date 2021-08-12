package com.example.cobaproyek2

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cobaproyek2.room.quotes.QuotesIslami
import com.example.cobaproyek2.room.quotes.QuotesIslamiDB
import kotlinx.android.synthetic.main.item_quotes.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class QuotesAdapter (private val quotess: ArrayList<QuotesIslami>): RecyclerView.Adapter<QuotesAdapter.quotesViewHolder>() {

    var listenerLike: RecyclerViewClickListener?=null
    var listenerUnlike: RecyclerViewClickListener?=null

    interface RecyclerViewClickListener{
        fun like(view: View, id: Int)
        fun unlike(view: View, id: Int)
    }

    inner class quotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txtItemQuotes: TextView = itemView.txtItemQuotes
        var btnLike: ImageView = itemView.btnLike
        var btnLiked: ImageView = itemView.btnLiked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): quotesViewHolder {
//        TODO("not implemented")
        //To change body of created functions use File | Settings | File Templates.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_quotes, parent, false)

        return quotesViewHolder(view)
    }

    override fun getItemCount(): Int {
//        TODO("not implemented")
        //To change body of created functions use File | Settings | File Templates.
        return quotess.size
    }

    override fun onBindViewHolder(holder: quotesViewHolder, position: Int) {
//        TODO("not implemented")
        //To change body of created functions use File | Settings | File Templates.
        var dataQuotes = quotess[position]
        holder.txtItemQuotes.text = dataQuotes.quotes

        if (dataQuotes.liked == "y"){
            holder.btnLike.visibility = View.INVISIBLE
            holder.btnLiked.visibility = View.VISIBLE
            holder.btnLiked.setOnClickListener {
                listenerUnlike?.unlike(it, dataQuotes.id)
            }
        }
        else{
            holder.btnLike.visibility = View.VISIBLE
            holder.btnLiked.visibility = View.INVISIBLE
            holder.btnLike.setOnClickListener {
                listenerLike?.like(it, dataQuotes.id)
            }
        }
    }

    fun isiData(list: List<QuotesIslami>){
        quotess.clear()
        quotess.addAll(list)
        notifyDataSetChanged()
    }

}