package com.sohnyi.pagingrepo.api

import com.sohnyi.pagingrepo.model.Repo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("users/{userName}/repos")
    suspend fun getRepos(
        @Path("userName") userName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<Repo>
}