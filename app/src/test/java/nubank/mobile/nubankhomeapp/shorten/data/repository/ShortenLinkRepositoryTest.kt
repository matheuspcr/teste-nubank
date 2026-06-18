package nubank.mobile.nubankhomeapp.shorten.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nubank.mobile.nubankhomeapp.shorten.ALIAS_STUB
import nubank.mobile.nubankhomeapp.shorten.ORIGINAL_URL_STUB
import nubank.mobile.nubankhomeapp.shorten.SHORTEN_URL_STUB
import nubank.mobile.nubankhomeapp.shorten.data.converter.AliasModelConverter
import nubank.mobile.nubankhomeapp.shorten.data.datasource.ShortenLinkDataSourceI 
import nubank.mobile.nubankhomeapp.shorten.data.model.AliasLinksResponse
import nubank.mobile.nubankhomeapp.shorten.data.model.ShortenUrlRequest
import nubank.mobile.nubankhomeapp.shorten.data.model.ShortenUrlResponse
import nubank.mobile.nubankhomeapp.shorten.data.service.AliasApiService
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
internal class ShortenLinkRepositoryTest {
    private val service: AliasApiService = mockk(relaxed = true)
    private val converter: AliasModelConverter = mockk(relaxed = true)
    private val dataSource: ShortenLinkDataSourceI = mockk(relaxed = true)

    private lateinit var repository: ShortenLinkRepository

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        repository = ShortenLinkRepositoryImpl(service, converter, dataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `shortenUrl should return AliasLinksUIModel on successful response`() = runTest {
        // GIVEN
        val testUrl = "https://url-shortener-server.onrender.com/api/alias/test"
        val shortenUrlResponse = ShortenUrlResponse(ALIAS_STUB, AliasLinksResponse(ORIGINAL_URL_STUB, SHORTEN_URL_STUB))
        val expectedAliasLinksUIModel = AliasLinksUIModel(ALIAS_STUB, ORIGINAL_URL_STUB, SHORTEN_URL_STUB)

        coEvery { service.shortenUrl(ShortenUrlRequest(testUrl)) } returns Response.success(shortenUrlResponse)
        coEvery { converter.convert(shortenUrlResponse) } returns expectedAliasLinksUIModel

        // WHEN
        val result = repository.shortenUrl(testUrl)

        // THEN
        assertThat(result).isEqualTo(expectedAliasLinksUIModel)
    }

    @Test(expected = Exception::class)
    fun `shortenUrl should throw Exception on unsuccessful response`() = runTest {
        // GIVEN
        val testUrl = "https://url-shortener-server.onrender.com/api/alias/error"
        val errorResponseBody = "{\"message\":\"Error\"}".toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { service.shortenUrl(ShortenUrlRequest(testUrl)) } returns Response.error(400, errorResponseBody)

        // WHEN
        repository.shortenUrl(testUrl)
    }

    @Test(expected = Exception::class)
    fun `shortenUrl should throw Exception WHEN successful response has null body`() = runTest {
        // GIVEN
        val testUrl = "https://url-shortener-server.onrender.com/api/alias/nullbody"

        coEvery { service.shortenUrl(ShortenUrlRequest(testUrl)) } returns Response.success(null as ShortenUrlResponse?)

        // WHEN
        repository.shortenUrl(testUrl)
    }

    @Test(expected = Exception::class)
    fun `shortenUrl should rethrow exception from service call`() = runTest {
        // GIVEN
        val testUrl = "https://url-shortener-server.onrender.com/api/alias/exception"
        val expectedException = Exception("Generic error")

        coEvery { service.shortenUrl(ShortenUrlRequest(testUrl)) } throws expectedException

        // WHEN
        repository.shortenUrl(testUrl)
    }
}