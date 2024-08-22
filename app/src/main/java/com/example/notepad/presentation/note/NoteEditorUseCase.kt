package com.example.notepad.presentation.note

class NoteEditorUseCase {
    fun validate(title: String): Boolean {
        return title.length > 255
    }
}