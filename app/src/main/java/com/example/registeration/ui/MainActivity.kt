package com.example.registeration.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.registeration.R
import com.example.registeration.data.api.LoginApi
import com.example.registeration.data.api.model.LoginModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
  lateinit var sharedPreferences: SharedPreferences
  lateinit var button: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textWelcome)
        button = findViewById(R.id.buttonLogout)
        // get sharedprefrance refreance
        sharedPreferences = getSharedPreferences("PREFERENCE" , MODE_PRIVATE )
        button.setOnClickListener(View.OnClickListener {
            logout()
        })


    }
fun logout(){
val editor = sharedPreferences.edit()
    editor.clear()
    editor.commit()
    editor.commit()
    val intent = Intent(applicationContext , UserLogin::class.java)
    startActivity(intent)
    finish()
}

}
