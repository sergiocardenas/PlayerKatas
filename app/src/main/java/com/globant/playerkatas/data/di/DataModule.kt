package com.globant.playerkatas.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.globant.playerkatas.data.RemoteConstants.BASE_URL
import com.globant.playerkatas.data.datasource.DeezerRemoteDataSource
import com.globant.playerkatas.data.datasource.FavLocalDataSource
import com.globant.playerkatas.data.datasource.LocalDataSource
import com.globant.playerkatas.data.datasource.RemoteDataSource
import com.globant.playerkatas.data.service.DeezerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()


    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit): DeezerService =
        retrofit.create(DeezerService::class.java)

    @Singleton
    @Provides
    fun provideRemoteDataSource(service: DeezerService): RemoteDataSource =
        DeezerRemoteDataSource(service)

    @Singleton
    @Provides
    fun provideLocalDataSource(@ApplicationContext appContext: Context): LocalDataSource =
        FavLocalDataSource(context = appContext)
}