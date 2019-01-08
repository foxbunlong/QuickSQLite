package com.longthay.quicksqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.provider.SyncStateContract.Helpers.update
import android.util.Log


/**
 * Created by Long Thay on 03/01/2019.
 * Concung.com
 * long.thay@concung.com
 */
class QSDbWrapper {

    companion object {

        var dbHelper: QSDbHelper? = null

        fun getReadableDb(context: Context): SQLiteDatabase {
            dbHelper = QSDbHelper(context)
            return dbHelper!!.readableDatabase
        }

        fun getWritableDb(context: Context): SQLiteDatabase {
            dbHelper = QSDbHelper(context)
            return dbHelper!!.writableDatabase
        }

        fun closeDb() {
            if (dbHelper != null) {
                dbHelper!!.close()
            }
        }

        fun getAll(context: Context): MutableList<QSModel> {
            val projection = arrayOf(
                    BaseColumns._ID,
                    QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_TITLE,
                    QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_SUBTITLE)
            val cursor = getReadableDb(context).query(QSDbHelper.QSModelContract.QSModelEntry.TABLE_NAME,
                    projection, null, null, null, null, null)

            val itemList = mutableListOf<QSModel>()
            with(cursor) {
                while (moveToNext()) {
                    val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val itemTitle = getString(getColumnIndexOrThrow(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_TITLE))
                    val itemSubtitle = getString(getColumnIndexOrThrow(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_SUBTITLE))
                    var entry = QSModel()
                    entry.id = itemId
                    entry.title = itemTitle
                    entry.subtitle = itemSubtitle
                    itemList.add(entry)
                }
            }

            return itemList
        }

        fun find(context: Context, entryId: Long): MutableList<QSModel> {
            val projection = arrayOf(
                    BaseColumns._ID,
                    QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_TITLE,
                    QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_SUBTITLE)

            val selection = "${BaseColumns._ID} = ?"
            val selectionArgs = arrayOf("$entryId")
            val cursor = getReadableDb(context).query(QSDbHelper.QSModelContract.QSModelEntry.TABLE_NAME,
                    projection, selection, selectionArgs, null, null, null)

            val itemList = mutableListOf<QSModel>()
            with(cursor) {
                while (moveToNext()) {
                    val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                    val itemTitle = getString(getColumnIndexOrThrow(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_TITLE))
                    val itemSubtitle = getString(getColumnIndexOrThrow(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_SUBTITLE))
                    var entry = QSModel()
                    entry.id = itemId
                    entry.title = itemTitle
                    entry.subtitle = itemSubtitle
                    itemList.add(entry)
                }
            }

            return itemList
        }

        fun insertOrUpdate(context: Context, entry: QSModel) {
            if (find(context, entry.id) != null && find(context, entry.id).size > 0) {
                QSLogger.log("Update ID ${entry.id}")
                update(context, entry)
            } else {
                QSLogger.log("Insert entry")
                insert(context, entry)
            }
        }

        fun insert(context: Context, entry: QSModel) {
            // Create a new map of values, where column names are the keys
            val values = ContentValues().apply {
                put(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_TITLE, entry.title)
                put(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_SUBTITLE, entry.subtitle)
            }

            // Insert the new row, returning the primary key value of the new row
            val newRowId = getWritableDb(context).insert(QSDbHelper.QSModelContract.QSModelEntry.TABLE_NAME, null, values)
            QSLogger.log(newRowId.toString())

            closeDb()
        }

        fun update(context: Context, entry: QSModel) {
            val values = ContentValues().apply {
                put(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_TITLE, entry.title)
                put(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_SUBTITLE, entry.subtitle)
            }

            val selection = "${BaseColumns._ID} = ?"
            val selectionArgs = arrayOf("${entry.id}")
            val count = getWritableDb(context).update(
                    QSDbHelper.QSModelContract.QSModelEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs)
            QSLogger.log("Update ${count}")
            closeDb()
        }

        fun deleteAll(context: Context) {
            val deletedRows = getWritableDb(context).delete(QSDbHelper.QSModelContract.QSModelEntry.TABLE_NAME, null, null)
            QSLogger.log("Delete ${deletedRows}")
            closeDb()
        }
    }

}