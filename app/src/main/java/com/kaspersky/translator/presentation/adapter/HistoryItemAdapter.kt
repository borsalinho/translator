package com.kaspersky.translator.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaspersky.domain.model.WordTranslation
import com.kaspersky.translator.databinding.ListItemBinding

class HistoryItemAdapter(
    private var itemList: List<WordTranslation>,
    private val onFavoriteClick: (WordTranslation) -> Unit
) : RecyclerView.Adapter<HistoryItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.querry.text = item.query
        holder.binding.translates.text = item.translation
        holder.binding.btnAddFavorite.text = if (item.favorite) "favorite" else "add"
        holder.binding.btnAddFavorite.setOnClickListener {
            onFavoriteClick(item)
            item.favorite = !item.favorite
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = itemList.size

    fun updateItems(newItems: List<WordTranslation>) {
        itemList = newItems
        notifyDataSetChanged()
    }

}