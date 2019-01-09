package com.longthay.quicksqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import com.longthay.quicksqlite.log.QSLogger
import com.longthay.quicksqlite.model.QSModel
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by Long Thay on 03/01/2019.
 * Concung.com
 * long.thay@concung.com
 */
class QSDbWrapper {

    companion object {

        var dbReadHelper: QSDbHelper? = null
        var dbWriteHelper: QSDbHelper? = null

        fun getReadableDb(context: Context): SQLiteDatabase {
            if (dbReadHelper == null) {
                dbReadHelper = QSDbHelper(context)
            }
            return dbReadHelper!!.readableDatabase
        }

        fun getWritableDb(context: Context): SQLiteDatabase {
            if (dbWriteHelper == null) {
                dbWriteHelper = QSDbHelper(context)
            }
            return dbWriteHelper!!.writableDatabase
        }

        fun closeReadDb() {
            if (dbReadHelper != null) {
//                dbReadHelper!!.readableDatabase.close()
                dbReadHelper!!.close()
            }
        }

        fun closeWriteDb() {
            if (dbWriteHelper != null) {
                dbWriteHelper!!.close()
            }
        }

        fun getAll(context: Context, callback: DataReturnListener): MutableList<QSModel> {
            var itemList = mutableListOf<QSModel>()
            Observable.create(object : ObservableOnSubscribe<String> {
                override fun subscribe(emitter: ObservableEmitter<String>) {
                    val projection = arrayOf(
                            BaseColumns._ID,
                            QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_TITLE,
                            QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_SUBTITLE)
                    val cursor = getReadableDb(context).query(QSDbHelper.QSModelContract.QSModelEntry.TABLE_NAME,
                            projection, null, null, null, null, "${BaseColumns._ID} DESC")

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
                    emitter.onComplete()
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), false, 100)
                    .subscribe(object : Observer<String> {
                        override fun onComplete() {
                            QSLogger.log("getAll Observable onComplete")
                            callback.onDataReceived(itemList)
                        }

                        override fun onSubscribe(d: Disposable) {
                            QSLogger.log("getAll Observable onSubscribe")
                        }

                        override fun onNext(t: String) {
                            QSLogger.log("getAll Observable onNext")
                        }

                        override fun onError(e: Throwable) {
                            QSLogger.log("getAll Observable onError")
                            e.printStackTrace()
                        }

                    })

            return itemList
        }

        fun find(context: Context, entryId: Long, callback: DataReturnListener) {

            var itemList = mutableListOf<QSModel>()
            Observable.create(object : ObservableOnSubscribe<String> {
                override fun subscribe(emitter: ObservableEmitter<String>) {
                    val projection = arrayOf(
                            BaseColumns._ID,
                            QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_TITLE,
                            QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_SUBTITLE)

                    val selection = "${BaseColumns._ID} = ?"
                    val selectionArgs = arrayOf("$entryId")
                    val cursor = getReadableDb(context).query(QSDbHelper.QSModelContract.QSModelEntry.TABLE_NAME,
                            projection, selection, selectionArgs, null, null, null)

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
//                    closeReadDb()
                    emitter.onComplete()
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), false, 9999)
                    .subscribe(object : Observer<String> {
                        override fun onComplete() {
                            QSLogger.log("find Observable onComplete")
                            callback.onDataReceived(itemList)
                        }

                        override fun onSubscribe(d: Disposable) {
                            QSLogger.log("find Observable onSubscribe")
                        }

                        override fun onNext(t: String) {
                            QSLogger.log("find Observable onNext")
                        }

                        override fun onError(e: Throwable) {
                            QSLogger.log("find Observable onError")
                            e.printStackTrace()
                        }

                    })
        }

        fun insertOrUpdate(context: Context, entry: QSModel, callback: DataReturnListener) {
            runThread(context, entry, callback)
        }

        fun insertOrUpdate(context: Context, entryList: MutableList<QSModel>, callback: DataReturnListener) {
            runThread(context, entryList, callback)
        }

        fun runThread(context: Context, entry: QSModel, callback: DataReturnListener) {
            Observable.create(object : ObservableOnSubscribe<QSModel> {
                override fun subscribe(emitter: ObservableEmitter<QSModel>) {
                    find(context, entry.id, object : DataReturnListener {
                        override fun onDataReceived(data: MutableList<QSModel>) {
                            if (data != null && data.size > 0) {
                                QSLogger.log("Update ID ${entry.id}")
                                update(context, entry)
                            } else {
                                QSLogger.log("Insert entry")
                                insert(context, entry)
                            }
                            emitter.onNext(entry)
                            emitter.onComplete()
                        }
                    })
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), false, 9999)
                    .subscribe(object : Observer<QSModel> {
                        override fun onComplete() {
                            QSLogger.log("insertOrUpdate Observable onComplete")
//                            closeWriteDb()
                        }

                        override fun onSubscribe(d: Disposable) {
                            QSLogger.log("insertOrUpdate Observable onSubscribe")
                        }

                        override fun onNext(t: QSModel) {
                            QSLogger.log("insertOrUpdate Observable onNext")
                            val list = mutableListOf<QSModel>()
                            list.add(t)
                            callback.onDataReceived(list)
                        }

                        override fun onError(e: Throwable) {
                            QSLogger.log("insertOrUpdate Observable onError")
                            e.printStackTrace()
                        }

                    })
        }

        fun runThread(context: Context, entryList: MutableList<QSModel>, callback: DataReturnListener) {
            Observable.create(object : ObservableOnSubscribe<String> {
                override fun subscribe(emitter: ObservableEmitter<String>) {
                    insert(context, entryList)
                    emitter.onNext("")
                    emitter.onComplete()
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), false, 9999)
                    .subscribe(object : Observer<String> {
                        override fun onComplete() {
                            QSLogger.log("insertOrUpdate Observable onComplete")
//                            closeWriteDb()
                        }

                        override fun onSubscribe(d: Disposable) {
                            QSLogger.log("insertOrUpdate Observable onSubscribe")
                        }

                        override fun onNext(t: String) {
                            QSLogger.log("insertOrUpdate Observable onNext")
                            callback.onDataReceived(entryList)
                        }

                        override fun onError(e: Throwable) {
                            QSLogger.log("insertOrUpdate Observable onError")
                            e.printStackTrace()
                        }

                    })
        }

        fun insert(context: Context, entry: QSModel) {
            // Create a new map of values, where column names are the keys
            val values = ContentValues().apply {
                put(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_TITLE, entry.title)
                put(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_SUBTITLE, entry.subtitle)
            }

            // Insert the new row, returning the primary key value of the new row
            val newRowId = getWritableDb(context).insert(QSDbHelper.QSModelContract.QSModelEntry.TABLE_NAME, null, values)
        }

        fun insert(context: Context, entryList: MutableList<QSModel>) {

            getWritableDb(context).beginTransaction()

            for (entry in entryList) {
                // Create a new map of values, where column names are the keys
                val values = ContentValues().apply {
                    put(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_TITLE, entry.title)
                    put(QSDbHelper.QSModelContract.QSModelEntry.COLUMN_NAME_SUBTITLE, entry.subtitle)
                }

                // Insert the new row, returning the primary key value of the new row
                val newRowId = getWritableDb(context).insert(QSDbHelper.QSModelContract.QSModelEntry.TABLE_NAME, null, values)
            }

            getWritableDb(context).setTransactionSuccessful()
            getWritableDb(context).endTransaction()
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
        }

        fun deleteAll(context: Context) {
            val deletedRows = getWritableDb(context).delete(QSDbHelper.QSModelContract.QSModelEntry.TABLE_NAME, null, null)
            QSLogger.log("Delete ${deletedRows}")
            closeWriteDb()
        }
    }

}