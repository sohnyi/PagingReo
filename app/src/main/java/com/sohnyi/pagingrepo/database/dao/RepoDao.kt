package com.sohnyi.pagingrepo.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sohnyi.pagingrepo.model.Repo

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(repo: Repo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Repo>)

    @Query("SELECT * FROM repos WHERE owner LIKE :queryString")
    fun reposByUsername(queryString: String): PagingSource<Int, Repo>

    @Update
    suspend fun update(repo: Repo)

    @Query("DELETE FROM repos")
    suspend fun clearRepos()

    @Query("SELECT * FROM repos")
    suspend fun getAll(): List<Repo>

}