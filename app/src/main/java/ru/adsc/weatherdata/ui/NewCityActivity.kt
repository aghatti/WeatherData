package ru.adsc.weatherdata.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import org.json.JSONObject
import ru.adsc.weatherdata.R

class NewCityActivity : AppCompatActivity() {
    private lateinit var editCityView: EditText
    private lateinit var editCountryView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_city)
        editCityView = findViewById(R.id.edit_city)
        editCountryView = findViewById(R.id.edit_country)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editCityView.text) || TextUtils.isEmpty(editCountryView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val city = editCityView.text.toString()
                val country = editCountryView.text.toString()
                val res = JSONObject()
                res.put("cityName", city)
                res.put("countryCode", country)
                replyIntent.putExtra(EXTRA_REPLY, res.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

    }
    companion object {
        const val EXTRA_REPLY = "ru.adsc.roomwordsample.citylistsql.REPLY"
    }

}