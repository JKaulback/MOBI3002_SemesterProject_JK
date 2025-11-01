package com.example.semesterproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PredictionActivity : AppCompatActivity() {

    // UI widgets
    private lateinit var iPredictTextView: TextView
    private lateinit var predictionTextView: TextView
    private lateinit var predictionImageView: ImageView
    private lateinit var finishedButton: Button

    private var predictionStringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction)

        // Declare the custom Toolbar with "return" arrow
        setSupportActionBar(findViewById(R.id.activity_prediction_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        iPredictTextView = findViewById(R.id.prediction_activity_i_predict_textView)
        predictionTextView = findViewById(R.id.prediction_activity_prediction_textView)
        predictionImageView = findViewById(R.id.prediction_activity_prediction_image_imageView)
        finishedButton = findViewById(R.id.prediction_activity_finish_button)

        processIntent()

        // Return a prediction to the calling activity and finish
        finishedButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(PREDICTION_INTENT_EXTRA, getString(R.string.prediction))
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    // Check if any data passed from the previous activity
    private fun processIntent() {
        intent?.apply {
            predictionStringBuilder.clear()
            val name = getStringExtra(EXTRA_NAME)
            if (!name.isNullOrBlank()) {
                predictionStringBuilder.append(name)
                predictionStringBuilder.append(", ")
            }
            predictionStringBuilder.append(getString(R.string.i_predict))

            iPredictTextView.text = predictionStringBuilder.toString()
        }
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
    }
}