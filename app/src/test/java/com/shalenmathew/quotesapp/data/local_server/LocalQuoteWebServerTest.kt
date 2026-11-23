package com.shalenmathew.quotesapp.data.local_server

import android.content.Context
import com.shalenmathew.quotesapp.domain.model.Collection
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.DeleteCustomQuote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.GetCustomQuotes
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.SaveCustomQuote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.UpdateCustomQuote
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavQuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.GetLikedQuotes
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.SaveLikedQuote
import com.shalenmathew.quotesapp.domain.usecases.library.CollectionUseCases
import com.shalenmathew.quotesapp.domain.usecases.library.GetAllCollections
import com.shalenmathew.quotesapp.domain.usecases.library.GetAllCrossRefs
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetUseCase
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class LocalQuoteWebServerTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var getLikedQuotes: GetLikedQuotes

    @Mock
    private lateinit var saveLikedQuote: SaveLikedQuote

    @Mock
    private lateinit var getCustomQuotes: GetCustomQuotes

    @Mock
    private lateinit var saveCustomQuote: SaveCustomQuote

    @Mock
    private lateinit var deleteCustomQuote: DeleteCustomQuote

    @Mock
    private lateinit var updateCustomQuote: UpdateCustomQuote

    @Mock
    private lateinit var getAllCollections: GetAllCollections

    @Mock
    private lateinit var getAllCrossRefs: GetAllCrossRefs

    @Mock
    private lateinit var updateWidgetUseCase: UpdateWidgetUseCase

    @Mock
    private lateinit var scheduleWidgetRefresh: ScheduleWidgetRefresh

    private lateinit var localServer: LocalQuoteWebServer

    @Before
    fun setUp() {
        val quoteUseCase = QuoteUseCase(
            getQuote = mock(),
            likedQuote = mock(),
            saveLikedQuote = saveLikedQuote,
            getLikedQuotes = getLikedQuotes,
            getLatestQuote = mock(),
            markAsDisplayed = mock(),
            getUndisplayedQuotes = mock(),
            getRandomRemoteQuote = mock()
        )
        val favQuoteUseCase = FavQuoteUseCase(
            getFavQuote = mock(),
            favLikedQuote = mock()
        )
        val customQuoteUseCases = CustomQuoteUseCases(
            getCustomQuotes = getCustomQuotes,
            saveCustomQuote = saveCustomQuote,
            deleteCustomQuote = deleteCustomQuote,
            updateCustomQuote = updateCustomQuote
        )
        val collectionUseCases = CollectionUseCases(
            getAllCollections = getAllCollections,
            addCollection = mock(),
            updateCollection = mock(),
            deleteCollection = mock(),
            getCollectionById = mock(),
            getCollectionByName = mock(),
            addQuoteToCollection = mock(),
            removeQuoteFromCollection = mock(),
            isQuoteInCollection = mock(),
            getCollectionIdsForQuote = mock(),
            searchQuotesInCollection = mock(),
            getAllCrossRefs = getAllCrossRefs
        )

        localServer = LocalQuoteWebServer(
            context = context,
            quoteUseCase = quoteUseCase,
            favQuoteUseCase = favQuoteUseCase,
            customQuoteUseCases = customQuoteUseCases,
            collectionUseCases = collectionUseCases,
            updateWidgetUseCase = updateWidgetUseCase,
            scheduleWidgetRefresh = scheduleWidgetRefresh
        )
    }

    @Test
    fun `handleApiRequest GET state returns quotes and collections`() = runTest {
        whenever(getCustomQuotes.invoke("")).thenReturn(
            flowOf(listOf(CustomQuote(id = 1, quote = "Custom 1", author = "Author 1")))
        )
        whenever(getLikedQuotes.invoke()).thenReturn(
            flowOf(listOf(Quote(id = 2, quote = "Liked 1", author = "Author 2", liked = true)))
        )
        whenever(getAllCollections.invoke()).thenReturn(
            flowOf(listOf(Collection(id = 1, name = "Focus")))
        )
        whenever(getAllCrossRefs.invoke()).thenReturn(emptyList())

        val (status, json) = localServer.handleApiRequest("GET", "/api/state", null)

        assertEquals(NanoHTTPD.Response.Status.OK, status)
        assertTrue(json.contains("Custom 1"))
        assertTrue(json.contains("Liked 1"))
        assertTrue(json.contains("Focus"))
    }

    @Test
    fun `handleApiRequest POST batch imports multiple custom quotes`() = runTest {
        val payload = """
            {
              "target": "custom",
              "quotes": [
                {"quote": "First batch quote", "author": "Author A"},
                {"quote": "Second batch quote", "author": "Author B"}
              ]
            }
        """.trimIndent()

        val (status, json) = localServer.handleApiRequest("POST", "/api/quotes/batch", payload)

        assertEquals(NanoHTTPD.Response.Status.OK, status)
        assertTrue(json.contains("\"importedCount\":2"))
        verify(saveCustomQuote, times(2)).invoke(any())
    }

    @Test
    fun `handleApiRequest POST deleteCorrupted removes only quotes with replacement char or csv header`() = runTest {
        val corrupted1 = CustomQuote(id = 1, quote = "Chapter 8 \uFFFD\uFFFD Business Service,Section 3 Banking", author = "Unknown")
        val corrupted2 = CustomQuote(id = 2, quote = "chapter,section,topic,en,zh,tag", author = "Unknown")
        val clean = CustomQuote(id = 3, quote = "The bank loaned me some money.\n银行贷给我一笔款。", author = "行业服务 · 银行贷款种类")
        whenever(getCustomQuotes.invoke("")).thenReturn(flowOf(listOf(corrupted1, corrupted2, clean)))

        val payload = """{"isCustom": true, "deleteCorrupted": true}"""
        val (status, json) = localServer.handleApiRequest("POST", "/api/quotes/delete", payload)

        assertEquals(NanoHTTPD.Response.Status.OK, status)
        assertTrue(json.contains("\"deletedCount\":2"))
        verify(deleteCustomQuote, times(1)).deleteBatch(listOf(corrupted1, corrupted2))
    }

    @Test
    fun `live HTTP server decodes UTF-8 Chinese POST body without socket error or corruption`() = runTest {
        val status = localServer.startServer(18989)
        try {
            assertTrue(status.isRunning)
            val url = java.net.URL("http://127.0.0.1:${status.port}/api/quotes/batch")
            val payload = """
                {
                  "target": "custom",
                  "quotes": [
                    {"quote": "The bank loaned me some money.\n银行贷给我一笔款。", "author": "行业服务 · 银行贷款种类"}
                  ]
                }
            """.trimIndent()
            val conn = (url.openConnection() as java.net.HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
            }
            conn.outputStream.use { it.write(payload.toByteArray(Charsets.UTF_8)) }
            val code = conn.responseCode
            val responseText = conn.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }

            assertEquals(200, code)
            assertTrue(responseText.contains("\"importedCount\":1"))
            verify(saveCustomQuote, times(1)).invoke(org.mockito.kotlin.check { saved ->
                assertEquals("The bank loaned me some money.\n银行贷给我一笔款。", saved.quote)
                assertEquals("行业服务 · 银行贷款种类", saved.author)
            })
        } finally {
            localServer.stopServer()
        }
    }
}
