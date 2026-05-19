package com.example.dailynotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dailynotes.data.local.Note
import com.example.dailynotes.data.remote.QuoteApi
import com.example.dailynotes.data.remote.QuoteResponse
import com.example.dailynotes.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class QuoteState {
    object Loading : QuoteState()
    data class Success(val quote: QuoteResponse) : QuoteState()
    data class Error(val message: String) : QuoteState()
}

class HomeViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _quoteState = MutableStateFlow<QuoteState>(QuoteState.Loading)
    val quoteState: StateFlow<QuoteState> = _quoteState.asStateFlow()

    val allNotes = repository.allNotes
    private val quoteApi = QuoteApi.create()

    init {
        fetchQuote()
    }

    fun fetchQuote() {
        viewModelScope.launch {
            _quoteState.value = QuoteState.Loading
            try {
                val quote = quoteApi.getRandomQuote()
                _quoteState.value = QuoteState.Success(quote)
            } catch (e: Exception) {
                _quoteState.value = QuoteState.Error("Failed to load quote: ${e.message}")
            }
        }
    }
}

class HomeViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
