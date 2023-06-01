package com.globant.playerkatas.domain.usecase

import com.globant.playerkatas.domain.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface MusicUseCase {
    suspend fun getMusicList(): Flow<List<MusicModel>>
    suspend fun saveFavMusic(song: MusicModel)
    suspend fun removeFavMusic(song: MusicModel)
    suspend fun getAllFav(): Flow<List<MusicModel>>
}