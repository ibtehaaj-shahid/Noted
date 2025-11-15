package com.noted.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noted.app.data.local.dao.NoteDao
import com.noted.app.data.local.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = true
)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "notes_database"
    }
}
