package com.example.notmynote.ui.deleted

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notmynote.R
import com.example.notmynote.database.NotesDatabase
import com.example.notmynote.database.models.Notes
import com.example.notmynote.databinding.FragmentDeletedNotesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DeletedFragment : Fragment() {

    private lateinit var deletedViewModel: DeletedViewModel
    private lateinit var binding: FragmentDeletedNotesBinding
    private lateinit var adapter: DeletedNotesAdapter
    private lateinit var db: NotesDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_deleted_notes, container, false)
        deletedViewModel =
            ViewModelProvider(this, DeletedViewModelFactory(requireActivity().application)).get(
                DeletedViewModel::class.java
            )

        adapter = DeletedNotesAdapter()
        db = NotesDatabase.getInstance(requireContext())

        binding.deletedRecycleView.layoutManager = LinearLayoutManager(this.activity)
        binding.deletedRecycleView.adapter = adapter

        deletedViewModel.getAllNotes().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        swipeToDoMe()

        return binding.root
    }

    private fun swipeToDoMe() {
        val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!
        val noteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_notes)!!

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val note = adapter.getNote(viewHolder.adapterPosition)
                    deletedViewModel.delete(note)
                    if (direction == ItemTouchHelper.LEFT) {
                        CoroutineScope(Dispatchers.IO + Job()).launch {
                            db.notesDao().insert(Notes(title = note.title, content = note.content))
                        }
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

                        val iconMarginVertical = (itemView.height - noteIcon.intrinsicHeight) / 2

                        noteIcon.setBounds(
                            itemView.right - iconMarginVertical - noteIcon.intrinsicWidth,
                            itemView.top + iconMarginVertical,
                            itemView.right - iconMarginVertical,
                            itemView.bottom - iconMarginVertical
                        )

                        noteIcon.level = 0
                        noteIcon.draw(c)
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
        itemTouchHelper.attachToRecyclerView(binding.deletedRecycleView)
    }

}
