package com.example.studentedit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_IS_ADD = "com.example.studentedit.is_add"
const val EXTRA_CURRENT_INDEX = "com.example.studentedit.current_index"
const val EXTRA_NEW_SURNAME = "com.example.studentedit.new_surname"
const val EXTRA_NEW_NAME = "com.example.studentedit.new_name"
const val EXTRA_NEW_PATRONYMIC = "com.example.studentedit.new_patronymic"
const val EXTRA_NEW_GROUP = "com.example.studentedit.new_group"
const val EXTRA_NEW_COURSE = "com.example.studentedit.new_course"
const val BAD_INDEX: Int = -1

class EditActivity : AppCompatActivity() {
    private lateinit var editSurname: EditText
    private lateinit var editName: EditText
    private lateinit var editPatronymic: EditText
    private lateinit var editGroup: EditText
    private lateinit var editCourse: EditText
    private lateinit var btnSave: Button
    private val shortToast = { message: String ->
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_SHORT
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val currentIndex = intent?.getIntExtra(EXTRA_CURRENT_INDEX, BAD_INDEX)?:BAD_INDEX
        val isAdd = intent?.getBooleanExtra(EXTRA_IS_ADD, false)?:false

        val inputSurname = intent?.getStringExtra(EXTRA_NEW_SURNAME)?: ""
        val inputName = intent?.getStringExtra(EXTRA_NEW_NAME)?: ""
        val inputPatronymic = intent?.getStringExtra(EXTRA_NEW_PATRONYMIC)?: ""
        val inputGroup = intent?.getStringExtra(EXTRA_NEW_GROUP)?: ""
        val inputCourse = intent?.getIntExtra(EXTRA_NEW_COURSE, BAD_INDEX)?: BAD_INDEX

        editSurname = findViewById(R.id.editSurname)
        editName = findViewById(R.id.editName)
        editPatronymic = findViewById(R.id.editPatronymic)
        editGroup = findViewById(R.id.editGroup)
        editCourse = findViewById(R.id.editCourse)
        btnSave = findViewById(R.id.btnSave)

        val emptyCheck = {isFocused: Boolean, et: EditText ->
            if(!isFocused && et.text?.isBlank() != false) {
                et.error = "Заполните поле!"
            }
        }

        val editTextList: MutableList<EditText> = mutableListOf(
            editSurname, editName, editPatronymic, editGroup, editCourse
        )

        for(item in editTextList) {
            item.setOnFocusChangeListener{_, isFocused -> emptyCheck(isFocused, item)}
        }

//        editSurname.setOnFocusChangeListener{_, isFocused -> emptyCheck(isFocused, editSurname)}
//        editName.setOnFocusChangeListener{_, isFocused -> emptyCheck(isFocused, editName)}
//        editPatronymic.setOnFocusChangeListener{_, isFocused -> emptyCheck(isFocused, editPatronymic)}
//        editGroup.setOnFocusChangeListener{_, isFocused -> emptyCheck(isFocused, editGroup)}
//        editCourse.setOnFocusChangeListener{_, isFocused -> emptyCheck(isFocused, editCourse)}

        editSurname.setText(inputSurname)
        editName.setText(inputName)
        editPatronymic.setText(inputPatronymic)
        editGroup.setText(inputGroup)
        if(inputCourse == BAD_INDEX) {
            editCourse.setText("")
        } else {
            editCourse.setText(inputCourse.toString())
        }

        val fieldError = shortToast("Заполните все поля!")
        val successSave = shortToast("Сохранено!")
        btnSave.setOnClickListener{
            btnSave.requestFocus()
            if(hasEmptyFiled(editTextList)) {
                fieldError.show()
                return@setOnClickListener
            }
            val data = Intent().apply {
                putExtra(EXTRA_IS_ADD, isAdd)
                putExtra(EXTRA_CURRENT_INDEX, currentIndex)
                putExtra(EXTRA_NEW_SURNAME, editSurname.text.toString())
                putExtra(EXTRA_NEW_NAME, editName.text.toString())
                putExtra(EXTRA_NEW_PATRONYMIC, editPatronymic.text.toString())
                putExtra(EXTRA_NEW_GROUP, editGroup.text.toString())
                putExtra(EXTRA_NEW_COURSE, editCourse.text.toString().toInt())
            }
            setResult(Activity.RESULT_OK, data)
            successSave.show()
        }
    }

    private fun hasEmptyFiled(list: MutableList<EditText> ): Boolean {
        var flag = false
        for (item: EditText in list) {
            if(item.text!!.isBlank()) {
                item.error = "Зполните поле!"

                if(!flag) {
                    flag = true
                }
            }
        }
        return flag
    }

    companion object {
        fun newIntent(packageContext: Context, isAdd: Boolean) : Intent {
            return Intent(packageContext, EditActivity::class.java).apply {
                putExtra(EXTRA_IS_ADD, isAdd)
            }
        }
        fun newIntent(
            packageContext: Context,
            index: Int,
            surname: String,
            name: String,
            patronymic: String,
            group: String,
            course: Int
        ): Intent {
            return Intent(packageContext, EditActivity::class.java).apply {
                putExtra(EXTRA_CURRENT_INDEX, index)
                putExtra(EXTRA_NEW_SURNAME, surname)
                putExtra(EXTRA_NEW_NAME, name)
                putExtra(EXTRA_NEW_PATRONYMIC, patronymic)
                putExtra(EXTRA_NEW_GROUP, group)
                putExtra(EXTRA_NEW_COURSE, course)
            }
        }
    }
}