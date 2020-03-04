package com.example.notmynote.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notmynote.database.models.ArchivedNotes

@Dao
interface ArchivedNotesDao {
    @Insert
    fun insert(note: ArchivedNotes)

    @Update
    fun update(note: ArchivedNotes)

    @Delete
    fun delete(note: ArchivedNotes)

    @Query("SELECT * from archieved_notes_table ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<ArchivedNotes>>
}