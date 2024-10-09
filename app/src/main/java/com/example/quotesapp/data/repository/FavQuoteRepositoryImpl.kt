package com.example.quotesapp.data.repository

import android.util.Log
import com.example.quotesapp.data.local.QuoteDatabase
import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.domain.repository.FavQuoteRepository
import com.example.quotesapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavQuoteRepositoryImpl (private val db: QuoteDatabase):FavQuoteRepository {

    override fun getAllLikedQuotes(): Flow<Resource<List<Quote>>> {
        return flow {

            emit(Resource.Loading())

            try {

                var list:List<Quote>?

                withContext(Dispatchers.IO){
                    list = db.getQuoteDao().getAllLikedQuotes()
                }

                Log.d("TAG","FavImpl "+ list?.size.toString())

                emit(Resource.Success(list))

            }catch (e:Exception){
                emit(Resource.Error(message = e.message.toString()))
            }
        }
    }

    override suspend fun saveLikedQuote(quote: Quote) {
        db.getQuoteDao().insertLikedQuote(quote)
    }
}