package com.noted.app.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noted.app.domain.usecase.DeleteNoteUseCase
import com.noted.app.domain.usecase.GetAllNotesUseCase
import com.noted.app.domain.usecase.SearchNotesUseCase
import com.noted.app.domain.usecase.TogglePinStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val togglePinStatusUseCase: TogglePinStatusUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    private val _isLoading = MutableStateFlow(false)

    private val _error = MutableStateFlow<String?>(null)

    private val debouncedSearchQuery = _searchQuery
        .debounce(300)

    private val notesFlow = combine(
        debouncedSearchQuery,
        _isSearchActive
    ) { query, isSearchActive ->
        if (isSearchActive && query.isNotBlank()) {
            query
        } else {
            ""
        }
    }.flatMapLatest { query ->
        if (query.isBlank()) {
            getAllNotesUseCase()
        } else {
            searchNotesUseCase(query)
        }
    }

    val uiState: StateFlow<NotesUiState> = combine(
        notesFlow,
        _isLoading,
        _searchQuery,
        _isSearchActive,
        _error
    ) { notes, isLoading, searchQuery, isSearchActive, error ->
        NotesUiState(
            notes = notes,
            isLoading = isLoading,
            searchQuery = searchQuery,
            isSearchActive = isSearchActive,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotesUiState(isLoading = true)
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSearchActiveChange(active: Boolean) {
        _isSearchActive.value = active
        if (!active) {
            _searchQuery.value = ""
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId).fold(
                onSuccess = {
                    // Note deleted successfully
                },
                onFailure = { error ->
                    _error.value = error.message
                }
            )
        }
    }

    fun togglePinStatus(noteId: Long, isPinned: Boolean) {
        viewModelScope.launch {
            togglePinStatusUseCase(noteId, !isPinned)
        }
    }

    fun clearError() {
        _error.value = null
    }
}
