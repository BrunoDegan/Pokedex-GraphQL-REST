package com.brunodegan.pokedex.base

import android.app.Application
import android.graphics.Bitmap
import coil.Coil
import coil.ImageLoader
import com.brunodegan.pokedex.base.network.NetworkModule
import com.brunodegan.pokedex.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class PokedexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .crossfade(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build()
        )
        startKoin {
            androidLogger()
            androidContext(this@PokedexApplication)
            modules(
                NetworkModule().module,
                AppModule().module
            )
        }
    }
}