package com.sohnyi.pagingrepo.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sohnyi.pagingrepo.api.GithubService
import com.sohnyi.pagingrepo.model.Repo
import retrofit2.HttpException

private const val TAG = "RepoPagingSource"

@Deprecated("Replace by RepoRemoteMediator")
class RepoPagingSource(
    private val userName: String,
    private val service: GithubService,
) : PagingSource<Int, Repo>() {

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        val position = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(position) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {

        Log.i(TAG, "load: params.key = ${params.key}")
        Log.i(TAG, "load: thread name: ${Thread.currentThread().name}")

        val page = params.key ?: FIRST_PAGE_INDEX

        try {
            val repos = service.getRepos(userName, page, params.loadSize)
            val nextPage = if (repos.isNotEmpty()) {
                page + 1
            } else {
                null
            }
            val prevPage = if (page == FIRST_PAGE_INDEX) {
                null
            } else {
                page - 1
            }
            return LoadResult.Page(
                data = repos,
                nextKey = nextPage,
                prevKey = prevPage
            )
        } catch (e: HttpException) {
            Log.e(TAG, "load: ERROR!", e)
            return if (e.code() == 404) {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            } else {
                LoadResult.Error(e)
            }
        } catch (e: Exception) {
            Log.e(TAG, "load: ERROR!", e)
            return LoadResult.Error(e)
        }
    }
}