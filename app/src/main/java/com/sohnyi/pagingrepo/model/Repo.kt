package com.sohnyi.pagingrepo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "repos")
data class Repo(
    @PrimaryKey
    val id: String,

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
    val htmlUrl: String?,
)