package com.example.semesterproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.semesterproject.R
import com.example.semesterproject.models.ForecastResponse
import com.example.semesterproject.viewholders.ForecastViewHolder

class ForecastAdapter(private val forecastModels: List<ForecastResponse>)
    : RecyclerView.Adapter<ForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_forecast, parent, false)

        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.resetViews()

        val forecastModel = forecastModels[position]

        holder.apply {
            cityTextView.text = itemView.context.getString(
                R.string.area_placeholder,
                forecastModel.location.name)
            predictedAtTextView.text = itemView.context.getString(
                R.string.predicted_at_placeholder,
                forecastModel.current.last_updated)
            windSpeedTextView.text = itemView.context.getString(
                R.string.wind_speed_placeholder,
                forecastModel.current.wind_kph)
            windDirectionTextView.text = itemView.context.getString(
                R.string.wind_dir_placeholder,
                forecastModel.current.wind_dir)
            tempCTextView.text = itemView.context.getString(
                R.string.temp_placeholder,
                forecastModel.current.temp_c)

        }
    }

    override fun getItemCount(): Int = forecastModels.size
}