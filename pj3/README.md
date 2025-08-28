# Project 3 ₿ — Cryptocurrency Prices App  

This project is part of **CIS 2818 – Android Development** and is implemented in **Kotlin**.  
It is a mobile app that fetches and displays **real-time cryptocurrency data** using the CoinCap REST API.  


## 📌 Features  
- Fetches live data from the **CoinCap API** (`https://api.coincap.io/v2/assets`).  
- Displays information for **100+ cryptocurrencies**.  
- User selects a cryptocurrency from a **dropdown menu (Spinner)**.  
- Shows:  
  - Name of the cryptocurrency  
  - Symbol (e.g., BTC, ETH, USDT)  
  - Supply (rounded to the nearest integer)  
  - Price in USD (2 decimal places)  
  - Percent change in the last 24 hours (2 decimal places)  
- Automatically updates display when a new crypto is selected.  


## 🛠️ Technologies & Concepts  
- **Kotlin** for Android development  
- **Volley** HTTP library for API requests  
- **JSON parsing** to extract crypto data  
- **Spinner (dropdown menu)** for selection  
- **TextViews** for formatted display  
- **Fragments** for UI organization  
