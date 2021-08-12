package com.example.cobaproyek2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cobaproyek2.room.history.History
import kotlinx.android.synthetic.main.item_history.view.*



class HistoryAdapter (private val historysolat : ArrayList<History>):RecyclerView.Adapter<HistoryAdapter.historyViewHolder>() {

    inner class historyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txtItemHistory: TextView = itemView.txtItemHistory
        var txtItemHistoryTgl : TextView = itemView.txtItemHistoryTgl
        var txtItemHistoryWaktu : TextView = itemView.txtItemHistoryWaktu

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.historyViewHolder {
        //TODO("Not yet implemented")
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)

        return historyViewHolder(view)
    }

    override fun getItemCount(): Int {
        //TODO("Not yet implemented")
        return historysolat.size
    }

    override fun onBindViewHolder(holder: HistoryAdapter.historyViewHolder, position: Int) {
        //TODO("Not yet implemented")
        var dataHistory = historysolat[position]
        holder.txtItemHistory.text = dataHistory.namasolat
        holder.txtItemHistoryTgl.text = dataHistory.tgl
        holder.txtItemHistoryWaktu.text = dataHistory.waktu
    }

    fun isiData(list: List<History>){
        historysolat.clear()
        historysolat.addAll(list)
        notifyDataSetChanged()
    }

}