package com.preidiot.triviastarter

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.preidiot.triviastarter.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var countDownTimer: CountDownTimer
    private var count = 0
    private var score = 0
    var timeLeftOnTimer = 0
    private lateinit var binding: FragmentResultBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Result"

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_result,container,false)

        binding.reaction.setImageResource(R.drawable.calculating)
        val args = ResultFragmentArgs.fromBundle(arguments!!)
        timer(2000,500)
        score = args.score
        binding.score.text = getString(R.string.score,score)

        binding.playAgain.setOnClickListener{
            view?.findNavController()?.navigate(ResultFragmentDirections.actionResultFragmentToTitleFragment())
        }


        return binding.root
    }

    private fun dot(): String {
        count++
        return when (count) {
            1 -> "."
            2 -> ".."
            3 -> "..."
            else -> "...."
        }
    }

    private fun reaction() {

        binding.reaction.setImageResource(when (score) {
            in 9..10 -> R.drawable.awesome
            in 6..8 -> R.drawable.good
            in 4..5 -> R.drawable.sad
            else -> R.drawable.sadextreme
        })
    }
    private fun timer(time: Long, countDownInterval: Long = 1000) {
        countDownTimer = object : CountDownTimer(time, countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = (millisUntilFinished / 1000).toInt()
                binding.Loading.text = getString(R.string.calculating_the_result,dot())
            }

            override fun onFinish() {
                reaction()
                binding.apply {
                    Loading.visibility = View.INVISIBLE
                    playAgain.visibility = View.VISIBLE
                    reaction.visibility = View.VISIBLE
                    score.visibility = View.VISIBLE
                    invalidateAll()
                }
            }
        }.start()
    }


}