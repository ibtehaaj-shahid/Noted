package com.noted.app.domain.repository

import com.noted.app.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(): Flow<List<Note>>

    fun getNoteById(noteId: Long): Flow<Note?>

    suspend fun getNoteByIdOnce(noteId: Long): Note?

    fun searchNotes(query: String): Flow<List<Note>>

    suspend fun insertNote(note: Note): Long

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun deleteNoteById(noteId: Long)

    suspend fun togglePinStatus(noteId: Long, isPinned: Boolean)
}
