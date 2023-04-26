package com.example.firebasechat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.databinding.MessageItemBinding

class MessagesAdapter: ListAdapter<Message, MessagesAdapter.MessagesHolder>(ItemComparator()) {
    class MessagesHolder(private val binding: MessageItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) = with(binding) {
            currentMessageText.text = message.message
            userName.text = message.userName
        }
        companion object {
            fun create(parent: ViewGroup): MessagesHolder {
                return MessagesHolder(MessageItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesHolder {
        return MessagesHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MessagesHolder, position: Int) {
        holder.bind(getItem(position))
    }
}