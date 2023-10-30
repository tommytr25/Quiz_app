package edu.tcu.thongtruong.quiz
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import edu.tcu.bmei.quiz.Constants
import java.math.BigDecimal
import java.math.RoundingMode

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val imageView = findViewById<ImageView>(R.id.result_iv) 
        val messageTv = findViewById<TextView>(R.id.result_message_tv) 
        val scoreTv= findViewById<TextView>(R.id.result_score_tv) 
        val username = intent.getStringExtra("username")
        val correctCount = intent.getIntExtra("correct answers", 0)
        val totalQuestions = Constants.getQuestions().size
        val percentCorrect =
            BigDecimal(correctCount).divide(BigDecimal(totalQuestions), RoundingMode.HALF_EVEN)
                ?: throw NullPointerException("Null result")

        if (percentCorrect.toDouble() >= 0.7) {
            imageView.setImageResource(R.drawable.ic_trophy)
            val message = getString(R.string.good_job_message, username.toString())
            messageTv.text = message

        } else {
            imageView.setImageResource(R.drawable.ic_sweat_face)
            val message = getString(R.string.good_luck_message, username.toString())
            messageTv.text = message
        }
        val scoreMessage = getString(R.string.score_message, correctCount, totalQuestions)
        scoreTv.text = scoreMessage

        val restartButton = findViewById<Button>(R.id.restart_btn)
        restartButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}