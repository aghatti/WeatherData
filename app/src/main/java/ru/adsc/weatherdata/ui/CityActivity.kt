package ru.adsc.weatherdata.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.json.JSONObject
import ru.adsc.weatherdata.BuildConfig
import ru.adsc.weatherdata.R
import ru.adsc.weatherdata.data.model.dWeather
import ru.adsc.weatherdata.data.processing.JSONWeatherParser
import ru.adsc.weatherdata.mApp
import ru.adsc.weatherdata.ui.adapters.WeatherRecordsAdapter
import ru.adsc.weatherdata.viewmodel.WeatherForecastViewModel
import ru.adsc.weatherdata.viewmodel.WeatherForecastViewModelFactory


class CityActivity : AppCompatActivity() {

    private val weatherForecastViewModel: WeatherForecastViewModel by viewModels {
        WeatherForecastViewModelFactory((application as mApp).repository)
    }

    private lateinit var weather: dWeather
    private lateinit var cityText: TextView
    private lateinit var condDescr: TextView
    private lateinit var temp: TextView
    private lateinit var press: TextView
    private lateinit var windSpeed: TextView
    private lateinit var windDeg: TextView
    private lateinit var hum: TextView
    private lateinit var imgView: ImageView

    private lateinit var pbar:View
    private lateinit var pOverlay:View
    private lateinit var mainLayout:View
    private lateinit var btnForecast:View
    private lateinit var wf_recyclerview:RecyclerView

    private lateinit var city:String
    private lateinit var urlAddOn:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        val intent = intent
        val JSONCity = JSONObject(intent.getStringExtra("city"))
        city = JSONCity.getString("cityName") + "," + JSONCity.getString("countryCode")
        val ctx = this
        weather = dWeather()
        cityText = findViewById<View>(R.id.cityText) as TextView
        condDescr = findViewById<View>(R.id.condDescr) as TextView
        temp = findViewById<View>(R.id.temp) as TextView
        hum = findViewById<View>(R.id.hum) as TextView
        press = findViewById<View>(R.id.press) as TextView
        windSpeed = findViewById<View>(R.id.windSpeed) as TextView
        windDeg = findViewById<View>(R.id.windDeg) as TextView
        imgView = findViewById<View>(R.id.condIcon) as ImageView
        pbar = findViewById<View>(R.id.pBar)
        pOverlay = findViewById<View>(R.id.progress_overlay)
        btnForecast = findViewById<View>(R.id.btnForecast)
        mainLayout = findViewById<View>(R.id.mainLayout)
        wf_recyclerview = findViewById<RecyclerView>(R.id.wf_recyclerview)

        weatherForecastViewModel.clearForecast()

        val adapter = WeatherRecordsAdapter()
        wf_recyclerview.adapter = adapter
        wf_recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        weatherForecastViewModel.weatherRecords.observe(this, Observer { weatherRecords ->
            // Update the cached copy of the words in the adapter.
            weatherRecords?.let { adapter.submitList(it) }
        })
        val API_KEY: String = BuildConfig.API_KEY
        urlAddOn = "?units=metric&lang=ru&APPID=" +
                API_KEY + "&q=" + city
        val reqUrl = getString(R.string.ow_url) + urlAddOn
        val imgUrl = getString(R.string.ow_url_img)
        val socketTimeout = 30000
        val policy: RetryPolicy = DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)

        val stringRequest = StringRequest(Request.Method.GET,
                reqUrl,
                { response ->
                    weather = JSONWeatherParser.getWeather(response)!!
                    cityText.text =
                            weather.location?.getCity() + "," + (weather.location?.getCountry())
                    condDescr.text =
                            weather.currentCondition.condition + "(" + weather.currentCondition.descr + ")"
                    temp.text = "" + Math.round(weather.temperature.temp) + " \u2103"
                    hum.text = "" + weather.currentCondition.humidity + "%"
                    press.text = "" + weather.currentCondition.pressure + " hPa"
                    windSpeed.text = "" + weather.wind.speed + " mps"
                    windDeg.text = "" + weather.wind.deg + " \u00B0"
                    Picasso.get()
                            .load(imgUrl + weather.currentCondition.icon.toString() + "@4x.png")
                            .into(imgView, object : Callback {
                                override fun onSuccess() {
                                }

                                override fun onError(e: Exception?) {
                                }
                            })
                },
                { error ->
                    Toast.makeText(
                            ctx,
                            "Error making network request to fetch weather: " + error.toString(),
                            Toast.LENGTH_LONG
                    ).show()
                }
        )
        stringRequest.retryPolicy = policy
        requestQueue.add(stringRequest)
    }
    fun showForecast(target: View?) {
        weatherForecastViewModel.generateForecast(getString(R.string.ow_url_forecast) + urlAddOn + "&cnt=7")
        wf_recyclerview.visibility = View.VISIBLE
        //btnForecast.isEnabled = false
        //pOverlay.visibility=View.VISIBLE
        // do background job
        // and reactivate ui
        //btnForecast.isEnabled = true
        //pOverlay.visibility=View.GONE

    }
}