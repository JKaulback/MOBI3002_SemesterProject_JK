package com.example.semesterproject

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var city: String? = ""

    // UI widgets
    private lateinit var cityInputView: EditText
    private lateinit var predictButton: Button

    private val predictWeatherLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { clearCityInput() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)

            // Declare the custom Toolbar
            setSupportActionBar(findViewById(R.id.activity_main_toolbar))

            cityInputView = findViewById(R.id.activity_main_city_input_editText)
            predictButton = findViewById(R.id.activity_main_predict_button)

            cityInputView.addTextChangedListener(object: TextWatcher {

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    predictButton.visibility = if (!s.isNullOrBlank()) View.VISIBLE else View.INVISIBLE
                }

            })

            predictButton.setOnClickListener {
                handlePredictButtonClick()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Crash in onCreate", e)
            // Show a Toast so the user gets immediate feedback about the error
            Toast.makeText(this, e.message ?: getString(R.string.prediction_loading_error), Toast.LENGTH_LONG).show()
            throw e
        }
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Stopping...")
    }

    // Read the name entered in the name input view
    private fun readCity() {
        Log.d(TAG, "Reading the Enter City EditText view")
        city = cityInputView.text.toString()
    }

    private fun clearCityInput() {
        cityInputView.setText("")
        city = ""
    }

    private fun handlePredictButtonClick() {
        readCity()

        predictWeatherLauncher.launch(
            PredictionActivity.newIntent(
                this@MainActivity,
                city
            )
        )
    }

    companion object {
        const val TAG = "com.example.semesterproject.MainActivity"
    }

}