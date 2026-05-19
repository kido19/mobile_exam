package com.example.dailynotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dailynotes.data.local.Note
import com.example.dailynotes.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteDetailViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _noteTitle = MutableStateFlow("")
    val noteTitle: StateFlow<String> = _noteTitle.asStateFlow()

    private val _noteContent = MutableStateFlow("")
    val noteContent: StateFlow<String> = _noteContent.asStateFlow()

    private var currentNoteId: Int? = null

    fun loadNote(id: Int) {
        viewModelScope.launch {
            val note = repository.getNoteById(id)
            note?.let {
                currentNoteId = it.id
                _noteTitle.value = it.title
                _noteContent.value = it.content
            }
        }
    }

    fun updateTitle(title: String) {
        _noteTitle.value = title
    }

    fun updateContent(content: String) {
        _noteContent.value = content
    }

    fun saveNote() {
        val title = _noteTitle.value.trim()
        val content = _noteContent.value.trim()
        if (title.isEmpty() && content.isEmpty()) return

        viewModelScope.launch {
            if (currentNoteId == null) {
                repository.insert(Note(title = title, content = content))
            } else {
                repository.update(Note(id = currentNoteId!!, title = title, content = content))
            }
        }
    }

    fun deleteNote() {
        currentNoteId?.let { id ->
            viewModelScope.launch {
                repository.delete(Note(id = id, title = _noteTitle.value, content = _noteContent.value))
            }
        }
    }
}

class NoteDetailViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
