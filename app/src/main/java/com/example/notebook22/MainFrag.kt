package com.example.notebook22

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notebook22.databinding.FragmentMainBinding


class MainFrag : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: NoteViewModel by activityViewModels() {
        NoteViewModelFactory(
            (activity?.application as BaseApplication).database.noteDao()
        )
    }
    private lateinit var adapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        initRcView()
        fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFrag_to_detailNoteFragment)
        }
    }

    private fun initRcView() = with(binding) {
        adapter = NoteAdapter{
            val action = MainFragDirections.actionMainFragToDetailNoteFragment(it.id)
            findNavController().navigate(action)

        }
        rcVNotes.adapter = adapter
        viewModel.listOfNotes.observe(viewLifecycleOwner){
           adapter.submitList(it)


        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFrag()
    }
}