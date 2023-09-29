package com.example.ui.home.tabs.tasksList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ItemTaskBinding
import com.example.todo.model.Task

class TasksAdapter(var tasks: MutableList<Task>?) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemTaskBinding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(itemTaskBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks!![position])
        if (onItemDeleteListener != null) {
            holder.itemTaskBinding.deleteImg
                .setOnClickListener {
                    holder.itemTaskBinding.swipeLayout.close(true)
                    onItemDeleteListener?.onItemClick(position, tasks!![position])
                }
        }
    }

    override fun getItemCount(): Int = tasks?.size ?: 0
    fun bindTasks(tasks: MutableList<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    fun taskDeleted(task: Task) {
        val pos = tasks?.indexOf(task)
        tasks?.remove(task)
        notifyItemRemoved(pos!!)
    }

    class ViewHolder(val itemTaskBinding: ItemTaskBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {
        fun bind(task: Task) {
            itemTaskBinding.title.text = task.title
            itemTaskBinding.description.text = task.description
        }
    }

    var onItemDeleteListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, task: Task)
    }
}