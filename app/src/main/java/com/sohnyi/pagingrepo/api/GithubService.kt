package com.sohnyi.pagingrepo.api

import com.sohnyi.pagingrepo.model.Repo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    /**
     * 获取指定用户的仓库列表
     * @param userName 用户名
     * @param page 页码
     * @param perPage 每页数量
     */
    @GET("users/{userName}/repos")
    suspend fun getRepos(
        @Path("userName") userName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<Repo>
}