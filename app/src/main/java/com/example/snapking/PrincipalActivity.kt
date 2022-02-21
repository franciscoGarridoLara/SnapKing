package com.example.snapking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.snapking.Firebase.User

class PrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)


        if(User.getInstancia()!= null){
            User.getInstancia()!!.printName()
            User.getInstancia()!!.printToken()
        }else{
            Log.d(User.TAG,"no hay usuario ")
        }
    }
}