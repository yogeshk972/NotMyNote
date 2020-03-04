package com.example.notmynote.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_notes_table")
data class DeletedNotes(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "Title")
    var title: String = "",
    @ColumnInfo(name = "Content")
    var content: String = ""
)