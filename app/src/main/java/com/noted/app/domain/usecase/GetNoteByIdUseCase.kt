package com.noted.app.domain.usecase

import com.noted.app.domain.model.Note
import com.noted.app.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(noteId: Long): Flow<Note?> {
        return repository.getNoteById(noteId)
    }
}
