package nubank.mobile.nubankhomeapp.shorten.data.repository

import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel

interface ShortenLinkRepository {
    suspend fun shortenUrl(url: String): AliasLinksUIModel
    fun getAliasList(): List<AliasLinksUIModel>
    fun checkAlreadyCreated(url: String): Boolean
    fun deleteAlias(position: Int)
}