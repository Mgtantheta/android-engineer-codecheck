package jp.co.yumemi.android.code_check

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.databinding.LayoutItemBinding
import jp.co.yumemi.android.code_check.model.GitHubRepositoryItem

class SearchListAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<GitHubRepositoryItem, SearchListAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(
        private val binding: LayoutItemBinding,
        private val itemClickListener: OnItemClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GitHubRepositoryItem) {
            binding.repositoryNameView.text = item.name
            binding.root.setOnClickListener {
                itemClickListener.itemClick(item)
            }
        }
    }

    interface OnItemClickListener {
        fun itemClick(item: GitHubRepositoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<GitHubRepositoryItem>() {
    override fun areItemsTheSame(oldItem: GitHubRepositoryItem, newItem: GitHubRepositoryItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: GitHubRepositoryItem, newItem: GitHubRepositoryItem): Boolean {
        return oldItem == newItem
    }

}
