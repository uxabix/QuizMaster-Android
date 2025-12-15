package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class PromptActivity : ComponentActivity() {
    private var correctAnswer: Boolean = false
    private lateinit var showCorrectAmswerButton: Button
    private lateinit var showCorrectAnswerTextView: TextView

    companion object {
        const val EXTRA_ANSWER_SHOWN = "com.example.myapplication.answer_shown"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prompt)
        correctAnswer = intent.getBooleanExtra("correctAnswer", false)

        showCorrectAmswerButton = findViewById(R.id.button)
        showCorrectAnswerTextView = findViewById(R.id.answer_text_view)
        showCorrectAmswerButton.setOnClickListener {
            val answerText = if (correctAnswer) R.string.button_true else R.string.button_false
            showCorrectAnswerTextView.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        setResult(RESULT_OK, resultIntent)
    }
}