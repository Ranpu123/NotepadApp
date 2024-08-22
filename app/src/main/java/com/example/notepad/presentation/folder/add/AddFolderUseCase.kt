package com.example.notepad.presentation.folder.add

class AddFolderUseCase {
    fun validateString(description: String): Boolean {
        return description.isNullOrBlank()
    }

    fun validateSize(description: String): Boolean{
        return description.length > 255
    }
}
