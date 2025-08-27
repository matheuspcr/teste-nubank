package nubank.mobile.nubankhomeapp.shorten.data.datasource

import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel

internal class ShortenLinkInMemoryDataSource : ShortenLinkDataSourceI{
    private val inMemory: MutableList<AliasLinksUIModel> = mutableListOf()

    override fun getAliasList(): List<AliasLinksUIModel> {
        return inMemory
    }

    override fun saveAlias(alias: AliasLinksUIModel) {
        inMemory.add(alias)
    }

    override fun checkAlias(url: String): Boolean {
        return inMemory.find { it.originalUrl == url } != null
    }

    override fun deleteAlias(position: Int) {
        inMemory.removeAt(position)
    }
}