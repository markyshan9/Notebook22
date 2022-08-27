package com.example.notebook22

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook22.data.Note
import com.example.notebook22.databinding.ItemListBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NoteAdapter(private val clickListener: (Note) -> Unit) :
    ListAdapter<Note, NoteAdapter.NoteHolder>(
        NoteHolder.DiffCallback
    ) {

    class NoteHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) = with(binding) {
            tvTitle.text = note.title
            tvDescription.text = note.description
//            tvTime.text = Date(note.time*1000).toString()
        }

        companion object DiffCallback : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }


        }



    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return NoteHolder(ItemListBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener(note)
        }
        holder.bind(note)
    }
}