package com.example.notmynote.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notmynote.R
import com.example.notmynote.database.models.Notes

class NotesAdapter() : ListAdapter<Notes, NotesAdapter.NoteViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.note_details, parent, false)
        return NoteViewHolder(v)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getNote(position)
        holder.title.text = note.title
        holder.content.text = note.content

        holder.itemView.setOnClickListener {
            val action = NotesFragmentDirections.actionNavNotesToNewEntry(note.title, note.content)
            action.setId(note.noteId)
            action.setUpdate(true)

            it.findNavController().navigate(action)
            notifyItemChanged(position)
        }

    }

    fun getNote(position: Int): Notes {
        return super.getItem(position)
    }

    inner class NoteViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.note_title)
        val content: TextView = v.findViewById(R.id.note_content)
    }

    class DiffCallback : DiffUtil.ItemCallback<Notes>() {
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.noteId == newItem.noteId
        }

        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.title == newItem.title && oldItem.content == newItem.content
        }
    }

}
