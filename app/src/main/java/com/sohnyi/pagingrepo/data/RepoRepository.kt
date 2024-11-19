package com.sohnyi.pagingrepo.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sohnyi.pagingrepo.api.GithubService
import com.sohnyi.pagingrepo.model.Repo
import com.sohnyi.pagingrepo.network.NetworkRepository
import kotlinx.coroutines.flow.Flow

const val FIRST_PAGE_INDEX = 1
private const val PAGE_SIZE = 20

class RepoRepository {

    fun getReposStream(userName: String): Flow<PagingData<Repo>> {
        val service = NetworkRepository.obtainRetrofitService(GithubService::class.java)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = 8 * PAGE_SIZE
            ),
            pagingSourceFactory = { RepoPagingSource(userName, service) }
        ).flow
    }
}
