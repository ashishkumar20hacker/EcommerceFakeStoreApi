package com.example.e_commercefreestoreapi.UI.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commercefreestoreapi.UI.ConnectionChecker
import com.example.e_commercefreestoreapi.DataSource.Constants.loginToken
import com.example.e_commercefreestoreapi.DataSource.SharePreferences
import com.example.e_commercefreestoreapi.R
import com.example.e_commercefreestoreapi.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    lateinit var preferences: SharePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SharePreferences(this)
    }

    override fun onResume() {
        super.onResume()
        checkNetworkContinuously()
    }

    private fun checkNetworkContinuously() {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (ConnectionChecker.isNetworkAvailable(this@SplashActivity)) {
                    nextActivity()
                } else {
                    binding.messageTv.text = getString(R.string.please_turn_on_the_internet)
                    // Check again after a delay
                    handler.postDelayed(this, 1000) // Check every second
                }
            }
        }, 1000) // Start checking after 1 second
    }

    private fun nextActivity() {
        Handler().postDelayed({
            if (preferences.getString(loginToken)?.isEmpty()!!) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                finish()
            }
        }, 1500)
    }

}