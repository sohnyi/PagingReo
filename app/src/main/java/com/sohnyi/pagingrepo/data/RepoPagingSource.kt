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
        // 获取用户最近访问的位置
        val position = state.anchorPosition ?: return null
        // 获取用户最近访问的分页
        val page = state.closestPageToPosition(position) ?: return null
        // 返回当前页码
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {

        Log.i(TAG, "load: params.key = ${params.key}， params.loadSize = ${params.loadSize}")

        // 获取当前需要加载的页码. null 则使用默认页码
        // 如果我们没有配置的话默认首次加载 params.key 为 null
        val page = params.key ?: FIRST_PAGE_INDEX

        try {
            // 网络请求
            val repos = service.getRepos(userName, page, params.loadSize)
            // 下一页
            val nextPage = if (repos.isNotEmpty()) {
                page + 1
            } else {
                // 没有更多数据
                null
            }
            // 上一页
            val prevPage = if (page == FIRST_PAGE_INDEX) {
                // 首页
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
                // 由于这个接口的 username 是作为 path 填入的, 对应用户不存在是会得到一个 404 的结果
                // 故此处单独处理了一个 404 的结果
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            } else {
                // 其他网络错误
                LoadResult.Error(e)
            }
        } catch (e: Exception) {
            // 其他错误
            Log.e(TAG, "load: ERROR!", e)
            return LoadResult.Error(e)
        }
    }
}