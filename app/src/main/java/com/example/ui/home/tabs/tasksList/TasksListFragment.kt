package com.example.ui.home.tabs.tasksList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.MyDataBase
import com.example.todo.databinding.FragmentTasksListBinding
import com.example.todo.model.Task
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.*

class TasksListFragment : Fragment() {
    lateinit var viewBinding: FragmentTasksListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentTasksListBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onStart() {
        super.onStart()
        loadTasks()
    }

    fun loadTasks() {
        context?.let {
            val tasks = MyDataBase.getInstance(it)
                .tasksDao()
                .getTasksByDay(selectedDate.timeInMillis)
            adapter.bindTasks(tasks.toMutableList())
        }
    }

    private val adapter = TasksAdapter(null)
    val selectedDate = Calendar.getInstance()

    init {
        selectedDate.set(Calendar.HOUR, 0)
        selectedDate.set(Calendar.MINUTE, 0)
        selectedDate.set(Calendar.SECOND, 0)
        selectedDate.set(Calendar.MILLISECOND, 0)
    }

    private fun initViews() {
        viewBinding.recyclerView.adapter = adapter
        adapter.onItemDeleteListener = TasksAdapter.OnItemClickListener { position, task ->
            deleteTaskFromDatabase(task)
            adapter.taskDeleted(task)
        }
        viewBinding.calendarView.setSelectedDate(
            CalendarDay.today()
        )
        viewBinding.calendarView.setOnDateChangedListener(
            OnDateSelectedListener { widget, date, selected ->
                if (selected) {
                    selectedDate.set(Calendar.YEAR, date.year)
                    selectedDate.set(Calendar.MONTH, date.month - 1)
                    selectedDate.set(Calendar.DAY_OF_MONTH, date.day)
                    loadTasks()
                }
            })
    }

    private fun deleteTaskFromDatabase(task: Task) {
        MyDataBase.getInstance(requireContext())
            .tasksDao()
            .deleteTask(task)
    }

}