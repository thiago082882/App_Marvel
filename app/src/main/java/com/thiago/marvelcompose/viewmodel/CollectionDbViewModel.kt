package com.thiago.marvelcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thiago.marvelcompose.model.CharacterResult
import com.thiago.marvelcompose.model.Note
import com.thiago.marvelcompose.model.db.CollectionDbRepository
import com.thiago.marvelcompose.model.db.DbCharacter
import com.thiago.marvelcompose.model.db.DbNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDbViewModel @Inject constructor(
    private val repo: CollectionDbRepository
) : ViewModel() {

    val currentCharacter = MutableStateFlow<DbCharacter?>(null)
    val collection = MutableStateFlow<List<DbCharacter>>(listOf())
    val notes = MutableStateFlow<List<DbNote>>(listOf())

    init {
        getCollection()
        getNotes()
    }


    private fun getCollection() {
        viewModelScope.launch {
            repo.getCharactersFromRepo().collect {
                collection.value = it
            }
        }
    }

    fun setCurrentCharacterId(characterId: Int?) {
        characterId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                repo.getCharacterFromRepo(it).collect {
                    currentCharacter.value = it

                }
            }
        }
    }

    fun addCharacter(character: CharacterResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addCharacterToRepo(DbCharacter.fromCharacter(character))
        }

    }

    fun deleteCharacter(character: DbCharacter) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllNotes(character)
            repo.deleteCharacterFromRepo(character)
        }
    }

    private fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllNotes().collect {
                notes.value = it
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addNoteToRepo(DbNote.fromNote(note))
        }
    }

    fun deleteNote(note: DbNote) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNoteFromRepo(note)

        }
    }

}
