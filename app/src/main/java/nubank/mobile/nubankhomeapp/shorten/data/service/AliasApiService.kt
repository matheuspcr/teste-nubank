package nubank.mobile.nubankhomeapp.shorten.data.service

import nubank.mobile.nubankhomeapp.shorten.data.model.ShortenUrlRequest
import nubank.mobile.nubankhomeapp.shorten.data.model.ShortenUrlResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AliasApiService {

    @POST("api/alias")
    suspend fun shortenUrl(@Body body: ShortenUrlRequest): Response<ShortenUrlResponse>
}
