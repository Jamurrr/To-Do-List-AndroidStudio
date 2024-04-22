package com.example.lab4

import Task
import android.app.Activity
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class EditActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit2)

        var editedTask = intent.getParcelableExtra<Task>("task")
        val applyButton = findViewById<AppCompatButton>(R.id.applyBtn)
        val deleteButton = findViewById<AppCompatButton>(R.id.deleteBtn)
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val dateEditText = findViewById<EditText>(R.id.dateEditText)
        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        titleEditText.setText(editedTask?.title.toString())
        descriptionEditText.setText(editedTask?.description.toString())

        val dateFormatInput = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        val dateFormatOutput = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateFormatted = dateFormatOutput.format(dateFormatInput.parse(editedTask?.date.toString()))
        dateEditText.setText(dateFormatted.toString())

        applyButton.setOnClickListener {

            if (titleEditText.text.isNullOrEmpty() || dateEditText.text.isNullOrEmpty()) {
                val snackbar = Snackbar.make(it, "Введите обязательные поля", Snackbar.LENGTH_SHORT)
                snackbar.setAnchorView(applyButton)
                snackbar.show()
            }
            else {
                editedTask?.title = titleEditText.text.toString()
                editedTask?.description = descriptionEditText.text.toString()
                val new_date: LocalDate = LocalDate.parse(dateEditText.text.toString(), dateFormat)
                editedTask?.date = new_date
                // Обновление выбранного объекта Task в списке
                val intent = Intent()
                intent.putExtra("editedTask", editedTask)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        deleteButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("deleteTask", editedTask)
            setResult(Activity.RESULT_OK, intent)
            finish()
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
            this@EditActivity,
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