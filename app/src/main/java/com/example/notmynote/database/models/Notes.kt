package com.example.notmynote.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_notes_table")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0,

    @ColumnInfo(name = "Title")
    var title: String = "",

    @ColumnInfo(name = "Content")
    var content: String = ""

)