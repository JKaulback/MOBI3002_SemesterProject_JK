package com.example.semesterproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat.setTint
import com.example.semesterproject.api.WeatherRetrofitApi
import com.example.semesterproject.models.ForecastResponse
import com.example.semesterproject.persistence.AppDatabase
import com.example.semesterproject.persistence.EntityModelConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PredictionActivity : AppCompatActivity() {

    // UI widgets
    private lateinit var iPredictTextView: TextView
    private lateinit var areaTextView: TextView
    private lateinit var latLonTextView: TextView
    private lateinit var localTimeTextView: TextView
    private lateinit var tempTimeTextView: TextView
    private lateinit var tempTextView: TextView
    private lateinit var windSpeedTextView: TextView
    private lateinit var windDirectionTextView: TextView
    private lateinit var finishedButton: Button
    private lateinit var progressBar: ProgressBar

    private var iPredictStringBuilder = StringBuilder()

    private val coroutineContext = Job() + Dispatchers.IO
    private val coroutineScope = CoroutineScope(coroutineContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction)
        Log.d(TAG, "PredictionActivity running onCreate")
        // Declare the custom Toolbar with "return" arrow
        val toolbar = findViewById<Toolbar>(R.id.activity_prediction_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon?.let { drawable ->
            setTint(drawable, android.graphics.Color.WHITE)
        }

        Log.d(TAG, "PredictionActivity setting Views")
        iPredictTextView = findViewById(R.id.prediction_activity_i_predict_textView)
        areaTextView = findViewById(R.id.prediction_activity_location_area_textView)
        latLonTextView = findViewById(R.id.prediction_activity_location_lat_lon_textView)
        localTimeTextView = findViewById(R.id.prediction_activity_location_local_time_textView)
        tempTimeTextView = findViewById(R.id.prediction_activity_current_temp_time_textView)
        tempTextView = findViewById(R.id.prediction_activity_current_temp_textView)
        windSpeedTextView = findViewById(R.id.prediction_activity_current_wind_speed_textView)
        windDirectionTextView = findViewById(R.id.prediction_activity_current_wind_dir_textView)
        finishedButton = findViewById(R.id.prediction_activity_finish_button)
        progressBar = findViewById(R.id.prediction_activity_progressBar)

        processIntent()
        // Return a prediction to the calling activity and finish
        Log.d(TAG, "PredictionActivity setting finishedButton onClickListener")
        finishedButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        Log.d(TAG, "PredictionActivity updating forecast")
        updateForecast()
    }

    // Check if any data passed from the previous activity
    private fun processIntent() {
        intent?.apply {
            iPredictStringBuilder.clear()
            val name = getStringExtra(EXTRA_NAME)
            if (!name.isNullOrBlank()) {
                iPredictStringBuilder.append(name)
                iPredictStringBuilder.append(", ")
            }
            iPredictStringBuilder.append(getString(R.string.i_predict))

            iPredictTextView.text = iPredictStringBuilder.toString()
        }
    }

    private fun updateForecast() {
        progressBar.visibility = View.VISIBLE
        Log.d(TAG, "PredictionActivity launching coroutine")
        coroutineScope.launch {
            Log.d(TAG, "Coroutine launched")
            try {
                Log.d(TAG, "Fetching response...")
                val forecastResponse = WeatherRetrofitApi.getForecast("Halifax", 1)
                Log.d(TAG, "Response received... Attempting to update predictionTextView")
                runOnUiThread {
                    areaTextView.text = getString(
                        R.string.area_placeholder,
                        forecastResponse.location.name,
                        forecastResponse.location.region,
                        forecastResponse.location.country)

                    latLonTextView.text = getString(
                        R.string.lat_lon_placeholder,
                        forecastResponse.location.lat,
                        forecastResponse.location.lon
                    )

                    localTimeTextView.text = getString(
                        R.string.local_time_placeholder,
                        forecastResponse.location.localtime
                    )

                    tempTimeTextView.text = getString(
                        R.string.temp_time_placeholder,
                        forecastResponse.current.last_updated
                    )

                    tempTextView.text = getString(
                        R.string.temp_placeholder,
                        forecastResponse.current.temp_c,
                        forecastResponse.current.temp_f
                    )

                    windSpeedTextView.text = getString(
                        R.string.wind_speed_placeholder,
                        forecastResponse.current.wind_kph,
                        forecastResponse.current.wind_mph
                    )

                    windDirectionTextView.text = getString(
                        R.string.wind_dir_placeholder,
                        forecastResponse.current.wind_dir
                    )
                }
                val saveSuccess = saveForecast(forecastResponse)
                var text = "Forecast saved!"
                if (!saveSuccess) {
                    text = "Forecast failed to save"
                }
                runOnUiThread {
                    Toast.makeText(this@PredictionActivity, text, Toast.LENGTH_SHORT).show()
                }
            } catch(e: Exception) {
                Log.d(TAG, e.message ?: getString(R.string.weather_fetch_error) )

                runOnUiThread {
                    val toast = Toast(this@PredictionActivity)
                    toast.duration = Toast.LENGTH_LONG
                    toast.setText(getString(R.string.weather_fetch_error))
                    toast.show()
                }
            } finally {
                runOnUiThread { progressBar.visibility = View.INVISIBLE }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    suspend fun saveForecast(model: ForecastResponse): Boolean {
        try {
            val entity = EntityModelConverter.convertModelToEntity(model)
            AppDatabase.getDatabase(this@PredictionActivity).forecastDao().insert(entity)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    companion object {
        // Encapsulated key to package name captured from intent used to create this activity
        private const val EXTRA_NAME = "com.example.semesterproject.predictionactivity.name"

        private const val TAG = "com.example.semesterproject.predictionactivity"

        // A utility function called by any activity to start this one
        fun newIntent(context: Context, userName: String?) : Intent {
            return Intent(context, PredictionActivity::class.java).apply {
                userName?.let { name ->
                    putExtra(EXTRA_NAME, name)
                }
            }
        }
    }
}