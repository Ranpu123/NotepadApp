package com.example.notepad.presentation.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.R
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.di.DatabaseModule
import com.example.notepad.di.HomeModule
import com.example.notepad.models.Folder
import com.example.notepad.presentation.folder.add.AddFolderActivity
import com.example.notepad.presentation.folder.list.FolderListActivity
import com.example.notepad.presentation.folder.list.FolderListActivity.Companion.FOLDER_BUNDLE
import com.example.notepad.presentation.folder.list.FolderListActivity.Companion.FOLDER_ID
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf

class HomeActivity :
    HomeContract.View,
    AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pinnedFolderAdapter: FolderAdapter
    private lateinit var allFolderAdapter: FolderAdapter
    private val presenter: HomeContract.Presenter by inject {
        parametersOf(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stopKoin()
        startKoin { modules(
            HomeModule
        ) }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        setupToolbar()
        initComponents()
    }

    override fun onResume() {
        super.onResume()
        initComponents()
    }

    private fun initComponents(){
        presenter.initData()
    }

    private fun setupRecyclerView() {
        binding.rVPinnedFolders.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        pinnedFolderAdapter = FolderAdapter(
            { isPinned, folder ->
                presenter.pinnedClicked(isPinned, folder)
            },
            this,
            false
        )
        binding.rVPinnedFolders.adapter = pinnedFolderAdapter
        pinnedFolderAdapter.onItemClicked = {
            var bundle = Bundle().apply {
                putInt(FOLDER_ID, it.id)
            }
            startActivity(FolderListActivity.instance(this).putExtra(FOLDER_BUNDLE, bundle))
        }

        binding.rVAllFolders.layoutManager = GridLayoutManager(this,2)
        allFolderAdapter = FolderAdapter(
            { isPinned, folder ->
                presenter.pinnedClicked(isPinned, folder)
            },
            this
        )
        allFolderAdapter.onAddClicked = {
            startActivity(AddFolderActivity.instance(this))
        }
        allFolderAdapter.onItemClicked = {
            var bundle = Bundle().apply {
                putInt(FOLDER_ID, it.id)
            }
            startActivity(FolderListActivity.instance(this).putExtra(FOLDER_BUNDLE, bundle))
        }
        binding.rVAllFolders.adapter = allFolderAdapter
    }

    override fun showPinnedFolders(folders: List<Folder>) {
        pinnedFolderAdapter.setFolders(folders)
    }

    override fun showAllFolders(folders: List<Folder>) {
        allFolderAdapter.setFolders(folders)
    }

    override fun showError(errorMsg: String) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun showSuccessMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
        }
    }
}