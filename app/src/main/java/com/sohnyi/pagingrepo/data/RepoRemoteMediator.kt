package com.sohnyi.pagingrepo.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.sohnyi.pagingrepo.api.GithubService
import com.sohnyi.pagingrepo.database.RepoDatabase
import com.sohnyi.pagingrepo.database.entities.RemoteKeys
import com.sohnyi.pagingrepo.model.Repo


private const val TAG = "RepoRemoteMediator"

@OptIn(ExperimentalPagingApi::class)
class RepoRemoteMediator(
    private val userName: String,
    private val service: GithubService,
    private val database: RepoDatabase,
) : RemoteMediator<Int, Repo>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyCloseToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: FIRST_PAGE_INDEX
            }

            LoadType.PREPEND -> {
               val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItems(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                nextKey
            }
        }

        Log.i(TAG, "load: LoadType = $loadType, page = $page, pageSize = ${state.config.pageSize}")
        try {
            val repos = service.getRepos(userName, page, state.config.pageSize)
            val endOfPaginationReached = repos.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.repoDao().clearRepos()
                    database.remoteKeysDao().clearRemoteKeys()
                }
                val reposSize = database.repoDao().getAll().size
                Log.i(TAG, "load: reposSize: $reposSize")
                val prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1
                val nextPage = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextPage)
                }
                database.repoDao().insertAll(repos)
                database.remoteKeysDao().insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Log.e(TAG, "load: ERROR!", e)
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItems(state: PagingState<Int, Repo>): RemoteKeys? {
        Log.i(TAG, "getRemoteKeyForLastItems: state.page.size = ${state.pages.size}")
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.id
            ?.let { repoId ->
                database.remoteKeysDao().getRemoteKeys(repoId)
            }.also {
                Log.i(TAG, "getRemoteKeyForLastItems = $it")
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Repo>): RemoteKeys? {
        Log.i(TAG, "getRemoteKeyForLastItems: state.page.size = ${state.pages.size}")
        return state.pages.firstOrNull { it.data.isEmpty() }?.data?.firstOrNull()?.id
            ?.let { repoId ->
                database.remoteKeysDao().getRemoteKeys(repoId)
            }.also {
                Log.i(TAG, "getRemoteKeyForFirstItem: $it")
            }
    }

    private suspend fun getRemoteKeyCloseToCurrentPosition(state: PagingState<Int, Repo>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeysDao().getRemoteKeys(repoId)
            }
        }.also {
            Log.i(TAG, "getRemoteKeyCloseToCurrentPosition: $it")
        }
    }

}