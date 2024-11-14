package com.sohnyi.pagingrepo.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.sohnyi.pagingrepo.model.Repo
import com.sohnyi.pagingrepo.ui.holder.RepoHolder

class RepoAdapter(private val onItemClick: (position: Int) -> Unit) : PagingDataAdapter<Repo, RepoHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        return RepoHolder.create(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        val repo = getItem(position) ?: return
        holder.bind(repo)
    }


    private object DiffCallback : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem == newItem
        }

    }
}