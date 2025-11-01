package com.example.semesterproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat.setTint

class PredictionActivity : AppCompatActivity() {

    // UI widgets
    private lateinit var iPredictTextView: TextView
    private lateinit var predictionTextView: TextView
    private lateinit var predictionImageView: ImageView
    private lateinit var finishedButton: Button

    private var iPredictStringBuilder = StringBuilder()
    private var predictionText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction)

        // Declare the custom Toolbar with "return" arrow
        val toolbar = findViewById<Toolbar>(R.id.activity_prediction_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon?.let { drawable ->
            setTint(drawable, android.graphics.Color.WHITE)
        }


        iPredictTextView = findViewById(R.id.prediction_activity_i_predict_textView)
        predictionTextView = findViewById(R.id.prediction_activity_prediction_textView)
        predictionImageView = findViewById(R.id.prediction_activity_prediction_image_imageView)
        finishedButton = findViewById(R.id.prediction_activity_finish_button)

        processIntent()
        setRandomizedPrediction()
        // Return a prediction to the calling activity and finish
        finishedButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(PREDICTION_INTENT_EXTRA, predictionTextView.text)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
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

    private fun setRandomizedPrediction() {
        // Randomize the prediction text
        predictionText = WEATHER_OPTIONS.keys.random()

        // Update the prediction text view with the new value
        predictionTextView.text = predictionText

        // Update the prediction image
        val resource = WEATHER_OPTIONS[predictionText]
        resource?.let {
            predictionImageView.setImageResource(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        // Encapsulated key to package name captured from intent used to create this activity
        private const val EXTRA_NAME = "com.example.semesterproject.predictionactivity.name"

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

        val WEATHER_OPTIONS = mapOf(
            "Cloudy with sunny skies" to R.drawable.sun,
            "Dark and damp" to R.drawable.cloud,
            "Raining cats and dogs" to R.drawable.pet,
            "Hotter than the surface of the sun" to R.drawable.local_fire_department,
            "Snowy and frigid" to R.drawable.snowboarding,
            "Crisp and refreshing spring air" to R.drawable.air)
    }
}