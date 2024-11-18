package com.sohnyi.pagingrepo.database.converters

import android.util.Log
import androidx.room.TypeConverter
import com.sohnyi.pagingrepo.model.Owner

private const val TAG = "RepoConverters"
class RepoConverters {

    @TypeConverter
    fun fromOwner(owner: Owner): String {
        return owner.toString()
    }

    @TypeConverter
    fun toOwner(id: String): Owner? {
        return parseOwner(id)
    }

    private fun parseOwner(input: String): Owner? {
        try {

            val regex =
                Regex("""Owner\(id=(\d+), login=(.+?), avatarUrl=(.+?), url=(.+?), htmlUrl=(.+?), type=(.+?)\)""")
            val matchResult = regex.matchEntire(input) ?: return null

            val (id, login, avatarUrl, url, htmlUrl, type) = matchResult.destructured
            return Owner(id.toLong(), login, avatarUrl, url, htmlUrl, type)
        } catch (e: Exception) {
            Log.e(TAG, "parseOwner: ERROR!", e)
            return null
        }
    }
}