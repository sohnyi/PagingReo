package com.sohnyi.pagingrepo.model

import com.google.gson.annotations.SerializedName


data class Repo(
    val id: Long?,

    val name: String?,

    val fullName: String?,

    val description: String?,

    val url: String?,

    @field:SerializedName("stargazers_count")
    val stargazersCount: Int?,

    val forksCount: Int?,

    val language: String?,

    val owner: Owner?,

    val stars: Int?,

    @field:SerializedName("html_url")
    val htmlUrl: String?
)