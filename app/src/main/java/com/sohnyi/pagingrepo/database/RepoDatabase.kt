package com.sohnyi.pagingrepo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sohnyi.pagingrepo.database.converters.RepoConverters
import com.sohnyi.pagingrepo.database.dao.RemoteKeysDao
import com.sohnyi.pagingrepo.database.dao.RepoDao
import com.sohnyi.pagingrepo.database.entities.RemoteKeys
import com.sohnyi.pagingrepo.model.Repo


private const val DATABASE_NAME = "github.db"

@Database(
    entities = [Repo::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RepoConverters::class)
abstract class RepoDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RepoDatabase::class.java,
                DATABASE_NAME
            )
                .build()
    }
}