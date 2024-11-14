package com.sohnyi.pagingrepo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.sohnyi.pagingrepo.data.RepoRepository
import com.sohnyi.pagingrepo.model.Repo
import kotlinx.coroutines.flow.Flow

class MainViewModel(
    private val repository: RepoRepository,
) : ViewModel() {


    fun getRepos(userName: String): Flow<PagingData<Repo>> {
      return  repository.getReposStream(userName)
    }
}