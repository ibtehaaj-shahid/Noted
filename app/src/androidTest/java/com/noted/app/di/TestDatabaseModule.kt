package com.noted.app.di

import android.content.Context
import androidx.room.Room
import com.noted.app.data.local.NotesDatabase
import com.noted.app.data.local.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideTestNotesDatabase(
        @ApplicationContext context: Context
    ): NotesDatabase {
        return Room.inMemoryDatabaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: NotesDatabase): NoteDao {
        return database.noteDao()
    }
}