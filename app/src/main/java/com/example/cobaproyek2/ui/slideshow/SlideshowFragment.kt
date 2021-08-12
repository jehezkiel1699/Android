package com.example.cobaproyek2.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cobaproyek2.R
import com.example.cobaproyek2.room.quotes.QuotesIslami
import com.example.cobaproyek2.room.quotes.QuotesIslamiDB
import kotlinx.android.synthetic.main.fragment_slideshow.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SlideshowFragment : Fragment() {

//    private lateinit var slideshowViewModel: SlideshowViewModel
    private lateinit var DB: QuotesIslamiDB

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        slideshowViewModel =
//                ViewModelProviders.of(this).get(SlideshowViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
//        val textView: TextView = root.findViewById(R.id.text_slideshow)
//        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return inflater.inflate(R.layout.fragment_slideshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DB = QuotesIslamiDB.invoke(this.requireContext())
        btnAddQuotesToRoom.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                DB.QuotesIslamiDAO().tambahQuotes(
                    QuotesIslami(
                        0,
                        txtQuotesInput.text.toString(),
                        "n"
                    )
                )

            }
            Toast.makeText(this.requireContext(), "Tambah Quotes Berhasil", Toast.LENGTH_LONG).show()
        }
    }
}
