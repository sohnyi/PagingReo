package com.sohnyi.pagingrepo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.sohnyi.pagingrepo.data.RepoRepository
import com.sohnyi.pagingrepo.model.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"
private const val DEFAULT_USERNAME = "google"

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MainViewModel(
    private val repository: RepoRepository,
) : ViewModel() {

    val reposFlow: Flow<PagingData<UiModel>>

     val action: (UiAction) -> Unit

    init {
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val getRepos = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(userName = DEFAULT_USERNAME)) }

        var firstItem: UiModel.RepoItem? = null
        reposFlow = getRepos
            .filterIsInstance<UiAction.Search>()
            .flatMapLatest {
                getRepos(userName = it.userName)
            }
            .map { pagingData ->
                pagingData.map { repo ->
                    UiModel.RepoItem(repo).also {
                        if (firstItem == null) {
                            firstItem = it
                        }
                    }
                }
            }
            .map {
                it.insertSeparators { before, after ->
                    val beforeFirstLetter = before?.repo?.name?.firstOrNull()?.uppercase()
                    val afterFirstLetter = after?.repo?.name?.firstOrNull()?.uppercase()
                    if (beforeFirstLetter == null) {
                        if (firstItem == null) {
                            firstItem = before
                        }
                        return@insertSeparators null
                    }
                    if (afterFirstLetter == null) {
                        return@insertSeparators null
                    }
                    if (beforeFirstLetter != afterFirstLetter) {
                        UiModel.LetterItem(afterFirstLetter)
                    } else {
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)

        action = { action ->
            viewModelScope.launch {
                actionStateFlow.emit(action)
            }
        }
    }

    private fun getRepos(userName: String): Flow<PagingData<Repo>> {
        return repository.getReposStream2(userName)
    }

}

sealed class UiAction {
    data class Search(val userName: String) : UiAction()
}

sealed class UiModel {
    // 仓库
    data class RepoItem(val repo: Repo) : UiModel()
    // 首字母-分割
    data class LetterItem(val letter: String) : UiModel()
}