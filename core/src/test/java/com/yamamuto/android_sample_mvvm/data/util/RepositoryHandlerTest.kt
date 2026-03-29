package com.yamamuto.android_sample_mvvm.data.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class RepositoryHandlerTest {

    // ── handleWithCache ──

    @Test
    fun `handleWithCache returns cached data when not expired`() = runTest {
        val result = handleWithCache(
            load = { "cached" },
            fetch = { error("should not fetch") },
            toModel = { it },
            cachedAt = { System.currentTimeMillis() },
        )

        assertTrue(result.isSuccess)
        assertEquals("cached", result.getOrNull())
    }

    @Test
    fun `handleWithCache fetches when cache is null`() = runTest {
        var saved: String? = null
        val result = handleWithCache(
            load = { null },
            fetch = { "fresh" },
            toModel = { it },
            save = { saved = it },
        )

        assertTrue(result.isSuccess)
        assertEquals("fresh", result.getOrNull())
        assertEquals("fresh", saved)
    }

    @Test
    fun `handleWithCache fetches when forceRefresh is true`() = runTest {
        val result = handleWithCache(
            forceRefresh = true,
            load = { "cached" },
            fetch = { "fresh" },
            toModel = { it },
        )

        assertTrue(result.isSuccess)
        assertEquals("fresh", result.getOrNull())
    }

    @Test
    fun `handleWithCache fetches when cache is expired`() = runTest {
        val result = handleWithCache(
            load = { "cached" },
            fetch = { "fresh" },
            toModel = { it },
            cachedAt = { 0L },
        )

        assertTrue(result.isSuccess)
        assertEquals("fresh", result.getOrNull())
    }

    @Test
    fun `handleWithCache wraps IOException as Network`() = runTest {
        val result = handleWithCache<String, String>(
            load = { null },
            fetch = { throw IOException("timeout") },
            toModel = { it },
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is AppException.Network)
    }

    @Test
    fun `handleWithCache wraps HttpException as Server`() = runTest {
        val httpException = HttpException(
            Response.error<String>(500, "error".toResponseBody()),
        )

        val result = handleWithCache<String, String>(
            load = { null },
            fetch = { throw httpException },
            toModel = { it },
        )

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull() as AppException.Server
        assertEquals(500, exception.code)
    }

    @Test
    fun `handleWithCache passes through AppException`() = runTest {
        val result = handleWithCache<String, String>(
            load = { null },
            fetch = { throw AppException.NotFound("pikachu") },
            toModel = { it },
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is AppException.NotFound)
    }

    @Test
    fun `handleWithCache wraps unknown exception as Unknown`() = runTest {
        val result = handleWithCache<String, String>(
            load = { null },
            fetch = { throw IllegalStateException("unexpected") },
            toModel = { it },
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is AppException.Unknown)
    }

    // ── handleRemote ──

    @Test
    fun `handleRemote returns success on fetch`() = runTest {
        val result = handleRemote(
            fetch = { 42 },
            toModel = { it.toString() },
        )

        assertTrue(result.isSuccess)
        assertEquals("42", result.getOrNull())
    }

    @Test
    fun `handleRemote wraps IOException as Network`() = runTest {
        val result = handleRemote<String, String>(
            fetch = { throw IOException("no network") },
            toModel = { it },
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is AppException.Network)
    }

    // ── handleLocal ──

    @Test
    fun `handleLocal returns success on query`() = runTest {
        val result = handleLocal(
            query = { listOf("a", "b") },
            toModel = { it.size },
        )

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull())
    }

    @Test
    fun `handleLocal wraps exception as Unknown`() = runTest {
        val result = handleLocal<String, String>(
            query = { throw RuntimeException("db error") },
            toModel = { it },
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is AppException.Unknown)
    }
}
