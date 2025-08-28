package com.shkrivani.dailypulse

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

// This activity represents the "Explore" screen of the app.
class ExploreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity to the 'activity_explore' layout file.
        setContentView(R.layout.activity_explore)

        // Set up the toolbar for this activity.
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    // Inflate the options menu, reusing the same menu resource as the History activity.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Load the menu resource defined in 'menu_history.xml'.
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    // Handle menu item selections.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // If "My Notes" is selected, navigate to the History activity.
            R.id.action_my_notes -> {
                val intent = Intent(this, History::class.java)
                startActivity(intent)
                true
            }
            // If "Explore" is selected, show a toast message as the user is already in Explore.
            R.id.action_api_notes -> {
                Toast.makeText(this, "Already in Explore", Toast.LENGTH_SHORT).show()
                true
            }
            // If "About" is selected, navigate to the About activity.
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            // Default case for handling unrecognized menu items.
            else -> super.onOptionsItemSelected(item)
        }
    }
}