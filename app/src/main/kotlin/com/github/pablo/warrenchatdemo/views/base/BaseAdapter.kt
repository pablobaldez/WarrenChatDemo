package com.github.pablo.warrenchatdemo.views.base

import android.support.v7.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var list: MutableList<T>? = null

    override fun getItemCount(): Int = list.orEmpty().size

    fun add(item: T) {
        list?.let {
            it.add(item)
            notifyItemInserted(it.size)
        }
    }

}