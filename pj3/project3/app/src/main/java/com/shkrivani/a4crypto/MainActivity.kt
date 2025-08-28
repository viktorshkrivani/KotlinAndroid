package com.shkrivani.a4crypto

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    // Declare UI components
    private lateinit var cryptoSpinner: Spinner
    private lateinit var cryptoName: TextView
    private lateinit var cryptoSymbol: TextView
    private lateinit var cryptoPrice: TextView
    private lateinit var cryptoSupply: TextView
    private lateinit var cryptoChange: TextView
    private lateinit var cryptoRank: TextView
    private lateinit var cryptoLink: TextView
    private val cryptoList = mutableListOf<Crypto>() // Holds cryptocurrency data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        cryptoSpinner = findViewById(R.id.crypto_spinner)
        cryptoName = findViewById(R.id.crypto_name)
        cryptoSymbol = findViewById(R.id.crypto_symbol)
        cryptoPrice = findViewById(R.id.crypto_price)
        cryptoSupply = findViewById(R.id.crypto_supply)
        cryptoChange = findViewById(R.id.crypto_change)
        cryptoRank = findViewById(R.id.crypto_rank)
        cryptoLink = findViewById(R.id.crypto_link)

        // Fetch data from API
        fetchCryptoData()
    }

    // Fetch cryptocurrency data from API
    private fun fetchCryptoData() {
        val url = "https://api.coincap.io/v2/assets" // API URL
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val gson = Gson()
                val cryptoResponse = gson.fromJson(response.toString(), CryptoResponse::class.java)
                cryptoList.addAll(cryptoResponse.data)
                setupSpinner() // Set up dropdown menu with the data
            },
            { error ->
                // Display error message
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        requestQueue.add(jsonObjectRequest) // Add the request to the queue
    }

    // Set up dropdown menu (Spinner) with cryptocurrency names
    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            cryptoList.map { it.name } // Use names for dropdown options
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cryptoSpinner.adapter = adapter

        // Handle dropdown item selection
        cryptoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCrypto = cryptoList[position] // Get selected cryptocurrency
                displayCryptoDetails(selectedCrypto) // Update details display
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // Display details of the selected cryptocurrency
    private fun displayCryptoDetails(crypto: Crypto) {
        // Format price to 2 decimal places
        val formattedPrice = String.format("%.2f", crypto.priceUsd.toDoubleOrNull() ?: 0.0)
        // Format 24-hour change to 2 decimal places
        val formattedChange = String.format("%.2f", crypto.changePercent24Hr.toDoubleOrNull() ?: 0.0)

        // Format supply with suffixes (M for Million, B for Billion, T for Trillion)
        val supply = crypto.supply.toDoubleOrNull() ?: 0.0
        val formattedSupply = when {
            supply >= 1_000_000_000_000 -> String.format("%.2fT", supply / 1_000_000_000_000)
            supply >= 1_000_000_000 -> String.format("%.2fB", supply / 1_000_000_000)
            supply >= 1_000_000 -> String.format("%.2fM", supply / 1_000_000)
            else -> String.format("%.0f", supply)
        }

        // Update UI with cryptocurrency details
        cryptoRank.text = "Rank: #${crypto.rank}" // Rank of the cryptocurrency
        cryptoName.text = crypto.name // Name
        cryptoSymbol.text = "(${crypto.symbol})" // Symbol
        cryptoPrice.text = "$$formattedPrice" // Price in USD
        cryptoSupply.text = "Supply: $formattedSupply" // Formatted supply
        cryptoChange.text = "Change: $formattedChange%" // 24-hour percentage change
        cryptoLink.text = "Explorer: ${crypto.explorer}" // Link to explorer

        // Change color of 24-hour change based on value
        val changePercent = crypto.changePercent24Hr.toDoubleOrNull() ?: 0.0
        val color = if (changePercent >= 0) R.color.green else R.color.red
        cryptoChange.setTextColor(ContextCompat.getColor(this, color))
    }
}