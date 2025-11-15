package com.noted.app.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val color: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val isPinned: Boolean = false
) {
    val isEmpty: Boolean
        get() = title.isBlank() && content.isBlank()

}
