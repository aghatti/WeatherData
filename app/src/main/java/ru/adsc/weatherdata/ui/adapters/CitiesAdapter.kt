package ru.adsc.weatherdata.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.adsc.weatherdata.R
import ru.adsc.weatherdata.data.entities.City

class CitiesAdapter(private val onClick: (City) -> Unit): ListAdapter<City, CitiesAdapter.CityViewHolder>(CITIES_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder.create(parent, onClick)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = getItem(position)
        holder.bind(city)
    }

    class CityViewHolder(itemView: View, val onClick: (City) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val cityItemView: TextView = itemView.findViewById(R.id.textView)
        private var currentCity: City? = null
        init {
            itemView.setOnClickListener {
                currentCity?.let {
                    onClick(it)
                }
            }
        }
        fun bind(city: City) {
            currentCity = city
            cityItemView.text = city.cityName + ", " + city.countryCode
        }
        companion object {
            fun create(parent: ViewGroup, onClick: (City) -> Unit): CityViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return CityViewHolder(view, onClick)
            }
        }
    }

    companion object {
        private val CITIES_COMPARATOR = object : DiffUtil.ItemCallback<City>() {
            override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
                return oldItem.cityName == newItem.cityName
            }
        }
    }

}