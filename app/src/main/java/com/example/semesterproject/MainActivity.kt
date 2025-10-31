package com.example.semesterproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var weatherPredictionText: String? = STATE_PREDICTION_DEFAULT
    private var userName: String? = ""

    private var predictionStringBuilder = StringBuilder()

    private lateinit var userNameInputView: EditText
    private lateinit var predictButton: Button
    private lateinit var iPredictTextView: TextView
    private lateinit var predictionTextView: TextView
    private lateinit var predictionImageViewShown: ImageView

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
            handlePredictButtonClick()
        }

        // Initialize the textView for the prediction text
        predictionTextView = findViewById(R.id.activity_main_prediction_textView)

        // Initialize the EditText element for the user name
        userNameInputView = findViewById(R.id.activity_main_name_input_edit_text)

        // Initialize the "I predict..." element
        iPredictTextView = findViewById(R.id.activity_main_i_predict_textView)

        // Setup the view
        updatePredictionText()
        showPredictionImage()

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Stopping...")
    }

    // Randomizes the value of the weatherPredictionText
    private fun randomizePrediction() {
        Log.d(TAG, "Randomizing prediction")
        weatherPredictionText = WEATHER_OPTIONS.keys.random()
    }

    // Read the name entered in the name input view
    private fun readName() {
        Log.d(TAG, "Reading the Enter Name EditText view")
        userName = userNameInputView.text.toString()
        predictionStringBuilder.append(userName)
    }

    // Updates the prediction text view with the value stored in weatherPredictionText
    private fun updatePredictionText() {
        Log.d(TAG, "Updating prediction text")
        predictionTextView.text = weatherPredictionText
    }

    // Show an image based on the prediction
    private fun showPredictionImage() {
        Log.d(TAG, "Showing prediction image")
        val resource = WEATHER_OPTIONS[weatherPredictionText]
        resource?.let {
            predictionImageViewShown = findViewById(resource)
            predictionImageViewShown.visibility = View.VISIBLE
        }
    }

    // Hide the prediction image
    private fun hidePredictionImage() {
        Log.d(TAG, "Hiding prediction image")
        predictionImageViewShown.visibility = View.INVISIBLE
    }

    // Update the prediction image
    private fun updatePredictionImage() {
        Log.d(TAG, "Updating prediction image")
        hidePredictionImage()
        showPredictionImage()
    }

    private fun clearPredictionStringBuilder() {
        predictionStringBuilder.setLength(0)
    }

    private fun updateIPredictText() {
        if (predictionStringBuilder.isNotEmpty()) predictionStringBuilder.append(", ")
        predictionStringBuilder.append(getString(R.string.i_predict))
        iPredictTextView.text = predictionStringBuilder.toString()
    }

    private fun handlePredictButtonClick() {
        clearPredictionStringBuilder()
        readName()
        updateIPredictText()
        randomizePrediction()
        updatePredictionText()
        updatePredictionImage()
    }

    companion object {
        const val STATE_PREDICTION_DEFAULT = "Cloudy with sunny skies"
        val WEATHER_OPTIONS = mapOf("Cloudy with sunny skies" to R.id.activity_main_sun_imageView,
            "Dark and damp" to R.id.activity_main_cloud_imageView,
            "Raining cats and dogs" to R.id.activity_main_pet_imageView,
            "Hotter than the surface of the sun" to R.id.activity_main_local_fire_department_imageView,
            "Snowy and frigid" to R.id.activity_main_snowboarding_imageView,
            "Crisp and refreshing spring air" to R.id.activity_main_air_imageView)
        const val TAG = "com.example.semesterproject.MainActivity"
    }

}