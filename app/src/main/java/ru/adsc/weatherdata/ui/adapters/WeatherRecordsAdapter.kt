package ru.adsc.weatherdata.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.adsc.weatherdata.R
import ru.adsc.weatherdata.data.model.dDayForecast

import java.text.SimpleDateFormat


class WeatherRecordsAdapter(): ListAdapter<dDayForecast, WeatherRecordsAdapter.WeatherRecordsViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherRecordsViewHolder {
        return WeatherRecordsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WeatherRecordsViewHolder, position: Int) {
        val dDayForecast = getItem(position)
        holder.bind(dDayForecast)
    }

    class WeatherRecordsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wrDate: TextView = itemView.findViewById(R.id.tvDate)
        private val wrTempDay: TextView = itemView.findViewById(R.id.tvTempDay)
        private val wrTempNight: TextView = itemView.findViewById(R.id.tvTempNight)

        fun bind(dayForecast: dDayForecast) {
            wrDate.text = dayForecast.getStringDate()
            wrTempDay.text = dayForecast.forecastTemp.day.toString() + " \u2103"
            wrTempNight.text = dayForecast.forecastTemp.night.toString() + " \u2103"
        }
        companion object {
            fun create(parent: ViewGroup): WeatherRecordsViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.wf_recyclerview_item, parent, false)
                return WeatherRecordsViewHolder(view)
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<dDayForecast>() {
            override fun areItemsTheSame(oldItem: dDayForecast, newItem: dDayForecast): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: dDayForecast, newItem: dDayForecast): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }
        }
        private val sdf: SimpleDateFormat = SimpleDateFormat("E, dd-MM")

    }

}