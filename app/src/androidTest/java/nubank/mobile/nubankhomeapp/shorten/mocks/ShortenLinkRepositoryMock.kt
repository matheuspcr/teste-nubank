package nubank.mobile.nubankhomeapp.shorten.mocks

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import nubank.mobile.nubankhomeapp.helper.waitFor
import nubank.mobile.nubankhomeapp.shorten.data.repository.ShortenLinkRepository
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel
import nubank.mobile.nubankhomeapp.stub.ALIAS_UI_MODEL_STUB
import nubank.mobile.nubankhomeapp.stub.ALIAS_UI_MODEL_STUB_2
import nubank.mobile.nubankhomeapp.stub.ALIAS_UI_MODEL_STUB_3
import nubank.mobile.nubankhomeapp.stub.ORIGINAL_URL_STUB
import nubank.mobile.nubankhomeapp.stub.ORIGINAL_URL_STUB_2
import nubank.mobile.nubankhomeapp.stub.ORIGINAL_URL_STUB_3

class ShortenLinkRepositoryMock : ShortenLinkRepository {
    override suspend fun shortenUrl(url: String): AliasLinksUIModel {
        return when (url) {
            ORIGINAL_URL_STUB -> ALIAS_UI_MODEL_STUB
            ORIGINAL_URL_STUB_2 -> ALIAS_UI_MODEL_STUB_2
            ORIGINAL_URL_STUB_3 -> ALIAS_UI_MODEL_STUB_3
            else -> throw Exception()
        }
    }
}