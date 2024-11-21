package com.sohnyi.pagingrepo.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sohnyi.pagingrepo.api.GithubService
import com.sohnyi.pagingrepo.model.Repo
import kotlinx.coroutines.flow.Flow

const val FIRST_PAGE_INDEX = 1
private const val PAGE_SIZE = 20

/**
 * Github 仓库数据仓库
 */
class RepoRepository(private val service: GithubService) {
    /**
     * 获取仓库列表数据流
     * @param userName 用户名
     * @return 仓库列表数据流
     */
    fun getReposStream(userName: String): Flow<PagingData<Repo>> {
        return Pager(
            // 分页配置
            config = PagingConfig(
                // 每页加载的项目数量
                pageSize = PAGE_SIZE,
                // 是否启用占位
                enablePlaceholders = true
            ),
            // 加载分页数据的数据源
            pagingSourceFactory = { RepoPagingSource(userName, service) }
        ).flow
    }
}
