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
    private lateinit var binding: ActivityPrincipalBinding

    private lateinit var btnAjustes:ImageButton
    private lateinit var btnPerfil:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityPrincipalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.activity_principal)

        incializarBotones()


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


        btnPerfil.setOnClickListener()
        {
            startActivity(Intent(this,AmigosActivity::class.java))
            finish()
        }

        //BaseDatos.getInstance()!!.escribir()
        BaseDatos.getInstance()!!.escribirUsuario(User.getInstancia()!!.wrapper)

    }

    private fun incializarBotones() {
        btnAjustes = findViewById(R.id.btnAjustes)
        btnPerfil = findViewById(R.id.btnPerfil)
    }


}