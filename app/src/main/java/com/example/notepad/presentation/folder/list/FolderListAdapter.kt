package com.example.notepad.presentation.folder.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.R
import com.example.notepad.data.local.models.FolderWithNotes
import com.example.notepad.databinding.NoteAddBinding
import com.example.notepad.databinding.NoteItemBinding
import com.example.notepad.models.Note
import java.time.format.DateTimeFormatter

class FolderListAdapter(
    private val context: Context
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var notes = mutableListOf<Note>()
    var onAddClicked: () -> Unit = {}
    var onItemClicked: (Note) -> Unit = {}
    var onDeleteClicked: (Note) -> Unit = {}

    fun setNotes(notes: List<Note>){
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) ADD_NOTE_VIEW_TYPE else NOTE_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NOTE_VIEW_TYPE -> {
                FolderViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
                )
            }

            else -> {
                AddViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.note_add, parent, false)
                )
            }
        }
    }

    override fun getItemCount(): Int = notes.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ADD_NOTE_VIEW_TYPE -> (holder as AddViewHolder).bind()
            NOTE_VIEW_TYPE -> (holder as FolderViewHolder).bind(notes[position - 1])
        }
    }

    inner class FolderViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = NoteItemBinding.bind(itemView)

        fun bind(note: Note){
            binding.tvNoteDescription.text = note.title
            binding.tvNoteDate.text = note.createdAt.format(DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm"))
            binding.btnDelete.setOnClickListener { onDeleteClicked.invoke(note) }
            binding.llNoteDescription.setOnClickListener { onItemClicked.invoke(note) }
        }
    }

    inner class AddViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = NoteAddBinding.bind(itemView)

        fun bind(){
            binding.btnAddNote.setOnClickListener { onAddClicked.invoke() }
        }
    }

    companion object {
        const val NOTE_VIEW_TYPE = 0
        const val ADD_NOTE_VIEW_TYPE = 1
    }
}