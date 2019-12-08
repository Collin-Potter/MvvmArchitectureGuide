package com.example.mvvmarchitectureguide.dashboardmodule.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitectureguide.data.user.User
import com.example.mvvmarchitectureguide.databinding.ListItemUserBinding
import kotlinx.android.synthetic.main.list_item_user.view.*

/**
 * Adapter for the [RecyclerView] in [DashboardFragment].
 */
class UserAdapter : ListAdapter<User, UserAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.apply {
            bind(createOnClickListener(user.hobbies), user)
            itemView.tag = user
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(hobbies: String): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(it.context, hobbies, Toast.LENGTH_SHORT).show()
        }
    }

    class ViewHolder(
        private val binding: ListItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener,item: User) {
            binding.apply {
                clickListener = listener
                user = item
                executePendingBindings()
            }
        }
    }
}

private class UserDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}