package com.example.notmynote.ui.archive

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notmynote.database.NotesDatabase
import com.example.notmynote.database.models.ArchivedNotes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArchiveViewModel(application: Application) : ViewModel() {

    private val db = NotesDatabase.getInstance(application).archivedNotesDao()
    private var notes: LiveData<List<ArchivedNotes>> = db.getAllNotes()

    fun insert(note: ArchivedNotes) {
        CoroutineScope(Dispatchers.IO + Job()).launch {
            db.insert(note)
        }
    }

    fun update(note: ArchivedNotes) {
        CoroutineScope(Dispatchers.IO + Job()).launch {
            db.update(note)
        }
    }

    fun delete(note: ArchivedNotes) {
        CoroutineScope(Dispatchers.IO + Job()).launch {
            db.delete(note)
        }
    }

    fun getAllNotes(): LiveData<List<ArchivedNotes>> {
        return notes
    }
}