package com.example.notepad.presentation.folder.add

import com.example.notepad.data.repository.NotepadRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddFolderPresenter(
    private var repository: NotepadRepository,
    private var view: AddFolderContract.View,
    private var useCase: AddFolderUseCase
): AddFolderContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun addFolder(description: String) {
        view.showLoading()
        repository.addFolder(description)
            .doOnSubscribe { view.showLoading() }
            .doOnError { view.hideLoading() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { view.showSuccessMessage() },
                { error -> view.showError(error.message ?: "Error Adding Folder") }
            )
    }

    override fun validate(description: String) {
        var res = useCase.validate(description)
        if(res != null){
            view.showEditTextError(res)
            view.disableAddButton()
        } else {
            view.clearEditTextError()
            view.enableAddButton()
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }
}
