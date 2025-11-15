package com.noted.app.presentation.notedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noted.app.domain.model.Note
import com.noted.app.domain.usecase.AddNoteUseCase
import com.noted.app.domain.usecase.DeleteNoteUseCase
import com.noted.app.domain.usecase.GetNoteByIdUseCase
import com.noted.app.domain.usecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: Long? = savedStateHandle.get<Long>("noteId")

    private val _title = MutableStateFlow("")
    private val _content = MutableStateFlow("")
    private val _color = MutableStateFlow(0xFFFFFFFF.toInt())
    private val _isPinned = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(false)
    private val _isSaved = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    private val noteFlow = if (noteId != null && noteId != 0L) {
        getNoteByIdUseCase(noteId)
    } else {
        MutableStateFlow(null)
    }

    val uiState: StateFlow<NoteDetailUiState> = combine(
        combine(
            noteFlow,
            _title,
            _content,
            _color,
            _isPinned
        ) { note, title, content, color, isPinned ->
            NoteDetailUiState(
                note = note,
                title = title,
                content = content,
                color = color,
                isPinned = isPinned
            )
        },
        _isLoading,
        _isSaved,
        _error
    ) { state, isLoading, isSaved, error ->
        state.copy(
            isLoading = isLoading,
            isSaved = isSaved,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NoteDetailUiState(isLoading = noteId != null && noteId != 0L)
    )

    init {
        if (noteId != null && noteId != 0L) {
            loadNote()
        }
    }

    private fun loadNote() {
        viewModelScope.launch {
            noteFlow.collect { note ->
                note?.let {
                    _title.value = it.title
                    _content.value = it.content
                    _color.value = it.color
                    _isPinned.value = it.isPinned
                    _isLoading.value = false
                }
            }
        }
    }

    fun onTitleChange(title: String) {
        _title.value = title
    }

    fun onContentChange(content: String) {
        _content.value = content
    }

    fun onColorChange(color: Int) {
        _color.value = color
    }

    fun togglePin() {
        _isPinned.update { !it }
    }

    fun saveNote() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            val note = if (noteId != null && noteId != 0L) {
                Note(
                    id = noteId,
                    title = _title.value.trim(),
                    content = _content.value.trim(),
                    color = _color.value,
                    createdAt = uiState.value.note?.createdAt ?: currentTime,
                    updatedAt = currentTime,
                    isPinned = _isPinned.value
                )
            } else {
                Note(
                    title = _title.value.trim(),
                    content = _content.value.trim(),
                    color = _color.value,
                    createdAt = currentTime,
                    updatedAt = currentTime,
                    isPinned = _isPinned.value
                )
            }

            val result = if (noteId != null && noteId != 0L) {
                updateNoteUseCase(note)
            } else {
                addNoteUseCase(note).map { }
            }

            result.fold(
                onSuccess = {
                    _isSaved.value = true
                },
                onFailure = { error ->
                    _error.value = error.message ?: "Failed to save note"
                }
            )
        }
    }

    fun deleteNote() {
        noteId?.let { id ->
            if (id != 0L) {
                viewModelScope.launch {
                    deleteNoteUseCase(id).fold(
                        onSuccess = {
                            _isSaved.value = true
                        },
                        onFailure = { error ->
                            _error.value = error.message ?: "Failed to delete note"
                        }
                    )
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
