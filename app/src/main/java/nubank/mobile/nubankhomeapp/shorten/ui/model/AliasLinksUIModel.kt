package nubank.mobile.nubankhomeapp.shorten.ui.model

data class AliasLinksUIModel(
    val alias: String,
    val originalUrl: String,
    val shortenUrl: String
) {
    override fun toString(): String {
        return buildString {
            append(alias)
            append(originalUrl)
            append(shortenUrl)
        }
    }
}
