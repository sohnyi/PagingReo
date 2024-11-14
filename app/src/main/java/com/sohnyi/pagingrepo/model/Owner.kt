package com.sohnyi.pagingrepo.model

import com.google.gson.annotations.SerializedName

data class Owner(
    val id: Long?,

    @field:SerializedName("avatar_url")
    val avatarUrl: String?,

    val url: String?,

    @field:SerializedName("html_url")
    val htmlUrl: String?,

    val type: String?,
)
