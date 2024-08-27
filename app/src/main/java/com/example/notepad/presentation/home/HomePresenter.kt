package com.example.notepad.presentation.home

import android.annotation.SuppressLint
import android.util.Log
import com.example.notepad.data.repository.NotepadRepository
import com.example.notepad.models.Folder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenter(
    private var repository: NotepadRepository,
    private var view: HomeContract.View
): HomeContract.Presenter {

    private var compositeDisposable = CompositeDisposable()

    override fun initData() {
        var dispose = repository.getAllFolders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
            { folders ->
                view.showAllFolders(folders)
                view.showPinnedFolders(folders.filter { it.pinned })
            },
            { error ->
                view.showError(error.message ?: "Error Loading Data")
            }
        )
        compositeDisposable.add(dispose)
    }

    @SuppressLint("CheckResult")
    override fun pinnedClicked(isPinned: Boolean, folder: Folder) {
        if(isPinned){
            repository.unpinFolder(folderId = folder.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { view.showSuccessMessage("${folder.description} Pinned") },
                    { error -> view.showError(error.message ?: "Error Unpinning Folder") }
                )
        }else {
            repository.pinFolder(folderId = folder.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { view.showSuccessMessage("${folder.description} Unpinned") },
                    { error -> view.showError(error.message ?: "Error Pinning Folder") }
                )
        }
    }
}