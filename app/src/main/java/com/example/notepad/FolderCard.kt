package com.example.notepad

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.notepad.databinding.FolderCardBinding

class FolderCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = FolderCardBinding.inflate(LayoutInflater.from(context), this, true)

    var onPinClickListener: (Boolean) -> Unit = {}

    var isPinned = false
        set(isPinned) {
            field = isPinned
            binding.ibPin.setImageDrawable(
                if (isPinned) ContextCompat.getDrawable(context, R.drawable.pinned_icon)
                else ContextCompat.getDrawable(context, R.drawable.unpinned_icon)
            )
        }
    var folderName = ""
        set(folderName) {
            field = folderName
            binding.tvFolderName.text = folderName
        }
    var totalNotes = 0
        set(totalNotes) {
            field = totalNotes
            binding.tvTotalNotes.text = resources.getQuantityString(R.plurals.notes_count, totalNotes, totalNotes)
        }
    var cardBackground = ContextCompat.getColor(context, R.color.white)
        set(backgroundColor) {
            field = backgroundColor
            binding.cardContainer.setCardBackgroundColor(backgroundColor)
        }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.FolderCard, defStyleAttr, 0).apply {
            isPinned = getBoolean(R.styleable.FolderCard_pinned, false)
            folderName = getString(R.styleable.FolderCard_folderName) ?: ""
            totalNotes = getInt(R.styleable.FolderCard_totalNotes, 0)
            cardBackground = getColor(R.styleable.FolderCard_cardBackground, cardBackground)
        }

        binding.ibPin.setOnClickListener {
            isPinned = !isPinned
            onPinClickListener(isPinned)
        }
    }


}