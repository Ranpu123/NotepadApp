package com.example.notepad.di

import android.content.Context
import androidx.room.Room
import com.example.notepad.data.local.source.dao.NotepadDao
import com.example.notepad.data.local.database.AppDatabase
import com.example.notepad.data.local.source.INotepadFileSource
import com.example.notepad.data.local.source.NotepadLocalFileSource
import com.example.notepad.data.repository.NotepadRepository
import com.example.notepad.presentation.folder.add.AddFolderContract
import com.example.notepad.presentation.folder.add.AddFolderPresenter
import com.example.notepad.presentation.folder.add.AddFolderUseCase
import com.example.notepad.presentation.folder.list.FolderListContract
import com.example.notepad.presentation.folder.list.FolderListPresenter
import com.example.notepad.presentation.home.HomeContract
import com.example.notepad.presentation.home.HomePresenter
import com.example.notepad.presentation.note.NoteEditorContract
import com.example.notepad.presentation.note.NoteEditorPresenter
import com.example.notepad.presentation.note.NoteEditorUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun provideDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java,"appDatabase")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

fun provideNotepadDao(db: AppDatabase): NotepadDao = db.notepadDao()

val DatabaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideNotepadDao(get()) }
}

val HomeModule = module {
    single<INotepadFileSource> { NotepadLocalFileSource() }
    single { NotepadRepository(get(), get()) }
    single<HomeContract.Presenter> { (view: HomeContract.View) -> HomePresenter(get(), view) }
}

val FolderListModule = module {
    single<INotepadFileSource> { NotepadLocalFileSource() }
    single { NotepadRepository(get(), get()) }
    single<FolderListContract.Presenter> { (view: FolderListContract.View) -> FolderListPresenter(view, get()) }
}

val AddFolderModule = module {
    single<INotepadFileSource> { NotepadLocalFileSource() }
    single { NotepadRepository(get(), get()) }
    single<AddFolderContract.Presenter> { (view: AddFolderContract.View) -> AddFolderPresenter(get(), view, AddFolderUseCase()) }
}

val NoteEditorModule = module {
    single<INotepadFileSource> { NotepadLocalFileSource() }
    single { NotepadRepository(get(), get()) }
    single<NoteEditorContract.Presenter> { (view: NoteEditorContract.View) -> NoteEditorPresenter(view, get(), NoteEditorUseCase(), androidContext()) }
}