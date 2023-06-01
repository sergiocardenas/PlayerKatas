package com.globant.playerkatas.domain.di

import com.globant.playerkatas.data.datasource.LocalDataSource
import com.globant.playerkatas.data.datasource.RemoteDataSource
import com.globant.playerkatas.domain.usecase.DeezerUseCase
import com.globant.playerkatas.domain.usecase.MusicUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    fun provideMusicUseCase(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ) : MusicUseCase =
        DeezerUseCase(remoteDataSource, localDataSource)

}