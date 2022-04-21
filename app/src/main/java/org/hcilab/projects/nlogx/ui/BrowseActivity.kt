package org.hcilab.projects.nlogx.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import org.hcilab.projects.nlogx.R

class BrowseActivity : AppCompatActivity(), OnRefreshListener {
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var adapter: BrowseAdapter? = null
    private var emptyStateText: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.list)
        recyclerView?.layoutManager = layoutManager
        swipeRefreshLayout = findViewById(R.id.swiper)
        swipeRefreshLayout?.setColorSchemeResources(R.color.colorAccent)
        swipeRefreshLayout?.setOnRefreshListener(this)
        emptyStateText = findViewById(R.id.emptyStateText)
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
                swipeRefreshLayout?.isRefreshing = true
                onRefresh()
                return true
            }
            R.id.menu_filter -> {
                filter()
                return true
            }
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun update() {
        adapter = BrowseAdapter(this)
        recyclerView!!.adapter = adapter
        if (adapter!!.itemCount == 0) {
            emptyStateText?.visibility = View.VISIBLE
        } else {
            emptyStateText?.visibility = View.GONE
        }
    }

    private fun filter() {
        val editText = EditText(this)
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_filter)
            .setView(editText)
            .setPositiveButton(R.string.dialog_ok) { dialog, which ->
                adapter?.setFilter(editText.text.toString())
            }
            .setNegativeButton(R.string.dialog_remove_filter) { dialog, which ->
               adapter?.setFilter(null)
            }
            .show() }

    override fun onRefresh() {
        update()
        swipeRefreshLayout!!.isRefreshing = false
    }
}