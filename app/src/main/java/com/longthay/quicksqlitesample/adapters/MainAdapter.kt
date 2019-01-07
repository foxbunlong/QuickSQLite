package com.longthay.quicksqlitesample.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.longthay.quicksqlite.QSModel
import com.longthay.quicksqlitesample.R
import com.longthay.quicksqlitesample.models.events.MainItemSelectedEvent
import org.greenrobot.eventbus.EventBus


/**
 * Created by Long Thay on 07/01/2019.
 * Concung.com
 * long.thay@concung.com
 */
class MainAdapter(val context: Context) : RecyclerView.Adapter<MainAdapter.ItemViewHolder>() {

    var bankList = mutableListOf<QSModel>()

    fun setData(bankList: List<QSModel>) {
        this.bankList = bankList as MutableList<QSModel>
    }

    fun refreshData() {
        bankList = mutableListOf<QSModel>()
    }

    override fun onBindViewHolder(holder: MainAdapter.ItemViewHolder, position: Int) {
        // Item
        holder?.tvTitle?.setText(bankList[position].title)
        holder?.tvSubTitle?.setText(bankList[position].subtitle)
    }

    override fun getItemCount(): Int {
        return bankList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v1 = LayoutInflater.from(parent?.context).inflate(R.layout.main_list_item, parent, false)
        return ItemViewHolder(v1)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var tvTitle: TextView
        internal var tvSubTitle: TextView

        init {
            tvTitle = itemView.findViewById<View>(R.id.tvTitle) as TextView
            tvSubTitle = itemView.findViewById<View>(R.id.tvSubTitle) as TextView

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var event = MainItemSelectedEvent()
            event.position = adapterPosition
            EventBus.getDefault().post(event)
        }
    }
}