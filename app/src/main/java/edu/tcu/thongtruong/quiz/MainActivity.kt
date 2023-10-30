package edu.tcu.thongtruong.quiz

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameEt = findViewById<TextInputEditText>(R.id.name_et)
        nameEt.setOnEditorActionListener{_,actionID,_ ->
             if(actionID == EditorInfo.IME_ACTION_GO){
                 gotoQuestion(nameEt)
                //Move on to next activity
                 true
        }
             else false
        }

        val startButton = findViewById<Button>(R.id.start_btn)
        startButton.setOnClickListener {
            gotoQuestion(nameEt)
        }

    }

    private fun gotoQuestion(nameEt:TextInputEditText){
        if (nameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this,QuestionActivity::class.java)
        intent.putExtra("username",nameEt.text.toString())
        startActivity(intent)
        finish()
    }

}