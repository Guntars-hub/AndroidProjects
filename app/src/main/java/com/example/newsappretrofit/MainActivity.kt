package com.example.newsappretrofit

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsappretrofit.adapters.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://api.giphy.com"

class MainActivity : AppCompatActivity() {

    lateinit var countDownTimer: CountDownTimer

    private var imagesList = mutableListOf<String>()
    private var adapter = RecyclerAdapter(imagesList)
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiRequest::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()

        searchButton.setOnClickListener {
            hideKeyboard()
        }

//        TODO automatically hide keyboard - DONE

//        TODO single line in editText - DONE

//        TODO implement - DONE

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                hideKeyboard()
            }
            true
        }

        editText.addTextChangedListener { _ ->
            makeAPIRequest()
        }
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setUpRecyclerView() {
        val gridLayoutManager =
            GridLayoutManager(applicationContext, 3, LinearLayoutManager.VERTICAL, false)
        rv_recyclerView.layoutManager = gridLayoutManager
        rv_recyclerView.adapter = adapter
    }

    private fun makeAPIRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val query = editText.text.toString()
                if (query.isNotEmpty() && query.length > 2) {

                    val response = api.getGifs("b3yanGY4AmT3AtBM2KeYY25UfSByTv41", query, "15")

                    imagesList.clear()
                    for (gifUrl in response.data) {
                        Log.i("MainActivity", "Result = $gifUrl")
                        imagesList.add(gifUrl.images.original.url)
                    }

                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", e.toString())
            }
        }
    }
}