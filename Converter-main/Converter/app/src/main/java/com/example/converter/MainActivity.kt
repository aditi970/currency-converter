package com.example.currency_converter

import android.R
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.currency_converter.R
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.BreakIterator
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    var keysList: MutableList<String>? = null
    var toCurrency: Spinner? = null
    var textView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toCurrency = findViewById<View>(R.id.planets_spinner) as Spinner
        val edtEuroValue = findViewById<View>(R.id.editText4) as EditText
        val btnConvert = findViewById<View>(R.id.button) as Button
        textView = findViewById<View>(R.id.textView7) as TextView
        try {
            loadConvTypes()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        btnConvert.setOnClickListener {
            if (!edtEuroValue.text.toString().isEmpty()) {
                val toCurr = toCurrency!!.selectedItem.toString()
                val euroVlaue = java.lang.Double.valueOf(edtEuroValue.text.toString())
                Toast.makeText(this@MainActivity, "Please Wait..", Toast.LENGTH_SHORT).show()
                try {
                    convertCurrency(toCurr, euroVlaue)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Please Enter a Value to Convert..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @Throws(IOException::class)
    fun loadConvTypes() {
        val url = "https://api.exchangeratesapi.io/latest"
        val client = OkHttpClient()
        val request: Request = Builder()
            .url(url)
            .header("Content-Type", "application/json")
            .build()
        client.newCall(request).enqueue(object : Callback() {
            fun onFailure(request: Request?, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
                Toast.makeText(this@MainActivity, mMessage, Toast.LENGTH_SHORT).show()
            }

            @Throws(IOException::class)
            fun onResponse(response: Response) {
                val mMessage: String = response.body().string()
                runOnUiThread { //Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    try {
                        val obj = JSONObject(mMessage)
                        val b = obj.getJSONObject("rates")
                        val keysToCopyIterator: Iterator<*> = b.keys()
                        keysList = ArrayList()
                        while (keysToCopyIterator.hasNext()) {
                            val key = keysToCopyIterator.next() as String
                            keysList.add(key)
                        }
                        val spinnerArrayAdapter = ArrayAdapter(
                            applicationContext, R.layout.simple_spinner_item, keysList
                        )
                        toCurrency!!.adapter = spinnerArrayAdapter
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    @Throws(IOException::class)
    fun convertCurrency(toCurr: String?, euroVlaue: Double) {
        val url = "https://api.exchangeratesapi.io/latest"
        val client = OkHttpClient()
        val request: Request = Builder()
            .url(url)
            .header("Content-Type", "application/json")
            .build()
        client.newCall(request).enqueue(object : Callback() {
            fun onFailure(request: Request?, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
                Toast.makeText(this@MainActivity, mMessage, Toast.LENGTH_SHORT).show()
            }

            @Throws(IOException::class)
            fun onResponse(response: Response) {
                val mMessage: String = response.body().string()
                runOnUiThread { //Toast.makeText(MainActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                    try {
                        val obj = JSONObject(mMessage)
                        val b = obj.getJSONObject("rates")
                        val `val` = b.getString(toCurr)
                        val output = euroVlaue * java.lang.Double.valueOf(`val`)
                        textView!!.text = output.toString()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    companion object {
        var data: BreakIterator? = null
    }
}