package com.globant.playerkatas.data.datasource

import com.globant.playerkatas.data.mapper.toModel
import com.globant.playerkatas.data.responde.MusicResponse
import com.globant.playerkatas.data.service.DeezerService
import com.globant.playerkatas.domain.model.MusicModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeezerRemoteDataSource @Inject constructor(
    private val service: DeezerService
): RemoteDataSource {
    override suspend fun getReminderList(): Flow<List<MusicModel>> = flow {
        val list = withContext(Dispatchers.IO) {
            var chartList: List<MusicResponse> = listOf()
            service.getChart().body()?.let {
                chartList = it.tracks.data
            }
            chartList.map { it.toModel() }
        }
        emit(list)
    }
}