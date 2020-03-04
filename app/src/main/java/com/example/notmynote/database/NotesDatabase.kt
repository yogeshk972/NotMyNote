package com.example.notmynote.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notmynote.database.dao.ArchivedNotesDao
import com.example.notmynote.database.dao.DeletedNotesDao
import com.example.notmynote.database.dao.NotesDao
import com.example.notmynote.database.models.ArchivedNotes
import com.example.notmynote.database.models.DeletedNotes
import com.example.notmynote.database.models.Notes

@Database(entities = arrayOf(Notes::class, ArchivedNotes::class, DeletedNotes::class), version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun deletedNotesDao(): DeletedNotesDao
    abstract fun archivedNotesDao(): ArchivedNotesDao

    companion object {

        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            NotesDatabase::class.java,
                            "myNotesDatabase.db"
                        )
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}