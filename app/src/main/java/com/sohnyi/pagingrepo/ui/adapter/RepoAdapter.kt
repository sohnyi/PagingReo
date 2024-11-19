package com.sohnyi.pagingrepo.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sohnyi.pagingrepo.R
import com.sohnyi.pagingrepo.ui.holder.LetterHolder
import com.sohnyi.pagingrepo.ui.holder.RepoHolder
import com.sohnyi.pagingrepo.viewmodel.UiModel

class RepoAdapter(private val onItemClick: (position: Int) -> Unit) :
    PagingDataAdapter<UiModel, ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == R.layout.item_repo) {
            RepoHolder.create(parent, onItemClick)
        } else {
            LetterHolder.create(parent)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uiModel = getItem(position)
        when (uiModel) {
            is UiModel.RepoItem -> (holder as RepoHolder).bind(uiModel.repo)
            is UiModel.LetterItem -> (holder as LetterHolder).bind(uiModel.letter)
            null -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        val uiModel = getItem(position)
        return when (uiModel) {
            is UiModel.RepoItem -> {
                R.layout.item_repo
            }

            is UiModel.LetterItem -> {
                R.layout.item_letter
            }

            else -> throw IllegalStateException("Unknown view")
        }
    }


    private object DiffCallback : DiffUtil.ItemCallback<UiModel>() {
        override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
            return (oldItem is UiModel.RepoItem && newItem is UiModel.RepoItem && oldItem.repo.id == newItem.repo.id)
                    || (oldItem is UiModel.LetterItem && newItem is UiModel.LetterItem && oldItem.letter == newItem.letter)
        }

        override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
            return oldItem == newItem
        }
    }
}