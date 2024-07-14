package com.acdetorres.nidoregistration.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.acdetorres.nidoregistration.ActivityMainViewModel
import com.acdetorres.nidoregistration.databinding.ActivityLoginBinding
import com.acdetorres.nidoregistration.string
import dagger.hilt.android.AndroidEntryPoint
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    val viewModel by viewModels<ActivityMainViewModel>()

    val binding : ActivityLoginBinding? get() = mBinding

    var mBinding : ActivityLoginBinding? = null

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getLoggedOnAmbassador()

        viewModel.getProvinces()

        viewModel.loggedOnAmbassador.observe(this@LoginActivity) { loggedOnAmbassador ->
            if (loggedOnAmbassador != null) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                mBinding = ActivityLoginBinding.inflate(layoutInflater)
                setContentView(binding?.root)
                fullScreen()
            }

        }


        viewModel.getAmbassadors()

        viewModel.getLocalAmbassadors()

        viewModel.getLoggedOnAmbassador()



//
//            viewModel.loggedOnAmbassador.observe(this@LoginActivity) { loggedOnAmbassador ->
//                if (loggedOnAmbassador != null) {
//                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                    finish()
//                }
//
//            }

            viewModel.ambassador.observe(this@LoginActivity) { ambassadors ->


                binding?.run {
                    tvLogin.setOnClickListener {
                        if (etEmail.string().isEmpty() || etPassword.string().isEmpty()) {
                            Toast.makeText(
                                this@LoginActivity,
                                "You must fill all fields.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        }
                        Log.d("CLICK", ambassadors.toString())
                        if (!ambassadors.isNullOrEmpty()) {

                            Log.d("ambassador filter", ambassadors.toString())


                            val bcrypt = BCryptPasswordEncoder()
//                        val isPasswordMatches: Boolean = bcrypt.matches(
//                            etPassword.string(),
//                            encryptedPasswordFromDb
//                        )
                            val loginResult = ambassadors.filter {
                                it.email == etEmail.string() && bcrypt.matches(
                                    etPassword.string(),
                                    it.password
                                )
                            }

                            Log.d("ambassador filter", ambassadors.toString())

                            if (loginResult.isNotEmpty()) {

                                viewModel.insertLoggedOnAmbassador(loginResult.first())

                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Wrong credentials",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "You need to connect first to the internet and restart app to download credentials.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }


    }

    override fun onResume() {
        super.onResume()

        fullScreen()
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