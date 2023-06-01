package com.globant.playerkatas.data.datasource

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.globant.playerkatas.data.entity.MusicEntity
import com.globant.playerkatas.data.mapper.toEntity
import com.globant.playerkatas.data.mapper.toModel
import com.globant.playerkatas.domain.model.MusicModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavLocalDataSource(
    val context: Context
): LocalDataSource {
    private val MUSIC_KEY = "MUSIC_KEY_"
    private val gson: Gson = Gson()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("kataPlayerToken")

    override suspend fun saveFavMusic(song: MusicModel){
        val entity = song.toEntity()
        withContext(Dispatchers.IO) {
            val songPrefKey = stringPreferencesKey(MUSIC_KEY+entity.id)
            context.dataStore.edit {pref ->
                pref[songPrefKey] = gson.toJson(entity)
            }
        }
    }

    override suspend fun removeFavMusic(song: MusicModel){
        val entity = song.toEntity()
        withContext(Dispatchers.IO) {
            val songPrefKey = stringPreferencesKey(MUSIC_KEY+entity.id)
            context.dataStore.edit {pref ->
                pref.remove(songPrefKey)
            }
        }
    }

    override suspend fun getAllFav() = flow {
        val list = mutableListOf<MusicEntity>()
        withContext(Dispatchers.IO) {
        }
        val preferences = context.dataStore.data.first()
        val keys = preferences.asMap().keys.toList()
        for(key in keys){
            val gsonSong: String = preferences[key] as String
            list.add(gson.fromJson(gsonSong, MusicEntity::class.java))
        }
        emit(list.map { it.toModel() })
    }

}