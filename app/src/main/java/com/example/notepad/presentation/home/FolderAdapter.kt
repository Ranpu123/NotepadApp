package com.example.notepad.presentation.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.R
import com.example.notepad.databinding.FolderAddBinding
import com.example.notepad.databinding.FolderItemBinding
import com.example.notepad.models.Folder

class FolderAdapter(
    private val onPinChanged: (Boolean, Folder) -> Unit,
    private val context: Context,
    private val showAdd: Boolean = true
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Folder>()
    var onAddClicked: () -> Unit = {}
    var onItemClicked: (Folder) -> Unit = {}

    fun setFolders(folders: List<Folder>){
        this.items.clear()
        this.items.addAll(folders)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if(showAdd && position == 0) ADD_FOLDER_VIEW_TYPE else FOLDER_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(showAdd) {
            when (viewType) {
                FOLDER_VIEW_TYPE -> {
                    FolderViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false)
                    )
                }

                else -> {
                    AddViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.folder_add, parent, false)
                    )
                }
            }
        }else{
            FolderViewHolder(
                LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ADD_FOLDER_VIEW_TYPE -> (holder as AddViewHolder).bind()
            FOLDER_VIEW_TYPE -> (holder as FolderViewHolder).bind(items[if(showAdd) position - 1 else position])
        }
    }

    override fun getItemCount(): Int = if (showAdd) items.size + 1 else items.size

    inner class FolderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = FolderItemBinding.bind(itemView)

        fun bind(folder: Folder){
            binding.folderCard.apply {
                folderName = folder.description
                isPinned = folder.pinned
                totalNotes = 0
                onPinClickListener = {
                    onPinChanged(!isPinned, folder)
                }
                setOnClickListener { onItemClicked.invoke(folder) }
            }
        }
    }

    inner class AddViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = FolderAddBinding.bind(itemView)

        fun bind(){
            binding.cardContainer.setOnClickListener{ onAddClicked.invoke() }
        }
    }

    companion object {
        const val FOLDER_VIEW_TYPE = 0
        const val ADD_FOLDER_VIEW_TYPE = 1
    }
}