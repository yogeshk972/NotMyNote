package com.example.notmynote.ui.newNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notmynote.R
import com.example.notmynote.database.NotesDatabase
import com.example.notmynote.database.models.ArchivedNotes
import com.example.notmynote.database.models.Notes
import com.example.notmynote.databinding.FragmentNewEntryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditNoteFragment : Fragment() {
    private lateinit var viewModel: AddNewNoteViewModel
    private lateinit var binding: FragmentNewEntryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val dbNotes = NotesDatabase.getInstance(application).notesDao()
        val dbArchieve = NotesDatabase.getInstance(application).archivedNotesDao()

        viewModel = ViewModelProvider(this).get(AddNewNoteViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_entry, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.newEntryViewModel = viewModel


        val args = AddEditNoteFragmentArgs.fromBundle(arguments!!)
        binding.newEntryTitle.setText(args.title)
        binding.newEntryContent.setText(args.content)


        viewModel.clearAll.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.newEntryContent.setText("")
                binding.newEntryTitle.setText("")
            }
        })

        binding.submit.setOnClickListener {
            val title = binding.newEntryTitle.text.toString()
            val content = binding.newEntryContent.text.toString()
            val note = Notes(args.id, title = title, content = content)

            if (args.update) {
                if (args.fromArcheive) {
                    val archivedNote = ArchivedNotes(note.noteId, note.title, note.content)
                    CoroutineScope(Dispatchers.IO).launch {
                        if (title.isEmpty() && content.isEmpty()) {
                            dbArchieve.delete(archivedNote)
                        } else dbArchieve.update(archivedNote)
                    }
                } else {

                    CoroutineScope(Dispatchers.IO).launch {
                        if (title.isEmpty() && content.isEmpty()) {
                            dbNotes.delete(note)
                        } else {
                            dbNotes.update(note)
                        }
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    dbNotes.insert(note)
                }
            }
            requireActivity().onBackPressed()
        }

        return binding.root
    }


}