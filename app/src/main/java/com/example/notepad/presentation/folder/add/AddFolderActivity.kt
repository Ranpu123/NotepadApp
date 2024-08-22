package com.example.notepad.presentation.folder.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.notepad.R
import com.example.notepad.databinding.ActivityAddFolderBinding
import com.example.notepad.di.AddFolderModule
import com.example.notepad.di.DatabaseModule
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf

class AddFolderActivity : AppCompatActivity(), AddFolderContract.View {

    private val presenter: AddFolderContract.Presenter by inject {
        parametersOf(this)
    }

    private lateinit var binding: ActivityAddFolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_add_folder)

        stopKoin()
        startKoin { modules(
            DatabaseModule,
            AddFolderModule,
        ) }

        binding = ActivityAddFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.etFolderName.addTextChangedListener {
            presenter.validate(it.toString())
        }

        binding.btnAddFolder.setOnClickListener {
            presenter.addFolder(binding.etFolderName.text.toString())
        }
        setupToolbar()

    }

    override fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            setDisplayShowTitleEnabled(false)
        }
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

    override fun showSuccessMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun showLoading() {
        binding.pBLoading.visibility = View.VISIBLE
        binding.btnAddFolder.isEnabled = false
        binding.etFolderName.isEnabled = false
    }

    override fun hideLoading() {
        binding.pBLoading.visibility = View.VISIBLE
        binding.btnAddFolder.isEnabled = true
        binding.etFolderName.isEnabled = true
    }

    override fun showEditTextError(errorMsg: String) {
        binding.etFolderName.error = errorMsg
    }

    override fun clearEditTextError() {
        binding.etFolderName.error = null
    }

    override fun enableAddButton() {
        binding.btnAddFolder.isEnabled = true
    }

    override fun disableAddButton() {
        binding.btnAddFolder.isEnabled = false
    }

    override fun showError(errorMsg: String) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    companion object{
        fun instance(context: Context): Intent {
            return Intent(context, AddFolderActivity::class.java)
        }
    }
}