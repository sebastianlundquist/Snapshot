package com.sebastianlundquist.snapshot

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnKeyListener {

    internal var signUpModeActive = true
    lateinit var loginText: TextView
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var logoView: ImageView
    lateinit var backgroundLayout: ConstraintLayout
    lateinit var signUpButton: Button
    val mAuth = FirebaseAuth.getInstance()

    fun signUpOrLogin(view: View) {
        if (!emailInput.text.toString().matches("".toRegex()) && !passwordInput.text.toString().matches("".toRegex())) {
            if (signUpModeActive) {
                mAuth.createUserWithEmailAndPassword(emailInput.text.toString(), passwordInput.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                login()
                            } else {
                                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
            } else {
                mAuth.signInWithEmailAndPassword(emailInput.text.toString(), passwordInput.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                login()
                            } else {
                                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        } else {
            Toast.makeText(this, "Username and password are required.", Toast.LENGTH_SHORT).show()
        }
    }

    fun login() {
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(R.string.login_title)
        loginText = findViewById(R.id.loginText)
        loginText.setOnClickListener(this)
        emailInput = findViewById(R.id.userInput)
        passwordInput = findViewById(R.id.passwordInput)
        logoView = findViewById(R.id.logoView)
        backgroundLayout = findViewById(R.id.backgroundLayout)
        logoView.setOnClickListener(this)
        backgroundLayout.setOnClickListener(this)
        passwordInput.setOnKeyListener(this)

        if (mAuth.currentUser != null) {

        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.loginText) {
            signUpButton = findViewById(R.id.signUpButton)
            if (signUpModeActive) {
                signUpModeActive = false
                signUpButton.setText(R.string.login)
                loginText.setText(R.string.or_sign_up)
            } else {
                signUpModeActive = true
                signUpButton.setText(R.string.sign_up)
                loginText.setText(R.string.or_login)
            }
        } else if (view.id == R.id.logoView || view.id == R.id.backgroundLayout) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun onKey(view: View, i: Int, keyEvent: KeyEvent): Boolean {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
            signUpOrLogin(view)
        }
        return false
    }
}
