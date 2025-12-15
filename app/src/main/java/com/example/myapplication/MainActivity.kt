package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var hintButton: Button
    private var correctCount = 0
    private var answerWasShown = false
    private var currentQuestionAnswered = false

    companion object {
        private const val KEY_CURRENT_INDEX = "currentIndex"
        private const val KEY_CORRECT_COUNT = "correctCount"
        private const val KEY_ANSWERED = "answered"
        private const val KEY_ANSWER_WAS_SHOWN = "answerWasShown"
    }

    private val questions: Array<Question> = arrayOf(
        Question(R.string.question_1, false),
        Question(R.string.question_2, true),
        Question(R.string.question_3, false),
        Question(R.string.question_4, true),
        Question(R.string.question_5, true)
    )

    private var currentIndex = 0

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            answerWasShown = it.data?.getBooleanExtra(PromptActivity.EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("Events", "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Events", "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Events", "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Events", "onStop() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("Events", "onSaveInstanceState() called")
        outState.putInt(KEY_CURRENT_INDEX, currentIndex)
        outState.putInt(KEY_CORRECT_COUNT, correctCount)
        outState.putBoolean(KEY_ANSWERED, currentQuestionAnswered)
        outState.putBoolean(KEY_ANSWER_WAS_SHOWN, answerWasShown)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Events", "onDestroy() called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Events", "onCreate(Bundle?) called")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX)
            correctCount = savedInstanceState.getInt(KEY_CORRECT_COUNT)
            currentQuestionAnswered = savedInstanceState.getBoolean(KEY_ANSWERED)
            answerWasShown = savedInstanceState.getBoolean(KEY_ANSWER_WAS_SHOWN)
        }

        setupViews()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        updateQuestion()
    }

    private fun setupViews() {
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        hintButton = findViewById(R.id.buttonHint)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            if (currentIndex == questions.size - 1) {
                val scoreText = getString(R.string.score_text, correctCount, questions.size)
                Toast.makeText(this, scoreText, Toast.LENGTH_LONG).show()
                correctCount = 0
                currentIndex = 0
            } else {
                currentIndex++
            }
            answerWasShown = false
            currentQuestionAnswered = false
            updateQuestion()
        }

        hintButton.setOnClickListener {
            val intent = Intent(this, PromptActivity::class.java)
            intent.putExtra("correctAnswer", questions[currentIndex].answerTrue)
            getResult.launch(intent)
        }
    }

    private fun updateQuestion() {
        val questionText = questions[currentIndex].textResId
        questionTextView.setText(questionText)

        trueButton.isEnabled = !currentQuestionAnswered
        falseButton.isEnabled = !currentQuestionAnswered
    }

    private fun checkAnswer(userAnswer: Boolean) {
        currentQuestionAnswered = true
        val correctAnswer = questions[currentIndex].answerTrue
        val messageResId = when {
            answerWasShown -> R.string.answer_was_shown
            userAnswer == correctAnswer -> {
                correctCount++
                R.string.correct
            }
            else -> R.string.incorrect
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        updateQuestion()
    }
}
