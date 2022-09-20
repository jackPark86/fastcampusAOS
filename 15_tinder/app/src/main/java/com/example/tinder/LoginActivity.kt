package com.example.tinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        loginButton.isEnabled = false
        signUpButton.isEnabled = false

        initLoginButton()
        initSignUpButton()
        initEmailAndPasswordEditText()
    }


    private fun initLoginButton() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val email = getInputEmail()
            val passWord = getInputPassword()

            auth.signInWithEmailAndPassword(email, passWord).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    handleSuccessLogin()
                } else {
                    Toast.makeText(this, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

    private fun initSignUpButton() {
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            Toast.makeText(
                this,
                "initSignUpButton",
                Toast.LENGTH_SHORT
            ).show()
            val email = getInputEmail()
            val passWord = getInputPassword()

            auth.createUserWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(this) { task ->
                    Toast.makeText(this, "addOnCompleteListener enter!!", Toast.LENGTH_SHORT).show()
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "회원가입에 성공했습니다. 로그인 버튼을 눌러 로그인해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.e("TAG", task.exception.toString())
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun initEmailAndPasswordEditText() {

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        emailEditText.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            loginButton.isEnabled = enable
            signUpButton.isEnabled = enable
        }

        passwordEditText.addTextChangedListener {
            val enable = emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()
            loginButton.isEnabled = enable
            signUpButton.isEnabled = enable
        }
    }

    private fun getInputEmail(): String {
        return findViewById<EditText>(R.id.emailEditText).text.toString()
    }

    private fun getInputPassword(): String {
        return findViewById<EditText>(R.id.passwordEditText).text.toString()
    }


    private fun handleSuccessLogin() {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = auth.currentUser?.uid.orEmpty() //uid가 null일 경우 empty로 변경해 준다!
        val currentUserDB = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        currentUserDB.updateChildren(user)
        finish()
    }
}