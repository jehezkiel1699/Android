package com.example.cobaproyek2.ui.gallery

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cobaproyek2.QuotesAdapter
import com.example.cobaproyek2.R
import com.example.cobaproyek2.room.quotes.QuotesIslami
import com.example.cobaproyek2.room.quotes.QuotesIslamiDB
import com.example.cobaproyek2.ui.slideshow.SlideshowFragment
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GalleryFragment : Fragment(), QuotesAdapter.RecyclerViewClickListener {

//    private lateinit var galleryViewModel: GalleryViewModel

    private lateinit var quotesAdapter: QuotesAdapter

    val DB by lazy {
        QuotesIslamiDB(this.requireContext())
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        galleryViewModel =
//                ViewModelProviders.of(this).get(GalleryViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
//        val textView: TextView = root.findViewById(R.id.text_gallery)
//        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        activity?.runOnUiThread {
            btnToAddQuotes.setOnClickListener {
                val fragment2 = SlideshowFragment()
                val fragmentManager: FragmentManager? = fragmentManager
                val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                fragmentTransaction.replace(this.id, fragment2)
                fragmentTransaction.commit()
            }

            txtSearch.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
//                    TODO("not implemented")
                    //To change body of created functions use File | Settings | File Templates.
                    Log.d("search str", s.toString())
                    var searchStr = "%${s.toString()}%"
                    if(s.toString() == ""){
                        CoroutineScope(Dispatchers.IO).launch {
                            val quotess = DB.QuotesIslamiDAO().getAllQuotes()
                            Log.d("room search all", quotess.toString())
                            withContext(Dispatchers.Main) {
                                quotesAdapter.isiData(quotess)
                            }
                        }
                    }
                    else{
                        CoroutineScope(Dispatchers.IO).launch {
                            val quotess = DB.QuotesIslamiDAO().searchQuotes(searchStr)
                            Log.d("room search", quotess.toString())
                            withContext(Dispatchers.Main) {
                                quotesAdapter.isiData(quotess)
                            }
                        }
                    }

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
//                    TODO("not implemented")
                    //To change body of created functions use File | Settings | File Templates.
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    TODO("not implemented")
                    //To change body of created functions use File | Settings | File Templates.
                    Log.d("search str", s.toString())
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val quotess = DB.QuotesIslamiDAO().searchQuotes(s.toString())
//                        Log.d("room", quotess.toString())
//                        withContext(Dispatchers.Main) {
//                            quotesAdapter.isiData(quotess)
//                        }
//                    }
                }
            })
        }

        TampilkanData()
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val quotess = DB.QuotesIslamiDAO().getAllQuotes()
            Log.d("room", quotess.toString())
            withContext(Dispatchers.Main) {
                quotesAdapter.isiData(quotess)
            }
        }
    }

    private fun TampilkanData()
    {
        recyclerQuotes.layoutManager = LinearLayoutManager(this.requireContext())
        quotesAdapter = QuotesAdapter(arrayListOf())
        recyclerQuotes.adapter = quotesAdapter
        quotesAdapter.listenerLike = this
        quotesAdapter.listenerUnlike = this
    }

    override fun like(view: View, id: Int) {
//        TODO("not implemented")
        //To change body of created functions use File | Settings | File Templates.

        CoroutineScope(Dispatchers.IO).launch {
            DB.QuotesIslamiDAO().likeQuotes(id)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val quotes = DB.QuotesIslamiDAO().getAllQuotes()
            withContext(Dispatchers.Main){
                quotesAdapter.isiData(quotes)
            }
        }
    }

    override fun unlike(view: View, id: Int) {
//        TODO("not implemented")
        //To change body of created functions use File | Settings | File Templates.

        CoroutineScope(Dispatchers.IO).launch {
            DB.QuotesIslamiDAO().unlikeQuotes(id)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val quotes = DB.QuotesIslamiDAO().getAllQuotes()
            withContext(Dispatchers.Main){
                quotesAdapter.isiData(quotes)
            }
        }
    }
}
