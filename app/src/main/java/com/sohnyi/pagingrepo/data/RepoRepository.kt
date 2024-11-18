package com.sohnyi.pagingrepo.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sohnyi.pagingrepo.api.GithubService
import com.sohnyi.pagingrepo.database.RepoDatabase
import com.sohnyi.pagingrepo.model.Repo
import kotlinx.coroutines.flow.Flow

const val FIRST_PAGE_INDEX = 1
private const val PAGE_SIZE = 30

class RepoRepository(
    private val service: GithubService,
    private val database: RepoDatabase
) {

   /* @Deprecated("Replace by getReposStream2")
    fun getReposStream(userName: String): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            initialKey = 3,
            pagingSourceFactory = { RepoPagingSource(userName, service) }
        ).flow
    }*/

    @OptIn(ExperimentalPagingApi::class)
    fun getReposStream2(userName: String): Flow<PagingData<Repo>> {
        val dbQuery = "%${userName.replace(' ', '%')}%"
        val pagingSourceFactory = { database.repoDao().reposByUsername(dbQuery) }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = 8 * PAGE_SIZE
            ),
            remoteMediator = RepoRemoteMediator(userName, service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}
