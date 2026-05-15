package com.shalenmathew.quotesapp.data.repository

import android.content.Context
import android.util.Log
import com.shalenmathew.quotesapp.data.local.QuoteDatabase
import com.shalenmathew.quotesapp.data.mappers.toQuote
import com.shalenmathew.quotesapp.data.remote.QuoteApi
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.model.QuoteHome
import com.shalenmathew.quotesapp.domain.repository.QuoteRepository
import com.shalenmathew.quotesapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

class QuoteRepositoryImplementation(private val api: QuoteApi, private val db: QuoteDatabase, private val context: Context) :
    QuoteRepository {


    override fun getQuote(): Flow<Resource<QuoteHome>> {

        return flow {

            var quoteHome: QuoteHome?

            emit(Resource.Loading())

            Log.d("TAG", Thread.currentThread().name)

            coroutineScope {

                var quotesList: List<Quote> = emptyList()
                var qot: List<Quote> = emptyList()
                var retryCount = 0
                val maxRetries = 2

                while (retryCount <= maxRetries) {
                    try {
                        Log.d("QuoteRepository", "Fetching from ZenQuotes API (Attempt ${retryCount + 1})")
                        
                        // Fetch sequentially to avoid rate limiting (429 Too Many Requests or REFUSED_STREAM)
                        quotesList = api.getQuotesList(System.currentTimeMillis()).map { it.toQuote() }
                        
                        // ZenQuotes strictly limits to 1 request per second. We MUST delay here.
                        delay(1500) 
                        
                        qot = api.getQuoteOfTheDay(System.currentTimeMillis()).map { it.toQuote() }
                        
                        if (quotesList.isEmpty() || qot.isEmpty()) {
                            throw Exception("ZenQuotes returned empty list")
                        }
                        
                        // If we reach here, both requests succeeded
                        break
                    } catch (e: Exception) {
                        retryCount++
                        if (retryCount > maxRetries) {
                            throw e // Re-throw if we've exhausted retries
                        }
                        Log.w("QuoteRepository", "ZenQuotes request failed, retrying in 3 seconds...", e)
                        delay(3000) // Wait 3 seconds before retrying to let rate limits reset
                    }
                }

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
            Log.e("QuoteRepository", "API Call Failed with exception: ${e.message}", e)
            val errorMessage = throwExceptionMessage(e)


            emit(Resource.Error(errorMessage))

        }.flowOn(Dispatchers.IO)

    }

    fun throwExceptionMessage(e: Throwable): String{

        return when (e) {
            is java.net.SocketTimeoutException -> "Connection timed out. Please try again."
            is java.net.UnknownHostException -> "No internet connection. Please check your network."
            is IOException -> "Network error. Please try again."
            is HttpException -> {
                when (e.code()) {
                    400 -> "Bad Request"
                    401 -> "Unauthorized Request"
                    403 -> "Forbidden Request"
                    429 -> "To many request to the server please check back in some time"
                    500 -> "Server is down...Please try again later"
                    else -> {
                        "Unknown error,Please try again."
                    }
                }
            }

            else -> "Something went wrong. Please try again."
        }

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

    override suspend fun getRandomQuoteFromNetwork(): Resource<Quote> {
        return try {
            val response = api.getRandomQuote(System.currentTimeMillis())
            val quote = response.firstOrNull()?.toQuote()
            if (quote != null) {
                Resource.Success(quote)
            } else {
                Resource.Error("Empty response from ZenQuotes")
            }
        } catch (e: Exception) {
            Log.e("QuoteRepository", "Failed to fetch random quote", e)
            Resource.Error(throwExceptionMessage(e))
        }
    }
}
