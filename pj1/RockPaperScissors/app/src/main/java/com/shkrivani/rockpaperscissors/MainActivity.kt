package com.shkrivani.rockpaperscissors

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var playerScoreText: TextView
    private lateinit var computerScoreText: TextView
    private lateinit var resultMessage: TextView
    private lateinit var playerSelectionImage: ImageView
    private lateinit var computerSelectionImage: ImageView
    private lateinit var resetButton: Button

    private var playerScore = 0
    private var computerScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        playerScoreText = findViewById(R.id.playerScore)
        computerScoreText = findViewById(R.id.computerScore)
        resultMessage = findViewById(R.id.resultMessage)
        playerSelectionImage = findViewById(R.id.playerSelection)
        computerSelectionImage = findViewById(R.id.computerSelection)
        resetButton = findViewById(R.id.resetButton)

        val rockButton: Button = findViewById(R.id.rockButton)
        val paperButton: Button = findViewById(R.id.paperButton)
        val scissorsButton: Button = findViewById(R.id.scissorsButton)

        // Button listeners
        rockButton.setOnClickListener { playRound("rock") }
        paperButton.setOnClickListener { playRound("paper") }
        scissorsButton.setOnClickListener { playRound("scissors") }

        resetButton.setOnClickListener { resetGame() }
    }

    private fun playRound(playerChoice: String) {
        val choices = arrayOf("rock", "paper", "scissors")
        val computerChoice = choices[Random.nextInt(choices.size)]

        updateImages(playerChoice, computerChoice)

        when {
            playerChoice == computerChoice -> {
                resultMessage.text = "It's a Tie!"
            }
            (playerChoice == "rock" && computerChoice == "scissors") ||
                    (playerChoice == "scissors" && computerChoice == "paper") ||
                    (playerChoice == "paper" && computerChoice == "rock") -> {
                playerScore++
                resultMessage.text = "Player Wins This Round!"
            }
            else -> {
                computerScore++
                resultMessage.text = "Computer Wins This Round!"
            }
        }

        updateScores()

        if (playerScore == 10 || computerScore == 10) {
            declareWinner()
        }
    }

    private fun updateImages(playerChoice: String, computerChoice: String) {
        val playerImage = when (playerChoice) {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            "scissors" -> R.drawable.scissors
            else -> R.drawable.ic_launcher_background
        }
        val computerImage = when (computerChoice) {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            "scissors" -> R.drawable.scissors
            else -> R.drawable.ic_launcher_background
        }

        playerSelectionImage.setImageResource(playerImage)
        computerSelectionImage.setImageResource(computerImage)
    }

    private fun updateScores() {
        playerScoreText.text = "Player Score: $playerScore"
        computerScoreText.text = "Computer Score: $computerScore"
    }

    private fun declareWinner() {
        resultMessage.text = if (playerScore == 10) "Player Wins the Game!" else "Computer Wins the Game!"
        findViewById<Button>(R.id.rockButton).isEnabled = false
        findViewById<Button>(R.id.paperButton).isEnabled = false
        findViewById<Button>(R.id.scissorsButton).isEnabled = false
        resetButton.visibility = View.VISIBLE
    }

    private fun resetGame() {
        playerScore = 0
        computerScore = 0
        updateScores()
        resultMessage.text = "No Winner Yet"
        resetButton.visibility = View.GONE
        findViewById<Button>(R.id.rockButton).isEnabled = true
        findViewById<Button>(R.id.paperButton).isEnabled = true
        findViewById<Button>(R.id.scissorsButton).isEnabled = true
    }
}