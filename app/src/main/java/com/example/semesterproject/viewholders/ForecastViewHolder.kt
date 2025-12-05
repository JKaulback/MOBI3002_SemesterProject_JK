package com.example.semesterproject.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.semesterproject.R

class ForecastViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val cityTextView: TextView = view.findViewById(R.id.view_holder_city_textView)
    val predictedAtTextView: TextView = view.findViewById(R.id.view_holder_predicted_at_textView)
    val windSpeedTextView: TextView = view.findViewById(R.id.view_holder_wind_textView)
    val windDirectionTextView: TextView = view.findViewById(R.id.view_holder_direction_textView)
    val tempCTextView: TextView = view.findViewById(R.id.view_holder_temp_c_textView)

    fun resetViews() {
        cityTextView.text = ""
        predictedAtTextView.text = ""
        windSpeedTextView.text = ""
        windDirectionTextView.text = ""
        tempCTextView.text = ""
    }
}