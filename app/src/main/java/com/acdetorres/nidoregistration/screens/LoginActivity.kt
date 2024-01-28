package com.acdetorres.nidoregistration.screens

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.acdetorres.nidoregistration.ActivityMainViewModel
import com.acdetorres.nidoregistration.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    val viewModel by viewModels<ActivityMainViewModel>()

    val binding : ActivityLoginBinding? get() = mBinding!!

    var mBinding : ActivityLoginBinding? = null

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        fullScreen()

        binding?.run {
            tvLogin.setOnClickListener {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }


    }


    private fun fullScreen() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.also {
                it.hide(WindowInsets.Type.statusBars())
                it.hide(WindowInsets.Type.navigationBars())
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}