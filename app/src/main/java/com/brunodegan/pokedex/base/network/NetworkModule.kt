package com.brunodegan.pokedex.base.network

import com.brunodegan.pokedex.data.api.GraphQLApiService
import com.brunodegan.pokedex.data.api.GraphQLApiService.Companion.GRAPHQL_BASE_URL
import com.brunodegan.pokedex.data.api.RestApiService
import com.brunodegan.pokedex.data.api.RestApiService.Companion.REST_API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private inline fun <reified T : Any> Retrofit.createApi(): T = create(T::class.java)

@Module
@ComponentScan("com.brunodegan.pokedex")
class NetworkModule {

    @Single
    fun getRestApiClient(): RestApiService {
        return Retrofit.Builder()
            .baseUrl(REST_API_BASE_URL)
            .client(provideHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
            .createApi<RestApiService>()
    }

    @Single
    fun getGraphQlApiClient(): GraphQLApiService {
        return Retrofit.Builder()
            .baseUrl(GRAPHQL_BASE_URL)
            .client(provideHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
            .createApi<GraphQLApiService>()
    }

    private fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(provideHttpInterceptor())
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    private fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    private fun provideHttpInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    companion object {
        private const val REQUEST_TIMEOUT = 60L
    }
}
