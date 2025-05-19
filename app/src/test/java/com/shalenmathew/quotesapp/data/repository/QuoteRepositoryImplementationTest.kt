//package com.shalenmathew.quotesapp.data.repository
//
//import com.shalenmathew.quotesapp.data.local.QuoteDao
//import com.shalenmathew.quotesapp.data.local.QuoteDatabase
//import com.shalenmathew.quotesapp.data.mappers.toQuote
//import com.shalenmathew.quotesapp.data.remote.QuoteApi
//import com.shalenmathew.quotesapp.data.remote.dto.QuotesDto
//import com.shalenmathew.quotesapp.data.remote.dto.QuotesDtoItem
//import com.shalenmathew.quotesapp.domain.model.Quote
//import com.shalenmathew.quotesapp.domain.model.QuoteHome
//import com.shalenmathew.quotesapp.util.Resource
//import junit.framework.TestCase.assertEquals
//import junit.framework.TestCase.assertTrue
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.toList
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Assert
//import org.junit.Before
//import org.junit.Test
//import org.mockito.Mockito.doNothing
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.mockStatic
//import org.mockito.kotlin.any
//import org.mockito.kotlin.whenever
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class QuoteRepositoryImplementationTest {
//    private lateinit var api: QuoteApi
//    private lateinit var db: QuoteDatabase
//    private lateinit var dao: QuoteDao
//    private lateinit var repository: QuoteRepositoryImplementation
//    private val testDispatcher = StandardTestDispatcher()
//
//    @Before
//    fun setUp() {
//        Dispatchers.setMain(testDispatcher)
//        api = mock()
//        db = mock()
//        dao = mock()
//        whenever(db.getQuoteDao()).thenReturn(dao)
//        repository = QuoteRepositoryImplementation(api, db)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun getQuote_Success() = runTest(testDispatcher) {
//        // Setup data
//        val quoteDto = QuotesDto().apply {
//            add(QuotesDtoItem(a = "auth1", c = "", h = "", q = "quote1"))
//            add(QuotesDtoItem(a = "auth2", c = "", h = "", q = "quote2"))
//        }
//        val qotDto = QuotesDto().apply {
//            add(QuotesDtoItem(a = "qot", c = "", h = "", q = "qot"))
//        }
//
//        // Mock API responses
//        whenever(api.getQuotesList()).thenReturn(quoteDto)
//        whenever(api.getQuoteOfTheDay()).thenReturn(qotDto)
//
//        // Mock DAO with dynamic list
//        val currentQuotes = mutableListOf<Quote>()
//        whenever(dao.getAllQuotes()).thenReturn(currentQuotes)
//        whenever(dao.deleteQuote(any())).thenAnswer { quote ->
//            currentQuotes.remove(quote.arguments.first() as Quote)
//        }
//        whenever(dao.insertQuoteList(any())).thenAnswer { quotes ->
//            currentQuotes.addAll(quotes.arguments.first() as List<Quote>)
//        }
//
//        // Collect emissions
//        val emissions = mutableListOf<Resource<QuoteHome>>()
//        val job = launch(testDispatcher) { repository.getQuote().collect { emissions.add(it) } }
//
//        // Advance coroutines
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        // Verify emissions
//
//        assertTrue(emissions[0] is Resource.Loading)
//        assertTrue(emissions[1] is Resource.Success)
//
//        val success = emissions[1] as Resource.Success
//        assertEquals(quoteDto.size, success.data?.quotesList?.size)
//        assertEquals(qotDto.size, success.data?.quotesOfTheDay?.size)
//
//
//        job.cancel()
//    }
//}