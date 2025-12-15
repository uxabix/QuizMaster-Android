package com.example.myapplication

import androidx.annotation.StringRes

data class Question(
    @StringRes val textResId: Int,
    val answerTrue: Boolean
)
