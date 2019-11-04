package com.ngenge.apps.biometricauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val biometricManager = BiometricManager.from(this)
        val biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Authenticate to continue")
            .setDeviceCredentialAllowed(true)
            .build()

        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            biometricPromptInstance().authenticate(biometricPromptInfo)

        } else {
            showMessage("No biometric device found")
            return
        }


    }


    fun showMessage(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()

    }

    fun biometricPromptInstance(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)
        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                showMessage("Authentication error: $errString")
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationFailed() {
                showMessage("Authentication failed for an unknown reason")
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                showMessage("Authentication successful")
                val intent = Intent(this@MainActivity, SuccessActivity::class.java)
                startActivity(intent)
                finish()
                super.onAuthenticationSucceeded(result)
            }
        }

        return BiometricPrompt(this, executor, callback)
    }
}
