package nubank.mobile.nubankhomeapp.shorten.data.converter

import nubank.mobile.nubankhomeapp.shorten.data.model.ShortenUrlResponse
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel

internal interface AliasModelConverter {

    fun convert(model: ShortenUrlResponse): AliasLinksUIModel
}