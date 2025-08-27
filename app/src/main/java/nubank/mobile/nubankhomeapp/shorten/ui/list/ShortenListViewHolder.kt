package nubank.mobile.nubankhomeapp.shorten.ui.list

import androidx.recyclerview.widget.RecyclerView
import nubank.mobile.nubankhomeapp.databinding.ShortenUrlListItemBinding
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel

internal class ShortenListViewHolder(
    private val binding: ShortenUrlListItemBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(entity: AliasLinksUIModel) = with(binding) {
        aliasText.text = entity.alias
        shortenUrlText.text = entity.shortenUrl
        originalUrlText.text = entity.originalUrl
        root.setOnClickListener { onClickListener.invoke(entity.originalUrl) }
    }
}
