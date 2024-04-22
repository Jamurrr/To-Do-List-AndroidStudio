package com.example.lab4

import Task
import TaskAdapter
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val tasks = ArrayList<Task>()
    private lateinit var adapter: TaskAdapter
    private lateinit var selectedTask : Task
    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        tasks.add(Task("Название дела 1", "Краткое описание 1", LocalDate(2024, 12,2 ), false))
//        tasks.add(Task("Название дела 2", "Краткое описание 2", LocalDate(2024, 11, 3), false))
//        tasks.add(Task("Название дела 3", "Краткое описание 3", Date(2024, 10, 1), false))
//        tasks.add(Task("Название дела 4", "Краткое описание 4", Date(2024, 1 ,5), false))
        tasks.sortWith(compareBy({ it.completed }, { it.date }))
        val listview = findViewById<ListView>(R.id.listview)
        adapter = TaskAdapter(this, tasks)
        listview.adapter = adapter
        val addButton = findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST)
        }

        listview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            selectedTask = tasks[position]
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("task", selectedTask)
            startActivityForResult(intent, DETAILS_COMPLETE_REQUEST)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK) {
            val newTask = data?.getParcelableExtra<Task>("newTask")
            Log.i("newtask = ", "${newTask}")
            newTask?.let {
                tasks.add(it)
                Log.i("My tag", "tasks = $tasks")
                tasks.sortWith(compareBy({ it.completed }, { it.date }))
                adapter.notifyDataSetChanged()
            }
        }

        if (requestCode == DETAILS_COMPLETE_REQUEST && resultCode == RESULT_OK) {
            val taskCompleted = data?.getBooleanExtra("taskCompleted", false)
            val editedTask = data?.getParcelableExtra<Task>("editedTask")
            val deletedTask = data?.getParcelableExtra<Task>("deleteTask")

            if (editedTask != null) {
                val index = tasks.indexOf(selectedTask)
                tasks[index] = editedTask
                tasks.sortWith(compareBy({ it.completed }, { it.date }))
                adapter.notifyDataSetChanged()

            } else if (deletedTask != null) {
                Log.i("My tag", "deletedtask = $deletedTask")
                tasks.remove(deletedTask)
                Log.i("My tag", "tasks = $tasks")
                tasks.sortWith(compareBy({ it.completed }, { it.date }))
                adapter.notifyDataSetChanged()

            }

            if (taskCompleted == true) {
                selectedTask.completed = true
                tasks.sortWith(compareBy({ it.completed }, { it.date }))
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val ADD_TASK_REQUEST = 1
        const val DETAILS_COMPLETE_REQUEST = 1
    }
}