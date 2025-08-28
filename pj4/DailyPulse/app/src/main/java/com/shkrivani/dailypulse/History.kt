package com.shkrivani.dailypulse

import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


// This activity represents the "History" screen of the app, displaying notes and providing navigation options.
class History : AppCompatActivity() {
    // Declare variables for database interaction, the ListView for notes, and its adapter.
    private lateinit var databaseHelper: NotesDatabaseHelper
    private lateinit var notesListView: ListView
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity to the 'activity_history' layout file.
        setContentView(R.layout.activity_history)

        // Set up the toolbar for this activity.
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize the database helper to interact with the SQLite database.
        databaseHelper = NotesDatabaseHelper(this)

        // Find UI components in the layout.
        notesListView = findViewById(R.id.listView_notes)
        val backButton: Button = findViewById(R.id.button_back_to_home)

        // Set up the back button functionality to return to the previous activity.
        backButton.setOnClickListener {
            finish() // Ends the current activity and navigates back.
        }

        // Retrieve and display notes in the ListView.
        refreshNotesList()
    }

    // Refreshes the notes list by fetching updated data from the database.
    private fun refreshNotesList() {
        val notesList = getAllNotes() // Fetch sorted notes from the database.
        notesAdapter = NotesAdapter(this, notesList) // Create a new adapter with the fetched notes.
        notesListView.adapter = notesAdapter // Set the adapter to the ListView.
    }

    // Fetches all notes from the database in descending order of their timestamp.
    private fun getAllNotes(): MutableList<Pair<String?, String?>> {
        val db = databaseHelper.readableDatabase
        val cursor = db.query(
            NotesDatabaseHelper.TABLE_NAME,
            arrayOf(
                NotesDatabaseHelper.COLUMN_MOOD,
                NotesDatabaseHelper.COLUMN_NOTE,
                NotesDatabaseHelper.COLUMN_TIMESTAMP
            ),
            null,
            null,
            null,
            null,
            "${NotesDatabaseHelper.COLUMN_TIMESTAMP} DESC" // Order notes by the most recent first.
        )

        val notes = mutableListOf<Pair<String?, String?>>()
        with(cursor) {
            // Iterate through the results and add them to the notes list.
            while (moveToNext()) {
                val mood = getString(getColumnIndexOrThrow(NotesDatabaseHelper.COLUMN_MOOD))
                val note = getString(getColumnIndexOrThrow(NotesDatabaseHelper.COLUMN_NOTE))
                notes.add(Pair(mood, note)) // Add the mood and note as a pair to the list.
            }
            close() // Close the cursor to release resources.
        }
        return notes // Return the list of notes.
    }

    // Inflate the options menu for this activity.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu) // Load the menu resource defined in 'menu_history.xml'.
        return true
    }

    // Handle user interactions with menu items.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Navigate to the "My Notes" view and refresh the notes list.
            R.id.action_my_notes -> {
                refreshNotesList()
                true
            }
            // Navigate to the "Explore" activity.
            R.id.action_api_notes -> {
                val intent = Intent(this, ExploreActivity::class.java)
                startActivity(intent)
                true
            }
            // Navigate to the "About" activity.
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            // Default case for handling unrecognized menu items.
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Placeholder for refreshing quotes when the "Explore" menu item is selected.
    private fun refreshQuotesList() {
        // Display a message indicating that the functionality is not yet implemented.
        Toast.makeText(this, "API Notes selected", Toast.LENGTH_SHORT).show()
    }
}