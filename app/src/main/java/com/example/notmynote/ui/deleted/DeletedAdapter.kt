package com.example.notmynote.ui.deleted

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notmynote.R
import com.example.notmynote.database.models.DeletedNotes

class DeletedNotesAdapter :
    ListAdapter<DeletedNotes, DeletedNotesAdapter.NoteViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.note_details, parent, false)
        return NoteViewHolder(v)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getNote(position)
        holder.title.text = note.title
        holder.content.text = note.content
    }

    fun getNote(pos: Int): DeletedNotes {
        return super.getItem(pos)
    }

    inner class NoteViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.note_title)
        val content: TextView = v.findViewById(R.id.note_content)
    }

    class DiffCallback : DiffUtil.ItemCallback<DeletedNotes>() {
        override fun areItemsTheSame(oldItem: DeletedNotes, newItem: DeletedNotes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DeletedNotes, newItem: DeletedNotes): Boolean {
            return oldItem.title == newItem.title && oldItem.content == newItem.content
        }
    }
}

