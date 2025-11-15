package com.noted.app.domain.usecase

import com.noted.app.domain.model.Note
import com.noted.app.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Result<Unit> {
        return try {
            if (note.isEmpty) {
                Result.failure(Exception("Note cannot be empty"))
            } else {
                repository.updateNote(note)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
