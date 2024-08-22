package com.example.notepad.presentation.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.notepad.R
import com.example.notepad.databinding.ActivityNoteEditorBinding
import com.example.notepad.di.DatabaseModule
import com.example.notepad.di.NoteEditorModule
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf

class NoteEditorActivity : AppCompatActivity(), NoteEditorContract.View {

    private lateinit var binding: ActivityNoteEditorBinding
    private val presenter: NoteEditorContract.Presenter by inject {
        parametersOf(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stopKoin()
        startKoin{ modules(
                DatabaseModule,
                NoteEditorModule
            )
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_note_editor)

        binding = ActivityNoteEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setup()
    }


    override fun onResume() {
        super.onResume()
        initComponents()
    }

    private fun setup(){
        var noteId = getNoteId()
        if(noteId != -1){
            presenter.getNoteData(noteId)
        }else{
            hideLoading()
        }

    }

    override fun initComponents(){
        setupToolbar()
        checkForFolderId()

        binding.richEditText.setStyleBar(binding.styleBar)
        binding.styleBar.setEditText(binding.richEditText)

        binding.eTNoteTitle.addTextChangedListener {
            presenter.validate(it.toString())
        }

        binding.btnSave.setOnClickListener {
            onSaveNote()
        }
    }

    override fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun showTitle(title: String) {
        binding.eTNoteTitle.apply {
            text.clear()
            append(title)
        }
    }

    override fun showRichText(richText: String) {
        binding.richEditText.text?.clear()
        binding.richEditText.setHtml(richText)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onSaveNote() {
        val title = binding.eTNoteTitle.text.toString()
        val richText = binding.richEditText.getHtml()

        var noteId = getNoteId()
        if(noteId == -1){
            presenter.createNewNote(
                richText,
                title,
                getFolderId()
            )
        }else{
            presenter.updateNoteData(
                richText,
                title,
                noteId
            )
        }
    }

    override fun showLoading() {
        binding.apply {
            pBLoading.visibility = View.VISIBLE
            cardView.visibility = View.GONE
            eTNoteTitle.visibility = View.GONE
            eTNoteTitle.isEnabled = false
            richEditText.isEnabled = false

            btnSave.isEnabled = false
        }
    }

    override fun hideLoading() {
        binding.apply {
            pBLoading.visibility = View.GONE
            cardView.visibility = View.VISIBLE
            eTNoteTitle.visibility = View.VISIBLE
            eTNoteTitle.isEnabled = true
            richEditText.isEnabled = true

            btnSave.isEnabled = true
        }
    }

    override fun showSuccessMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun enableSave() {
        binding.btnSave.isEnabled = true
    }

    override fun disableSave() {
        binding.btnSave.isEnabled = false
    }

    override fun editTextError(message: String?) {
        binding.eTNoteTitle.error = message
    }

    override fun backToList() {
        finish()
    }

    private fun getNoteId(): Int {
        intent.getBundleExtra(NOTE_BUNDLE)?.let {
            return it.getInt(NOTE_ID, -1)
        }
        return -1
    }

    private fun getFolderId(): Int {
        intent.getBundleExtra(NOTE_BUNDLE)?.let {
            return it.getInt(FOLDER_ID, -1)
        }
        return -1
    }

    private fun checkForFolderId(){
        if(getFolderId() == -1){
            showErrorMessage("Failed to load folder id!")
            finish()
        }
    }

    companion object{
        const val NOTE_ID = "noteId"
        const val FOLDER_ID = "folderId"
        const val NOTE_BUNDLE = "noteBundle"

        private fun instance(context: Context) = Intent(context, NoteEditorActivity::class.java)

        fun instance(context: Context, folderId: Int, noteId: Int? = null): Intent {
            var bundle = Bundle().apply {
                noteId?.let { putInt(NOTE_ID, it) }
                putInt(FOLDER_ID, folderId)
            }
            return instance(context).apply {
                putExtra(NOTE_BUNDLE, bundle)
            }
        }
    }
}