package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    var showSplash by remember { mutableStateOf(true) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var restartQuiz by remember { mutableStateOf(false) }
    var quizViewModel by remember { mutableStateOf(QuizViewModel()) }

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
            QuizScreen(quizViewModel, onRetry = { restartQuiz = true }, onExit = { isLoggedIn = false })
        }
        else -> QuizScreen(quizViewModel, onRetry = { restartQuiz = true }, onExit = { isLoggedIn = false })
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color.Blue, Color.Cyan))),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Welcome to QuizApp!", style = MaterialTheme.typography.headlineLarge, color = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf(TextFieldValue()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF6A11CB)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Enter your Uid to continue",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    label = { Text("Uid") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color(0xFF6A11CB),
                        unfocusedIndicatorColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { if (email.text.isNotEmpty()) onLoginSuccess() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, shape = RoundedCornerShape(50)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A11CB))
                ) {
                    Text(text = "Login", color = Color.White)
                }
            }
        }
    }
}


@Composable
fun QuizScreen(viewModel: QuizViewModel, onRetry: () -> Unit, onExit: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onExit) {
                Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "Exit", tint = Color.Red)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (viewModel.isQuizFinished) {
            ScoreScreen(viewModel.score, onRetry)
        } else {
            val question = viewModel.getCurrentQuestion()
            question?.let {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Green)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = it.text, style = MaterialTheme.typography.headlineMedium, color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                it.options.forEachIndexed { index, answer ->
                    Button(
                        onClick = { viewModel.answerQuestion(index) },
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ) {
                        Text(text = answer, color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Score: ${viewModel.score}", style = MaterialTheme.typography.bodyLarge, color = Color.Magenta)
        }
    }
}

@Composable
fun ScoreScreen(score: Int, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).background(Brush.verticalGradient(colors = listOf(Color.Cyan, Color.Blue))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Quiz Completed!", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Your Score: $score", style = MaterialTheme.typography.headlineLarge, color = Color.Yellow)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)) {
            Text(text = "Retry", color = Color.White)
        }
    }
}

class QuizViewModel : ViewModel() {
    private val questions = listOf(
        Question("What is the capital of India?", listOf("Patna", "Delhi", "UP", "Bihar"), 1),
        Question("What is 2 + 2?", listOf("3", "4", "5", "6"), 1),
        Question("Who wrote 'Hamlet'?", listOf("Shakespeare", "Keats", "Wordsworth", "Milton"), 0),
        Question("What is the largest planet in our solar system?", listOf("Earth", "Jupiter", "Mars", "Saturn"), 1),
        Question("Which element has the chemical symbol 'O'?", listOf("Oxygen", "Gold", "Osmium", "Iron"), 0),
        Question("What is the square root of 64?", listOf("6", "7", "8", "9"), 2),
        Question("How many continents are there on Earth?", listOf("5", "6", "7", "8"), 2),
        Question("Which is the longest river in the world?", listOf("Amazon", "Nile", "Ganges", "Yangtze"), 1),
        Question("Who painted the Mona Lisa?", listOf("Picasso", "Van Gogh", "Da Vinci", "Rembrandt"), 2),
        Question("What is the boiling point of water in Celsius?", listOf("50°C", "75°C", "100°C", "150°C"), 2)
    )
    private var currentQuestionIndex by mutableIntStateOf(0)
    var score by mutableIntStateOf(0)
        private set
    var isQuizFinished by mutableStateOf(false)
        private set

    fun getCurrentQuestion(): Question? = questions.getOrNull(currentQuestionIndex)

    fun answerQuestion(selectedIndex: Int) {
        getCurrentQuestion()?.let { question ->
            if (selectedIndex == question.correctAnswer) {
                score++
            }
        }
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
        } else {
            isQuizFinished = true
        }
    }
}


data class Question(val text: String, val options: List<String>, val correctAnswer: Int)