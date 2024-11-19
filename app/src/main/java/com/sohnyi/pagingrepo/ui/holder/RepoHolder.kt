package com.sohnyi.pagingrepo.ui.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sohnyi.pagingrepo.R
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

        Glide.with(itemView.context)
            .load(repo.owner?.avatarUrl)
            .override(binging.ivAvatar.width, binging.ivAvatar.height)
            .apply(RequestOptions.circleCropTransform())
            .into(binging.ivAvatar)

        binging.tvPosition.text = itemView.context.getString(R.string.format_position, absoluteAdapterPosition + 1)

        binging.tvUsername.text = repo.owner?.login ?: " "

        binging.tvName.text = repo.name ?: ""
        binging.tvDesc.text = repo.description ?: ""

        binging.tvStarCount.text = (repo.stargazersCount ?: 0).toString()
        binging.tvLanguage.text = repo.language ?: ""
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