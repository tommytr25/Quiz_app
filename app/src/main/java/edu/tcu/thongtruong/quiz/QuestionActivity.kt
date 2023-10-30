package edu.tcu.thongtruong.quiz
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import edu.tcu.bmei.quiz.Constants
import edu.tcu.bmei.quiz.Question

class QuestionActivity : AppCompatActivity(), View.OnClickListener {
    private var answerCheck = false
    private var correctCount = 0
    private val optionTvs: MutableList<TextView?> = mutableListOf()
    private var questionId = 0
    private val questions: List<Question> = Constants.getQuestions().shuffled()
    private var selectedOptionId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.max = questions.size
        val submitButton = findViewById<Button>(R.id.submit_btn)
        submitButton.setOnClickListener(this)
        setQuestion()
    }

    private fun setQuestion() {
        selectedOptionId = -1
        answerCheck = false
        val question = questions[questionId]

        val questionTv = findViewById<TextView>(R.id.question_tv)
        questionTv.text = question.question

        val flag = findViewById<ImageView>(R.id.flag_iv)
        flag.setImageResource(question.image)

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.progress = questionId + 1

        val progressTv = findViewById<TextView>(R.id.progress_tv)
        ((questionId + 1).toString() +"/" +(questions.size).toString()).also { progressTv.text = it }

        setOptionTvs(question)

        val submitButton = findViewById<Button>(R.id.submit_btn)
        "Submit".also { submitButton.text = it }

    }

    private fun setOptionTvs(question: Question) {
        val optionLl = findViewById<View>(R.id.option_ll) as LinearLayout
        optionLl.removeAllViews()
        optionTvs.clear()
        for (option in question.options.shuffled()) {
            val optionTv = TextView(this)
            optionTv.setOnClickListener(this)
            optionTv.typeface = Typeface.DEFAULT
            optionTvs.add(optionTv)
            val layoutParams = LinearLayout.LayoutParams(-1, -2)
            layoutParams.topMargin = dpToPx(10)
            optionTv.layoutParams = layoutParams
            optionTv.setTextColor(
                ContextCompat.getColor(this, R.color.black))
            optionTv.setBackgroundResource(R.drawable.default_option_bg)
            optionTv.gravity = 17
            optionTv.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10))
            optionTv.textSize = 16.0f
            optionTv.text = option
            optionLl.addView(optionTv)
        }
    }

    private fun selectedOptionView(selectedOptionTv: TextView?) {
        for (optionTv in optionTvs) {
            optionTv!!.setTextColor(ContextCompat.getColor(this, R.color.black))
            optionTv.typeface = Typeface.DEFAULT
            optionTv.setBackgroundResource(R.drawable.default_option_bg)
        }
        selectedOptionTv!!.setTextColor(ContextCompat.getColor(this, R.color.black))
        selectedOptionTv.typeface = Typeface.DEFAULT_BOLD
        selectedOptionTv.setBackgroundResource(R.drawable.selected_option_bg)
    }


    private fun answerView(correctOptionTv: TextView?) {
        optionTvs[selectedOptionId]!!.setBackgroundResource(R.drawable.wrong_option_bg)
        correctOptionTv!!.setTextColor(ContextCompat.getColor(this, R.color.black))
        correctOptionTv.typeface = Typeface.DEFAULT_BOLD
        correctOptionTv.setBackgroundResource(R.drawable.correct_option_bg)
        for (optionTv in optionTvs) {
            optionTv!!.setOnClickListener(null as View.OnClickListener?)
        }
        val submitButton = findViewById<Button>(R.id.submit_btn)
        if (questionId < questions.size - 1) {
            submitButton.text = getString(R.string.next_question_button)
        } else {
            submitButton.text = getString(R.string.finish_button)        }
    }

    private fun goToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        println("aaa " + getIntent().getStringExtra("username"))
        intent.putExtra("username", getIntent().getStringExtra("username"))
        intent.putExtra("correct answers", correctCount)
        startActivity(intent)
        finish()
    }


    private fun dpToPx(dp: Int): Int {
        return ((dp.toFloat() * resources.displayMetrics.density).toDouble() + 0.5).toInt()
    }

    override fun onClick(view: View) {
        if (view != findViewById(R.id.submit_btn)) {
            for (optionTv in optionTvs) {
                if (view == optionTv) {
                    selectedOptionId = optionTvs.indexOf(optionTv)
                    selectedOptionView(optionTv)
                }
            }
        } else if (!answerCheck) {
            if (selectedOptionId == -1) {
                Toast.makeText(this, "Please make a selection.", Toast.LENGTH_SHORT).show()
                return
            }
            for (optionTv2 in optionTvs) {
                if (optionTv2?.text == questions[questionId].correctAnswer) {
                    answerView(optionTv2)
                    if (selectedOptionId == optionTvs.indexOf(optionTv2)) {
                        correctCount++
                    }
                    answerCheck = true
                    return
                }
            }
        }
        else if (questionId < questions.size - 1) {
        questionId++
        setQuestion()
        }
        else {
            goToResult()
        }
    }
}
