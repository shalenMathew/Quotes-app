package com.example.quotesapp.data.repository

import android.util.Log
import com.example.quotesapp.data.local.QuoteDatabase
import com.example.quotesapp.data.mappers.toQuote
import com.example.quotesapp.data.remote.QuoteApi
import com.example.quotesapp.domain.model.Quote
import com.example.quotesapp.domain.model.QuoteHome
import com.example.quotesapp.domain.repository.QuoteRepository
import com.example.quotesapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

class QuoteRepositoryImplementation(private val api:QuoteApi, private val db:QuoteDatabase):QuoteRepository {


    override fun getQuote(): Flow<Resource<QuoteHome>> {

        return flow {

            var quoteHome:QuoteHome? = null

            emit(Resource.Loading())

            Log.d("TAG",Thread.currentThread().name)

            coroutineScope {

                val quotesListDef = async { api.getQuotesList().map { it.toQuote() } }
                val qotDef =  async { api.getQuoteOfTheDay().map { it.toQuote() } }

                val currList = db.getQuoteDao().getAllQuotes()

                currList.onEach {
                    if(!it.liked){
                        db.getQuoteDao().deleteQuote(it)
                    }
                }

                val quotesList = quotesListDef.await()
                val qot=qotDef.await()

                quotesList.let { list->
                    db.getQuoteDao().insertQuoteList(quotesList)
                }

                Log.d("TAG","QuoteImpl inside try - Size of all list="
                        + db.getQuoteDao().getAllQuotes().size)

                quoteHome= QuoteHome(
                    quotesList = db.getQuoteDao().getAllQuotes(),
                    quotesOfTheDay = qot
                )

                emit(Resource.Success(quoteHome))
            }

        }.catch { e ->

            val errorMessage = when (e) {
                is IOException-> "No internet connection. Please try again."
                is HttpException -> {
                    when (e.code()) {
                        400 -> "Bad Request"
                        401 -> "Unauthorized"
                        403 -> "Forbidden"
                        429 -> "To many request to the server please check back in some time"
                        else -> {"Unknown error ${e.message()}"}
                    }
                    }
                else -> "Something went wrong. Please try again."
            }


            Log.d("TAG", "Error in getQuote: ${e.message}")

            emit(Resource.Error(errorMessage))

        }.flowOn(Dispatchers.IO)

    }

    override suspend fun saveLikedQuote(quote: Quote) {
        db.getQuoteDao().insertLikedQuote(quote)
    }


}