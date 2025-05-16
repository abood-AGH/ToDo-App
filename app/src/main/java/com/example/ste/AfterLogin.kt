package com.example.ste

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AfterLoginActivity : AppCompatActivity() {

    // المكان اللي بنضيف فيه المهام الجديدة
    private lateinit var taskContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.afterlogin)
        val addTaskButton: ImageButton = findViewById(R.id.addTaskButton)
        taskContainer = findViewById(R.id.tasksContainer)


        addTaskButton.setOnClickListener {
            showAddTaskDialog()
        }

        loadTasks()
    }

    // لفتح نافذة حوار فيها إدخال اسم المهمة + التاريخ + الوقت
    private fun showAddTaskDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialogaddtask, null)

        val taskNameInput = dialogView.findViewById<EditText>(R.id.taskNameInput)
        val dateButton = dialogView.findViewById<Button>(R.id.dateButton)
        val timeButton = dialogView.findViewById<Button>(R.id.timeButton)

        var selectedDate = ""
        var selectedTime = ""


        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    selectedDate = "$d/${m + 1}/$y"
                    dateButton.text = "Date: $selectedDate"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { _, h, m ->
                selectedTime = String.format("%02d:%02d", h, m)
                timeButton.text = "Time: $selectedTime"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }

        // بناء نافذة الحوار
        AlertDialog.Builder(this)
            .setTitle("Add New Task")
            .setView(dialogView)
            .setPositiveButton("Create") { _, _ ->
                val name = taskNameInput.text.toString().trim()
                if (name.isNotEmpty() && selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                    val fullText = "$name\n$selectedDate at $selectedTime"
                    addTaskToList(fullText)
                    saveTask(fullText)
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // لعرض المهمة على الشاشة
    private fun addTaskToList(taskText: String) {
        val taskLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(25, 25, 25, 25)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            setBackgroundColor(Color.parseColor("#FFF3E0"))
        }

        val checkBox = CheckBox(this).apply {
            text = taskText
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            setTextColor(Color.DKGRAY)
        }

        val deleteButton = ImageButton(this).apply {
            setImageResource(R.drawable.trash)
            layoutParams = LinearLayout.LayoutParams(80, 100).apply {
                setMargins(16, 0, 0, 0)
            }
            setBackgroundColor(Color.TRANSPARENT)
            scaleType = ImageView.ScaleType.FIT_XY
            setOnClickListener {
                taskContainer.removeView(taskLayout)
                deleteTask(taskText)
            }
        }

        taskLayout.addView(checkBox)
        taskLayout.addView(deleteButton)
        taskContainer.addView(taskLayout)
    }

    //  SharedPreferences
    private fun saveTask(task: String) {
        val prefs = getSharedPreferences("tasks_prefs", MODE_PRIVATE)
        val tasks = prefs.getStringSet("tasks", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        tasks.add(task)
        prefs.edit().putStringSet("tasks", tasks).apply()
    }

    private fun loadTasks() {
        val prefs = getSharedPreferences("tasks_prefs", MODE_PRIVATE)
        val tasks = prefs.getStringSet("tasks", setOf()) ?: setOf()
        for (task in tasks) {
            addTaskToList(task)
        }
    }

    private fun deleteTask(task: String) {
        val prefs = getSharedPreferences("tasks_prefs", MODE_PRIVATE)
        val tasks = prefs.getStringSet("tasks", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        tasks.remove(task)
        prefs.edit().putStringSet("tasks", tasks).apply()
    }
}
