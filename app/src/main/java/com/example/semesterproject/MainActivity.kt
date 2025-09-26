package com.example.semesterproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var weatherPredictionText: String? = STATE_PREDICTION_DEFAULT

    private lateinit var predictButton: Button

    private lateinit var predictionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize and configure the weather prediction button
        predictButton =findViewById(R.id.activity_main_predict_button)
        predictButton.setOnClickListener {
            randomizePrediction()
            updatePredictionText()
        }

        // Initialize the textView for the prediction text
        predictionTextView = findViewById(R.id.activity_main_prediction_textView)

        // Checks if there are any saved instance state bundles. If there are, the let block runs
        // to load the stored weatherPredictionText.
        // If the saved instance state is null, the run block... runs to attempt to load the
        /// weatherPredictionText from SharedPrefs
        savedInstanceState?.let {
            loadWeatherPrediction(it)
        } ?: run {
            loadWeatherPredictionFromSharedPrefs()
        }

        updatePredictionText()

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Stopping...")
        val sharedPrefs = getSharedPreferences(
            getString(R.string.preference_file_key),
            MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putString(STATE_PREDICTION, weatherPredictionText)
            apply()
        }
    }

    // Randomizes the value of the weatherPredictionText
    private fun randomizePrediction() {
        Log.d(TAG, "Randomizing prediction")
        weatherPredictionText = WEATHER_OPTIONS[Random.nextInt(WEATHER_OPTIONS.size)]
    }

    // Updates the prediction text view with the value stored in weatherPredictionText
    private fun updatePredictionText() {
        Log.d(TAG, "Updating prediction text")
        predictionTextView.text = weatherPredictionText
    }

    // Loads the saved weather prediction state from when the activity was destroyed and recreated
    private fun loadWeatherPrediction(bundle: Bundle) {
        Log.d(TAG, "Loading from instance state")
        weatherPredictionText = bundle.getString(STATE_PREDICTION)
    }

    // Loads the saved prediction state from SharedPrefs when the activity is being created
    private fun loadWeatherPredictionFromSharedPrefs() {
        Log.d(TAG, "Loading from SP")
        getSharedPreferences(
            getString(R.string.preference_file_key),
            MODE_PRIVATE
            ).run {
                weatherPredictionText = getString(STATE_PREDICTION, STATE_PREDICTION_DEFAULT)
                Log.d(TAG,"got weather prediction from SP")
            }
    }

    // This function MUST be overridden to add the state of weatherPredictionText
    // to the instanceState bundle. This is always called when the activity
    // is about to be destroyed due to an orientation change
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_PREDICTION, weatherPredictionText)
        super.onSaveInstanceState(outState)
    }


    companion object {
        const val STATE_PREDICTION = "prediction"
        const val STATE_PREDICTION_DEFAULT = "Cloudy with sunny skies"
        val WEATHER_OPTIONS = arrayOf<String>("Cloudy with sunny skies", "Dark and damp", "Raining cats and dogs",
            "Hotter than the surface of the sun", "Snowy and frigid", "Crisp and refreshing spring air")
        const val TAG = "com.example.semesterproject.MainActivity"
    }

}