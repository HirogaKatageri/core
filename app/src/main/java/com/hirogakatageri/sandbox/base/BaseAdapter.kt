package com.hirogakatageri.sandbox.base

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

abstract class BaseAdapter<VH : RecyclerView.ViewHolder, T : BaseItem>(
    context: Context
) : RecyclerView.Adapter<VH>(),
    KodeinAware {

    abstract val list: List<T>

    override fun getItemViewType(position: Int): Int = list[position].type

    override fun getItemCount(): Int = list.size

    override val kodein: Kodein by org.kodein.di.android.kodein(context)
}