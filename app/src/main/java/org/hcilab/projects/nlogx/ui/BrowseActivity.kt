package org.hcilab.projects.nlogx.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.hcilab.projects.nlogx.ui.BrowseAdapter
import android.os.Bundle
import org.hcilab.projects.nlogx.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.view.Menu
import org.hcilab.projects.nlogx.ui.DetailsActivity
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast

class BrowseActivity : AppCompatActivity(), OnRefreshListener {
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var adapter: BrowseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.list)
        recyclerView?.setLayoutManager(layoutManager)
        swipeRefreshLayout = findViewById(R.id.swiper)
        swipeRefreshLayout?.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout?.setOnRefreshListener(this)
        update()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && DetailsActivity.ACTION_REFRESH == data.getStringExtra(DetailsActivity.EXTRA_ACTION)) {
            update()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.browse, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                update()
                return true
            }
            R.id.menu_filter -> {
                filter()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun update() {
        adapter = BrowseAdapter(this)
        recyclerView!!.adapter = adapter
        if (adapter!!.itemCount == 0) {
            Toast.makeText(this, R.string.empty_log_file, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun filter() {}
    override fun onRefresh() {
        update()
        swipeRefreshLayout!!.isRefreshing = false
    }
}