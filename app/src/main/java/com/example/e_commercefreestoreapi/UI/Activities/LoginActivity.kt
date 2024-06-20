package com.example.e_commercefreestoreapi.UI.Activities

import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.e_commercefreestoreapi.DataSource.Constants.loginToken
import com.example.e_commercefreestoreapi.DataSource.SharePreferences
import com.example.e_commercefreestoreapi.ViewModel.LoginViewModel
import com.example.e_commercefreestoreapi.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var preferences: SharePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = SharePreferences(this)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.loginBt.setOnClickListener {
            if (!checkEtsAreEmpty()) {
                loginViewModel.loginUser(
                    binding.usernameEt.text.toString().trim(),
                    binding.passwordEt.text.toString().trim()
                )
                {
                    runOnUiThread {
                        if (it.isNotEmpty()) {
                            preferences.putString(loginToken, it)
                            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

    private fun checkEtsAreEmpty(): Boolean {
        if (binding.usernameEt.text.toString().trim().isEmpty()) {
            binding.usernameEt.setError("Please Enter UserName")
            binding.usernameEt.requestFocus()
            return true
        }

        if (binding.passwordEt.text.toString().trim().isEmpty()) {
            binding.passwordEt.setError("Please Enter Password")
            binding.passwordEt.requestFocus()
            return true
        }

        return false
    }
}