package com.example.notebook22

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notebook22.data.Note
import com.example.notebook22.databinding.FragmentDetailNoteBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class DetailNoteFragment : Fragment() {

    private val navigationArgs : DetailNoteFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailNoteBinding
    private val viewModel: NoteViewModel by activityViewModels() {
        NoteViewModelFactory(
            (activity?.application as BaseApplication).database.noteDao()
        )
    }

    private lateinit var note: Note
    private var id : Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentDetailNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        id = navigationArgs.id
        if (id!! >0) {
            viewModel.getNoteById(id!!)?.observe(viewLifecycleOwner) {
                note = it
                bindText()
            }
            fabIns.setOnClickListener {
                updateNote()
            }
        } else {
            fabIns.setOnClickListener {
                insertNote()
            }
        }

    }

    private fun bindText() = with(binding) {
        edDesc.setText(note.description)
        edTitle.setText(note.title)
    }

    private fun insertNote() = with(binding) {
        if(isValidEntry()) {
            viewModel.insertNote(
                edTitle.text.toString(),
                edDesc.text.toString(),
                getCurrentTime()
            )
            findNavController().navigate(R.id.action_detailNoteFragment_to_mainFrag)
        }
    }


    private fun updateNote() {
        if(isValidEntry()) {
            viewModel.updateNote(
                id = navigationArgs.id,
                title = binding.edTitle.text.toString(),
                description = binding.edDesc.text.toString(),
                time = getCurrentTime()
            )
            findNavController().navigate(R.id.action_detailNoteFragment_to_mainFrag)
        }
    }

    private fun isValidEntry() = with(binding) {
        viewModel.isValidEntry(
            edTitle.text.toString(),
            edDesc.text.toString()
        )
    }

    private fun getCurrentTime() : Int {
        val currentDate = Date(System.currentTimeMillis())
        val millis = currentDate.time/1000
        return millis.toInt()
    }

    companion object {

        @JvmStatic
        fun newInstance() = DetailNoteFragment()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.delete_menu -> {
                id = 0
                deleteNote(note)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun deleteNote(note: Note) {
        viewModel.deleteNote(note)
        findNavController().navigate(R.id.action_detailNoteFragment_to_mainFrag)
    }


}