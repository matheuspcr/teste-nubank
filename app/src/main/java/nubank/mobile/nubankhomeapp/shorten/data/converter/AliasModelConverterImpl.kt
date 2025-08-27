package nubank.mobile.nubankhomeapp.shorten.data.converter

import nubank.mobile.nubankhomeapp.shorten.data.model.ShortenUrlResponse
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel

internal class AliasModelConverterImpl: AliasModelConverter {

    override fun convert(model: ShortenUrlResponse) = AliasLinksUIModel(
        alias = model.alias,
        originalUrl = model._links.self,
        shortenUrl = model._links.short
    )
}