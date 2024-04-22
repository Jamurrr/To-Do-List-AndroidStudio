package com.example.lab4

import Task
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val addButton = findViewById<AppCompatButton>(R.id.addTaskButton)

        addButton.setOnClickListener {
            val title = findViewById<EditText>(R.id.titleEditText).text.toString()
            val description = findViewById<EditText>(R.id.descriptionEditText).text.toString()
            val date = findViewById<EditText>(R.id.dateEditText).text.toString()

            if (title.isNullOrEmpty() || date.isNullOrEmpty()) {
                val snackbar = Snackbar.make(it, "Введите обязательные поля", Snackbar.LENGTH_SHORT)
                snackbar.setAnchorView(addButton)
                snackbar.show()
            }
            else {
                val new_date: LocalDate = LocalDate.parse(date, dateFormat)
                val newTask = Task(title, description, new_date, false)
                val intent = Intent()
                intent.putExtra("newTask", newTask)
                setResult(RESULT_OK, intent)
                finish()
            }
        }



        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    fun showDatePickerDialog(view: View) {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        DatePickerDialog(
            this@AddTaskActivity,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy" // Формат даты
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        findViewById<EditText>(R.id.dateEditText).setText(sdf.format(calendar.time))
    }
}

