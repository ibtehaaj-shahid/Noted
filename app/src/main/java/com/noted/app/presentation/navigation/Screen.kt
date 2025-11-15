package com.noted.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Notes : Screen

    @Serializable
    data class NoteDetail(val noteId: Long = 0L) : Screen
}
