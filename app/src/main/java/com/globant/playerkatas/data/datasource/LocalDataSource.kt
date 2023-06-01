package com.globant.playerkatas.data.datasource

import com.globant.playerkatas.data.entity.MusicEntity
import com.globant.playerkatas.domain.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveFavMusic(song: MusicModel)
    suspend fun removeFavMusic(song: MusicModel)
    suspend fun getAllFav(): Flow<List<MusicModel>>
}