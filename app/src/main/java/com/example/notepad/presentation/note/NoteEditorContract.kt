package com.example.notepad.presentation.note

import com.example.notepad.models.Note

interface NoteEditorContract {
    interface View {
        fun initComponents()
        fun setupToolbar()
        fun showTitle(title: String)
        fun showRichText(richText: String)
        fun showLoading()
        fun hideLoading()
        fun showCreatedSuccessMessage()
        fun showUpdatedSuccessMessage()

        fun showErrorMessage(message: String)
        fun enableSave()
        fun disableSave()
        fun editTextError(message: String?)
        fun backToList()
    }

    interface Presenter {
        fun createNewNote(richText: String, title: String, folderId: Int)
        fun updateNoteData(richText: String, title: String, noteId: Int)
        fun getNoteData(noteId: Int)
        fun validate(title: String)
    }
}