package com.example.notmynote.ui.newNote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddNewNoteViewModel() : ViewModel() {


    private var _clearAll = MutableLiveData<Boolean>()
    val clearAll: LiveData<Boolean>
        get() = _clearAll

    init {
        _clearAll.value = false
    }

    fun onClear() {
        _clearAll.value = true
    }

}