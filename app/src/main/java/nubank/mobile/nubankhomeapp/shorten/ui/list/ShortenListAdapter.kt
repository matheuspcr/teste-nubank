package nubank.mobile.nubankhomeapp.shorten.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import nubank.mobile.nubankhomeapp.databinding.ShortenUrlListItemBinding
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel

internal class ShortenListAdapter(
    private val onClickListener: (String) -> Unit
) : ListAdapter<AliasLinksUIModel, ShortenListViewHolder>(DiffUtilCallback) {
    var aliases: List<AliasLinksUIModel> = emptyList()
        set(value) {
            field = value
            submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortenListViewHolder {
        val binding= ShortenUrlListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShortenListViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: ShortenListViewHolder, position: Int) {
        val alias = getItem(position)
        holder.bind(alias)
    }
}