package com.shkrivani.dailypulse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class NotesAdapter(
    private val context: Context, // Context from the calling activity or fragment
    private val notes: MutableList<Pair<String?, String?>> // List of notes with mood and content
) : BaseAdapter() {

    // Returns the total number of notes in the list
    override fun getCount(): Int = notes.size

    // Returns the note at the given position
    override fun getItem(position: Int): Pair<String?, String?> = notes[position]

    // Returns the ID for the given position (using the position as ID)
    override fun getItemId(position: Int): Long = position.toLong()

    // Responsible for creating or reusing a view for each note item in the ListView
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Reuse an existing view if possible, otherwise inflate a new one
        val rowView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false)

        // Use a ViewHolder to optimize view lookups
        val viewHolder = rowView.tag as? NoteViewHolder ?: NoteViewHolder(rowView).also { rowView.tag = it }

        // Get the mood and note content for the current position
        val (mood, note) = notes[position]

        // Update the TextViews with the mood and note content
        viewHolder.moodTextView.text = mood ?: "No Mood" // Fallback to "No Mood" if null
        viewHolder.noteTextView.text = note ?: "No Note Content" // Fallback to "No Note Content" if null

        // Set the background color based on the mood
        val color = getColorForMood(mood)
        rowView.setBackgroundColor(color)

        // Handle the delete button click
        viewHolder.deleteButton.setOnClickListener {
            val db = NotesDatabaseHelper(context) // Initialize the database helper

            // Remove the note from the database
            db.deleteNote(mood, note)

            // Remove the note from the list and refresh the adapter
            notes.removeAt(position)
            notifyDataSetChanged() // Notify the adapter that the data has changed
        }

        return rowView
    }

    // Updates the list of notes and refreshes the adapter
    fun updateNotes(newNotes: List<Pair<String?, String?>>) {
        notes.clear() // Clear the existing list
        notes.addAll(newNotes) // Add the new list of notes
        notifyDataSetChanged() // Notify the adapter to update the UI
    }

    // Returns a color based on the mood string
    private fun getColorForMood(mood: String?): Int {
        return when (mood?.toLowerCase()?.trim() ?: "") {
            "tranquile" -> ContextCompat.getColor(context, R.color.blue) // Calm blue
            "freshness" -> ContextCompat.getColor(context, R.color.green) // Fresh green
            "creative" -> ContextCompat.getColor(context, R.color.purple) // Creative purple
            "love" -> ContextCompat.getColor(context, R.color.pink) // Romantic pink
            "enthusiasm" -> ContextCompat.getColor(context, R.color.orange) // Energetic orange
            "nervous" -> ContextCompat.getColor(context, R.color.yellow) // Nervous yellow
            "anger" -> ContextCompat.getColor(context, R.color.red) // Angry red
            "seriousness" -> ContextCompat.getColor(context, R.color.black1) // Serious black
            else -> ContextCompat.getColor(context, R.color.gray) // Default gray
        }
    }

    // ViewHolder pattern to cache view references for improved performance
    class NoteViewHolder(rowView: View) {
        val moodTextView: TextView = rowView.findViewById(R.id.textView_mood) // TextView for the mood
        val noteTextView: TextView = rowView.findViewById(R.id.textView_note) // TextView for the note content
        val deleteButton: Button = rowView.findViewById(R.id.button_delete_note) // Button to delete the note
    }
}