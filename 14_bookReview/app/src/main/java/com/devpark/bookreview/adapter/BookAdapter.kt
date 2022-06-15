package com.devpark.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devpark.bookreview.databinding.ItemBookBinding
import com.devpark.bookreview.model.Book

class BookAdapter(private val itemClickedListener: (Book) -> Unit) : ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    inner class BookItemViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookModel: Book) {
            binding.titleTextView.text = bookModel.title
            binding.descriptionTextView.text = bookModel.description

            binding.root.setOnClickListener {
                itemClickedListener(bookModel)
            }
            Glide.with(binding.coverImageView.context).load(bookModel.coverSmallUrl).into(binding.coverImageView)
        }
    }

    /**
     * diffUtil은 RecyclerView가 뷰 포지션이 변경이 되었을 때 새로운 값을 할당할지 말지 결정해주는 diffUtil
     */

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {

            // oldItem과 newItem가 아이템이 같은지 비교
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            //oldItem과 newItem가  컨텐츠가 같은지 비교
            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }


}