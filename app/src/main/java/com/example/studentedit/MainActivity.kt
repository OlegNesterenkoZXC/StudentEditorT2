package com.example.studentedit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.studentedit.data.Student
import com.example.studentedit.models.ActivityMainModel

class MainActivity : AppCompatActivity() {
    private lateinit var btnNext: ImageButton
    private lateinit var btnPrev: ImageButton
    private lateinit var btnAdd: Button
    private lateinit var btnEdit: Button
    private lateinit var btnRemove: Button
    private lateinit var output: TextView

    private val shortToast = { message: String ->
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_SHORT
        )
    }

    private val viewModel: ActivityMainModel by lazy {
        val provider = ViewModelProvider(this)
        provider[ActivityMainModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrev)
        btnAdd = findViewById(R.id.btnAdd)
        btnEdit = findViewById(R.id.btnEdit)
        btnRemove = findViewById(R.id.btnRemove)

        output = findViewById(R.id.textView)
        updateStudent()

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if(result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    val ansIsAdd = data?.getBooleanExtra(EXTRA_IS_ADD, false)?: false
                    val ansCurIndex = data?.getIntExtra(EXTRA_CURRENT_INDEX, BAD_INDEX)?: BAD_INDEX

                    val ansSurname = data?.getStringExtra(EXTRA_NEW_SURNAME)?: ""
                    val ansName = data?.getStringExtra(EXTRA_NEW_NAME)?: ""
                    val ansPatronymic = data?.getStringExtra(EXTRA_NEW_PATRONYMIC)?: ""
                    val ansGroup = data?.getStringExtra(EXTRA_NEW_GROUP)?: ""
                    val ansCourse = data?.getIntExtra(EXTRA_NEW_COURSE, BAD_INDEX)?: BAD_INDEX

                    if(!ansIsAdd && ansCurIndex == BAD_INDEX) {
                        return@registerForActivityResult
                    }

                    if(ansIsAdd) {
                        viewModel.addStudent(Student(
                            0,
                            ansSurname,
                            ansName,
                            ansPatronymic,
                            ansGroup,
                            ansCourse
                        ))
                        updateStudent()
                    } else {
                        val student: Student = viewModel.getStudent(ansCurIndex)
                        student.surname = ansSurname
                        student.name = ansName
                        student.patronymic = ansPatronymic
                        student.group = ansGroup
                        student.course = ansCourse

                        updateStudent()
                    }
                }
        }

        btnNext.setOnClickListener { viewModel.moveToNext(); updateStudent() }
        btnPrev.setOnClickListener { viewModel.moveToPrev(); updateStudent() }

        btnAdd.setOnClickListener{
            val intent = EditActivity.newIntent(
                this@MainActivity,
                true
            )
            resultLauncher.launch(intent)
        }

        val editError = shortToast("Нечего редактировать!")
        btnEdit.setOnClickListener{
            if (viewModel.size() == 0) {
                editError.show()
                return@setOnClickListener
            }

            val student = viewModel.getStudent()
            val intent = EditActivity.newIntent(
                this@MainActivity,
                viewModel.currentIndex,
                student.surname,
                student.name,
                student.patronymic,
                student.group,
                student.course
            )
            resultLauncher.launch(intent)
        }

        val removeError = shortToast("Нечего удалять!")
        btnRemove.setOnClickListener{
            if(viewModel.size() == 0) {
                removeError.show()
            } else {
                viewModel.remove()
            }
            updateStudent()
        }

    }

    private fun updateStudent() {
        output.text = if(viewModel.size() == 0) {
            "Нет студентов"
        } else {
            viewModel.getStudent().toString()
        }
    }
}