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

    private var userName: String? = ""

    private lateinit var userNameInputView: EditText
    private lateinit var predictButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize and configure the weather prediction button
        predictButton =findViewById(R.id.activity_main_predict_button)
        predictButton.setOnClickListener {
            handlePredictButtonClick()
        }

        // Initialize the EditText element for the user name
        userNameInputView = findViewById(R.id.activity_main_name_input_edit_text)

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
    }

    companion object {
        const val TAG = "com.example.semesterproject.MainActivity"
    }

}