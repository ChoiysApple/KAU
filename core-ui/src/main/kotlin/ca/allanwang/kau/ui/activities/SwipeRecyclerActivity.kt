package ca.allanwang.kau.ui.activities

import android.os.Bundle
import ca.allanwang.kau.adapters.fastAdapter
import ca.allanwang.kau.internal.KauBaseActivity
import ca.allanwang.kau.ui.R
import ca.allanwang.kau.ui.views.SwipeRecyclerView
import ca.allanwang.kau.utils.bindView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import org.jetbrains.anko.AnkoAsyncContext

/**
 * Created by Allan Wang on 07/04/18.
 */
abstract class SwipeRecyclerActivity<T : IItem<*, *>>(
        private val clearAdapterOnRefresh: Boolean = false
) : KauBaseActivity() {

    val adapter = ItemAdapter<T>()
    val srv: SwipeRecyclerView by bindView(R.id.kau_swipe_recycler_view)

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kau_swipe_recycler_view)
        val fastAdapter = fastAdapter(adapter)
        srv.recycler.adapter = fastAdapter
        if (clearAdapterOnRefresh)
            srv.onPreRefresh = { adapter.clear() }
        srv.onRefresh = { onRefresh() }
        srv.refresh()
        onCreate(savedInstanceState, fastAdapter)
    }

    abstract fun onCreate(savedInstanceState: Bundle?, fastAdapter: FastAdapter<T>)

    abstract fun AnkoAsyncContext<SwipeRecyclerView>.onRefresh()
}
