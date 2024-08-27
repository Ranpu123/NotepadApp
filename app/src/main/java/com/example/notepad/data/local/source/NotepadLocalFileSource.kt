package com.example.notepad.data.local.source

import android.util.Log
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class NotepadLocalFileSource: INotepadFileSource{

    override fun writeToStorage(writeTo: File, content: String): Boolean {
        return try {
            FileOutputStream(writeTo).use{
                it.write(content.toByteArray())
                it.flush()
            }
            true
        } catch (e: Exception) {
            Log.e("[INTERNAL STORAGE]", "Error saving file: ${writeTo.name}. Caused by: ${e.message}")
            false
        }
    }

    override fun readFromStorage(readFrom: File): String? {
        if(!readFrom.exists() || !readFrom.canRead()) return null

        return try {
            val content = ByteArray(readFrom.length().toInt())
            FileInputStream(readFrom).use{ it.read(content) }
            String(content)
        } catch (e: Exception) {
            Log.e("[INTERNAL STORAGE]", "Error reading file: ${readFrom.name}. Caused by: ${e.message}")
            null
        }

    }

    override fun removeFromStorage(file: File): Boolean {
        if(!file.exists()){
            Log.e("[INTERNAL STORAGE]", "File: ${file.name} does not exist.")
            return true
        }
        return try {
            file.delete()
            true
        } catch (e: Exception) {
            Log.e("[INTERNAL STORAGE]", "Error deleting file: ${file.name}. Caused by: ${e.message}")
            false
        }
    }
}