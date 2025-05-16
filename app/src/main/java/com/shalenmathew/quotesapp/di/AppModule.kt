package com.shalenmathew.quotesapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.shalenmathew.quotesapp.data.local.DefaultQuoteStylePreferencesImpl
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.data.remote.QuoteApi
import com.shalenmathew.quotesapp.data.repository.FavQuoteRepositoryImpl
import com.shalenmathew.quotesapp.data.repository.QuoteRepositoryImplementation
import com.shalenmathew.quotesapp.domain.repository.DefaultQuoteStylePreferences
import com.shalenmathew.quotesapp.domain.repository.FavQuoteRepository
import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavLikedQuote
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavQuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.GetFavQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.LikedQuote
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

  @Singleton
@Provides
fun providesQuoteUsecase(getQuote: GetQuote, likedQuote: LikedQuote): QuoteUseCase {
return QuoteUseCase(getQuote = getQuote, likedQuote =likedQuote )
}

    @Singleton
    @Provides
    fun providesQuoteDatabase(application: Application):QuoteDatabase{
        return Room.databaseBuilder(application,QuoteDatabase::class.java,"quote_db")
            .fallbackToDestructiveMigration()
            .build()
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

    @Singleton
    @Provides
    fun providesQuotesApi():QuoteApi{
        return  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
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
}