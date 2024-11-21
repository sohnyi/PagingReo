package com.sohnyi.pagingrepo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sohnyi.pagingrepo.data.RepoRepository
import com.sohnyi.pagingrepo.model.Repo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"
private const val DEFAULT_USERNAME = "google"

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MainViewModel(
    private val repository: RepoRepository,
) : ViewModel() {

    // 暴露给 UI 层的 Flow
    val reposFlow: Flow<PagingData<Repo>>

     val action: (UiAction) -> Unit

    init {
        val actionStateFlow = MutableSharedFlow<UiAction>()

        val getRepos = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart {
                // 开始时加载默认数据
                emit(UiAction.Search(userName = DEFAULT_USERNAME))
            }

        reposFlow = getRepos
            .flatMapLatest {
                getRepos(userName = it.userName)
            }
            // 缓存 pagingData
            .cachedIn(viewModelScope)

        action = { action ->
            viewModelScope.launch {
                actionStateFlow.emit(action)
            }
        }
    }

    /**
     * 获取仓库列表数据流
     */
    private fun getRepos(userName: String): Flow<PagingData<Repo>> {
        return repository.getReposStream(userName)
    }

}

sealed class UiAction {
    data class Search(val userName: String) : UiAction()
}