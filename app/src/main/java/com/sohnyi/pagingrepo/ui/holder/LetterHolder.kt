package com.sohnyi.pagingrepo.ui.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sohnyi.pagingrepo.databinding.ItemLetterBinding

class LetterHolder(
    private val binding: ItemLetterBinding,
) : ViewHolder(binding.root) {


    fun bind(letter: String) {
        binding.tvLetter.text = letter
    }

    companion object {
        fun create(parent: ViewGroup): LetterHolder {
            val binding =
                ItemLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LetterHolder(binding)
        }
    }
}