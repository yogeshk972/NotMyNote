package com.example.notmynote.ui.notes

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notmynote.database.NotesDatabase
import com.example.notmynote.database.models.Notes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : ViewModel() {

    private val db = NotesDatabase.getInstance(application).notesDao()
    private val allNotes: LiveData<List<Notes>> = db.getAllNotes()

    fun insert(note: Notes) {
        CoroutineScope(Dispatchers.IO + Job()).launch {
            db.insert(note)
        }
    }

    fun update(note: Notes) {
        db.update(note)
    }

    fun delete(note: Notes) {
        CoroutineScope(Dispatchers.IO + Job()).launch {
            db.delete(note)
        }
    }

    fun getAllNotes(): LiveData<List<Notes>> {
        return allNotes
    }


}