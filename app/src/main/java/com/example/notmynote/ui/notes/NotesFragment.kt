package com.example.notmynote.ui.notes

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notmynote.R
import com.example.notmynote.database.NotesDatabase
import com.example.notmynote.database.models.ArchivedNotes
import com.example.notmynote.database.models.DeletedNotes
import com.example.notmynote.database.models.Notes
import com.example.notmynote.databinding.FragmentMyNotesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NotesFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var binding: FragmentMyNotesBinding
    private lateinit var adapter: NotesAdapter
    private lateinit var db: NotesDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_notes, container, false)
        notesViewModel =
            ViewModelProvider(this, NotesViewModelFactory(requireActivity().application)).get(
                NotesViewModel::class.java
            )
        adapter = NotesAdapter()
        db = NotesDatabase.getInstance(requireActivity().application)

        binding.floatingActionButton.setOnClickListener {
            it.findNavController()
                .navigate(NotesFragmentDirections.actionNavNotesToNewEntry("", ""))
        }

        binding.notesRecycleView.layoutManager = LinearLayoutManager(this.activity)

        binding.notesRecycleView.adapter = adapter
        notesViewModel.getAllNotes().observe(viewLifecycleOwner, Observer<List<Notes>> {
            adapter.submitList(it)
        })

        swipeToDoMe()
        return binding.root
    }

    private fun swipeToDoMe() {

        val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!
        val archieveIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_archive)!!

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false // do nothing on move
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val note = adapter.getNote(viewHolder.adapterPosition)
                    notesViewModel.delete(note)

                    if (direction == ItemTouchHelper.RIGHT) {
                        CoroutineScope(Dispatchers.IO + Job()).launch {
                            db.deletedNotesDao()
                                .insert(DeletedNotes(title = note.title, content = note.content))
                        }
                        Toast.makeText(requireContext(), "deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        CoroutineScope(Dispatchers.IO + Job()).launch {
                            db.archivedNotesDao()
                                .insert(ArchivedNotes(title = note.title, content = note.content))
                        }
                        Toast.makeText(requireContext(), "achieved", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    val itemView = viewHolder.itemView

                    if (dX > 0) {
                        val iconMarginVertical = (itemView.height - deleteIcon.intrinsicHeight) / 2

                        deleteIcon.setBounds(
                            itemView.left + iconMarginVertical,
                            itemView.top + iconMarginVertical,
                            itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth,
                            itemView.bottom - iconMarginVertical
                        )

                        deleteIcon.draw(c)
                    } else {

                        val iconMarginVertical =
                            (itemView.height - archieveIcon.intrinsicHeight) / 2

                        archieveIcon.setBounds(
                            itemView.right - iconMarginVertical - archieveIcon.intrinsicWidth,
                            itemView.top + iconMarginVertical,
                            itemView.right - iconMarginVertical,
                            itemView.bottom - iconMarginVertical
                        )

                        archieveIcon.level = 0
                        archieveIcon.draw(c)
                    }

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.notesRecycleView)
    }

}
