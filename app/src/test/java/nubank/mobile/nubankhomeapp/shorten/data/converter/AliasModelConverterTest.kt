package nubank.mobile.nubankhomeapp.shorten.data.converter

import nubank.mobile.nubankhomeapp.shorten.ALIAS_STUB
import nubank.mobile.nubankhomeapp.shorten.ORIGINAL_URL_STUB
import nubank.mobile.nubankhomeapp.shorten.SHORTEN_URL_STUB
import nubank.mobile.nubankhomeapp.shorten.data.model.AliasLinksResponse
import nubank.mobile.nubankhomeapp.shorten.data.model.ShortenUrlResponse
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel
import org.junit.Test

internal class AliasModelConverterTest {
    private val converter: AliasModelConverter = AliasModelConverterImpl()

    @Test
    fun `convert should return AliasLinksUIModel with same property values from ShortenUrlResponse input`() {
        // GIVEN
        val expected = AliasLinksUIModel(
            alias = ALIAS_STUB,
            originalUrl = ORIGINAL_URL_STUB,
            shortenUrl = SHORTEN_URL_STUB
        )
        val input = ShortenUrlResponse(
            alias = ALIAS_STUB,
            _links = AliasLinksResponse(
                self = ORIGINAL_URL_STUB,
                short = SHORTEN_URL_STUB
            )
        )

        // WHEN
        val result = converter.convert(input)

        // THEN
        assert(expected.alias == result.alias)
        assert(expected.originalUrl == result.originalUrl)
        assert(expected.shortenUrl == result.shortenUrl)
    }
}