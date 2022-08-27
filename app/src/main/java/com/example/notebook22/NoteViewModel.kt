package com.example.notebook22

import androidx.lifecycle.*
import com.example.notebook22.data.Note
import com.example.notebook22.data.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class NoteViewModel(private val noteDao: NoteDao) : ViewModel() {


    val listOfNotes: LiveData<List<Note>> = noteDao.getAll().asLiveData()



    fun getNoteById(id: Int): LiveData<Note>? = noteDao.getNoteById(id)?.asLiveData()

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.deleteNote(note)
        }
    }

    fun insertNote(
        title: String,
        description: String,
        time: Int
    ) {
        val note = Note(
            title = title,
            description = description,
            time = time
        )

        viewModelScope.launch {
            noteDao.addNote(note)
        }
    }

    fun updateNote(
        id: Int,
        title: String,
        description: String,
        time: Int
    ) {
        val note = Note(
            id = id,
            title = title,
            description = description,
            time = time
        )
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.updateNote(note)
        }
    }

    fun isValidEntry(title: String, description: String): Boolean {
        return title.isNotBlank() || description.isNotBlank()
    }
}

class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}