package com.example.cobaproyek2.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.AlarmClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.cobaproyek2.R
import com.example.cobaproyek2.room.history.History
import com.example.cobaproyek2.room.history.HistoryDB
import com.example.cobaproyek2.room.quotes.QuotesIslamiDB
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class HomeFragment : Fragment() {

    private var greetImg: ImageView? = null
//    private var greetText: TextView? = null
    private var tv_quotes: TextView? = null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    private var PERMISSION_ID = 52

    private lateinit var HDB: HistoryDB

    lateinit var hnamasolat:String
    lateinit var htgl:String
    lateinit var hwaktu:String

    val DB by lazy {
        QuotesIslamiDB(this.requireContext())
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext())

        greetImg = getView()?.findViewById(R.id.greeting_img)
//        greetText = getView()?.findViewById(R.id.greeting_text)
        tv_quotes = getView()?.findViewById(R.id.tv_quotes)

        greeting()
        getLastLocation()

        HDB = HistoryDB.invoke(this.requireContext())

    }

    private fun greeting() {

        var now = LocalDateTime.now()
        var hour = now.hour

        if (hour >= 0 && hour < 11) {
            greetImg?.setImageResource(R.drawable.img_default_half_morning)
            Log.d("jam", "${hour.toString()} pagi")
        }
        else if (hour >= 11 && hour < 16) {
            greetImg?.setImageResource(R.drawable.img_default_half_afternoon)
            Log.d("jam", "${hour.toString()} siang")
        }
        else if (hour >= 16 && hour < 18) {
            greetImg?.setImageResource(R.drawable.img_default_half_without_sun)
            Log.d("jam", "${hour.toString()} sore")
        }
        else if (hour >= 18 && hour < 24) {
            greetImg?.setImageResource(R.drawable.img_default_half_night)
            Log.d("jam", "${hour.toString()} malam")
        }

        var tanggal_sekarang = now.dayOfMonth.toString() + " " + now.month.toString().toLowerCase().capitalize() + " " + now.year
        tv_tanggal.text = tanggal_sekarang

        CoroutineScope(Dispatchers.IO).launch {
            if(!DB.QuotesIslamiDAO().getAllQuotes().isNullOrEmpty()){
                val quotess = DB.QuotesIslamiDAO().getRandomQuotes()
                Log.d("room random", quotess.get(0).quotes)
                activity?.runOnUiThread {
                    tv_quotes?.setText("Quotes : '${quotess[0].quotes.toString()}...'")
                }

//                withContext(Dispatchers.Main){
//                    Log.d("room random", quotess.toString())
//                }
            }
            else{
                activity?.runOnUiThread {
                    tv_quotes?.setText("Quotes : 'Doa adalah ibadah...'")
                }

            }
        }
    }

    private fun loadJadwal(lat: Double, long: Double) {
        try {
            var client: OkHttpClient = OkHttpClient()
//        var url: String = "https://api.aladhan.com/v1/calendar?latitude=37.4220186&longitude=-122.0839727&method=2"
            var url: String = "https://api.aladhan.com/v1/calendar?latitude=${lat.toString()}&longitude=${long.toString()}&method=2"
            var request: Request = Request.Builder().url(url).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                TODO("not implemented")
                    //To change body of created functions use File | Settings | File Templates.
                    Log.d("error api", e.printStackTrace().toString())
                }

                override fun onResponse(call: Call, response: Response) {
//                TODO("not implemented")
                    //To change body of created functions use File | Settings | File Templates.
                    if (response.isSuccessful) {

                        var now = LocalDateTime.now()
                        var indexAPI = now.dayOfMonth - 1
                        var jsonObject: JSONObject = JSONObject(response.body()!!.string())
                        Log.d(
                            "API res",
                            jsonObject.getJSONArray("data").getString(indexAPI).toString()
                        )
                        var fajr = jsonObject.getJSONArray("data").getString(indexAPI).toString()
                            .substring(20..24)
                        var dhuhr = jsonObject.getJSONArray("data").getString(indexAPI).toString()
                            .substring(66..70)
                        var asr = jsonObject.getJSONArray("data").getString(indexAPI).toString()
                            .substring(86..90)
                        var maghrib = jsonObject.getJSONArray("data").getString(indexAPI).toString()
                            .substring(133..137)
                        var isha = jsonObject.getJSONArray("data").getString(indexAPI).toString()
                            .substring(154..158)

                        activity?.runOnUiThread {
                            tv_subuh.text = fajr
                            tv_dzuhur.text = dhuhr
                            tv_ashar.text = asr
                            tv_maghrib.text = maghrib
                            tv_isya.text = isha
                        }




                        btnAlarmSubuh.setOnClickListener {
                            var jam = fajr.split(":")[0]
                            var menit = fajr.split(":")[1]
                            Log.d("dataJadwal", "$jam $menit")
                            var timeToCheck: LocalDateTime = LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(jam.toInt(), menit.toInt())
                            )
                            cekWaktu(timeToCheck, now, "Sholat Subuh")
                        }

                        btnAlarmDzuhur.setOnClickListener {
                            var jam = dhuhr.split(":")[0]
                            var menit = dhuhr.split(":")[1]
                            Log.d("dataJadwal", "$jam $menit")
                            var timeToCheck: LocalDateTime = LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(jam.toInt(), menit.toInt())
                            )
                            cekWaktu(timeToCheck, now, "Sholat Dzuhur")
                        }

                        btnAlarmAzhar.setOnClickListener {
                            var jam = asr.split(":")[0]
                            var menit = asr.split(":")[1]
                            Log.d("dataJadwal", "$jam $menit")
                            var timeToCheck: LocalDateTime = LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(jam.toInt(), menit.toInt())
                            )
                            cekWaktu(timeToCheck, now, "Sholat Ashar")
                        }

                        btnAlarmMaghrib.setOnClickListener {
                            var jam = maghrib.split(":")[0]
                            var menit = maghrib.split(":")[1]
                            Log.d("dataJadwal", "$jam $menit")
                            var timeToCheck: LocalDateTime = LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(jam.toInt(), menit.toInt())
                            )
                            cekWaktu(timeToCheck, now, "Sholat Maghrib")
                        }

                        btnAlarmIsha.setOnClickListener {
                            var jam = isha.split(":")[0]
                            var menit = isha.split(":")[1]
                            Log.d("dataJadwal", "$jam $menit")
                            var timeToCheck: LocalDateTime = LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(jam.toInt(), menit.toInt())
                            )
                            cekWaktu(timeToCheck, now, "Sholat Isha")
                        }

                        btnCheckSubuh.setOnClickListener{
                            var jam = fajr.split(":")[0]
                            var menit = fajr.split(":")[1]
                            hwaktu = jam + ":" + menit
                            hnamasolat = "Fajr"
                            var now = LocalDateTime.now()
                            htgl = now.dayOfMonth.toString() + " " + now.month.toString().toLowerCase().capitalize() + " " + now.year

                            var timeToCheck: LocalDateTime = LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(jam.toInt(), menit.toInt())
                            )

                            addHistory(hwaktu, hnamasolat, htgl, timeToCheck, now)
                        }
                        btnCheckDzuhur.setOnClickListener {
                            var jam = dhuhr.split(":")[0]
                            var menit = dhuhr.split(":")[1]
                            hwaktu = jam + ":" + menit
                            hnamasolat = "Dzuhur"
                            var now = LocalDateTime.now()
                            htgl = now.dayOfMonth.toString() + " " + now.month.toString().toLowerCase().capitalize() + " " + now.year

                            var timeToCheck: LocalDateTime = LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(jam.toInt(), menit.toInt())
                            )

                            addHistory(hwaktu, hnamasolat, htgl, timeToCheck, now)
                        }

                        btnCheckAzhar.setOnClickListener {
                            var jam = asr.split(":")[0]
                            var menit = asr.split(":")[1]
                            hwaktu = jam + ":" + menit
                            hnamasolat = "Azhar"
                            var now = LocalDateTime.now()
                            htgl = now.dayOfMonth.toString() + " " + now.month.toString().toLowerCase().capitalize() + " " + now.year

                            var timeToCheck: LocalDateTime = LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(jam.toInt(), menit.toInt())
                            )

                            addHistory(hwaktu, hnamasolat, htgl, timeToCheck, now)
                        }

                        btnCheckMaghrib.setOnClickListener {
                            var jam = maghrib.split(":")[0]
                            var menit = maghrib.split(":")[1]
                            hwaktu = jam + ":" + menit
                            hnamasolat = "Maghrib"
                            var now = LocalDateTime.now()
                            htgl = now.dayOfMonth.toString() + " " + now.month.toString().toLowerCase().capitalize() + " " + now.year

                            var timeToCheck: LocalDateTime = LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(jam.toInt(), menit.toInt())
                            )

                            addHistory(hwaktu, hnamasolat, htgl, timeToCheck, now)
                        }

                        btnCheckIsha.setOnClickListener {
                            var jam = isha.split(":")[0]
                            var menit = isha.split(":")[1]
                            hwaktu = jam + ":" + menit
                            hnamasolat = "Isha"
                            var now = LocalDateTime.now()
                            htgl = now.dayOfMonth.toString() + " " + now.month.toString().toLowerCase().capitalize() + " " + now.year

                            var timeToCheck: LocalDateTime = LocalDateTime.of(
                                LocalDate.now(),
                                LocalTime.of(jam.toInt(), menit.toInt())
                            )

                            addHistory(hwaktu, hnamasolat, htgl, timeToCheck, now)
                        }

                        }
                }
            })
        }catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun addHistory(waktu: String, nama:String, tanggal: String, time: LocalDateTime, now: LocalDateTime){

        if(time.isAfter(now)){
            Toast.makeText(this.requireContext(), "Belum waktunya", Toast.LENGTH_LONG).show()
        }
        else {

            CoroutineScope(Dispatchers.IO).launch {
                if (HDB.HistoryDAO().getCheckHistory(nama, tanggal, waktu).isNullOrEmpty()){
                    HDB.HistoryDAO().tambahHistory(
                        History(0, nama, tanggal, waktu)
                    )
                    activity?.runOnUiThread{
                        Toast.makeText(this@HomeFragment.requireContext(),"History berhasil masuk", Toast.LENGTH_LONG).show()
                    }
                    Log.d("dataJadwal","History berhasil masuk")
                }
                else{
                    activity?.runOnUiThread{
                        Toast.makeText(this@HomeFragment.requireContext(), "Data sudah ada", Toast.LENGTH_LONG).show()
                    }
                    Log.d("dataJadwal", "Data sudah ada")
                }
            }

        }

    }

    private fun cekWaktu(time: LocalDateTime, now: LocalDateTime, msg: String){
        if(time.isBefore(now)){
            Toast.makeText(this.requireContext(), "Sudah lewat", Toast.LENGTH_LONG).show()
        }
        else {
            var alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, msg)
                putExtra(AlarmClock.EXTRA_HOUR, time.hour)
                putExtra(AlarmClock.EXTRA_MINUTES, time.minute)
            }
            startActivity(alarmIntent)
        }
    }



    private fun getLastLocation(){
        if(CheckPermission()){
            if(isLocEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener{task ->
                    var location = task.result
                    if(location == null){
                        getNewLocation()
                    }
                    else{
                        Log.d("loc Location", location.latitude.toString())
                        Log.d("loc Location", location.longitude.toString())
                        tv_location.text = getCityName(location.latitude, location.longitude) + ", " +
                                getCountryName(location.latitude, location.longitude)
                        loadJadwal(location.latitude, location.longitude)

                    }
                }
            }
            else{
                Log.d("EnableLoc", "Please Enable Loc")
//                Toast.makeText(this.applicationContext, "Please Enable Loc", Toast.LENGTH_LONG).show()
            }
        }
        else{
            RequestPermission()
        }
    }

    private fun getNewLocation(){
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation = p0.lastLocation
//            txtLocation.text = lastLocation.latitude.toString() + lastLocation.longitude.toString()
            Log.d("loc Location", lastLocation.latitude.toString())
            Log.d("loc Location", lastLocation.longitude.toString())
            tv_location.text = getCityName(lastLocation.latitude, lastLocation.longitude) + ", " +
                    getCountryName(lastLocation.latitude, lastLocation.longitude)
            loadJadwal(lastLocation.latitude, lastLocation.longitude)
        }
    }

    private fun CheckPermission(): Boolean{
        if(ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this.requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_ID
        )
    }

    private fun isLocEnabled(): Boolean {
        var locManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug:", "You Have the Permission")
            }
        }
    }

    private fun getCityName(lat:Double, long:Double): String{
        var CityName = ""
        var geoCoder = Geocoder(this.requireContext(), Locale.getDefault())
        var Address = geoCoder.getFromLocation(lat, long, 1)
        CityName = Address[0].locality
        return CityName
    }

    private fun getCountryName(lat:Double, long:Double): String{
        var CountryName = ""
        var geoCoder = Geocoder(this.requireContext(), Locale.getDefault())
        var Address = geoCoder.getFromLocation(lat, long, 1)
        CountryName = Address.get(0).countryName
        return CountryName
    }
}
