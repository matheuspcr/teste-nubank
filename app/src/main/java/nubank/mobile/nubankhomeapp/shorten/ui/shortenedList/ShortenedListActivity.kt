package nubank.mobile.nubankhomeapp.shorten.ui.shortenedList

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import nubank.mobile.nubankhomeapp.R
import nubank.mobile.nubankhomeapp.databinding.ActivityShortenedListBinding
import nubank.mobile.nubankhomeapp.shorten.ui.list.ShortenListAdapter
import nubank.mobile.nubankhomeapp.shorten.ui.viewmodel.ShortenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.net.toUri
import androidx.recyclerview.widget.ItemTouchHelper
import nubank.mobile.nubankhomeapp.shorten.ui.list.delete.DeleteHelperCallback

class ShortenedListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityShortenedListBinding.inflate(layoutInflater) }
    private val adapter by lazy { ShortenListAdapter(::openBrowser) }
    private val gestureCallback by lazy {
        ItemTouchHelper(DeleteHelperCallback(::onDelete)) }
    private val viewModel by viewModel<ShortenedListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupEdgeInsets()
        setupList()
        setupListener()
    }

    private fun setupEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun setupList() {
        binding.recentlyUrlsList.adapter = adapter
        adapter.aliases = viewModel.getList()
        gestureCallback.attachToRecyclerView(binding.recentlyUrlsList)
    }

    private fun setupListener() {
        binding.backButton.setOnClickListener { finish() }
    }

    private fun openBrowser(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, link.toUri())
        startActivity(intent)
    }

    private fun onDelete(position: Int) {
        viewModel.deleteAlias(position)
        adapter.aliases = viewModel.getList()
        adapter.notifyItemRemoved(position)
    }
}