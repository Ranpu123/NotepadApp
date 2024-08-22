package com.example.notepad.presentation.folder.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.R
import com.example.notepad.data.local.models.FolderWithNotes
import com.example.notepad.databinding.ActivityFolderListBinding
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.di.DatabaseModule
import com.example.notepad.di.FolderListModule
import com.example.notepad.di.HomeModule
import com.example.notepad.presentation.folder.add.AddFolderActivity
import com.example.notepad.presentation.home.FolderAdapter
import com.example.notepad.presentation.note.NoteEditorActivity
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf

class FolderListActivity : AppCompatActivity(), FolderListContract.View {
    private lateinit var binding: ActivityFolderListBinding
    private lateinit var folderListAdapter: FolderListAdapter
    private val presenter: FolderListContract.Presenter by inject {
        parametersOf(this)
    }

    private var folderId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stopKoin()
        startKoin { modules(
            DatabaseModule,
            FolderListModule
        ) }

        enableEdgeToEdge()
        setContentView(R.layout.activity_folder_list)

        binding = ActivityFolderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    override fun onResume() {
        super.onResume()
        initComponents()
    }

    private fun initComponents() {
        setupRecyclerView()
        setupToolbar()
        getBundleData()
    }

    private fun getBundleData() {
        intent.getBundleExtra(FOLDER_BUNDLE)?.let { bundle ->
            folderId = bundle.getInt(FOLDER_ID)
            presenter.getFolder(folderId)
        }
    }

    private fun setupRecyclerView() {
        folderListAdapter = FolderListAdapter(this)
        binding.notesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.notesList.adapter = folderListAdapter

        folderListAdapter.onAddClicked = {
            startActivity(NoteEditorActivity.instance(this, folderId))
        }
        folderListAdapter.onItemClicked = {
            startActivity(NoteEditorActivity.instance(this, it.folderId, it.id))
        }
        folderListAdapter.onDeleteClicked = {
            presenter.deleteNote(it)
        }
    }

    override fun showFolder(folder: FolderWithNotes) {
        folderListAdapter.setNotes(folder.notes)
    }

    override fun showLoading() {
        binding.notesList.visibility = View.GONE
        binding.pBLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.notesList.visibility = View.VISIBLE
        binding.pBLoading.visibility = View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            setDisplayShowTitleEnabled(false)
        }

        binding.btnDelete.setOnClickListener { presenter.deleteFolder(folderId) }
    }

    override fun backToHome() {
        finish()
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

    companion object {
        const val FOLDER_BUNDLE = "folder_bundle"
        const val FOLDER_ID = "folder_id"

        fun instance(context: Context): Intent {
            return Intent(context, FolderListActivity::class.java)
        }
    }
}