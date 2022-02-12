package com.drimov.pokedex.di

import android.app.Application
import android.media.AsyncPlayer
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.imageLoader
import com.drimov.pokedex.data.PokedexDatabase
import com.drimov.pokedex.data.remote.PokedexApi
import com.drimov.pokedex.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    fun providePokedexDatabase(app: Application): PokedexDatabase {
//        return Room.databaseBuilder(
//            app, PokedexDatabase::class.java, PokedexDatabase.DB_NAME
//        ).build()
//    }

    @Provides
    @Singleton
    fun providePokedexApi(): PokedexApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient
                    .Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        this.level = HttpLoggingInterceptor.Level.BODY
                    }).build()
            ).build()
            .create(PokedexApi::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideImageLoader(app: Application ): ImageLoader {
//        return ImageLoader.Builder(app.baseContext)
//            .componentRegistry {
//                add(SvgDecoder(app.baseContext))
//            }
//            .build()
//    }

}