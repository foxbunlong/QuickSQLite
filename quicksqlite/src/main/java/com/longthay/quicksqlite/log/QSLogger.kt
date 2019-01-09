package com.longthay.quicksqlite.log

import android.util.Log


/**
 * Created by Long Thay on 03/01/2019.
 * Concung.com
 * long.thay@concung.com
 */
class QSLogger {

    companion object {

        val TAG = "QSLogger"

        fun log(output: String) {
            Log.d(TAG, output)
        }
    }

}