package com.example.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.databinding.ActivityHomeBinding
import com.example.ui.home.tabs.SettingsFragment
import com.example.ui.home.tabs.tasksList.TasksListFragment
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityHomeBinding
    var tasksListFragmentRef: TasksListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_tasks_list -> {
                    tasksListFragmentRef = TasksListFragment()
                    showFragment(tasksListFragmentRef!!)
                }
                R.id.navigation_setting -> {
                    showFragment(SettingsFragment())
                }
            }
            return@setOnItemSelectedListener true
        }

        viewBinding.addTaskBtn.setOnClickListener {
            showAddTaskBottomSheet()
        }

        viewBinding.bottomNav.selectedItemId = R.id.navigation_tasks_list
    }

    private fun showAddTaskBottomSheet() {
        val addTaskSheet = AddTaskFragment()
        addTaskSheet.onTaskAddedListener = AddTaskFragment.OnTaskAddedListener {
            Snackbar.make(viewBinding.root, "Task Added Successfully", Snackbar.LENGTH_LONG)
                .show()
            tasksListFragmentRef?.loadTasks()
        }
        addTaskSheet.show(supportFragmentManager, "")
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}