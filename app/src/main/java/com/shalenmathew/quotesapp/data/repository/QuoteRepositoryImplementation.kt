package com.shalenmathew.quotesapp.data.repository

import android.util.Log
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.data.mappers.toQuote
import com.shalenmathew.quotesapp.data.remote.QuoteApi
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.model.QuoteHome
import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
import com.shalenmathew.quotesapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

class QuoteRepositoryImplementation(private val api: QuoteApi, private val db: QuoteDatabase) :
    QuoteRepository {


    override fun getQuote(): Flow<Resource<QuoteHome>> {

        return flow {

            var quoteHome: QuoteHome?

            emit(Resource.Loading())

            Log.d("TAG", Thread.currentThread().name)

            coroutineScope {

                /// coroutine scope is a type of scope that allows you to launch multiple coroutines and await for their results
                // before moving forward

                val quotesListDef = async { api.getQuotesList().map { it.toQuote() } }
                val qotDef = async { api.getQuoteOfTheDay().map { it.toQuote() } }

                val quotesList = quotesListDef.await()
                val qot = qotDef.await()

                val currList = db.getQuoteDao().getAllQuotes()

                currList.onEach {
                    if (!it.liked) {
                        db.getQuoteDao()
                            .deleteQuote(it)  // here u dont need to launch a coroutine as ...
                        // launch keyword is used to launch a coroutine and flow already have a running coroutine under its hood ...
                        // so u don't need to launch another coroutine
                    }
                }

                quotesList.let { list ->
                    db.getQuoteDao().insertQuoteList(list)
                }

                Log.d(
                    "TAG", "QuoteImpl inside try - Size of all list="
                            + db.getQuoteDao().getAllQuotes().size
                )

                quoteHome = QuoteHome(
                    quotesList = db.getQuoteDao().getAllQuotes(),
                    quotesOfTheDay = qot
                )

                emit(Resource.Success(quoteHome))
            }

        }.catch { e ->

            val errorMessage = when (e) {
                is IOException -> "No internet connection. Please try again."
                is HttpException -> {
                    when (e.code()) {
                        400 -> "Bad Request"
                        401 -> "Unauthorized Request"
                        403 -> "Forbidden Request"
                        429 -> "To many request to the server please check back in some time"
                        500 -> "Server is down...Please try again later"
                        else -> {
                            "Unknown error ${e.message()},Please try again."
                        }
                    }
                }

                else -> "Something went wrong. Please try again."
            }


//            Log.d("TAG", "Error in getQuote: ${e.message}")

            emit(Resource.Error(errorMessage))

        }.flowOn(Dispatchers.IO)

    }

    override suspend fun saveLikedQuote(quote: Quote) {
        db.getQuoteDao().insertLikedQuote(quote)
    }

    override fun getAllLikedQuotes(): Flow<List<Quote>> {
        return db.getQuoteDao().getAllLikedQuotes()
    }

    override suspend fun getQuoteById(id: Int): Quote? =
        db.getQuoteDao().getQuoteById(id)

    override suspend fun getLatestQuote(): Quote? =
        db.getQuoteDao().getLatestQuote()

    override suspend fun markAsDisplayed(quoteId: Int) {
        db.getQuoteDao().markAsDisplayed(quoteId)
    }

    override suspend fun getUndisplayedCount(): Int {
        return db.getQuoteDao().getUndisplayedCount()
    }

    override suspend fun getUndisplayedQuotes(): Resource<List<Quote>> {
        val undisplayedCount = db.getQuoteDao().getUndisplayedCount()
        return if (undisplayedCount > 0) {
            Resource.Success(db.getQuoteDao().getUndisplayedQuotes())
        } else {
            Resource.Success(emptyList())
        }

    }
}
