package nubank.mobile.nubankhomeapp.shorten.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nubank.mobile.nubankhomeapp.shorten.data.converter.AliasModelConverter
import nubank.mobile.nubankhomeapp.shorten.data.datasource.ShortenLinkDataSourceI
import nubank.mobile.nubankhomeapp.shorten.data.model.ShortenUrlRequest
import nubank.mobile.nubankhomeapp.shorten.data.service.AliasApiService
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel

internal class ShortenLinkRepositoryImpl(
    private val service: AliasApiService,
    private val converter: AliasModelConverter,
    private val dataSource: ShortenLinkDataSourceI
): ShortenLinkRepository {

    override suspend fun shortenUrl(url: String): AliasLinksUIModel {
        val result = withContext(Dispatchers.IO) { service.shortenUrl(ShortenUrlRequest(url)) }
        if (result.isSuccessful) {
            val alias = converter.convert(result.body()!!)
            dataSource.saveAlias(alias)
            return alias
        }
        throw Exception()
    }

    override fun checkAlreadyCreated(url: String): Boolean {
        return dataSource.checkAlias(url)
    }

    override fun getAliasList(): List<AliasLinksUIModel> {
        return dataSource.getAliasList()
    }

    override fun deleteAlias(position: Int) {
        dataSource.deleteAlias(position)
    }
}