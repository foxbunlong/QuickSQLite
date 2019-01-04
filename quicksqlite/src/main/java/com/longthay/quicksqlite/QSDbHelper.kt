package com.longthay.quicksqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


/**
 * Created by Long Thay on 03/01/2019.
 * Concung.com
 * long.thay@concung.com
 */

class QSDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    object QSModelContract {
        object QSModelEntry : BaseColumns {

            const val TABLE_NAME = "entry"
            const val COLUMN_NAME_TITLE = "title"
            const val COLUMN_NAME_SUBTITLE = "subtitle"

            const val SQL_CREATE_ENTRIES =
                    "CREATE TABLE ${QSModelEntry.TABLE_NAME} (" +
                            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                            "${QSModelEntry.COLUMN_NAME_TITLE} TEXT," +
                            "${QSModelEntry.COLUMN_NAME_SUBTITLE} TEXT)"

            const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${QSModelEntry.TABLE_NAME}"

        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(QSModelContract.QSModelEntry.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(QSModelContract.QSModelEntry.SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }
}


