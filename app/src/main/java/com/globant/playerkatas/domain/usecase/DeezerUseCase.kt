package com.globant.playerkatas.domain.usecase

import com.globant.playerkatas.data.datasource.LocalDataSource
import com.globant.playerkatas.data.datasource.RemoteDataSource
import com.globant.playerkatas.domain.model.MusicModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeezerUseCase @Inject constructor(
    private val remote : RemoteDataSource,
    private val local: LocalDataSource
): MusicUseCase{
    override suspend fun getMusicList(): Flow<List<MusicModel>> {
        return remote.getReminderList()
    }

    override suspend fun saveFavMusic(song: MusicModel) {
        local.saveFavMusic(song)
    }

    override suspend fun removeFavMusic(song: MusicModel) {
        local.removeFavMusic(song)
    }

    override suspend fun getAllFav(): Flow<List<MusicModel>> {
        return local.getAllFav()
    }


}