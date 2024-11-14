package com.sohnyi.pagingrepo.ui.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sohnyi.pagingrepo.databinding.ItemRepoBinding
import com.sohnyi.pagingrepo.model.Repo

class RepoHolder(private val binging: ItemRepoBinding) : ViewHolder(binging.root) {

    private var onItemClick: ((position: Int) -> Unit)? = null

    init {
        itemView.setOnClickListener {
            onItemClick?.invoke(absoluteAdapterPosition)
        }
    }

    fun bind(repo: Repo) {
        binging.tvName.text = repo.name
        binging.tvDesc.text = repo.description
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (position: Int) -> Unit): RepoHolder {
            val binding =
                ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val holder = RepoHolder(binding)
            holder.onItemClick = onItemClick
            return holder
        }
    }

}