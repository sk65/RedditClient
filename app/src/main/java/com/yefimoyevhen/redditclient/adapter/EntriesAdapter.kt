package com.yefimoyevhen.redditclient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.yefimoyevhen.redditclient.databinding.ItemEntryPreviewBinding
import com.yefimoyevhen.redditclient.model.Entry
import javax.inject.Inject

class EntriesAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<EntriesAdapter.EntryViewHolder>() {


    private val differCallback = object : DiffUtil.ItemCallback<Entry>() {
        override fun areItemsTheSame(oldItem: Entry, newItem: Entry): Boolean {
            return oldItem.entryLink == newItem.entryLink
        }

        override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder =
        EntryViewHolder(
            ItemEntryPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )


    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = differ.currentList[position]
        holder.binding.apply {
            tvAuthor.text = entry.author
            tvSubreddit.text = entry.subreddit
            tvNumberOfComments.text = entry.numberOfComments
            tvPostTime.text = entry.postDate
            tvRate.text = entry.rate
            tvTitle.text = entry.title
            tvNumberOfComments.text = entry.numberOfComments
            root.setOnClickListener {
                onItemClickListener?.let { it(entry.entryLink) }
            }
            glide.load(entry.thumbnailUrl).into(ivArticleImage)
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    inner class EntryViewHolder(val binding: ItemEntryPreviewBinding) :
        RecyclerView.ViewHolder(binding.root)

}