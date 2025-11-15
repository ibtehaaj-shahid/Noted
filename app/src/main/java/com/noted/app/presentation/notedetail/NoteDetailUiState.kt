package com.noted.app.presentation.notedetail

import com.noted.app.domain.model.Note

data class NoteDetailUiState(
    val note: Note? = null,
    val title: String = "",
    val content: String = "",
    val color: Int = 0xFFFFFFFF.toInt(),
    val isPinned: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)
