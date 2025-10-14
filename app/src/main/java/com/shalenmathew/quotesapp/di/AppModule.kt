package com.shalenmathew.quotesapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shalenmathew.quotesapp.BuildConfig
import com.shalenmathew.quotesapp.data.local.AnimationPreferencesImpl
import com.shalenmathew.quotesapp.data.local.DefaultQuoteStylePreferencesImpl
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.data.remote.QuoteApi
import com.shalenmathew.quotesapp.data.repository.FavQuoteRepositoryImpl
import com.shalenmathew.quotesapp.data.repository.QuoteRepositoryImplementation
import com.shalenmathew.quotesapp.domain.repository.AnimationPreferences
import com.shalenmathew.quotesapp.domain.repository.DefaultQuoteStylePreferences
import com.shalenmathew.quotesapp.domain.repository.FavQuoteRepository
import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavLikedQuote
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavQuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.GetFavQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetLikedQuotes
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.LikedQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
fun providesQuoteUsecase(getQuote: GetQuote, likedQuote: LikedQuote, getLikedQuotes: GetLikedQuotes): QuoteUseCase {
return QuoteUseCase(getQuote = getQuote, likedQuote = likedQuote, getLikedQuotes = getLikedQuotes)
}

    @Singleton
    @Provides
    fun providesQuoteDatabase(application: Application):QuoteDatabase{
        return Room.databaseBuilder(application,QuoteDatabase::class.java,"quote_db")
            .addMigrations(DB_MIGRATION)
//            .fallbackToDestructiveMigration(true)
            .build()
    }

    val DB_MIGRATION = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE Quote ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
        }
    }

@Singleton
@Provides
fun providesQuoteRepository(api:QuoteApi,db:QuoteDatabase):QuoteRepository{
        return QuoteRepositoryImplementation(api,db)
}

    @Singleton
    @Provides
    fun providesFavQuoteRepository(db:QuoteDatabase): FavQuoteRepository {
        return FavQuoteRepositoryImpl(db)
    }


    @Singleton
    @Provides
    fun providesFavQuoteUseCase(getFavQuote: GetFavQuote,favLikedQuote: FavLikedQuote):FavQuoteUseCase{
        return FavQuoteUseCase(getFavQuote,favLikedQuote)
    }

    @Provides
    @Singleton
    fun providesOkhttpClient(@ApplicationContext context: Context):OkHttpClient
    {

        return OkHttpClient
            .Builder()
            .cache(Cache(context.cacheDir,(5 * 1024 * 1024).toLong()))
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level =  if (BuildConfig.DEBUG) Level.BODY else Level.NONE
                }
            )
            .connectTimeout(10,TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .readTimeout(10,TimeUnit.SECONDS)
            .build()


    }

    @Singleton
    @Provides
    fun providesQuotesApi(okHttpClient: OkHttpClient):QuoteApi{
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .build()
            .create(QuoteApi::class.java)
    }

    @Provides
    @Singleton
    fun providesSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            Constants.SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun providesDefaultQuoteStylePreferences(sharedPreferences: SharedPreferences): DefaultQuoteStylePreferences {
        return DefaultQuoteStylePreferencesImpl(sharedPreferences)
    }

    @Singleton
    @Provides
    fun providesAnimationPreferences(): AnimationPreferences {
        return AnimationPreferencesImpl()
    }
}