package com.example.semesterproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var userName: String? = ""
    private var predictionCount: Int = 0
    private var predictionStringBuilder = StringBuilder()

    // UI widgets
    private lateinit var userNameInputView: EditText
    private lateinit var predictButton: Button
    private lateinit var predictionHistoryTextView: TextView

    private val predictWeatherLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // If the weather was successfully predicted
        if (result.resultCode == RESULT_OK) {
            // Grab the prediction string
            result.data?.let { resultIntent ->
                // Update the historical predictions
                updateHistoricalPredictions(resultIntent)
            }
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
            predictionHistoryTextView = findViewById(R.id.activity_main_predictions_textView)

            predictButton.setOnClickListener {
                handlePredictButtonClick()
            }
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

    private fun updateHistoricalPredictions(intent: Intent) {
        val predictionString = intent.getStringExtra(PredictionActivity.PREDICTION_INTENT_EXTRA)
        // Append the new count to the historical prediction string
        predictionCount++
        predictionStringBuilder.clear()
        updatePredictionCountString()

        // Append prediction text to historical prediction string
        predictionStringBuilder.append(predictionString)

        // Update the rendered historical prediction
        predictionHistoryTextView.text = getString(
            R.string.placeholder,
            predictionHistoryTextView.text.toString(),
            predictionStringBuilder.toString()
        )

    }

    private fun updatePredictionCountString() {
        predictionStringBuilder.append("#")
        predictionStringBuilder.append(predictionCount)
        predictionStringBuilder.append(". ")
    }

    companion object {
        const val TAG = "com.example.semesterproject.MainActivity"
    }

}