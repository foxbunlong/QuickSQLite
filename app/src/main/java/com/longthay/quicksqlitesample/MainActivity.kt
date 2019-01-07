package com.longthay.quicksqlitesample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.longthay.quicksqlite.QSDbWrapper
import com.longthay.quicksqlite.QSModel
import com.longthay.quicksqlitesample.adapters.MainAdapter
import com.longthay.quicksqlitesample.models.events.MainItemSelectedEvent
import com.longthay.quicksqlitesample.views.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    lateinit var listAdapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        MyModel.getNames()

        setupUI()
        setupUIEvents()
        bindData()

//        var entry = QSModel()
//        entry.title = "BBBBB"
//        entry.subtitle = "BBBBBBB"
//        QSDbWrapper.update(this@MainActivity, 1, entry)

    }

    private fun setupUI() {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayout.VERTICAL
        rvList!!.layoutManager = llm
        rvList!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        rvList!!.setHasFixedSize(true)

        listAdapter = MainAdapter(this@MainActivity)
        rvList.adapter = listAdapter
    }

    private fun setupUIEvents() {
        btnAdd.setOnClickListener {
            var entry = QSModel()
            entry.title = etTitle.text.toString()
            entry.subtitle = etSubTitle.text.toString()

            QSDbWrapper.insert(this@MainActivity, entry)

            bindData()
        }
    }

    private fun bindData() {
        val list = QSDbWrapper.getAll(this@MainActivity)
        listAdapter.bankList = list
        listAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_delete_all -> {
                QSDbWrapper.deleteAll(this@MainActivity)
                bindData()
                return true
            }
            android.R.id.home -> {
                this.onBackPressed()
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MainItemSelectedEvent) {

    }
}
