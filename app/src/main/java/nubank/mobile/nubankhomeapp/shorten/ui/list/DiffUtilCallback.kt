package nubank.mobile.nubankhomeapp.shorten.ui.list

import androidx.recyclerview.widget.DiffUtil
import nubank.mobile.nubankhomeapp.shorten.ui.model.AliasLinksUIModel

object DiffUtilCallback : DiffUtil.ItemCallback<AliasLinksUIModel>() {

    override fun areItemsTheSame(oldItem: AliasLinksUIModel, newItem: AliasLinksUIModel): Boolean {
        return oldItem.alias == newItem.alias
    }

    override fun areContentsTheSame(oldItem: AliasLinksUIModel, newItem: AliasLinksUIModel): Boolean {
        return oldItem.toString() == newItem.toString()
    }
}