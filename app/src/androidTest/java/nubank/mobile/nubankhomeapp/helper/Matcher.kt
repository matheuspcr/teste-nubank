package nubank.mobile.nubankhomeapp.helper

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object Matcher {
    fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var childView: View? = null

            override fun describeTo(description: Description) {
                description.appendText("with item at position $position : ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                if (view !is RecyclerView) {
                    return false
                }
                val viewHolder = view.findViewHolderForAdapterPosition(position) ?: return false
                childView = viewHolder.itemView
                return itemMatcher.matches(childView)
            }
        }
    }
}