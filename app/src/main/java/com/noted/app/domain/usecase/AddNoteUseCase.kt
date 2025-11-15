package com.noted.app.domain.usecase

import com.noted.app.domain.model.Note
import com.noted.app.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Result<Long> {
        return try {
            if (note.isEmpty) {
                Result.failure(Exception("Note cannot be empty"))
            } else {
                val id = repository.insertNote(note)
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
