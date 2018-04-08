package ca.allanwang.kau.ui.views

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import ca.allanwang.kau.logging.KL
import ca.allanwang.kau.ui.R
import ca.allanwang.kau.utils.bindView
import ca.allanwang.kau.utils.inflate
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by Allan Wang on 07/04/18.
 */
class SwipeRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    val recycler: RecyclerView by bindView(R.id.kau_srv_recycler)

    /**
     * Callable action on the ui thread before the refresh has started
     */
    var onPreRefresh: (byDrag: Boolean) -> Unit = {}
    /**
     * Synchronized execution during refresh
     * The refresh status will automatically be set to false afterwards
     */
    var onRefresh: AnkoAsyncContext<SwipeRecyclerView>.(byDrag: Boolean) -> Unit = {}
    /**
     * Callable action on the ui thread after a refresh has ended
     * This will only be called if the refresh is successful
     */
    var onPostRefresh: (byDrag: Boolean) -> Unit = {}
    /**
     * Handler to manage a refresh failure
     * Note that this may be done on a background thread
     * The refresh status will automatically be set to false afterwards
     */
    var exceptionHandler: (Throwable) -> Unit = {
        KL.e(it) { "SRV refresh failed" }
    }

    var refresh: (byDrag: Boolean) -> Unit = { byDrag ->
        isRefreshing = true
        onPreRefresh(byDrag)
        doAsync(exceptionHandler = {
            this.exceptionHandler(it)
            post { isRefreshing = false }
        }, task = {
            onRefresh(byDrag)
            uiThread {
                isRefreshing = false
                onPostRefresh(byDrag)
            }
        })
    }

    fun refresh() = refresh(false)

    init {
        inflate(R.layout.kau_swipe_recycler_view_base)
        setOnRefreshListener(this::refresh)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return recycler.canScrollVertically(-1) && super.onInterceptTouchEvent(ev)
    }
}