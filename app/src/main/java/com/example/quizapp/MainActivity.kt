package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizapp.ui.theme.QuizAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                QuizScreen()
            }
        }
    }
}

// Data model
data class Question(val text: String, val options: List<String>, val correctAnswer: Int)

// ViewModel to manage quiz logic
class QuizViewModel : ViewModel() {
    private val _questions = listOf(
        Question("What is the capital of France?", listOf("Berlin", "Paris", "Madrid", "Rome"), 1),
        Question("What is 2 + 2?", listOf("3", "4", "5", "6"), 1),
        Question("Who developed Android?", listOf("Apple", "Google", "Microsoft", "IBM"), 1),
        Question("Which planet is known as the Red Planet?", listOf("Earth", "Mars", "Jupiter", "Venus"), 1),
        Question("What is the square root of 16?", listOf("2", "3", "4", "5"), 2)
    )
    var currentQuestionIndex by mutableStateOf(0)
        private set
    var score by mutableStateOf(0)
        private set
    var isQuizFinished by mutableStateOf(false)
        private set

    val currentQuestion: Question?
        get() = _questions.getOrNull(currentQuestionIndex)

    fun answerQuestion(selectedIndex: Int) {
        if (currentQuestion != null && selectedIndex == currentQuestion!!.correctAnswer) {
            score++
        }
        if (currentQuestionIndex < _questions.size - 1) {
            currentQuestionIndex++
        } else {
            isQuizFinished = true
        }
    }
}

@Composable
fun QuizScreen(viewModel: QuizViewModel = viewModel()) {
    if (viewModel.isQuizFinished) {
        ScoreScreen(viewModel.score)
    } else {
        val question = viewModel.currentQuestion
        var showQuestion by remember { mutableStateOf(true) }

        AnimatedVisibility(
            visible = showQuestion,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                if (question != null) {
                    Text(text = question.text, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    question.options.forEachIndexed { index, answer ->
                        Button(onClick = {
                            showQuestion = false
                            viewModel.answerQuestion(index)
                            showQuestion = true
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = answer)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Score: ${viewModel.score}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun ScoreScreen(score: Int) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(1000))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Quiz Completed!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Your Score: $score", style = MaterialTheme.typography.headlineLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    QuizAppTheme {
        QuizScreen()
    }
}