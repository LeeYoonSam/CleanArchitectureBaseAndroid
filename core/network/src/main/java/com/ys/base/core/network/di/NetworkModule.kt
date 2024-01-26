package com.ys.base.core.network.di

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ys.base.core.network.retrofit.DefaultApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .build()

    /**
     * 앱에서 SVG를 표시하기 때문에 Coil에는 이 형식을 지원하는 이미지 로더가 필요합니다.
     * Coil이 초기화되는 동안 `applicationContext.newImageLoader()`를 호출하여 이미지로더를 가져옵니다.
     *
     * @see <a href="https://github.com/coil-kt/coil/blob/main/coil-singleton/src/main/java/coil/Coil.kt">Coil</a>
     */
    @Provides
    @Singleton
    fun imageLoader(
        okHttpCallFactory: Call.Factory,
        @ApplicationContext application: Context,
    ): ImageLoader = ImageLoader.Builder(application)
        .callFactory(okHttpCallFactory)
        .components {
            add(SvgDecoder.Factory())
        }
        // Assume most content images are versioned urls
        // but some problematic images are fetching each time
        .respectCacheHeaders(false)
        .apply {
            if (BuildConfig.DEBUG) {
                logger(DebugLogger())
            }
        }
        .build()


    private const val BASE_URL: String = "https://jsonplaceholder.typicode.com"

    @Provides
    @Singleton
    fun provideDefaultApi(
        networkJson: Json,
        okhttpCallFactory: Call.Factory,
    ): DefaultApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .callFactory(okhttpCallFactory)
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(DefaultApi::class.java)
    }
}