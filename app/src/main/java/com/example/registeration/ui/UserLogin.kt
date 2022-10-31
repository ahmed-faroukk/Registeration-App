package com.example.registeration.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.registeration.R
import com.example.registeration.data.api.LoginApi
import com.example.registeration.data.api.model.LoginModel
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserLogin : AppCompatActivity() {
    lateinit var mail: TextInputEditText
    lateinit var pass: TextInputEditText
    lateinit var signInBtn: Button
    lateinit var signUpBtn: TextView
    lateinit var progressBaar: ProgressBar
    lateinit var tokenObject: String
    lateinit var sharedPreferences : SharedPreferences

    var status: Boolean = false
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)
        signUpBtn = findViewById(R.id.RegisterText2)
        signInBtn = findViewById(R.id.appCompatButton)
        mail = findViewById(R.id.UserNameEDT)
       pass = findViewById(R.id.PasswordEdT)
        progressBaar = findViewById(R.id.progressBar2)
        progressBaar.visibility = View.INVISIBLE
         sharedPreferences = getSharedPreferences("PREFERENCE" , MODE_PRIVATE)
        //check data remember me
        val nameChecker = sharedPreferences.getString("email", null)
        val passwordChecker = sharedPreferences.getString("password", null)
        if(nameChecker!= null &&passwordChecker!=null){
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }




        signUpBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, UserSIgnUp::class.java)
            startActivity(intent)
        })
        signInBtn.setOnClickListener(View.OnClickListener {
            if(mail.text.toString()=="" ){
                Toast.makeText(applicationContext, "you must enter your email" , Toast.LENGTH_LONG).show()
            } else if(pass.text.toString()=="" ){
                Toast.makeText(applicationContext, "you must enter your password" , Toast.LENGTH_LONG).show()
            }else {
                progressBaar.visibility = View.VISIBLE
                Login(mail.text.toString(), pass.text.toString())
            }
        })

    }

    fun Login(email: String, password: String) {
        val loginModel = LoginModel(email, password)
        LoginApi.invoke().Login(loginModel).enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>, response: Response<LoginModel?>) {
                status = response.isSuccessful
                tokenObject = ""
                var toastFail = "please enter a valid E-mail and password";
                if (status) {
                    tokenObject = response.body().toString()
                }
                if (tokenObject != "") {
                    //sharedprefrance used to save token in local devise

                       val editor : SharedPreferences.Editor = sharedPreferences.edit()
                       editor.putString("email" ,email )
                       editor.putString("password" ,password )
                       editor.apply()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, toastFail, Toast.LENGTH_LONG).show();
                }
                progressBaar.visibility = View.INVISIBLE


            }

            override fun onFailure(call: Call<LoginModel?>, t: Throwable) {
                Toast.makeText(getApplicationContext(), "please enter a valid E-mail and password", Toast.LENGTH_LONG)
                    .show();
                progressBaar.visibility = View.INVISIBLE

            }

        })

    }
}