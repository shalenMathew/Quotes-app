package com.shalenmathew.quotesapp.data.local_server

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shalenmathew.quotesapp.BuildConfig
import com.shalenmathew.quotesapp.domain.model.BackupData
import com.shalenmathew.quotesapp.domain.model.CustomQuote
import com.shalenmathew.quotesapp.domain.model.Quote
import com.shalenmathew.quotesapp.domain.usecases.custom_quote_usecases.CustomQuoteUseCases
import com.shalenmathew.quotesapp.domain.usecases.fav_screen_usecases.FavQuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.home_screen_usecases.QuoteUseCase
import com.shalenmathew.quotesapp.domain.usecases.library.CollectionUseCases
import com.shalenmathew.quotesapp.domain.usecases.widget.UpdateWidgetUseCase
import com.shalenmathew.quotesapp.presentation.workmanager.widget.ScheduleWidgetRefresh
import dagger.hilt.android.qualifiers.ApplicationContext
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.net.Inet4Address
import java.net.NetworkInterface
import javax.inject.Inject
import javax.inject.Singleton

data class LocalServerStatus(
    val isRunning: Boolean = false,
    val ipAddress: String = "0.0.0.0",
    val port: Int = 8080,
    val url: String = "",
    val errorMessage: String? = null
)

data class QuoteItemPayload(
    val id: Int? = null,
    val quote: String = "",
    val author: String = "Unknown",
    val isCustom: Boolean = true,
    val collectionId: Int? = null
)

data class BatchImportPayload(
    val quotes: List<QuoteItemPayload> = emptyList(),
    val target: String = "custom", // "custom" or "favorites"
    val collectionId: Int? = null
)

data class CollectionPayload(
    val id: Int? = null,
    val name: String = ""
)

@Singleton
class LocalQuoteWebServer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val quoteUseCase: QuoteUseCase,
    private val favQuoteUseCase: FavQuoteUseCase,
    private val customQuoteUseCases: CustomQuoteUseCases,
    private val collectionUseCases: CollectionUseCases,
    private val updateWidgetUseCase: UpdateWidgetUseCase,
    private val scheduleWidgetRefresh: ScheduleWidgetRefresh
) {
    private val gson = Gson()
    private var server: QuoteHttpServer? = null

    private val _status = MutableStateFlow(
        LocalServerStatus(
            isRunning = false,
            ipAddress = detectLocalIpAddress(),
            port = DEFAULT_PORT
        )
    )
    val status: StateFlow<LocalServerStatus> = _status.asStateFlow()

    @Synchronized
    fun startServer(preferredPort: Int = DEFAULT_PORT): LocalServerStatus {
        if (server?.isAlive == true) {
            refreshIpAddress()
            return _status.value
        }

        val ip = detectLocalIpAddress()
        val portsToTry = listOf(preferredPort, 8088, 8888, 9090)

        for (port in portsToTry) {
            try {
                val httpServer = QuoteHttpServer(port)
                httpServer.start(SOCKET_READ_TIMEOUT, false)
                server = httpServer
                val newStatus = LocalServerStatus(
                    isRunning = true,
                    ipAddress = ip,
                    port = port,
                    url = "http://$ip:$port",
                    errorMessage = null
                )
                _status.value = newStatus
                Log.i(TAG, "LocalQuoteWebServer started at ${newStatus.url}")
                return newStatus
            } catch (e: Exception) {
                Log.w(TAG, "Failed to bind port $port: ${e.message}")
            }
        }

        val failedStatus = LocalServerStatus(
            isRunning = false,
            ipAddress = ip,
            port = preferredPort,
            url = "",
            errorMessage = "Could not bind local server port. Please try again."
        )
        _status.value = failedStatus
        return failedStatus
    }

    @Synchronized
    fun stopServer() {
        try {
            server?.stop()
        } catch (e: Exception) {
            Log.w(TAG, "Error stopping server: ${e.message}")
        } finally {
            server = null
            val ip = detectLocalIpAddress()
            _status.value = LocalServerStatus(
                isRunning = false,
                ipAddress = ip,
                port = _status.value.port,
                url = "",
                errorMessage = null
            )
        }
    }

    fun refreshIpAddress() {
        val ip = detectLocalIpAddress()
        val current = _status.value
        _status.value = current.copy(
            ipAddress = ip,
            url = if (current.isRunning) "http://$ip:${current.port}" else ""
        )
    }

    fun detectLocalIpAddress(): String {
        runCatching {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                if (networkInterface.isLoopback || !networkInterface.isUp) continue
                val name = networkInterface.name.lowercase()
                // Prefer wlan / eth / ap interfaces
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val addr = addresses.nextElement()
                    if (!addr.isLoopbackAddress && addr is Inet4Address) {
                        val host = addr.hostAddress ?: continue
                        if (name.startsWith("wlan") || name.startsWith("eth") || name.startsWith("ap") || name.startsWith("en")) {
                            return host
                        }
                    }
                }
            }
            // Fallback: any non-loopback IPv4 address
            val fallbackInterfaces = NetworkInterface.getNetworkInterfaces()
            while (fallbackInterfaces.hasMoreElements()) {
                val networkInterface = fallbackInterfaces.nextElement()
                if (networkInterface.isLoopback || !networkInterface.isUp) continue
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val addr = addresses.nextElement()
                    if (!addr.isLoopbackAddress && addr is Inet4Address) {
                        return addr.hostAddress ?: continue
                    }
                }
            }
        }
        runCatching {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            val ipInt = wifiManager?.connectionInfo?.ipAddress ?: 0
            if (ipInt != 0) {
                return String.format(
                    "%d.%d.%d.%d",
                    ipInt and 0xff,
                    ipInt shr 8 and 0xff,
                    ipInt shr 16 and 0xff,
                    ipInt shr 24 and 0xff
                )
            }
        }
        return "127.0.0.1"
    }

    internal fun handleApiRequest(
        method: String,
        uri: String,
        body: String?
    ): Pair<NanoHTTPD.Response.IStatus, String> = runBlocking {
        try {
            when {
                method == "GET" && uri == "/api/state" -> {
                    val customQuotes = customQuoteUseCases.getCustomQuotes("").first()
                    val likedQuotes = quoteUseCase.getLikedQuotes().first()
                    val collections = collectionUseCases.getAllCollections().first()
                    val crossRefs = collectionUseCases.getAllCrossRefs()

                    val payload = mapOf(
                        "status" to "ok",
                        "appVersion" to BuildConfig.VERSION_NAME,
                        "customQuotes" to customQuotes,
                        "likedQuotes" to likedQuotes,
                        "collections" to collections,
                        "crossRefs" to crossRefs
                    )
                    NanoHTTPD.Response.Status.OK to gson.toJson(payload)
                }

                method == "POST" && uri == "/api/quotes/custom" -> {
                    val item = gson.fromJson(body ?: "{}", QuoteItemPayload::class.java)
                    val quoteText = item.quote.trim()
                    if (quoteText.isEmpty()) {
                        return@runBlocking NanoHTTPD.Response.Status.BAD_REQUEST to
                            gson.toJson(mapOf("error" to "Quote text cannot be empty"))
                    }
                    val authorText = item.author.trim().ifEmpty { "Unknown" }
                    customQuoteUseCases.saveCustomQuote(
                        CustomQuote(quote = quoteText, author = authorText)
                    )
                    if (item.collectionId != null && item.collectionId > 0) {
                        val newest = customQuoteUseCases.getCustomQuotes("").first().firstOrNull()
                        if (newest != null) {
                            collectionUseCases.addQuoteToCollection(item.collectionId, newest.id, true)
                        }
                    }
                    scheduleWidgetRefresh.scheduleWidgetRefreshWorkManager()
                    NanoHTTPD.Response.Status.OK to gson.toJson(mapOf("status" to "ok"))
                }

                method == "PUT" && uri == "/api/quotes/custom" -> {
                    val item = gson.fromJson(body ?: "{}", QuoteItemPayload::class.java)
                    val id = item.id ?: return@runBlocking NanoHTTPD.Response.Status.BAD_REQUEST to
                        gson.toJson(mapOf("error" to "Missing quote ID"))
                    val quoteText = item.quote.trim()
                    if (quoteText.isEmpty()) {
                        return@runBlocking NanoHTTPD.Response.Status.BAD_REQUEST to
                            gson.toJson(mapOf("error" to "Quote text cannot be empty"))
                    }
                    val authorText = item.author.trim().ifEmpty { "Unknown" }
                    val existing = customQuoteUseCases.getCustomQuotes("").first().find { it.id == id }
                    val updated = CustomQuote(
                        id = id,
                        quote = quoteText,
                        author = authorText,
                        createdAt = existing?.createdAt ?: System.currentTimeMillis()
                    )
                    customQuoteUseCases.updateCustomQuote(updated)
                    scheduleWidgetRefresh.scheduleWidgetRefreshWorkManager()
                    NanoHTTPD.Response.Status.OK to gson.toJson(mapOf("status" to "ok"))
                }

                method == "POST" && uri == "/api/quotes/delete" -> {
                    val type = object : TypeToken<Map<String, Any>>() {}.type
                    val map: Map<String, Any> = gson.fromJson(body ?: "{}", type)
                    val isCustom = map["isCustom"] as? Boolean ?: true
                    val idsRaw = map["ids"] as? List<*> ?: emptyList<Any>()
                    val ids = idsRaw.mapNotNull { (it as? Number)?.toInt() }.toSet()

                    if (isCustom) {
                        val allCustom = customQuoteUseCases.getCustomQuotes("").first()
                        allCustom.filter { it.id in ids }.forEach { cq ->
                            customQuoteUseCases.deleteCustomQuote(cq)
                        }
                    } else {
                        val allLiked = quoteUseCase.getLikedQuotes().first()
                        allLiked.filter { it.id != null && it.id in ids }.forEach { q ->
                            // Unliking removes it from favorites list
                            favQuoteUseCase.favLikedQuote.saveLikedQuote(q)
                        }
                    }
                    scheduleWidgetRefresh.scheduleWidgetRefreshWorkManager()
                    NanoHTTPD.Response.Status.OK to gson.toJson(
                        mapOf("status" to "ok", "deletedCount" to ids.size)
                    )
                }

                method == "POST" && uri == "/api/quotes/batch" -> {
                    val batch = gson.fromJson(body ?: "{}", BatchImportPayload::class.java)
                    val validQuotes = batch.quotes.filter { it.quote.trim().isNotEmpty() }
                    var importedCount = 0

                    if (batch.target == "favorites") {
                        validQuotes.forEach { item ->
                            val newQuote = Quote(
                                quote = item.quote.trim(),
                                author = item.author.trim().ifEmpty { "Unknown" },
                                liked = true,
                                displayed = true,
                                updatedAt = System.currentTimeMillis(),
                                isCustom = false
                            )
                            quoteUseCase.saveLikedQuote(newQuote)
                            importedCount++
                        }
                    } else {
                        validQuotes.forEach { item ->
                            val cq = CustomQuote(
                                quote = item.quote.trim(),
                                author = item.author.trim().ifEmpty { "Unknown" }
                            )
                            customQuoteUseCases.saveCustomQuote(cq)
                            importedCount++
                        }
                        if (batch.collectionId != null && batch.collectionId > 0) {
                            val latestCustom = customQuoteUseCases.getCustomQuotes("").first().take(importedCount)
                            latestCustom.forEach { cq ->
                                collectionUseCases.addQuoteToCollection(batch.collectionId, cq.id, true)
                            }
                        }
                    }

                    scheduleWidgetRefresh.scheduleWidgetRefreshWorkManager()
                    NanoHTTPD.Response.Status.OK to gson.toJson(
                        mapOf("status" to "ok", "importedCount" to importedCount)
                    )
                }

                method == "POST" && uri == "/api/collections" -> {
                    val payload = gson.fromJson(body ?: "{}", CollectionPayload::class.java)
                    val name = payload.name.trim()
                    if (name.isEmpty()) {
                        return@runBlocking NanoHTTPD.Response.Status.BAD_REQUEST to
                            gson.toJson(mapOf("error" to "Collection name required"))
                    }
                    val result = collectionUseCases.addCollection(name)
                    result.fold(
                        onSuccess = {
                            NanoHTTPD.Response.Status.OK to gson.toJson(mapOf("status" to "ok"))
                        },
                        onFailure = { e ->
                            NanoHTTPD.Response.Status.BAD_REQUEST to
                                gson.toJson(mapOf("error" to (e.message ?: "Failed to create collection")))
                        }
                    )
                }

                method == "POST" && uri == "/api/collections/assign" -> {
                    val type = object : TypeToken<Map<String, Any>>() {}.type
                    val map: Map<String, Any> = gson.fromJson(body ?: "{}", type)
                    val collectionId = (map["collectionId"] as? Number)?.toInt()
                        ?: return@runBlocking NanoHTTPD.Response.Status.BAD_REQUEST to
                            gson.toJson(mapOf("error" to "Missing collectionId"))
                    val quoteId = (map["quoteId"] as? Number)?.toInt()
                        ?: return@runBlocking NanoHTTPD.Response.Status.BAD_REQUEST to
                            gson.toJson(mapOf("error" to "Missing quoteId"))
                    val isCustom = map["isCustom"] as? Boolean ?: true
                    val remove = map["remove"] as? Boolean ?: false

                    if (remove) {
                        collectionUseCases.removeQuoteFromCollection(collectionId, quoteId, isCustom)
                    } else {
                        collectionUseCases.addQuoteToCollection(collectionId, quoteId, isCustom)
                    }
                    NanoHTTPD.Response.Status.OK to gson.toJson(mapOf("status" to "ok"))
                }

                method == "POST" && uri == "/api/widget/push" -> {
                    val item = gson.fromJson(body ?: "{}", QuoteItemPayload::class.java)
                    val quote = Quote(
                        id = item.id ?: 1,
                        quote = item.quote.trim(),
                        author = item.author.trim().ifEmpty { "Unknown" },
                        liked = !item.isCustom,
                        isCustom = item.isCustom
                    )
                    updateWidgetUseCase(quote)
                    NanoHTTPD.Response.Status.OK to gson.toJson(mapOf("status" to "ok"))
                }

                method == "GET" && uri == "/api/backup/export" -> {
                    val likedQuotes = quoteUseCase.getLikedQuotes().first()
                    val customQuotes = customQuoteUseCases.getCustomQuotes("").first()
                    val collections = collectionUseCases.getAllCollections().first()
                    val crossRefs = collectionUseCases.getAllCrossRefs()

                    val backupData = BackupData(
                        likedQuotes = likedQuotes,
                        customQuotes = customQuotes,
                        collections = collections,
                        crossRefs = crossRefs,
                        appVersionName = BuildConfig.VERSION_NAME
                    )
                    NanoHTTPD.Response.Status.OK to gson.toJson(backupData)
                }

                method == "POST" && uri == "/api/backup/import" -> {
                    val backupData = gson.fromJson(body ?: "{}", BackupData::class.java)
                    var importedQuotes = 0

                    backupData.likedQuotes?.forEach { quote ->
                        quoteUseCase.saveLikedQuote(quote)
                        importedQuotes++
                    }
                    backupData.customQuotes?.forEach { customQuote ->
                        customQuoteUseCases.saveCustomQuote(customQuote)
                        importedQuotes++
                    }

                    val oldToNewIdMap = mutableMapOf<Int, Int>()
                    backupData.collections?.forEach { collection ->
                        val existing = collectionUseCases.getCollectionByName(collection.name)
                        if (existing != null) {
                            oldToNewIdMap[collection.id] = existing.id
                        } else {
                            val newId = collectionUseCases.addCollection(collection.name).fold(
                                onSuccess = { collectionUseCases.getCollectionByName(collection.name)?.id },
                                onFailure = { null }
                            )
                            if (newId != null) {
                                oldToNewIdMap[collection.id] = newId
                            }
                        }
                    }

                    backupData.crossRefs?.forEach { crossRef ->
                        val newCollectionId = oldToNewIdMap[crossRef.collectionId]
                        if (newCollectionId != null) {
                            collectionUseCases.addQuoteToCollection(
                                newCollectionId,
                                crossRef.quoteId,
                                crossRef.isCustom
                            )
                        }
                    }

                    scheduleWidgetRefresh.scheduleWidgetRefreshWorkManager()
                    NanoHTTPD.Response.Status.OK to gson.toJson(
                        mapOf("status" to "ok", "importedCount" to importedQuotes)
                    )
                }

                else -> {
                    NanoHTTPD.Response.Status.NOT_FOUND to
                        gson.toJson(mapOf("error" to "Endpoint not found: $method $uri"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "API request error on $method $uri", e)
            NanoHTTPD.Response.Status.INTERNAL_ERROR to
                gson.toJson(mapOf("error" to (e.message ?: "Internal server error")))
        }
    }

    private inner class QuoteHttpServer(port: Int) : NanoHTTPD(port) {
        override fun serve(session: IHTTPSession): Response {
            val uri = session.uri ?: "/"
            val method = session.method.name

            // Handle CORS preflight
            if (method == "OPTIONS") {
                return newFixedLengthResponse(Response.Status.OK, "text/plain", "").apply {
                    addCorsHeaders()
                }
            }

            if (uri == "/" || uri == "/index.html") {
                return try {
                    val html = context.assets.open("web/index.html").bufferedReader().use { it.readText() }
                    newFixedLengthResponse(Response.Status.OK, "text/html; charset=utf-8", html)
                } catch (e: Exception) {
                    newFixedLengthResponse(
                        Response.Status.INTERNAL_ERROR,
                        "text/plain",
                        "Failed to load Web UI: ${e.message}"
                    )
                }
            }

            if (uri.startsWith("/api/")) {
                val bodyMap = HashMap<String, String>()
                if (method == "POST" || method == "PUT") {
                    try {
                        session.parseBody(bodyMap)
                    } catch (e: Exception) {
                        Log.w(TAG, "Failed to parse request body", e)
                    }
                }
                val postData = bodyMap["postData"]
                val (status, jsonResponse) = handleApiRequest(method, uri, postData)
                return newFixedLengthResponse(status, "application/json; charset=utf-8", jsonResponse).apply {
                    addCorsHeaders()
                }
            }

            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found")
        }

        private fun Response.addCorsHeaders() {
            addHeader("Access-Control-Allow-Origin", "*")
            addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
            addHeader("Access-Control-Allow-Headers", "Content-Type")
        }
    }

    companion object {
        private const val TAG = "LocalQuoteWebServer"
        const val DEFAULT_PORT = 8080
        private const val SOCKET_READ_TIMEOUT = 15_000
    }
}
