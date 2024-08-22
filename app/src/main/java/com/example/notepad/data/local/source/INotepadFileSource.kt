package com.example.notepad.data.local.source

import java.io.File

interface INotepadFileSource {

    fun writeToStorage(writeTo: File, content: String): Boolean

    fun readFromStorage(readFrom: File): String?

    fun removeFromStorage(file: File): Boolean

    companion object{
        const val TEMP_CONTENT_NAME = "temp.txt"
        const val TEMP_TITLE_NAME = "temp_title.txt"
    }

}
