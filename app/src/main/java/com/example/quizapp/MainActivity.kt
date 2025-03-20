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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    var showSplash by remember { mutableStateOf(true) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var restartQuiz by remember { mutableStateOf(false) }
    var quizViewModel: QuizViewModel? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        delay(2000)
        showSplash = false
    }

    when {
        showSplash -> SplashScreen()
        !isLoggedIn -> LoginScreen { isLoggedIn = true }
        restartQuiz -> {
            restartQuiz = false
            quizViewModel = QuizViewModel()
            QuizScreen(quizViewModel!!, onRetry = { restartQuiz = true })
        }
        else -> {
            if (quizViewModel == null) quizViewModel = QuizViewModel()
            QuizScreen(quizViewModel!!, onRetry = { restartQuiz = true })
        }
    }
}

@Composable
fun SplashScreen() {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(1000))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Welcome to QuizApp!", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Enter your Email", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { if (email.text.isNotEmpty()) onLoginSuccess() }) {
            Text(text = "Login")
        }
    }
}

// Quiz Screen
@Composable
fun QuizScreen(viewModel: QuizViewModel, onRetry: () -> Unit = {}) {
    if (viewModel.isQuizFinished) {
        ScoreScreen(viewModel.score, onRetry)
    } else {
        val question = viewModel.currentQuestion
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            if (question != null) {
                Text(text = question.text, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                question.options.forEachIndexed { index, answer ->
                    Button(onClick = { viewModel.answerQuestion(index) }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = answer)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Score: ${viewModel.score}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

// Quiz ViewModel
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

// Data Model
data class Question(val text: String, val options: List<String>, val correctAnswer: Int)

@Composable
fun ScoreScreen(score: Int, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Quiz Completed!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Your Score: $score", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}
