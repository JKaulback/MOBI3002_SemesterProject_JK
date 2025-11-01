package com.example.semesterproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PredictionActivity : AppCompatActivity() {

    // UI widgets
    private lateinit var iPredictTextView: TextView
    private lateinit var predictionTextView: TextView
    private lateinit var predictionImageView: ImageView
    private lateinit var finishedButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction)

        // Declare the custom Toolbar with "return" arrow
        setSupportActionBar(findViewById(R.id.activity_prediction_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    companion object {
        // Encapsulated key to package name captured from intent used to create this activity
        private const val EXTRA_NAME = "com.example.semesterproject.predictionactivity.name"

        // Public key to package up new prediction to be passed to previous activity
        private const val PREDICTION_INTENT_EXTRA = "com.example.semesterproject.predictionactivity.prediction"

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