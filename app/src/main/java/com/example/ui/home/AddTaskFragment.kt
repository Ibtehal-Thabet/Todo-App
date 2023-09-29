package com.example.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.MyDataBase
import com.example.todo.databinding.FragmentAddTaskBinding
import com.example.todo.model.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class AddTaskFragment : BottomSheetDialogFragment() {
    lateinit var viewBinding: FragmentAddTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.addTaskButton.setOnClickListener {
            createTask()
        }
        viewBinding.timeContainer.setOnClickListener {
            showDatePickerDialog()
        }
    }

    val calender = Calendar.getInstance()
    private fun showDatePickerDialog() {
        context?.let {
            val dialog = DatePickerDialog(it)
            dialog.setOnDateSetListener { datePicker, year, month, day ->
                viewBinding.time.setText(
                    "$year-${month + 1}-$day"
                )
                calender.set(year, month, day)
                //to ignore time
                calender.set(Calendar.HOUR_OF_DAY, 0)
                calender.set(Calendar.MINUTE, 0)
                calender.set(Calendar.SECOND, 0)
                calender.set(Calendar.MILLISECOND, 0)
            }
            dialog.show()
        }
    }

    private fun createTask() {
        if (!valid()) {
            return
        }
        val task = Task(
            title = viewBinding.title.text.toString(),
            description = viewBinding.description.text.toString(),
            dateTime = calender.timeInMillis
        )
        MyDataBase.getInstance(requireContext())
            .tasksDao()
            .insertTask(task)
        onTaskAddedListener?.onTaskAdded()
        dismiss()
    }

    private fun valid(): Boolean {
        var isValid = true
        if (viewBinding.title.text.toString().isNullOrBlank()) {
            viewBinding.titleContainer.error = "Please enter title"
            isValid = false
        } else {
            viewBinding.titleContainer.error = null
        }
        if (viewBinding.description.text.toString().isNullOrBlank()) {
            viewBinding.descriptionContainer.error = "Please enter description"
            isValid = false
        } else {
            viewBinding.descriptionContainer.error = null
        }
        if (viewBinding.time.text.toString().isNullOrBlank()) {
            viewBinding.timeContainer.error = "Please choose date"
            isValid = false
        } else {
            viewBinding.timeContainer.error = null
        }
        return isValid
    }

    var onTaskAddedListener: OnTaskAddedListener? = null

    fun interface OnTaskAddedListener {
        fun onTaskAdded()
    }
}