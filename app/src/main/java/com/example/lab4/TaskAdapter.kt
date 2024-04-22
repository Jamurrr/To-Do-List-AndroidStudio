import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.lab4.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskAdapter(context: Context, tasks: List<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.task_item_layout, parent, false)
        }

        val currentTask = getItem(position)

        val titleTextView = listItemView!!.findViewById<TextView>(R.id.titleTextView)
//        titleTextView.text = currentTask?.title

        val descriptionTextView = listItemView.findViewById<TextView>(R.id.descriptionTextView)
//        descriptionTextView.text = currentTask?.description

        // Обрезаем название до 15 символов
        val abbreviatedTitle = abbreviateText(currentTask?.title.toString(), 15)
        titleTextView.text = abbreviatedTitle

        // Обрезаем описание до 70 символов
        val abbreviatedDescription = abbreviateText(currentTask?.description.toString(), 70)
        descriptionTextView.text = abbreviatedDescription


        val dateFormatInput = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        val dateFormatOutput = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateFormatted = dateFormatOutput.format(dateFormatInput.parse(currentTask?.date.toString()))

        val dateTextView = listItemView.findViewById<TextView>(R.id.dateTextView)
        dateTextView.text = dateFormatted

        val currentDate = Calendar.getInstance()
        val taskDate = dateFormatInput.parse(currentTask?.date.toString())
        if (taskDate != null && taskDate.before(currentDate.time)) {
            // Дата прошедшая, применяем красный цвет
            dateTextView.setTextColor(ContextCompat.getColor(context, R.color.red))
        } else {
            // Дата не прошедшая, применяем обычный цвет
            dateTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        if (currentTask?.completed == true) {
            titleTextView.paintFlags = titleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            descriptionTextView.paintFlags = descriptionTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            dateTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
            listItemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
        } else {
            titleTextView.paintFlags = titleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            descriptionTextView.paintFlags = descriptionTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            listItemView.setBackgroundColor(Color.TRANSPARENT)
        }

        return listItemView
    }

    private fun abbreviateText(text: String, maxLength: Int): String {
        if (text.length > maxLength) {
            return text?.substring(0, maxLength - 3) + "..." // добавляем троеточие
        }
        return text
    }
}
