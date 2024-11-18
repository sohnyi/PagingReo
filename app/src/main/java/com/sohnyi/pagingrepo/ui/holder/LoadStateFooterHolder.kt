package com.sohnyi.pagingrepo.ui.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sohnyi.pagingrepo.databinding.ItemLoadStateFooterBinding

class LoadStateFooterHolder(
    private val binding: ItemLoadStateFooterBinding,
    private val retry: () -> Unit,
) : ViewHolder(binding.root) {


    fun bind(state: LoadState) {

        binding.progressBar.isVisible = state is LoadState.Loading

        binding.btnRetry.isVisible = state is LoadState.Error
        binding.tvErrorMsg.isVisible = state is LoadState.Error
        if (state is LoadState.Error) {
            binding.tvErrorMsg.text = state.error.message
        }

        binding.btnRetry.setOnClickListener {
            retry()
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateFooterHolder {
            val binding = ItemLoadStateFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return LoadStateFooterHolder(binding, retry)
        }
    }
}