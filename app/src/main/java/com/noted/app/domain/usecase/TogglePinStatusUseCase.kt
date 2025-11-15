package com.noted.app.domain.usecase

import com.noted.app.domain.repository.NoteRepository
import javax.inject.Inject

class TogglePinStatusUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long, isPinned: Boolean): Result<Unit> {
        return try {
            repository.togglePinStatus(noteId, isPinned)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
