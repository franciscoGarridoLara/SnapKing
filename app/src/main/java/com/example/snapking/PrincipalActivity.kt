package com.example.snapking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.Firebase.User
import com.example.snapking.databinding.ActivityPrincipalBinding
import com.example.snapking.modelo.Usuario
import com.example.snapking.modelo.WrapperUsuario

class PrincipalActivity : AppCompatActivity() {

    private lateinit var btnAjustes:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //var binding=ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_principal)

        btnAjustes = findViewById(R.id.btnAjustes)

        btnAjustes.setOnClickListener()
        {
            User.getInstancia()!!.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        if(User.getInstancia()!= null){
            User.getInstancia()!!.printName()
            User.getInstancia()!!.printToken()
        }else{
            Log.d(User.TAG,"no hay usuario ")
        }

        //BaseDatos.getInstance()!!.escribir()
        BaseDatos.getInstance()!!.escribirUsuario(User.getInstancia()!!.wrapper)

    }
}