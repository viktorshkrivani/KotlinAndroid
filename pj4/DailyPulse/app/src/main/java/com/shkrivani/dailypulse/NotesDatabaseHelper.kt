package com.shkrivani.dailypulse

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class NotesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database constants
        const val DATABASE_NAME = "notes.db" // Name of the database file
        const val DATABASE_VERSION = 2 // Current database version
        const val TABLE_NAME = "notes" // Name of the table
        const val COLUMN_ID = "id" // Primary key column
        const val COLUMN_NOTE = "note" // Column for note content
        const val COLUMN_MOOD = "mood" // Column for the mood associated with the note
        const val COLUMN_TIMESTAMP = "timestamp" // Column for the timestamp of the note
    }

    // Called when the database is first created
    override fun onCreate(db: SQLiteDatabase) {
        try {
            // SQL query to create the "notes" table
            val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, -- Auto-incrementing ID
                $COLUMN_NOTE TEXT, -- Text content of the note
                $COLUMN_MOOD TEXT, -- Associated mood for the note
                $COLUMN_TIMESTAMP INTEGER DEFAULT (strftime('%s', 'now')) -- Timestamp with default value as current time
            )
            """.trimIndent()
            db.execSQL(createTableQuery) // Execute the SQL query

            // Insert a default note when the table is created
            val contentValues = ContentValues()
            contentValues.put(COLUMN_NOTE, "Welcome to Daily Pulse!") // Default note content
            contentValues.put(COLUMN_MOOD, "freshness") // Default mood
            contentValues.put(COLUMN_TIMESTAMP, System.currentTimeMillis()) // Current timestamp
            db.insert(TABLE_NAME, null, contentValues) // Insert the default note into the table
        } catch (e: SQLException) {
            Log.e("NotesDatabaseHelper", "Error creating table", e) // Log any errors
        }
    }

    // Called when the database version is updated
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            if (oldVersion < 2) {
                // Add the "timestamp" column if upgrading from a version without it
                db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_TIMESTAMP INTEGER DEFAULT (strftime('%s', 'now'))")
            }
        } catch (e: SQLException) {
            Log.e("NotesDatabaseHelper", "Error upgrading database", e) // Log any errors
        }
    }

    // Deletes a note from the database based on mood and note content
    fun deleteNote(mood: String?, note: String?) {
        writableDatabase.use { db ->
            // Build the WHERE clause dynamically based on whether the mood is null
            val whereClause = if (mood.isNullOrEmpty()) {
                "$COLUMN_MOOD IS NULL AND $COLUMN_NOTE = ?"
            } else {
                "$COLUMN_MOOD = ? AND $COLUMN_NOTE = ?"
            }

            // Build the WHERE arguments
            val whereArgs = if (mood.isNullOrEmpty()) {
                arrayOf(note) // Only the note content is used
            } else {
                arrayOf(mood, note) // Both mood and note content are used
            }

            db.delete(TABLE_NAME, whereClause, whereArgs) // Delete the note
        }
    }

    // Updates the database schema if necessary
    fun updateSchema() {
        val db = writableDatabase
        try {
            // Check if the "timestamp" column already exists
            val cursor = db.rawQuery("PRAGMA table_info($TABLE_NAME)", null)
            var timestampColumnExists = false

            while (cursor.moveToNext()) {
                if (cursor.getString(1) == COLUMN_TIMESTAMP) {
                    timestampColumnExists = true // "timestamp" column found
                    break
                }
            }

            cursor.close() // Close the cursor

            if (!timestampColumnExists) {
                // Add the "timestamp" column if it doesn't exist
                db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_TIMESTAMP TEXT")
                // Set default values for the new column
                val updateQuery =
                    "UPDATE $TABLE_NAME SET $COLUMN_TIMESTAMP = DATETIME('now', 'localtime')"
                db.execSQL(updateQuery)
            }
        } catch (e: SQLException) {
            Log.e("NotesDatabaseHelper", "Error updating schema", e) // Log any errors
        } finally {
            db.close() // Close the database connection
        }
    }

    // Inserts a new note into the database
    fun insertNote(mood: String, note: String) {
        val db = writableDatabase
        try {
            // Build the ContentValues object with note data
            val contentValues = ContentValues()
            contentValues.put(COLUMN_MOOD, mood) // Mood of the note
            contentValues.put(COLUMN_NOTE, note) // Content of the note
            contentValues.put(COLUMN_TIMESTAMP, System.currentTimeMillis()) // Current timestamp
            db.insert(TABLE_NAME, null, contentValues) // Insert the note into the table
        } catch (e: SQLException) {
            Log.e("NotesDatabaseHelper", "Error inserting note", e) // Log any errors
        } finally {
            db.close() // Close the database connection
        }
    }
}