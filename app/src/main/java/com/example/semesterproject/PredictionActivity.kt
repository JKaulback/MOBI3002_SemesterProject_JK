package com.example.semesterproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat.setTint
import com.example.semesterproject.api.WeatherRetrofitApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PredictionActivity : AppCompatActivity() {

    // UI widgets
    private lateinit var iPredictTextView: TextView
    private lateinit var predictionTextView: TextView
    private lateinit var finishedButton: Button

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
        predictionTextView = findViewById(R.id.prediction_activity_prediction_textView)
        finishedButton = findViewById(R.id.prediction_activity_finish_button)

        processIntent()
        // Return a prediction to the calling activity and finish
        Log.d(TAG, "PredictionActivity setting finishedButton onClickListener")
        finishedButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(PREDICTION_INTENT_EXTRA, predictionTextView.text)
            }
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
        Log.d(TAG, "PredictionActivity launching coroutine")
        coroutineScope.launch {
            Log.d(TAG, "Coroutine launched")
            try {
                Log.d(TAG, "Fetching response...")
                val forecastResponse = WeatherRetrofitApi.getForecast("Halifax", 1)
                Log.d(TAG, "Response recieved... Attempting to update predictionTextView")
                runOnUiThread {
                    predictionTextView.text = forecastResponse.toString()
                }

            } catch(e: Exception) {
                Log.d(TAG, e.message ?: getString(R.string.weather_fetch_error) )

                runOnUiThread {
                    val toast = Toast(this@PredictionActivity)
                    toast.duration = Toast.LENGTH_LONG
                    toast.setText(getString(R.string.weather_fetch_error))
                    toast.show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        // Encapsulated key to package name captured from intent used to create this activity
        private const val EXTRA_NAME = "com.example.semesterproject.predictionactivity.name"

        private const val TAG = "com.example.semesterproject.predictionactivity"

        // Public key to package up new prediction to be passed to previous activity
        const val PREDICTION_INTENT_EXTRA = "com.example.semesterproject.predictionactivity.prediction"

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