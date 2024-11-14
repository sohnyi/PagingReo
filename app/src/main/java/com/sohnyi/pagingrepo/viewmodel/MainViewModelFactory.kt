package com.sohnyi.pagingrepo.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.sohnyi.pagingrepo.data.RepoRepository

class MainViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: RepoRepository,
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
       if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
           @Suppress("UNCHECKED_CAST")
           return MainViewModel(repository) as T
       }
        throw IllegalStateException("Unknown ViewModel clas")
    }


}