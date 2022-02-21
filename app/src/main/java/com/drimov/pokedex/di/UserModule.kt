package com.drimov.pokedex.di

import android.content.Context
import com.drimov.pokedex.data.repository.UserPrefsRepositoryImpl
import com.drimov.pokedex.domain.repository.UserPrefsRepository
import com.drimov.pokedex.domain.use_case.GetStringPrefs
import com.drimov.pokedex.domain.use_case.PutStringPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Singleton
    @Provides
    fun provideUserPrefsRepository(@ApplicationContext appContext: Context): UserPrefsRepository {
        return UserPrefsRepositoryImpl(appContext)
    }

    @Singleton
    @Provides
    fun provideGetStringPrefs(repository: UserPrefsRepository): GetStringPrefs {
        return GetStringPrefs(repository)
    }
    @Singleton
    @Provides
    fun providePutStringPrefs(repository: UserPrefsRepository): PutStringPrefs {
        return PutStringPrefs(repository)
    }
}