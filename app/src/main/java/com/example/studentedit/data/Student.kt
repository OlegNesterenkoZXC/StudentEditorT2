package com.example.studentedit.data

data class Student(
    var id: Int,
    var surname: String,
    var name: String,
    var patronymic: String,
    var group: String,
    var course: Int){
    override fun toString(): String {
        return "Фамилия: $surname\nИмя: $name\nОтчество: $patronymic\nГруппа: $group\nКурс: $course"
    }
}