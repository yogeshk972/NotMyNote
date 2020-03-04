package com.example.notmynote.ui.deleted

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notmynote.database.NotesDatabase
import com.example.notmynote.database.models.DeletedNotes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DeletedViewModel(application: Application) : ViewModel() {

    private val db = NotesDatabase.getInstance(application).deletedNotesDao()
    private var notes: LiveData<List<DeletedNotes>> = db.getAllNotes()

    fun insert(note: DeletedNotes) {
        CoroutineScope(Dispatchers.IO + Job()).launch {
            db.insert(note)
        }
    }

    fun update(note: DeletedNotes) {
        CoroutineScope(Dispatchers.IO + Job()).launch {
            db.update(note)
        }
    }

    fun delete(note: DeletedNotes) {
        CoroutineScope(Dispatchers.IO + Job()).launch {
            db.delete(note)
        }
    }

    fun getAllNotes(): LiveData<List<DeletedNotes>> {
        return notes
    }

}