package com.example.notepad.presentation.note

class NoteEditorUseCase {
    fun validate(title: String): String? {
        if(title.isNullOrBlank()){
            return "Title cannot be empty"
        }

        if(title.length > 255){
            return "Title can't be bigger than 255 characters"
        }
        return null
    }
}