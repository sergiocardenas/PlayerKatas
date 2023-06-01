package com.globant.playerkatas.data.service

import com.globant.playerkatas.data.RemoteConstants.CHART_ENDPOINT
import com.globant.playerkatas.data.responde.ChartResponse
import retrofit2.Response
import retrofit2.http.GET

interface DeezerService {
    @GET(CHART_ENDPOINT)
    suspend fun getChart(): Response<ChartResponse>
}