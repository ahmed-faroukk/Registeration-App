package com.example.registeration.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.registeration.R
import com.example.registeration.data.api.SignUpApi
import com.example.registeration.data.api.model.UserModel
import com.example.registeration.utils.MultiPartUtil
import kotlinx.android.synthetic.main.activity_user_signup.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.regex.Pattern

class UserSIgnUp : AppCompatActivity() {
    lateinit var signupBtn : Button
    lateinit var imgChooser : Button
    lateinit var username : EditText
    lateinit var phone : EditText
    lateinit var email : EditText
    lateinit var pass : EditText
    lateinit var img : ImageView
    lateinit var imgUri : Uri


    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    var check = false
    val REQUEST_CODE =100
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup)
        signupBtn = findViewById(R.id.SignUp)
        imgChooser = findViewById(R.id.addImage)
        username = findViewById(R.id.UserName)
        phone = findViewById(R.id.Number)
        pass = findViewById(R.id.Password)
        img = findViewById(R.id.imageView)
        email = findViewById(R.id.mail)

        Log.d("result_numCheck" , checkNum("01019326050").toString())

        signupBtn.setOnClickListener(View.OnClickListener {
            if(check==false){
                Toast.makeText(this, "you must select image", Toast.LENGTH_SHORT).show()
            }else if(username.text.isEmpty()){
                Toast.makeText(this, "you must enter username", Toast.LENGTH_SHORT).show()

            }else if(phone.text.isEmpty()){
                Toast.makeText(this, "you must enter phone", Toast.LENGTH_SHORT).show()

            }else if(pass.text.isEmpty()){
                Toast.makeText(this, "you Must enter pass" , Toast.LENGTH_SHORT).show()

            }else if(email.text.isEmpty()){
                Toast.makeText(this, "you Must enter your Gmail" , Toast.LENGTH_SHORT).show()

            }else if(checkNum(phone.text.toString())==false){
                Toast.makeText(this, "your number is incorrect" , Toast.LENGTH_SHORT).show()

            }else if(pass.length() < 10 ){
                Toast.makeText(this, "password must be al least 10 numbers " , Toast.LENGTH_SHORT).show()

            }else if(CheckMail(email.text.toString()) ==false  ){
                Toast.makeText(this, "your mail is incorrect " , Toast.LENGTH_SHORT).show()

            }else {
                val userModel = UserModel(username = username.text.toString() ,
                    password = pass.text.toString(),
                    phone = phone.text.toString(),
                    email = email.text.toString() , imgUri )
                uploadUser(userModel)


            }



        })
        addImage.setOnClickListener(View.OnClickListener {
            openGalleryForImage()
        })


    }





    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            imageView.setImageURI(data?.data) // handle chosen image
            imgUri = data?.data!!
            check = true


        }

    }

    fun openGalleryForImage() {
        val i = intent
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        this.startActivityForResult(intent, REQUEST_CODE)

    }




    fun uploadUser(userModel: UserModel) {

        // TODO replace with image uri
        val multiPartPhoto =
            MultiPartUtil.fileToMultiPart(
                applicationContext, userModel.ImgUri,
                "uploaded-file"
            )

        //convert String to part
        val mediaType = "multipart/form-data".toMediaType()
        val username = userModel.username.toRequestBody(mediaType)
        val phone = userModel.phone.toRequestBody(mediaType)
        val password = userModel.password.toRequestBody(mediaType)
        val email = userModel.email.toRequestBody(mediaType)

        val text = System.currentTimeMillis().toString()
            .toRequestBody(mediaType)

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            SignUpApi.invoke().sendUser(multiPartPhoto , username , password ,phone , email)
            progressBar.visibility = View.INVISIBLE

        }.invokeOnCompletion {
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show()
            imageView.setImageResource(R.drawable.ic_baseline_person_24)
            ClearText()

        }


    }
    fun checkNum(phone :String) :Boolean{
        if(phone[0]=='0' && phone[1]=='1'){
            if(phone[2]=='1'||phone[2]=='2'||phone[2]=='0'||phone[2]=='5'){
                if(phone.length==11){
                    return true
                }
            }
        }
        return false
    }
    fun CheckMail(email : String ) : Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }
    fun ClearText(){
        phone.text.clear()
        email.text.clear()
        pass.text.clear()
        username.text.clear()
    }

}