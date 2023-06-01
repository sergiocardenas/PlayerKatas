package com.globant.playerkatas.data.datasource

import com.globant.playerkatas.domain.model.MusicModel
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getReminderList(): Flow<List<MusicModel>>
}