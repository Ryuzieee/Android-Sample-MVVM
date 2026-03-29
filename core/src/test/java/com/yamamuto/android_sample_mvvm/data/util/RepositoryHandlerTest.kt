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
    fun `handleWithCache returns cached data when not expired`() =
        runTest {
            val result =
                handleWithCache(
                    load = { "cached" },
                    fetch = { "raw" },
                    toEntity = { it },
                    toModel = { it },
                    cachedAt = { System.currentTimeMillis() },
                )

            assertTrue(result.isSuccess)
            assertEquals("cached", result.getOrNull())
        }

    @Test
    fun `handleWithCache fetches when cache is null`() =
        runTest {
            var saved: String? = null
            val result =
                handleWithCache(
                    load = { null },
                    fetch = { "raw" },
                    toEntity = { "${it}_entity" },
                    toModel = { it },
                    save = { saved = it },
                )

            assertTrue(result.isSuccess)
            assertEquals("raw_entity", result.getOrNull())
            assertEquals("raw_entity", saved)
        }

    @Test
    fun `handleWithCache fetches when forceRefresh is true`() =
        runTest {
            val result =
                handleWithCache(
                    forceRefresh = true,
                    load = { "cached" },
                    fetch = { "raw" },
                    toEntity = { "${it}_fresh" },
                    toModel = { it },
                )

            assertTrue(result.isSuccess)
            assertEquals("raw_fresh", result.getOrNull())
        }

    @Test
    fun `handleWithCache fetches when cache is expired`() =
        runTest {
            val result =
                handleWithCache(
                    load = { "cached" },
                    fetch = { "raw" },
                    toEntity = { "${it}_fresh" },
                    toModel = { it },
                    cachedAt = { 0L },
                )

            assertTrue(result.isSuccess)
            assertEquals("raw_fresh", result.getOrNull())
        }

    @Test
    fun `handleWithCache wraps IOException as Network`() =
        runTest {
            val result =
                handleWithCache<String, String, String>(
                    load = { null },
                    fetch = { throw IOException("timeout") },
                    toEntity = { it },
                    toModel = { it },
                )

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.Network)
        }

    @Test
    fun `handleWithCache wraps HttpException as Server`() =
        runTest {
            val httpException =
                HttpException(
                    Response.error<String>(500, "error".toResponseBody()),
                )

            val result =
                handleWithCache<String, String, String>(
                    load = { null },
                    fetch = { throw httpException },
                    toEntity = { it },
                    toModel = { it },
                )

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull() as AppException.Server
            assertEquals(500, exception.code)
        }

    @Test
    fun `handleWithCache passes through AppException`() =
        runTest {
            val result =
                handleWithCache<String, String, String>(
                    load = { null },
                    fetch = { throw AppException.NotFound("pikachu") },
                    toEntity = { it },
                    toModel = { it },
                )

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.NotFound)
        }

    @Test
    fun `handleWithCache wraps unknown exception as Unknown`() =
        runTest {
            val result =
                handleWithCache<String, String, String>(
                    load = { null },
                    fetch = { throw IllegalStateException("unexpected") },
                    toEntity = { it },
                    toModel = { it },
                )

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.Unknown)
        }

    // ── handleRemote ──

    @Test
    fun `handleRemote returns success on fetch`() =
        runTest {
            val result =
                handleRemote(
                    fetch = { 42 },
                    toModel = { it.toString() },
                )

            assertTrue(result.isSuccess)
            assertEquals("42", result.getOrNull())
        }

    @Test
    fun `handleRemote wraps IOException as Network`() =
        runTest {
            val result =
                handleRemote<String, String>(
                    fetch = { throw IOException("no network") },
                    toModel = { it },
                )

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.Network)
        }

    // ── handleLocal ──

    @Test
    fun `handleLocal returns success on query`() =
        runTest {
            val result =
                handleLocal(
                    query = { listOf("a", "b") },
                    toModel = { it.size },
                )

            assertTrue(result.isSuccess)
            assertEquals(2, result.getOrNull())
        }

    @Test
    fun `handleLocal wraps exception as Unknown`() =
        runTest {
            val result =
                handleLocal<String, String>(
                    query = { throw RuntimeException("db error") },
                    toModel = { it },
                )

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is AppException.Unknown)
        }
}
