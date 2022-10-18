package com.example.barrera_miranda

import android.app.Activity
import android.content.Intent
import android.graphics.Color.green
import android.graphics.Color.red
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
//import com.bignerdranch.android.barrera_miranda.databinding.ActivityMainBinding
//import com.bignerdranch.android.Barrera_Miranda.databinding.ActivityMainBinding
import com.example.barrera_miranda.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    //private lateinit var trueButton: Button
    //private lateinit var falseButton: Button
    private val cheatLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == Activity.RESULT_OK){
            quizViewModel.isCheater= result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?:false
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "OnCreate(Bundle?) called")
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizModelView: $quizViewModel: $quizViewModel")


        //trueButton = findViewById(R.id.true_button)
        //falseButton = findViewById(R.id.false_button)

      binding.trueButton.setOnClickListener{view: View ->
          checkAnswer(true,view)
      }

      binding.falseButton.setOnClickListener{view: View->
          checkAnswer(false,view)
      }

        binding.nextButton.setOnClickListener{
            //currentIndex = (currentIndex +1)%questionBank.size
            //val questionTextResId = questionBank[currentIndex].textResId
            //binding.questionTextView.setText(questionTextResId)
            quizViewModel.moveToNext()
            updateQuestion()
        }
        binding.cheatButton.setOnClickListener {

            //Start cheat activity
            //val intent = Intent(this, CheatActivity::class.java)
            val answerIsTrue= quizViewModel.currentQuestionAnswer
            val intent= CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            //startActivity(intent)
            cheatLauncher.launch(intent)
        }

        //val questionTextResId = questionBank[currentIndex].textResId
        //binding.questionTextView.setText(questionTextResId)
        updateQuestion()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, " onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, " onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d (TAG," onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, " onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion(){
        //val questionTextResId = questionBank[currentIndex].textResId
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)

    }

    private fun checkAnswer (userAnswer: Boolean, view:View){
        //val correctAnswer = questionBank[currentIndex].answer
        val correctAnswer = quizViewModel.currentQuestionAnswer


        val messageResId = when{
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else-> R.string.incorrect_toast
        }
        

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

    }


}