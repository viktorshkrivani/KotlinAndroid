package com.shkrivani.dailypulse

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: NotesDatabaseHelper // Helper for interacting with the database
    private var selectedMood: String = "" // Holds the currently selected mood

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Set the layout for the activity

        // Initialize the database helper for saving notes
        databaseHelper = NotesDatabaseHelper(this)

        // Find UI components from the layout
        val moodGridLayout: GridLayout = findViewById(R.id.grid_moods) // Grid layout for mood selection
        val noteEditText: EditText = findViewById(R.id.editText_note) // Input field for note text
        val buttonLayout: View = findViewById(R.id.button_layout) // Container for buttons (Save, Back)
        val saveNoteButton: Button = findViewById(R.id.button_save_note) // Button to save the note
        val backToFeelingsButton: Button = findViewById(R.id.button_back_to_feelings) // Button to return to mood selection
        val greetingTextView: TextView = findViewById(R.id.textView_greeting) // Greeting text
        val reviewProgressButton: Button = findViewById(R.id.button_check_progress) // Button to navigate to history/progress

        // Set click listeners for mood selection buttons in the grid layout
        for (i in 0 until moodGridLayout.childCount) {
            val moodSquare = moodGridLayout.getChildAt(i) // Get each child view in the grid
            moodSquare.setOnClickListener {
                // Map each mood square to its corresponding text
                val moodTextView = when (moodSquare.id) {
                    R.id.mood_tranquile -> moodSquare.findViewById<TextView>(R.id.text_tranquile)
                    R.id.mood_freshness -> moodSquare.findViewById<TextView>(R.id.text_freshness)
                    R.id.mood_creative -> moodSquare.findViewById<TextView>(R.id.text_creative)
                    R.id.mood_love -> moodSquare.findViewById<TextView>(R.id.text_love)
                    R.id.mood_enthusiasm -> moodSquare.findViewById<TextView>(R.id.text_enthusiasm)
                    R.id.mood_nervous -> moodSquare.findViewById<TextView>(R.id.text_nervous)
                    R.id.mood_anger -> moodSquare.findViewById<TextView>(R.id.text_anger)
                    R.id.mood_seriousness -> moodSquare.findViewById<TextView>(R.id.text_seriousness)
                    else -> null
                }

                // Save the selected mood text
                selectedMood = moodTextView?.text?.toString() ?: ""

                // Change the background color based on the selected mood
                val selectedColor = (moodSquare.background as? ColorDrawable)?.color ?: Color.WHITE
                findViewById<View>(R.id.main).setBackgroundColor(selectedColor)

                // Update UI to display the note input fields
                moodGridLayout.visibility = View.GONE
                greetingTextView.visibility = View.GONE
                reviewProgressButton.visibility = View.GONE
                noteEditText.visibility = View.VISIBLE
                buttonLayout.visibility = View.VISIBLE
            }
        }

        // Save note button functionality
        saveNoteButton.setOnClickListener {
            val note = noteEditText.text.toString() // Get the note text
            if (note.isNotEmpty() && selectedMood.isNotEmpty()) {
                saveNoteToDatabase(note, selectedMood) // Save the note to the database
                noteEditText.text.clear() // Clear the input field
            } else {
                // Show a message if the mood or note is not selected
                Toast.makeText(this, "Please enter a note and select a mood", Toast.LENGTH_SHORT).show()
            }
            resetUI() // Reset the UI to the mood selection screen
        }

        // Back to mood selection button functionality
        backToFeelingsButton.setOnClickListener { resetUI() }

        // Navigate to the history/progress screen
        reviewProgressButton.setOnClickListener {
            val intent = Intent(this, History::class.java) // Intent to start the History activity
            startActivity(intent)
        }
    }

    // Resets the UI to the default state (mood selection screen)
    private fun resetUI() {
        findViewById<View>(R.id.main).setBackgroundColor(Color.WHITE) // Reset background color
        findViewById<GridLayout>(R.id.grid_moods).visibility = View.VISIBLE // Show mood grid
        findViewById<TextView>(R.id.textView_greeting).visibility = View.VISIBLE // Show greeting
        findViewById<Button>(R.id.button_check_progress).visibility = View.VISIBLE // Show "Check Progress" button
        findViewById<EditText>(R.id.editText_note).visibility = View.GONE // Hide note input
        findViewById<View>(R.id.button_layout).visibility = View.GONE // Hide button layout
    }

    // Saves a note with the selected mood to the database
    private fun saveNoteToDatabase(note: String, mood: String) {
        val db = databaseHelper.writableDatabase
        val values = ContentValues().apply {
            put(NotesDatabaseHelper.COLUMN_NOTE, note) // Add note text to the database
            put(NotesDatabaseHelper.COLUMN_MOOD, mood) // Add mood to the database
        }
        val newRowId = db.insert(NotesDatabaseHelper.TABLE_NAME, null, values) // Insert the new row
        if (newRowId != -1L) {
            // Show success message
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
        } else {
            // Show error message
            Toast.makeText(this, "Error saving note", Toast.LENGTH_SHORT).show()
        }
        db.close() // Close the database connection
    }
}