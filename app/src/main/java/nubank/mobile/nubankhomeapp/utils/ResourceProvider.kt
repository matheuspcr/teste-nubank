package nubank.mobile.nubankhomeapp.utils

import android.content.Context
import androidx.annotation.StringRes

class ResourceProvider(private val context: Context) {
    fun getString(@StringRes id: Int): String = context.getString(id)

    fun getString(@StringRes id: Int, vararg args: Any): String = context.getString(id, *args)
}
