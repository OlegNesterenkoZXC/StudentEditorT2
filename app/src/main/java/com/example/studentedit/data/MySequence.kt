package com.example.studentedit.data

class MySequence {
    private var value: Int = Int.MIN_VALUE

    fun nextVal(): Int {
        return value++
    }
}
