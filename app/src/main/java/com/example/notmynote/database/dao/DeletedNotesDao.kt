package com.example.notmynote.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notmynote.database.models.DeletedNotes

@Dao
interface DeletedNotesDao {
    @Insert
    fun insert(note: DeletedNotes): Long

    @Update
    fun update(note: DeletedNotes)

    @Delete
    fun delete(note: DeletedNotes)

    @Query("SELECT * from deleted_notes_table ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<DeletedNotes>>
}