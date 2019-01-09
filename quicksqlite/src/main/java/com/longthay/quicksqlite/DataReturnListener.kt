package com.longthay.quicksqlite

import com.longthay.quicksqlite.model.QSModel


/**
 * Created by Long Thay on 08/01/2019.
 * Concung.com
 * long.thay@concung.com
 */
interface DataReturnListener {

    fun onDataReceived(data : MutableList<QSModel>)

}