package com.noted.app.data.mapper

import com.noted.app.data.local.entity.NoteEntity
import com.noted.app.domain.model.Note

fun NoteEntity.toDomainModel(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        color = color,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isPinned = isPinned
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        color = color,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isPinned = isPinned
    )
}

fun List<NoteEntity>.toDomainModelList(): List<Note> {
    return map { it.toDomainModel() }
}
