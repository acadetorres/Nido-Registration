package com.acdetorres.nidoregistration

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.acdetorres.nidoregistration.dao.AppDB
import com.acdetorres.nidoregistration.dao.RoomDao
import com.github.gcacace.signaturepad.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .addInterceptor { chain ->
                chain.proceed(chain.request())
//                chain.proceed(
//                    chain.request().newBuilder().addHeader(
//                        "Authorization", "Basic ${BuildConfig.AUTHORIZATIONKEY}"
//                    )
//                        .build()
//                )
            }

//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
//        okHttpClient.addInterceptor(loggingInterceptor)
//        okHttpClient.apply {
//            if (BuildConfig.DEBUG) {
////                addInterceptor(OkHttpProfilerInterceptor())
//                addInterceptor(loggingInterceptor)
//                addInterceptor(OkHttpProfilerInterceptor())
//                Timber.e("DEbug")
//            } else {
//                addInterceptor(loggingInterceptor)
//            }
//
//
//        }
        return Retrofit.Builder()
            .baseUrl("https://mizmoph.com/")
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

//    @Singleton
//    @Provides
//    fun provideSharedPref (@ApplicationContext context : Context) : AppSharedPref {
//        return AppSharedPref(context)
//    }

//    @Provides
//    fun provideContext (@ApplicationContext context : Context) : Context {
//        return context
//    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(
        @ApplicationContext context: Context
    ) : AppDB {
        return Room.databaseBuilder(context, AppDB::class.java, "AppDB").build()
    }


    @Singleton
    @Provides
    fun provideDao(
         appDB: AppDB): RoomDao {
        return appDB.getRoomDao()
    }





}


