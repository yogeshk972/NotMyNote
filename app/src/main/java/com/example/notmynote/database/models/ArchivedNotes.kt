package com.example.notmynote.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "archieved_notes_table")
data class ArchivedNotes(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name = "Title")
    var title: String = "",

    @ColumnInfo(name = "Content")
    var content: String = ""
)