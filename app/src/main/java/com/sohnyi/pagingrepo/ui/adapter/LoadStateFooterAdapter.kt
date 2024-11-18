package com.sohnyi.pagingrepo.ui.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.sohnyi.pagingrepo.ui.holder.LoadStateFooterHolder

class LoadStateFooterAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<LoadStateFooterHolder>() {
    override fun onBindViewHolder(holder: LoadStateFooterHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): LoadStateFooterHolder {
        return LoadStateFooterHolder.create(parent, retry)
    }
}