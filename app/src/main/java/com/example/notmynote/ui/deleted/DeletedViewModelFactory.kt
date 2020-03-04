package com.example.notmynote.ui.deleted

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DeletedViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeletedViewModel::class.java)) {
            return DeletedViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}