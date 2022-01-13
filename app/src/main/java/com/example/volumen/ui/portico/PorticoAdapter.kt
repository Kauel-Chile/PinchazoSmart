package com.example.volumen.ui.portico

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.volumen.api.portico.Data
import com.example.volumen.databinding.CardListPorticoBinding
import java.util.*

class PorticoAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Data, PorticoAdapter.PorticoViewHolder>(PorticoComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PorticoViewHolder {
        val binding =
            CardListPorticoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PorticoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PorticoViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem, listener)
        }
    }

    class PorticoViewHolder(private val binding: CardListPorticoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data, listener: OnItemClickListener) {
            binding.apply {
                tvPortico.text = data.name.uppercase(Locale.getDefault())
                cdPortico.setOnClickListener {
                    listener.onItemClick(data)
                }
            }
        }
    }

    class PorticoComparator : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem._id == newItem._id


        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem == newItem

    }

    interface OnItemClickListener {
        fun onItemClick(data: Data)
    }

}