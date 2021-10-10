package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel(){
    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    private var _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlatAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        _score.value = finalScore
        _eventPlayAgain.value = false
    }

    fun playAgain(){
        _eventPlayAgain.value = true
    }

    fun onPlayAgainComplete(){
        _eventPlayAgain.value = false
    }
}