package com.shkrivani.statecapitals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlin.random.Random

class CapitalViewModel(application: Application) : AndroidViewModel(application) {

    // from strings.xml
    private val capitalList: List<Capital> = loadStateList()

    // state capital data from strings.xml into objects
    private fun loadStateList(): List<Capital> {
        val rawDataArray = getApplication<Application>().resources.getStringArray(R.array.states)
        return rawDataArray.map { data ->
            val (state, capital) = data.split(",")
            Capital(state.trim(), capital.trim())
        }
    }

    // format capital info string
    private fun formatCapitalInfo(capital: Capital): String {
        return "${capital.capitalCity} is the capital of ${capital.state}"
    }

    // random state fnc
    fun getRandomCapitalInfo(): String {
        val randomCapital = capitalList[Random.nextInt(capitalList.size)]
        return formatCapitalInfo(randomCapital)
    }
}