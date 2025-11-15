package com.noted.app.data.repository

import com.noted.app.data.local.dao.NoteDao
import com.noted.app.data.mapper.toDomainModel
import com.noted.app.data.mapper.toDomainModelList
import com.noted.app.data.mapper.toEntity
import com.noted.app.domain.model.Note
import com.noted.app.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.toDomainModelList()
        }
    }

    override fun getNoteById(noteId: Long): Flow<Note?> {
        return noteDao.getNoteById(noteId).map { entity ->
            entity?.toDomainModel()
        }
    }

    override suspend fun getNoteByIdOnce(noteId: Long): Note? {
        return noteDao.getNoteByIdOnce(noteId)?.toDomainModel()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return noteDao.searchNotes(query).map { entities ->
            entities.toDomainModelList()
        }
    }

    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }

    override suspend fun deleteNoteById(noteId: Long) {
        noteDao.deleteNoteById(noteId)
    }

    override suspend fun togglePinStatus(noteId: Long, isPinned: Boolean) {
        noteDao.updatePinStatus(noteId, isPinned)
    }
}
