package com.example.notepad.presentation.folder.add

interface AddFolderContract {

    interface View {
        fun showSuccessMessage()
        fun showLoading()
        fun hideLoading()
        fun showEditTextError(errorMsg: String)
        fun clearEditTextError()
        fun enableAddButton()
        fun disableAddButton()
        fun showError(errorMsg: String)
        fun setupToolbar()
    }

    interface Presenter {
        fun addFolder(description: String)
        fun validate(description: String)
        fun onDestroy()
    }
}