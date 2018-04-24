package com.github.pablo.warrenchatdemo.views.base

import android.support.v7.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var list: MutableList<T> = ArrayList()

    override fun getItemCount(): Int = list.size

    fun add(item: T) {
        list.add(item)
        notifyItemInserted(list.size)
    }

}