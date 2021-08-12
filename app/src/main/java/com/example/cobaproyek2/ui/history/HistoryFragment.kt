package com.example.cobaproyek2.ui.history

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cobaproyek2.HistoryAdapter

import com.example.cobaproyek2.R
import com.example.cobaproyek2.room.history.HistoryDB
import kotlinx.android.synthetic.main.history_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {

    private lateinit var historyAdapter: HistoryAdapter

    val HDB by lazy {
        HistoryDB(this.requireContext())
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }

//    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        // TODO: Use the ViewModel

        TampilkanData()
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val historysolat = HDB.HistoryDAO().getAllHistory()
            Log.d("history sholat", historysolat.toString())
            withContext(Dispatchers.Main) {
                historyAdapter.isiData(historysolat)
            }
        }
    }

    private fun TampilkanData()
    {
        recyclerHistory.layoutManager = LinearLayoutManager(this.requireContext())
        historyAdapter = HistoryAdapter(arrayListOf())
        recyclerHistory.adapter = historyAdapter
    }

}
