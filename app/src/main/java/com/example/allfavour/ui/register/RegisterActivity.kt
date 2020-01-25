//package com.example.allfavour.ui.register
//
//import android.app.Activity
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.inputmethod.EditorInfo
//import android.widget.Toast
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProviders
//import com.example.allfavour.R
//import com.example.allfavour.ui.oldauth.*
//import kotlinx.android.synthetic.main.activity_register.*
//
//class RegisterActivity : AppCompatActivity() {
//
//    private lateinit var registerViewModel: RegisterViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//
//        registerViewModel = ViewModelProviders.of(this, RegisterViewModelFactory())
//            .get(RegisterViewModel::class.java)
//
//        registerViewModel.loginFormState.observe(this@RegisterActivity, Observer {
//            val loginState = it ?: return@Observer
//
//            // disable login button unless both username / password is valid
//            registerButton.isEnabled = loginState.isDataValid
//
//            if (loginState.usernameError != null) {
//                email.error = getString(loginState.usernameError)
//            }
//            if (loginState.passwordError != null) {
//                password.error = getString(loginState.passwordError)
//            }
//        })
//
//        registerViewModel.loginResult.observe(this@RegisterActivity, Observer {
//            val loginResult = it ?: return@Observer
//
//            val loggedUser = LoggedInUserView(it.userId)
//            updateUiWithUser(loggedUser)
//            setResult(Activity.RESULT_OK)
//
//            //Complete and destroy login activity once successful
//            finish()
//        })
//
//        switchToLoginText.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//
//            startActivity(intent)
//        }
//
//        email.afterTextChanged {
//            registerViewModel.loginDataChanged(
//                email.text.toString(),
//                password.text.toString()
//            )
//        }
//
//        password.apply {
//            afterTextChanged {
//                registerViewModel.loginDataChanged(
//                    email.text.toString(),
//                    password.text.toString()
//                )
//            }
//
//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE ->
//                        registerViewModel.register(
//                            email.text.toString(),
//                            password.text.toString()
//                        )
//                }
//                false
//            }
//
//            registerButton.setOnClickListener {
//                registerViewModel.register(
//                    email.text.toString(),
//                    password.text.toString()
//                )
//            }
//        }
//    }
//
//    private fun updateUiWithUser(model: LoggedInUserView) {
//        val welcome = getString(R.string.welcome)
//        val displayName = model.displayName
//        // TODO : initiate successful logged in experience
//        Toast.makeText(
//            applicationContext,
//            "$welcome $displayName",
//            Toast.LENGTH_LONG
//        ).show()
//    }
//}
