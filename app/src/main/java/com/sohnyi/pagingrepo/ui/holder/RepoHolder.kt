package com.sohnyi.pagingrepo.ui.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sohnyi.pagingrepo.databinding.ItemRepoBinding
import com.sohnyi.pagingrepo.model.Repo

class RepoHolder(private val binging: ItemRepoBinding) : ViewHolder(binging.root) {

    private var onItemClick: ((position: Int) -> Unit)? = null

    init {
        itemView.setOnClickListener {
            onItemClick?.invoke(absoluteAdapterPosition)
        }
    }

    fun bind(repo: Repo?) {
        val loading = repo == null
        binging.layoutLoading.isVisible = loading
        binging.groupContent.isVisible = !loading
        if (!loading) {
            repo?.let {
                Glide.with(itemView.context)
                    .load(it.owner?.avatarUrl)
                    .override(binging.ivAvatar.width, binging.ivAvatar.height)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binging.ivAvatar)

                binging.tvUsername.text = it.owner?.login ?: " "

                binging.tvName.text = it.name ?: ""
                binging.tvDesc.text = it.description ?: ""

                binging.tvStarCount.text = (it.stars ?: 0).toString()
                binging.tvLanguage.text = it.language ?: ""
            }
        }

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