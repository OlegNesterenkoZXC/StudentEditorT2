package com.example.studentedit.models

import androidx.lifecycle.ViewModel
import com.example.studentedit.data.MySequence
import com.example.studentedit.data.Student

class ActivityMainModel: ViewModel() {
    private var idSequence = MySequence()
    private var studentList: MutableList<Student> = mutableListOf()

    init {
        addStudent(Student(
            idSequence.nextVal(),
            "Даль",
            "Владмир",
            "Иванович",
            "12",
            1
        ))
        addStudent(Student(
            idSequence.nextVal(),
            "Клюев",
            "Борис",
            "Николаевич",
            "3ИТ",
            3
        ))
    }
    var currentIndex: Int = 0

    fun getStudent(index: Int): Student {
        return studentList[index]
    }

    private fun fixIndex() {
        if (currentIndex !in 0..<studentList.size) {
            currentIndex = studentList.size - 1
        }
    }

    fun getStudent(): Student {
        if(studentList.size == 0) {
            return Student(nextStudentId(), "", "", "", "", 0)
        }
        fixIndex()
        return studentList[currentIndex]
    }

    fun addStudent(student: Student) {
        student.id = nextStudentId()
        studentList.add(student)
        currentIndex = studentList.size - 1
    }

    fun moveToNext() {
        fixIndex()
        if(studentList.size != 0) {
            currentIndex = (currentIndex + 1) % studentList.size
        }
    }

    fun moveToPrev() {
        fixIndex()
        if(studentList.size != 0) {
            currentIndex = (studentList.size + currentIndex - 1) % studentList.size
        }
    }
    fun remove() {
        fixIndex()
        if(studentList.size != 0) {
            studentList.removeAt(currentIndex)
        }
    }
    fun size(): Int {
        return studentList.size
    }
    private fun nextStudentId(): Int {
        return idSequence.nextVal()
    }
}