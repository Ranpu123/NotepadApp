package com.example.notepad.presentation.folder.list

import com.example.notepad.data.local.models.FolderWithNotes
import com.example.notepad.models.Note

interface FolderListContract {
    interface View {
        fun showFolder(folder: FolderWithNotes)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun setupToolbar()
        fun backToHome()
    }

    interface Presenter {
        fun getFolder(folderId: Int)
        fun deleteNote(note: Note)
        fun deleteFolder(folderId: Int)
    }
}