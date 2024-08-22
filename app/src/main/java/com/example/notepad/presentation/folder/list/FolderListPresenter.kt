package com.example.notepad.presentation.folder.list

import com.example.notepad.data.repository.NotepadRepository
import com.example.notepad.models.Folder
import com.example.notepad.models.Note
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FolderListPresenter(
    private val view: FolderListContract.View,
    private val repository: NotepadRepository
): FolderListContract.Presenter {

    private var disposable = CompositeDisposable()
    private lateinit var folder: Folder

    override fun getFolder(folderId: Int) {
        disposable.add(
            repository.getFolderWithNotes(folderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { folderWithNotes ->
                        folder = folderWithNotes.folder
                        view.showFolder(folderWithNotes)
                        view.hideLoading()
                    },
                    { error -> view.showError(error.message ?: "") }
                )
        )
    }

    override fun deleteNote(note: Note) {
        repository.removeNote(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.showLoading() }
            .doOnError { error ->
                view.showError(error.message ?: "")
                view.hideLoading()
            }
            .doOnComplete {
                view.hideLoading()
            }
            .subscribe()
    }

    override fun deleteFolder(folderId: Int) {
        repository.removeFolder(folder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.showLoading() }
            .doOnError { error ->
                view.showError(error.message ?: "")
                view.hideLoading()
            }
            .doOnComplete {
                view.hideLoading()
                view.backToHome()
            }
            .subscribe()
    }
}