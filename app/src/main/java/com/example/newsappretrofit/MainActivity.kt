    package com.example.newsappretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappretrofit.adapters.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

const val BASE_URL = "https://api.giphy.com"

class MainActivity : AppCompatActivity() {

    lateinit var countDownTimer: CountDownTimer

    private var imagesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeAPIRequest()
    }

    private fun setUpRecyclerView() {
        rv_recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        rv_recyclerView.adapter = RecyclerAdapter(imagesList)
    }

    private fun addToList(image: String) {
        imagesList.add(image)
    }

    private fun makeAPIRequest() {

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val simpleSearchView = findViewById<SearchView>(R.id.simpleSearchView); // inititate a search view
                var query = simpleSearchView.query; // get the query string currently in the text field
                val response = api.getGifs("b3yanGY4AmT3AtBM2KeYY25UfSByTv41", "android", "25")

                for (gifUrl in response.data) {
                    Log.i("MainActivity", "Result = $gifUrl")
                    addToList(gifUrl.images.downsized.url)
                }

                withContext(Dispatchers.Main) {
                    setUpRecyclerView()
                }

            } catch (e: Exception) {
                Log.e("MainActivity", e.toString())
            }
        }
    }
}