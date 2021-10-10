package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

enum class BuzzType(val pattern: LongArray) {
    CORRECT(CORRECT_BUZZ_PATTERN),
    GAME_OVER(GAME_OVER_BUZZ_PATTERN),
    COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
    NO_BUZZ(NO_BUZZ_PATTERN)
}

class GameViewModel : ViewModel(){

    companion object {
        private const val DONE = 0L
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME = 60000L
    }

    private val timer: CountDownTimer
    private var timerCount: Long

    // The current word
    private var _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    private var _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private var _timerText = MutableLiveData<String>()
    val timerText: LiveData<String>
        get() = _timerText

    private var _vibrator = MutableLiveData<LongArray>()
    val vibrator: LiveData<LongArray>
        get() = _vibrator

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Log.i("GameViewModel", "GameViewModel created!")
        resetList()
        nextWord()
        _score.value = 0
        _eventGameFinish.value = false
        timerCount = COUNTDOWN_TIME

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){

            override fun onTick(millisUntilFinished: Long) {
                timerCount = timerCount.minus(ONE_SECOND)
                _timerText.value = DateUtils.formatElapsedTime(timerCount.div(ONE_SECOND))
                if (timerCount <= ONE_SECOND.times(5)){
                    _vibrator.value = BuzzType.COUNTDOWN_PANIC.pattern
                }
                Log.i("GameViewModel", "onTick called!")
            }

            override fun onFinish() {
                _eventGameFinish.value = true
                _vibrator.value = BuzzType.GAME_OVER.pattern
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    fun onGameFinishComplete(){
        _eventGameFinish.value = false
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = _score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _vibrator.value = BuzzType.CORRECT.pattern
        _score.value = _score.value?.plus(1)
        nextWord()
    }


}