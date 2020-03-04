package com.example.notmynote.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notmynote.database.models.Notes

@Dao
interface NotesDao {
    @Insert
    fun insert(note: Notes): Long

    @Update
    fun update(note: Notes)

    @Delete
    fun delete(note: Notes)

    @Query("select * from my_notes_table")
    fun getAllNotes(): LiveData<List<Notes>>
}