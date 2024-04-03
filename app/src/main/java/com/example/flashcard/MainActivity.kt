package com.example.flashcard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    var currentCardDisplayedIndex = 0
    lateinit var flashcardDatabase: FlashcardDatabase
    private var allFlashcards = mutableListOf<Flashcard>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flashcardDatabase = FlashcardDatabase(this)
        flashcardDatabase.initFirstCard()
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
        val isShowingAnswers = findViewById<ImageView>(R.id.toggle123)
        val flashcard_question = findViewById<TextView>(R.id.flashcard_question)
        val flashcard_reponse = findViewById<TextView>(R.id.flashcard_reponse)
        val NextButton = findViewById<ImageView>(R.id.arrow)


        NextButton.setOnClickListener {
            if (allFlashcards.isEmpty()) {
                return@setOnClickListener  // Il n'y a pas de cartes à afficher
            }

            currentCardDisplayedIndex++

            if (currentCardDisplayedIndex >= allFlashcards.size) {
                currentCardDisplayedIndex = 0  // Revenir à la première carte si nous avons atteint la fin
            }

            val (question, answer,wrongAnswer1,wrongAnswer2) = allFlashcards[currentCardDisplayedIndex]

            // Mettre à jour les TextViews avec la nouvelle carte
            flashcard_question.text = question
            flashcard_reponse.text = answer

        }
        flashcard_question.setOnClickListener {
            flashcard_question.visibility = View.INVISIBLE
            flashcard_reponse.visibility = View.VISIBLE
        }

        flashcard_reponse.setOnClickListener {
            flashcard_question.visibility = View.VISIBLE
            flashcard_reponse.visibility = View.INVISIBLE
        }
        isShowingAnswers.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }

    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data
        val extras = data?.extras

        if (extras != null) { // Check that we have data returned
            val question = extras.getString("question")
            val answer = extras.getString("answer")

            // Log the value of the strings for easier debugging
            Log.i("MainActivity", "question: $question")
            Log.i("MainActivity", "answer: $answer")

            // Display newly created flashcard
            findViewById<TextView>(R.id.flashcard_question).text = question
            findViewById<TextView>(R.id.flashcard_reponse).text = answer

            // Save newly created flashcard to database
            if (question != null && answer != null) {
                flashcardDatabase.insertCard(Flashcard(question, answer))
                // Update set of flashcards to include new card
                allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            } else {
                Log.e("TAG", "Missing question or answer to input into database. Question is $question and answer is $answer")
            }
        } else {
            Log.i("MainActivity", "Returned null data from AddCardActivity")
        }
    }
}
