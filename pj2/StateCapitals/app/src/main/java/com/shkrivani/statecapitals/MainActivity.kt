package com.shkrivani.statecapitals


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.shkrivani.statecapitals.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CapitalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initial info
        displayRandomCapital()

        // fetch new random state & capital
        binding.nextButton.setOnClickListener {
            displayRandomCapital()
        }
    }

    // display a random capital using the ViewModel
    private fun displayRandomCapital() {
        binding.capitalInfo.text = viewModel.getRandomCapitalInfo()
    }
}
