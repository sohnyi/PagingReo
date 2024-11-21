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
        // 加载中
        binding.progressBar.isVisible = state is LoadState.Loading
        // 出错按钮
        binding.btnRetry.isVisible = state is LoadState.Error
        // 出错信息
        if (state is LoadState.Error) {
            binding.tvErrorMsg.text = state.error.message
        }
        // 出错按钮点击事件
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