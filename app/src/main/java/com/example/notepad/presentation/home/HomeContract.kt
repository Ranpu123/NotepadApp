package com.example.notepad.presentation.home

import com.example.notepad.models.Folder

interface HomeContract {

    interface View{
        fun showPinnedFolders(folders: List<Folder>)
        fun showAllFolders(folders: List<Folder>)
        fun showError(errorMsg: String)
        fun showSuccessMessage(message: String)
        fun setupToolbar()
    }

    interface Presenter{
        fun initData()
        fun pinnedClicked(isPinned: Boolean, folder: Folder)
    }
}