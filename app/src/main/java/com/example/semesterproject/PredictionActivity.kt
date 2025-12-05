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
import androidx.recyclerview.widget.RecyclerView
import com.example.semesterproject.api.WeatherRetrofitApi
import com.example.semesterproject.models.ForecastResponse
import com.example.semesterproject.persistence.AppDatabase
import com.example.semesterproject.persistence.EntityModelConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PredictionActivity : AppCompatActivity() {
    private var city: String = ""
    // UI widgets
    private lateinit var forecastRecycleView: RecyclerView
    private lateinit var progressBar: ProgressBar

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

        Log.d(TAG, "PredictionActivity setting RecyclerView")
        forecastRecycleView = findViewById(R.id.activity_prediction_recycleView)
        progressBar = findViewById(R.id.activity_prediction_progressBar)

        processIntent()

        Log.d(TAG, "PredictionActivity updating forecast")
        updateForecast()
    }

    // Check if any data passed from the previous activity
    private fun processIntent() {
        intent?.apply {
            city = getStringExtra(EXTRA_CITY) ?: ""
        }
    }

    private fun updateForecast() {
        progressBar.visibility = View.VISIBLE
        Log.d(TAG, "PredictionActivity launching coroutine")
        coroutineScope.launch {
            Log.d(TAG, "Coroutine launched")
            try {
                Log.d(TAG, "Fetching response...")
                val forecastResponse = WeatherRetrofitApi.getForecast(city, 1)
                Log.d(TAG, "Response received... Attempting to update predictionTextView")
                runOnUiThread {
                    // TODO
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
            Log.d(TAG, e.toString())
            return false
        }
        return true
    }

    companion object {
        // Encapsulated key to package name captured from intent used to create this activity
        private const val EXTRA_CITY = "com.example.semesterproject.predictionactivity.city"

        private const val TAG = "com.example.semesterproject.predictionactivity"

        // A utility function called by any activity to start this one
        fun newIntent(context: Context, city: String?) : Intent {
            return Intent(context, PredictionActivity::class.java).apply {
                city?.let { city ->
                    putExtra(EXTRA_CITY, city)
                }
            }
        }
    }
}