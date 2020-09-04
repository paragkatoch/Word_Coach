package com.preidiot.triviastarter

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.preidiot.triviastarter.databinding.FragmentGameBinding


class GameFragment : Fragment() {

    data class Question(
        val text: String,
        val answers: List<String>
    )

    private val question: MutableList<Question> = mutableListOf(
        Question(
            text = "Which word is the opposite of bury?",
            answers = listOf("Reveal", "Review")
        ),
        Question(text = "Which word is similar to corpse?", answers = listOf("Body", "Melody")),
        Question(text = "Which word is opposite of urban?", answers = listOf("Rural", "Moral")),
        Question(text = "Which word is similar to accident", answers = listOf("mishap", "mistake")),
        Question(text = "Which word is similar to competition?", answers = listOf("Rivalry", "Surgery")),
        Question(text = "Which word is similar to inflate?", answers = listOf("Increase", "Improvise")),
        Question(text = "Which word is similar to gift?", answers = listOf("Present", "Content")),
        Question(text = "Which word is similar to invalidate?", answers = listOf("Disprove", "Dissuade")),
        Question(text = "Which word is similar to hurl?", answers = listOf("Throw", "Thin")),
        Question(text = "Which word is similar to spearhead?", answers = listOf("Lead", "Lengthen")),
        Question(text = "Which word is similar to sketch?", answers = listOf("Draw", "Dust")),
        Question(text = "Which word is similar to imply?", answers = listOf("Insinuate", "Incorporate")),
        Question(text = "Which word is similar to domination?", answers = listOf("Rule", "Rubble")),
        Question(text = "Which word is similar to firearm?", answers = listOf("Gun", "Gunman")),
        Question(text = "Which word is similar to articulate?", answers = listOf("Express", "Progress")),
    )

    var timeLeftOnTimer = 0
    private lateinit var mp: MediaPlayer
    private lateinit var binding: FragmentGameBinding
    private var questionIndex = -1
    private var score = 0
    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_game,
            container, false
        )

        binding.option1.setOnClickListener { onButtonClicked(it) }
        binding.option2.setOnClickListener { onButtonClicked(it) }
        binding.game = this

        randomizeQuestions()

        return binding.root
    }

    private fun randomizeQuestions() {
        question.shuffle()
        questionIndex = -1
        setQuestion()
    }

    private fun setQuestion() {
        questionIndex++
        currentQuestion = question[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()
        timer(10000)
        (activity as AppCompatActivity).supportActionBar?.title = "Question. ${questionIndex+1}"
    }


    private fun onButtonClicked(view: View) {
        check()
        when (view.id) {
            R.id.option1 -> if (answers[0] == currentQuestion.answers[0])
                score++
            else -> if (answers[1] == currentQuestion.answers[0])
                score++
        }

    }

    private fun check() {
        binding.apply {
            if (answers[0] == currentQuestion.answers[0]) {
                option1.setBackgroundColor(Color.GREEN)
                option2.setBackgroundColor(Color.RED)
            } else {
                option1.setBackgroundColor(Color.RED)
                option2.setBackgroundColor(Color.GREEN)
            }
            option1.isClickable = false
            option2.isClickable = false
            countDownTimer.cancel()
            timer(2000, show = false)
        }
    }


    private fun timer(time: Long, countDownInterval: Long = 1000, show: Boolean = true) {
        countDownTimer = object : CountDownTimer(time, countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = (millisUntilFinished / 1000).toInt()
                if (show) {
                    binding.time.text = getString(R.string.time, timeLeftOnTimer)
                    if (timeLeftOnTimer >= 1) {
                        mp = MediaPlayer.create(context, R.raw.ring)
                        mp.start()
                    } else if (timeLeftOnTimer < 1) {
                        mp = MediaPlayer.create(context, R.raw.end)
                        mp.start()
                    }
                }
            }

            override fun onFinish() {

                when {
                    questionIndex == 9 -> {
                        Log.i("Score", "$score")
                        view?.findNavController()?.navigate(
                            GameFragmentDirections.actionGameFragmentToResultFragment(score)
                        )
                    }
                    time == 5000L -> {
                        check()
                    }
                    else -> {
                        binding.apply {
                            option1.setBackgroundColor(Color.WHITE)
                            option1.isClickable = true
                            option2.setBackgroundColor(Color.WHITE)
                            option2.isClickable = true
                            invalidateAll()
                        }
                        setQuestion()
                    }
                }
            }
        }.start()
    }

}
