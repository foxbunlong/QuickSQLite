package com.longthay.quicksqlitesample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyModel.getNames()


//        var entry = QSModel()
//        entry.title = "AAAAA"
//        entry.subtitle = "BBBBBBB"
//
//        for (i in 0..100) {
//            QSDbWrapper.insert(this@MainActivity, entry)
//        }

//        val list = QSDbWrapper.getAll(this@MainActivity)
//        for (entry in list) {
//            Log.d("AAAAAA", entry.id.toString() + " - " + entry.title)
//        }

//        QSDbWrapper.deleteAll(this@MainActivity)

//        var entry = QSModel()
//        entry.title = "BBBBB"
//        entry.subtitle = "BBBBBBB"
//        QSDbWrapper.update(this@MainActivity, 1, entry)

    }
}
