package com.example.flashcard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AddCardActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val editTextField = findViewById<EditText>(R.id.editTextField)
        val editTextField1 = findViewById<EditText>(R.id.editTextField1)
        val showingAnswers = findViewById<ImageView>(R.id.icone_X)
        val saveAnswers = findViewById<ImageView>(R.id.icone_save)

        showingAnswers.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val question = intent.getStringExtra("question")
        val answer = intent.getStringExtra("answer")

        // Mettre à jour les EditText avec les données existantes
        editTextField.setText(question)
        editTextField1.setText(answer)


        saveAnswers.setOnClickListener {
            val question = editTextField.text.toString()
            val answer = editTextField1.text.toString()

            val intent = Intent()
            intent.putExtra("question", question)
            intent.putExtra("answer", answer)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
