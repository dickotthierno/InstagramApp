package com.example.instagramapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // check if there's a user logged in
        // if there is, take them to mainActivity
        ParseUser.logOut()
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }
        // setting an onclicklistener for the login button
        findViewById<Button>(R.id.login_button).setOnClickListener {
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(username, password)
        }
        // setting an onclicklistener for the sign up button
        findViewById<Button>(R.id.signUpBtn).setOnClickListener {
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username, password)

        }
    }


        // method to signUp
        private fun signUpUser(username: String, password: String) {
            val user = ParseUser()

            // Set fields for the user to be created
            user.setUsername(username)
            user.setPassword(password)

            user.signUpInBackground { e ->
                if (e == null) {
                    // User successfully created an account
                    // navigating to mainActivity
                    goToMainActivity()
                    // showing a toast for successfully log in
                    Toast.makeText(this, "Successfully created an account", Toast.LENGTH_SHORT).show()


                } else {
                    // Sign up didn't succeed. Look at the ParseException

                    Toast.makeText(this, "Failed to created an account", Toast.LENGTH_SHORT).show()
                    // to figure out what went wrong
                    e.printStackTrace()
                }
            }
        }

        // method to login in with the backend  ( username and password )
        private fun loginUser(username: String, password: String) {

            ParseUser.logInInBackground(
                username, password, ({ user, e ->
                    if (user != null) {
                        // Hooray!  The user is logged in.
                        Log.i(TAG, "Successfully logged in user ")
                        goToMainActivity()
                    } else {
                        // Signup failed.  Look at the ParseException to see what happened.
                        e.printStackTrace()
                        Toast.makeText(this, "Error login in ", Toast.LENGTH_SHORT).show()
                    }
                })
            )

        }
        // method to log out


        // method to navigate to mainActivity when successfully logged in

        private fun goToMainActivity() {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            //finish()
        }


        companion object {
            const val TAG = "LoginActivity"
        }

}



























