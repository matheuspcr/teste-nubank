package nubank.mobile.nubankhomeapp.shorten.data.datasource

import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel

internal interface ShortenLinkDataSourceI {
    fun getAliasList(): List<AliasLinksUIModel>
    fun saveAlias(alias: AliasLinksUIModel)
    fun checkAlias(url: String): Boolean
    fun deleteAlias(position: Int)
}