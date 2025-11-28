package com.example.semesterproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.semesterproject.persistence.AppDatabase
import com.example.semesterproject.persistence.EntityModelConverter
import com.example.semesterproject.persistence.ForecastEntity
import com.example.semesterproject.persistence.ForecastModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var userName: String? = ""

    private lateinit var forecasts: List<ForecastModel>

    // UI widgets
    private lateinit var userNameInputView: EditText
    private lateinit var predictButton: Button
    private lateinit var historicalPredictionsTextView: TextView

    private val predictWeatherLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // If the weather was successfully predicted
        if (result.resultCode == RESULT_OK) {
            Log.d(TAG, "Prediction Activity launched")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)

            // Declare the custom Toolbar
            setSupportActionBar(findViewById(R.id.activity_main_toolbar))

            userNameInputView = findViewById(R.id.activity_main_name_input_edit_text)
            predictButton = findViewById(R.id.activity_main_predict_button)
            historicalPredictionsTextView = findViewById(R.id.activity_main_historical_prediction_data_textView)

            predictButton.setOnClickListener {
                handlePredictButtonClick()
            }

            loadForecastsFromDatabase()

        } catch (e: Exception) {
            Log.e(TAG, "Crash in onCreate", e)
            throw e
        }
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Stopping...")
    }

    // Read the name entered in the name input view
    private fun readName() {
        Log.d(TAG, "Reading the Enter Name EditText view")
        userName = userNameInputView.text.toString()
    }

    private fun handlePredictButtonClick() {
        readName()

        predictWeatherLauncher.launch(
            PredictionActivity.newIntent(
                this@MainActivity,
                userName
            )
        )
    }

    private fun loadForecastsFromDatabase() {
        lifecycleScope.launch {
            try {
                val forecastEntities: List<ForecastEntity> = AppDatabase
                    .getDatabase(this@MainActivity)
                    .forecastDao()
                    .getForecasts()

                forecasts = forecastEntities.map {
                    EntityModelConverter.convertEntityToModel(it)
                }

                displayForecasts(forecasts)

            } catch (e: Exception) {
                displayHistoricalError(e)
            }
        }
    }

    private fun displayHistoricalError(e: Exception) {
        historicalPredictionsTextView.text = e.message ?: getString(R.string.prediction_loading_error)
    }

    private fun displayForecasts(forecasts: List<ForecastModel>) {
        val historicalPredictionStringBuilder = StringBuilder()
        var count = 1
        forecasts.forEach {
            historicalPredictionStringBuilder.append(
                getString(
                    R.string.main_activity_historical_prediction_placeholder,
                    count++,
                    it.location.name,
                    it.location.region,
                    it.location.country,
                    it.current.temp_c
                )
            )
        }

        historicalPredictionsTextView.text = historicalPredictionStringBuilder.toString()
    }

    companion object {
        const val TAG = "com.example.semesterproject.MainActivity"
    }

}