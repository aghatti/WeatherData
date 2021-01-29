package ru.adsc.weatherdata.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import ru.adsc.weatherdata.R
import ru.adsc.weatherdata.data.entities.City
import ru.adsc.weatherdata.mApp
import ru.adsc.weatherdata.ui.adapters.CitiesAdapter
import ru.adsc.weatherdata.viewmodel.CitiesViewModel
import ru.adsc.weatherdata.viewmodel.CitiesViewModelFactory

class MainActivity : AppCompatActivity() {
    private val newCityActivityRequestCode = 1
    private val citiesViewModel: CitiesViewModel by viewModels {
        CitiesViewModelFactory((application as mApp).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = CitiesAdapter{ city ->
            adapterOnClick(city)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        citiesViewModel.allCities.observe(this, Observer { cities ->
            // Update the cached copy of the words in the adapter.
            cities?.let { adapter.submitList(it) }
        })
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewCityActivity::class.java)
            startActivityForResult(intent, newCityActivityRequestCode)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newCityActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewCityActivity.EXTRA_REPLY)?.let {
                //val city = City(-1,it)
                val jsonCity = JSONObject(it)
                val city = City(jsonCity.getString("cityName"), jsonCity.getString("countryCode"))
                citiesViewModel.insert(city)
            }
        } else {
            Toast.makeText(
                applicationContext,
                    R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }
    private fun adapterOnClick(city: City) {
        val intent = Intent(this, CityActivity()::class.java)
        intent.putExtra("city", JSONObject().put("cityName",city.cityName).put("countryCode", city.countryCode).toString())
        this.startActivity(intent)
    }
}