package com.example.lab4

import Task
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailsActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val title = findViewById<TextView>(R.id.title)
        val description = findViewById<TextView>(R.id.description)
        val date = findViewById<TextView>(R.id.date)
        val completeBtn = findViewById<AppCompatButton>(R.id.completeButton)
        val editBtn = findViewById<AppCompatButton>(R.id.editButton)
        val task = intent.getParcelableExtra<Task>("task")


        val dateFormatInput = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        val dateFormatOutput = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateFormatted = dateFormatOutput.format(dateFormatInput.parse(task?.date.toString()))


//        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
//        val parsed_date: LocalDate = LocalDate.parse(task?.date.toString(), dateFormat)

        title.text = task?.title
        description.text = task?.description
        date.text =  dateFormatted.toString()

        completeBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Поздравляем! Задача выполнена").setCancelable(false).setPositiveButton("OK") {
                    dialog, _ ->
                val intent = Intent()
                intent.putExtra("taskCompleted", true)
                setResult(Activity.RESULT_OK, intent)
                finish()
                dialog.dismiss()
            }
            val alert = dialogBuilder.create()
            alert.show()
        }

        editBtn.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("task", task) // Передача выбранного объекта Task в новую активность
            startActivityForResult(intent, EDIT_TASK_REQUEST)
        }


        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_TASK_REQUEST && resultCode == Activity.RESULT_OK) {
            val editedTask = data?.getParcelableExtra<Task>("editedTask")
            val deletedTask = data?.getParcelableExtra<Task>("deleteTask")

            if (editedTask != null) {
                val resultIntent = Intent()
                resultIntent.putExtra("editedTask", editedTask)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else if (deletedTask != null) {
                val resultIntent = Intent()
                resultIntent.putExtra("deleteTask", deletedTask)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
                }
            }
        }

        companion object {
            const val EDIT_TASK_REQUEST = 1
            const val DETAILS_COMPLETE_REQUEST = 1

        }
    }



