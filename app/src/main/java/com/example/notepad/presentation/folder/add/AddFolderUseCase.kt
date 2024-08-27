package com.example.notepad.presentation.folder.add

class AddFolderUseCase {
    fun validate(description: String): String?{
        if(description.length > 255){
            return "Folder name can't be bigger than 255 characters"
        }

        if(description.isNullOrBlank()){
            return "Folder name cannot be empty"
        }
        return null
    }
}
