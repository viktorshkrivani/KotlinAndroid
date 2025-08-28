package com.shkrivani.dailypulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


// This activity displays information about the app and provides a way to navigate back to the History screen.
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity to the 'activity_about' layout file.
        setContentView(R.layout.activity_about)

        // Find the "Go Back" button in the layout using its ID.
        val backButton: Button = findViewById(R.id.button_back_to_history)

        // Set a click listener on the "Go Back" button.
        backButton.setOnClickListener {
            // Create an intent to navigate back to the History activity.
            val intent = Intent(this, History::class.java)
            startActivity(intent)

            // Optionally, finish the current AboutActivity to avoid stacking multiple instances.
            finish()
        }
    }
}